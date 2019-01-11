package com.yan.weather.service.facade;

public interface WeatherHistoryService {

	// 获取所有的城市列表
	void crawlWeatherCity();
	
	// 根据城市获取按月归纳的历史天气列表
	public void crawlWeatherMonthByAreaCode(String areaCode);
	
	// 按月获取历史天气
	void crawlWeatherHistoryByMonth(String areaCode, String yearMonth);
	
}
