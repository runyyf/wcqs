package com.yyf.wcqs.quartz;

import com.yyf.wcqs.cache.WeChatCache;
import com.yyf.wcqs.task.WeatherUpdateTask;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//每小时执行的定时任务
@Component
public class HourQuartzJob {
    @Scheduled(cron = "0 0 * * * *")
    public void scheduledTask(){
        WeChatCache.getInstance().flushAll();
        new WeatherUpdateTask().task();
    }
}
