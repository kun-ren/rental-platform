package edu.qust.userBusiness.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import edu.qust.common.base.Response;
import edu.qust.userBusiness.common.WebConstant;
import edu.qust.userBusiness.controller.base.WebBaseController;
import edu.qust.userDao.entity.ProductRecommendation;
import edu.qust.userDao.entity.Stuff;
import edu.qust.userDao.entity.UserRecommendation;
import edu.qust.userService.ProductRecsService;
import edu.qust.userService.StuffService;
import edu.qust.userService.UserRecsService;
import edu.qust.userService.UserService;
import edu.qust.userService.vo.StuffInVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Controller
@RequestMapping("/recommend")
public class RecommendController extends WebBaseController {

    @Autowired
    private StuffService stuffService;
    @Autowired
    private ProductRecsService productRecsService;
    @Autowired
    private UserRecsService userRecsService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/product")
    @ResponseBody
    public Response recommendByProduct(@RequestParam int productId){
        List<ProductRecommendation> recommendationList =productRecsService.recommendByProductId(productId);
        List<Integer> productIds = recommendationList.stream().map( e -> e.getProductId()).collect(Collectors.toList());
        LambdaQueryWrapper<Stuff> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Stuff::getId,productIds);
        List<Stuff> stuffList = stuffService.list(queryWrapper);
        Response response = Response.success(stuffList);
        return response;
    }

    @GetMapping("/user")
    public String recommendByUser(Model model, HttpSession session){
        Integer userId = currentUserId(session);
        List<UserRecommendation> recommendationList = userRecsService.recommendByUserId(userId);
        List<Integer> productIds = recommendationList.stream().map( e -> e.getProductId()).collect(Collectors.toList());
        List<StuffInVO> stuffInVOList = stuffService.listStuffInVO();
        List<StuffInVO> result = stuffInVOList.stream().filter(a -> productIds.stream().anyMatch(b -> b.equals(a.getId()))).collect(Collectors.toList());
        model.addAttribute("stuffInVOList",result);
        //Response response = Response.success(stuffList);
        return "itemCF_recommend";
    }

    @GetMapping("/hot")
    public String hot(Model model){

        List<String> productIdList = redisTemplate.opsForList().range("historicalHot", 0, -1);
        List<Integer> ids = new ArrayList<>();
        productIdList.forEach(e -> ids.add(Integer.valueOf(e)));
        List<StuffInVO> stuffInVOList = stuffService.listStuffInVO();
        List<StuffInVO> result = stuffInVOList.stream().filter( e-> ids.contains(e.getId())).collect(Collectors.toList());
        model.addAttribute("stuffInVOList",result);
        return "hot_recommend";
    }
    @GetMapping("/streaming")
    public String streaming(Model model, HttpSession session){
        System.out.println(currentUserId(session));
        List<String> productIdList = redisTemplate.opsForList().range("streamRecs|"+currentUserId(session),0,-1);
        List<Integer> ids = new ArrayList<>();
        productIdList.forEach(e -> ids.add(Integer.valueOf(e)));
        List<StuffInVO> stuffInVOList = stuffService.listStuffInVO();
        List<StuffInVO> result = stuffInVOList.stream().filter( e-> ids.contains(e.getId())).collect(Collectors.toList());
        model.addAttribute("stuffInVOList",result);
        return "streaming_recommend";
    }

}
