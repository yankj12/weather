package com.yan.weather.disruptor;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

@Service
public class WeatherMonthEventConfig {

	private static Logger logger = LoggerFactory.getLogger("disruptorLog");
	
	@Value("${disruptor.consumerNum}")
	private int consumerNum;//消费者数量
	
	private static WeatherMonthEventProducerWithTranslator producer;
	
	public WeatherMonthEventProducerWithTranslator getProducer(){
		if(null == producer) {
			this.initProducer();
		}
		return producer;
	}
	
	private void initProducer() {
		logger.debug("[disruptor prepare init producer]");
		
		// 事件的工厂
		WeatherMonthEventFactory factory = new WeatherMonthEventFactory();
		
		//指定环形缓冲器的大小，必须是2的幂
        int bufferSize = 2048;
        
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        
        // 下面的构造方法默认创建的是一个生产者
    	Disruptor<WeatherMonthEvent> disruptor = new Disruptor<WeatherMonthEvent>(factory, bufferSize, threadFactory, ProducerType.SINGLE, new BlockingWaitStrategy());
        
    	if(consumerNum > 1){
    		//多个消费者：
        	WeatherMonthEventHandler[] handlers = new WeatherMonthEventHandler[consumerNum];
        	for (int i = 0; i < handlers.length; i++) {
        		logger.debug("[create No." + (i+1) + " EventHandler, and add EventHandler to disruptor] [handlerId:"+i+", numbersOfConsumers:" + consumerNum + "]");
        		handlers[i] = new WeatherMonthEventHandler(i, consumerNum);
        	}
        	
        	logger.debug("[add EventHandlers to disruptor finished.]");
        	//连接处理程序 ：消费者
        	disruptor.handleEventsWith(handlers);
    	}else{
    		logger.debug("[create 1 EventHandler, and add EventHandler to disruptor]");
    		disruptor.handleEventsWith(new WeatherMonthEventHandler());
    	}
    	
    	//启动Disruptor，启动运行 
        disruptor.start();
        logger.debug("[start disruptor]");
        
        //从Disruptor获取用于发布数据的环形缓冲区。
        RingBuffer<WeatherMonthEvent> ringBuffer = disruptor.getRingBuffer();
        
        //生产者
        producer = new WeatherMonthEventProducerWithTranslator(ringBuffer);
	}
}
