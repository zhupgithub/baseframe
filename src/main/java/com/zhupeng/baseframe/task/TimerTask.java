package com.zhupeng.baseframe.task;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.util.DateUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class TimerTask {

    @Scheduled(cron = "0 0/1 * * * ?")
    public void timeTest(){
        log.info("定时任务开始……");
        System.out.println(DateUtil.formatDate(new Date(),DateUtil.PATTERN_RFC1036)+"   ====");
    }
}
