package com.yan.weather.disruptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.lmax.disruptor.EventHandler;
import com.yan.weather.WeatherApplication;
import com.yan.weather.schema.mysql.WeatherMonth;
import com.yan.weather.service.facade.WeatherHistoryService;

/**
 * 消费者
 * 
 * @author Yan
 *
 */
public class WeatherMonthEventHandler implements EventHandler<WeatherMonthEvent>{

	private static Logger disruptorLogger = LoggerFactory.getLogger("disruptorLog");
	private static Logger weatherHisLogger = LoggerFactory.getLogger("weatherHisLogger");
	
	//多个消费者示例：消费者id
	private final long handlerId;
	private final long numbersOfConsumers; 
	
	private static  WeatherHistoryService weatherHistoryService;
	
	//通过上下文获取Service
	static {
		weatherHistoryService = WeatherApplication.getCtx().getBean(WeatherHistoryService.class);
	}
	
	//构造方法
	public WeatherMonthEventHandler() {
		disruptorLogger.debug("[WeatherMonthEventHandler constructor method] [handlerId:" + 0 + ", numbersOfConsumers:" + 1 + "]");
		this.handlerId = 0;
		this.numbersOfConsumers = 1;
	}
	public WeatherMonthEventHandler(final long handlerId, final long numbersOfConsumers) {
		disruptorLogger.debug("[WeatherMonthEventHandler constructor method] [handlerId:" + handlerId + ", numbersOfConsumers:" + numbersOfConsumers + "]");
		//super();
		this.handlerId = handlerId;
		this.numbersOfConsumers = numbersOfConsumers;
	}
	
	@Override
	public void onEvent(WeatherMonthEvent event, long sequence, boolean endOfBatch) throws Exception {
		
		if((sequence % this.numbersOfConsumers) == this.handlerId) {
			WeatherMonth month = event.getWeatherMonth();
			
			String areaCode = month.getAreaCode();
			String yearMonth = month.getYearMonth();
			
			weatherHistoryService.crawlWeatherHistoryByMonth(areaCode, yearMonth);
			weatherHisLogger.info("handler[" + handlerId + "] crawl weather history(WeatherMonth)," + areaCode + "," + yearMonth + " finished.");
			try {
				// 每条之间1秒间隔
				Thread.sleep(1*1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
