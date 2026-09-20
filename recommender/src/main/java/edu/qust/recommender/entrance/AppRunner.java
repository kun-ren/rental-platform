package edu.qust.recommender.entrance;

import edu.qust.recommender.ItemCF.ItemCFRecommender;
import edu.qust.recommender.Offline.ALSTrainer;
import edu.qust.recommender.Offline.OfflineRecommender;
import edu.qust.recommender.Online.OnlineRecommender;
import edu.qust.recommender.dataLoader.DataLoader;
import edu.qust.recommender.statistics.StatisticsRecommender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;

@Component
public class AppRunner implements ApplicationRunner {

    @Autowired
    private DataLoader dataLoader;
    /*@Autowired
    private StatisticsRecommender statisticsRecommender;*/

    /*@Autowired
    private OfflineRecommender offlineRecommender;*/

    /*@Autowired
    private ALSTrainer alsTrainer;*/

    /*@Autowired
    private ItemCFRecommender itemCFRecommender;*/

    @Autowired
    private OnlineRecommender onlineRecommender;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //boolean r1 = dataLoader.readRatingCSVData();  //success
        //boolean r2 = dataLoader.readProductCSVData();//success
        //statisticsRecommender.compute();//success
        //offlineRecommender.recommend();
        //alsTrainer.test();
        //itemCFRecommender.recommender();

        onlineRecommender.recommend();
        System.out.println("Finished");

    }
}
