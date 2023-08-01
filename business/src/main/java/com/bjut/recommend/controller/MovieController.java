package com.bjut.recommend.controller;

import com.bjut.recommend.Service.QueryCollectionService;
import io.netty.util.Constant;
import org.apache.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @className: MovieController
 * @Description: TODO
 * @version: v1.８.0
 * @author: GONGWENXUE
 * @date: 2023/7/28 19:52
 */
@RestController
@RequestMapping("/movie")
public class MovieController {
    @Autowired
    private QueryCollectionService queryCollectionService;

    @GetMapping("/top250")
    public String getTopScoreMovie() {
        // 1. 从表中select title from a where id < 100;

        // 2. 返回的格式是：res{
        //              code:20000
        //              status: ,msg,
        //              data:{}
        // }
        boolean exists = queryCollectionService.collectionExists();
        return exists+"!";
    }

    @GetMapping("/rate")
    public String rateToMovie(@RequestParam("score") Double score) {
        String MOVIE_RATING_PREFIX = "MOVIE_RATING_PREFIX";
        //埋点日志
        Logger log = Logger.getLogger(MovieController.class.getName());
        System.out.print("=========complete=========");
        log.info(MOVIE_RATING_PREFIX + ":" + "1" +"|"+ "31" +"|"+ score +"|"+ System.currentTimeMillis()/1000);
        return "success";
    }
}
