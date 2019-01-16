package com.yan.weather.schedule;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

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
    			logger.info("本次有" + weatherCities.size() + "条需要爬取的天气城市年月（WeatherCity）数据。");
    			for(WeatherCity city:weatherCities) {
    				String areaCode = city.getAreaCode();
    				
    				weatherHistoryService.crawlWeatherMonthByAreaCode(areaCode);
    				logger.info("爬取的天气城市年月（WeatherCity）," + areaCode + "完成。");
    				try {
    					// 每条之间1秒间隔
    					Thread.sleep(1*1000);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    			}
    		}else {
    			logger.info("没有需要爬取的天气城市年月（WeatherCity）数据。");
    		}
    	}else{
    		logger.info("SCHEDUL_WEATHER_MONTH_CONFIG为关闭状态");
    	}
    }
    
    @Scheduled(initialDelay = 20000, fixedDelay = 20000)    //定时器将在1秒后每隔30秒执行
    public void crawlWeatherDayTask() {
    	logger.debug("The time is now {}", dateFormat.format(new Date()));
    	
    	WeatherConfig weatherConfig = weatherConfigMapper.findWeatherConfigByConfigCode(SCHEDUL_WEATHER_DAY_CONFIG_CODE);
    	
    	if(weatherConfig.getConfigValue() != null && "1".equals(weatherConfig.getConfigValue().trim())){
    		
    		// 查出前10条未被爬取的数据
    		List<WeatherMonth> weatherMonths = weatherMonthMapper.findWeatherMonthsUnCrawledFirst(10);
    		
    		if(weatherMonths != null && weatherMonths.size() > 0) {
    			logger.info("本次有" + weatherMonths.size() + "条需要爬取的天气城市年月（WeatherMonth）数据。");
    			for(WeatherMonth month:weatherMonths) {
    				String areaCode = month.getAreaCode();
    				String yearMonth = month.getYearMonth();
    				
    				weatherHistoryService.crawlWeatherHistoryByMonth(areaCode, yearMonth);
    				logger.info("爬取的天气城市年月（WeatherMonth）," + areaCode + "," + yearMonth + "完成。");
    				try {
    					// 每条之间1秒间隔
    					Thread.sleep(1*1000);
    				} catch (InterruptedException e) {
    					e.printStackTrace();
    				}
    			}
    		}else {
    			logger.info("没有需要爬取的天气城市年月（WeatherMonth）数据。");
    		}
    	}else{
    		logger.info("SCHEDUL_WEATHER_DAY_CONFIG为关闭状态");
    	}
    	
    }
}
