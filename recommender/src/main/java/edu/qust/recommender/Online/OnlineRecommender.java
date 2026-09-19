package edu.qust.recommender.Online;

import com.alibaba.dubbo.config.annotation.Reference;
import edu.qust.common.util.BeanUtil;
import edu.qust.commonInterface.ProductRatingServiceApi;
import edu.qust.commonInterface.ProductRecsServiceApi;
import edu.qust.commonInterface.param.ProductRecsParam;

import edu.qust.recommender.common.ProductRecs;
import edu.qust.recommender.common.Recommendation;
import edu.qust.recommender.dataLoader.ProductRating;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.ConsumerStrategies;
import org.apache.spark.streaming.kafka010.KafkaUtils;
import org.apache.spark.streaming.kafka010.LocationStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import scala.Serializable;
import scala.Tuple2;
import scala.Tuple4;
import java.util.*;
import java.util.stream.Collectors;

@Component
public  class OnlineRecommender implements Serializable {

    //@Autowired
    private static RedisTemplate redisTemplate;
    //@Autowired
    private static JavaSparkContext javaSparkContext;
    //@Reference
    private static ProductRecsServiceApi productRecsServiceApi;
    //@Reference
    private  static ProductRatingServiceApi productRatingServiceApi;

    private int MAX_USER_RATING_NUM = 20;
    private int MAX_SIM_PRODUCTS_NUM = 20;
    public void recommend() throws InterruptedException {


        List<ProductRecsParam> productRecsParams = productRecsServiceApi.list();
        List<ProductRecs>  productRecs = new ArrayList<>();
        productRecsParams.forEach(e ->{
            ProductRecs recs = new ProductRecs();
            recs.setProductId(e.getProductId());
            List<Recommendation> recommendations =new ArrayList<>();
            e.getRecs().forEach(t ->
                    recommendations.add(BeanUtil.map(t, Recommendation.class)));
            recs.setRecs(recommendations);
            productRecs.add(recs);
        });
        //JavaRDD<ProductRecs> productRecsRDD = javaSparkContext.parallelize(productRecs);
        Map<Integer, Map<Integer, Double>> simProductMatrix = new HashMap<>();
        productRecs.forEach( item ->{
            Map<Integer, Double> m = item.getRecs()
                    .stream().
                    collect(Collectors.toMap(Recommendation::getProductId, Recommendation::getScore));
            simProductMatrix.put(item.getProductId(),m);
        });


        Broadcast<Map<Integer, Map<Integer, Double>>> simProductMatrixBroadcast = javaSparkContext.broadcast(simProductMatrix);

        //连接kafka
        Map<String,Object> kafkaParam = new HashMap<>();
        kafkaParam.put("bootstrap.servers","8.130.21.36:9092");//TODO
        kafkaParam.put("key.deserializer", StringDeserializer.class);
        kafkaParam.put("value.deserializer", StringDeserializer.class);
        kafkaParam.put("group.id", "recommender");
        kafkaParam.put("auto.offset.reset", "latest");
        JavaStreamingContext ssc = new JavaStreamingContext(javaSparkContext, Durations.seconds(2));
        Collection<String> topics = new HashSet<>();
        topics.add("recommender");
        JavaInputDStream<ConsumerRecord<String, String>> kafkaStream = KafkaUtils.createDirectStream(ssc, LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics,kafkaParam));
        //JavaDStream<Tuple4<Integer, Integer, Double, Integer>> ratingStream = kafkaStream.map(msg -> {
        JavaDStream<Tuple4<Integer, Integer, Double, Integer>> ratingStream = kafkaStream.map(msg -> {
            String[] attr = msg.value().split("\\|");//userId|productId|score|timestamp
            return new Tuple4<>(Integer.valueOf(attr[0]),
                    Integer.valueOf(attr[1]), Double.valueOf(attr[2]), Integer.valueOf(attr[3]));
        });
        System.out.println("开始监听");
        ratingStream.foreachRDD( rdds ->{   // rdds 单次消费消息产生的一组RDD
            rdds.foreach( rdd ->{
                System.out.println(" receiving rating data ");
                Tuple2<Integer, Double>[] userRecentlyRatings = getUserRecentlyRatings(MAX_USER_RATING_NUM, rdd._1());

                int[] candidateProducts = getTopSimProducts(MAX_SIM_PRODUCTS_NUM, rdd._1(), rdd._2(), simProductMatrixBroadcast.getValue());
                Tuple2<Integer, Double>[] streamRecs = computeProductScore(candidateProducts, userRecentlyRatings, simProductMatrixBroadcast.getValue());
                //savaDataToMysql(userId, streamRecs);使用redis代替
                List<Integer> ids = Arrays.stream(streamRecs).map(e ->e._1).collect(Collectors.toList());
                String name = "streamRecs"+ rdd._1().toString();
                redisTemplate.opsForList().leftPushAll(name,ids);
            });
        });

        ssc.start();
        System.out.println("streaming started !");
        ssc.awaitTermination();
    }

    private int[] getTopSimProducts(Integer num, Integer userId, Integer productId, Map<Integer,Map<Integer, Double>> simProducts) {

        //从广播变量相似度矩阵中获得相似度列表
        List<Tuple2<Integer, Double>> allSimProducts = new ArrayList<>();
        simProducts.get(productId).forEach( (k,v) -> allSimProducts.add(new Tuple2<>(k,v)));

        //获取用户已经评分过的商品，过滤掉
        List<ProductRating> ratingCollection = productRatingServiceApi.list().stream().map(
                e -> BeanUtil.map(e, ProductRating.class)
        ).collect(Collectors.toList());

        List<Integer> ratingExist = ratingCollection.stream().filter(item -> item.getUserId() == userId)
                .map(item -> item.getProductId()).collect(Collectors.toList());

        int[] simProductIds =  allSimProducts.stream().filter( item -> ! ratingExist.contains( item._1))
                .sorted(Comparator.comparing( e -> e._2, Comparator.reverseOrder()))
                .limit(num)
                .mapToInt( x -> x._1).toArray();
        return simProductIds;
    }

    private Tuple2<Integer, Double>[] getUserRecentlyRatings(Integer num, Integer userId){
        //redis    List 键名为uid   值PRODUCTID: SCORE
        //String key = "userId"+userId;
        List<String> redisData = redisTemplate.opsForList().range("user:" + userId, 0, num);
        List<Tuple2<Integer, Double>> recentRating = redisData.stream()
                .map(item -> {
                    String[] attr = item.split("\\:");
                    return new Tuple2<>(Integer.valueOf(attr[0].trim()), Double.valueOf(attr[1].trim()));
                }).collect(Collectors.toList());
        Tuple2<Integer, Double>[] r = new Tuple2[recentRating.size()];
        recentRating.toArray(r);
        return r;

    }

    private Tuple2<Integer,Double>[] computeProductScore(int[] candidateProducts, Tuple2<Integer, Double>[] userRecentlyRatings, Map<Integer,Map<Integer, Double>> simProducts){

        List<Tuple2<Integer, Double>> scores = new ArrayList<>();
        //两个Map 记录每个商品的高分和低分的计数器productId  -> count
        Map<Integer, Integer> increMap = new HashMap<>();
        Map<Integer, Integer> decreMap = new HashMap<>();
        //遍历每个备选商品， 计算和已评分商品的相似度
        for(int candidateProduct : candidateProducts){
            for (Tuple2<Integer, Double> userRecentlyRating : userRecentlyRatings){
                double simScore = getProductsSimScore(candidateProduct, userRecentlyRating._1, simProducts);
                if(simScore > 0.4){
                    //按照公式加权叠加
                    scores.add( new Tuple2<>(candidateProduct, simScore * userRecentlyRating._2()));
                    if(userRecentlyRating._2 > 3){
                        increMap.put(candidateProduct, increMap.getOrDefault(candidateProduct,0 )+1);
                    } else {
                        decreMap.put(candidateProduct, decreMap.getOrDefault(candidateProduct, 0 )+1);
                    }
                }
            }
        }
        //根据公式计算的所有的推荐优先级，首先按productId分组
        List<Tuple2<Integer, Double>> score = JavaPairRDD.fromJavaRDD(javaSparkContext.parallelize(scores))
                .groupByKey()
                .map(item -> {
                    double sum = 0;
                    int num = 0;
                    for (double e : item._2) {
                        sum += e;
                        num++;
                    }
                    return new Tuple2<Integer, Double>(item._1, sum / num + log(increMap.getOrDefault(item._1, 1))- log(decreMap.getOrDefault(item._1, 1)));
                }).collect();
        Tuple2<Integer, Double>[] s = new Tuple2[score.size()];
        score.toArray(s);
        return s;
    }


    private double getProductsSimScore( Integer productA, Integer productB, Map<Integer,Map<Integer, Double>> simProducts){
        Map<Integer, Double> products = simProducts.get(productA);
        if(products != null) {
            if( products.get(productB) != null) return products.get(productB);
        }
        return 0;
    }

    private double log( int m ){
        int N= 10;
        return Math.log(m) / Math.log(N);
    }


    @Autowired
    public void setJavaSparkContext(JavaSparkContext javaSparkContext){
        OnlineRecommender.javaSparkContext = javaSparkContext;
    }

    @Reference
    public void setProductRecsServiceApi(ProductRecsServiceApi productRecsServiceApi){
        OnlineRecommender.productRecsServiceApi = productRecsServiceApi;
    }
    @Reference
    public void setProductRatingServiceApi(ProductRatingServiceApi productRatingServiceApi){
        OnlineRecommender.productRatingServiceApi = productRatingServiceApi;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate){
        OnlineRecommender.redisTemplate = redisTemplate;
    }
}
