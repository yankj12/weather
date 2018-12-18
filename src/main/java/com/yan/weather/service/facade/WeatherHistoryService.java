package com.yan.weather.service.facade;

import com.yan.weather.schema.mysql.WeatherCity;
import com.yan.weather.schema.mysql.WeatherMonth;

public interface WeatherHistoryService {

	// 获取所有的城市列表
	void crawlWeatherCity();
	
	// 根据城市获取按月归纳的历史天气列表
	void crawlWeatherHistoryByCity(WeatherCity weatherCity);
	
	// 按月获取历史天气
	void crawlWeatherHistoryByMonth(WeatherMonth weatherMonth);
	
}
