package com.yyf.wcqs.boot;


import com.yyf.wcqs.cache.WeChatCache;
import com.yyf.wcqs.task.WeatherUpdateTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @Author: yuyf
 * @Description:
 * @Date: Created in 16:43 2018/5/3
*/
@Component
public class WeChatBootStrap implements CommandLineRunner {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void run(String... strings) throws Exception {
        //  服务启动调用微信求情token
        logger.info("服务启动调用微信求情token");
        WeChatCache.getInstance().flushAll();
        logger.info("服务启动调用天气请求");
        new WeatherUpdateTask().task();
    }
}
