package com.yan.weather.schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.yan.weather.disruptor.WeatherMonthEventConfig;
import com.yan.weather.mapper.WeatherCityMapper;
import com.yan.weather.mapper.WeatherConfigMapper;
import com.yan.weather.mapper.WeatherMonthMapper;
import com.yan.weather.schema.mysql.WeatherCity;
import com.yan.weather.schema.mysql.WeatherConfig;
import com.yan.weather.schema.mysql.WeatherMonth;
import com.yan.weather.service.facade.WeatherHistoryService;

@Component
public class ScheduledTasks {

	private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    /**
     * 配置获取weatherMonth的定时任务是否执行
     * 1位启动
     * 0位不启动
     */
    public static final String SCHEDUL_WEATHER_MONTH_CONFIG_CODE = "SCHEDUL_WEATHER_MONTH_CONFIG";
    
    /**
     * 配置获取weatherDay的定时任务是否执行
     * 1位启动
     * 0位不启动
     */
    public static final String SCHEDUL_WEATHER_DAY_CONFIG_CODE = "SCHEDUL_WEATHER_DAY_CONFIG";
    
    @Autowired
	WeatherMonthEventConfig weatherMonthEventConfig;
    
    @Autowired
    WeatherMonthMapper weatherMonthMapper;
    
    @Autowired
	WeatherCityMapper weatherCityMapper;
    
    @Autowired
	WeatherHistoryService weatherHistoryService;
    
    @Autowired
    WeatherConfigMapper weatherConfigMapper;
    
    @Scheduled(initialDelay = 1000, fixedDelay = 20000)    //定时器将在1秒后每隔20秒执行
    public void crawlWeatherMonthTask() {
    	
    	WeatherConfig weatherConfig = weatherConfigMapper.findWeatherConfigByConfigCode(SCHEDUL_WEATHER_MONTH_CONFIG_CODE);
    	
    	if(weatherConfig.getConfigValue() != null && "1".equals(weatherConfig.getConfigValue().trim())){
    		// 查出前10条未被爬取的数据
    		List<WeatherCity> weatherCities = weatherCityMapper.findWeatherCitiesUnCrawledFirst(10);
    		
    		if(weatherCities != null && weatherCities.size() > 0) {
    			logger.info("There are " + weatherCities.size() + " weather history(WeatherCity)datas to crawl int this trun.");
    			for(WeatherCity city:weatherCities) {
    				String areaCode = city.getAreaCode();
    				
    				weatherHistoryService.crawlWeatherMonthByAreaCode(areaCode);
    				logger.info("crawl weather history(WeatherCity)data," + areaCode + " finished.");
    				try {
    					// 每条之间1秒间隔
    					Thread.sleep(1*1000);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    			}
    		}else {
    			logger.info("Threr is no weather history(WeatherCity)data to crawl in this trun.");
    		}
    	}else{
    		logger.info("SCHEDUL_WEATHER_MONTH_CONFIG is off.");
    	}
    }
    
    @Scheduled(initialDelay = 20000, fixedDelay = 20000)    //定时器将在1秒后每隔30秒执行
    public void crawlWeatherDayTask() {
    	
    	WeatherConfig weatherConfig = weatherConfigMapper.findWeatherConfigByConfigCode(SCHEDUL_WEATHER_DAY_CONFIG_CODE);
    	
    	if(weatherConfig.getConfigValue() != null && "1".equals(weatherConfig.getConfigValue().trim())){
    		
    		// 查出前10条未被爬取的数据
    		List<WeatherMonth> weatherMonths = weatherMonthMapper.findWeatherMonthsUnCrawledFirst(10);
    		
    		if(weatherMonths != null && weatherMonths.size() > 0) {
    			logger.info("There are " + weatherMonths.size() + " weather history(WeatherMonth)datas to crawl int this trun.");
    			
    			// 将这些未爬取的数据更新crawlFlag为准备状态
        		List<Long> ids = new ArrayList<Long>();
    			for(WeatherMonth month:weatherMonths) {
    				if(month.getId() != null){
    					
    				}
    				ids.add(month.getId());
    			}
    			logger.info("update these WeatherMonth crawlFlag to 'P'(P means Prepared)." + Arrays.toString(ids.toArray()));
    			weatherMonthMapper.updateCrawlFlagToPreparedByIds(ids);
    			
    			for(WeatherMonth month:weatherMonths) {
    				// 将weatherMonth添加到disruptor的队列中
    				weatherMonthEventConfig.getProducer().onData(month);
    			}
    		}else {
    			logger.info("There is no weather history(WeatherMonth)data to crawl in this trun.");
    		}
    	}else{
    		logger.info("SCHEDUL_WEATHER_DAY_CONFIG is off.");
    	}
    	
    }
}
