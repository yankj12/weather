package com.yan.weather.service.impl;

import org.springframework.stereotype.Service;

import com.yan.weather.schema.mysql.WeatherCity;
import com.yan.weather.schema.mysql.WeatherMonth;
import com.yan.weather.service.facade.WeatherHistoryService;

@Service
public class WeatherHistoryServiceImpl implements WeatherHistoryService{

	
	@Override
	public void crawlWeatherCity() {
		// TODO Auto-generated method stub
		// http://lishi.tianqi.com/#city_q
	}

	@Override
	public void crawlWeatherHistoryByCity(WeatherCity weatherCity) {
		// TODO Auto-generated method stub
		// http://lishi.tianqi.com/beijing/index.html
	}

	@Override
	public void crawlWeatherHistoryByMonth(WeatherMonth weatherMonth) {
		// TODO Auto-generated method stub
		// http://lishi.tianqi.com/beijing/201810.html
	}

}
