package com.yan.weather.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledTasks {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Scheduled(initialDelay = 1000, fixedDelay = 3000)    //定时器将在1秒后每隔3秒执行
    public void reportCurrentTime() {
    	logger.debug("The time is now {}", dateFormat.format(new Date()));
    }
}
