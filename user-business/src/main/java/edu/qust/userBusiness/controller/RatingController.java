package edu.qust.userBusiness.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.qust.common.base.Response;
import edu.qust.userBusiness.controller.base.WebBaseController;
import edu.qust.userDao.entity.ProductRating;
import edu.qust.userService.ItemService;
import edu.qust.userService.ProductRatingService;
import edu.qust.userService.StuffService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

@Slf4j
@Controller
@RequestMapping("/rating")
public class RatingController extends WebBaseController {

    @Resource
    private ProductRatingService productRatingService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Resource
    private ItemService itemService;

    @PostMapping("/{itemId}")
    @ResponseBody
    public Response addRating( @PathVariable int itemId, float score,HttpSession session){

        int stuffId = itemService.getById(itemId).getStuffId();
        ProductRating rating =new ProductRating();
        rating.setUserId(currentUserId(session));
        rating.setProductId(stuffId);
        rating.setScore(score);
        rating.setTime(new Timestamp(System.currentTimeMillis()));
        LambdaQueryWrapper<ProductRating> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ProductRating::getProductId,stuffId);
        queryWrapper.eq(ProductRating::getUserId,currentUserId(session));
        productRatingService.remove(queryWrapper);
        productRatingService.save(rating);
        String kafkaMessage = rating.getUserId()+"|"+stuffId+"|"+score+"|"+
                System.currentTimeMillis();

        //userId|productId|score|timestamp
        kafkaTemplate.send("recommender",kafkaMessage);
        Response response = Response.SUCCESS;
        return response;
    }

    @GetMapping
    @ResponseBody
    public Response getRating(@RequestParam int itemId, HttpSession httpSession){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ProductRating rating = productRatingService.getRating(currentNonRootUserId(authentication, httpSession),itemId);
        Response response = Response.success(rating);
        return  response;
    }
}
