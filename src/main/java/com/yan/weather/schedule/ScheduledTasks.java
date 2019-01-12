package com.yan.weather.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yan.weather.mapper.WeatherMonthMapper;
import com.yan.weather.schema.mysql.WeatherMonth;
import com.yan.weather.service.facade.WeatherHistoryService;

@Component
public class ScheduledTasks {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Autowired
    WeatherMonthMapper weatherMonthMapper;
    
    @Autowired
	WeatherHistoryService weatherHistoryService;
    
    @Scheduled(initialDelay = 1000, fixedDelay = 30000)    //定时器将在1秒后每隔20秒执行
    public void doTask() {
    	logger.debug("The time is now {}", dateFormat.format(new Date()));
    	
    	// 查出前10条未被爬取的数据
    	List<WeatherMonth> weatherMonths = weatherMonthMapper.findWeatherMonthsUnCrawledFirst(10);
    	
    	if(weatherMonths != null && weatherMonths.size() > 0) {
    		logger.info("本次有" + weatherMonths.size() + "条需要爬取的天气城市年月（WeatherMonth）数据。");
    		for(WeatherMonth month:weatherMonths) {
    			String areaCode = month.getAreaCode();
    			String yearMonth = month.getYearMonth();
    			
    			weatherHistoryService.crawlWeatherHistoryByMonth(areaCode, yearMonth);
    			logger.info("爬取的天气城市年月（WeatherMonth）," + areaCode + "," + yearMonth + "完成。");
    		}
    	}else {
    		logger.info("没有需要爬取的天气城市年月（WeatherMonth）数据。");
    	}
    }
}
