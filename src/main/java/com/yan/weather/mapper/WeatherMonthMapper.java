package com.yan.weather.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yan.weather.schema.mysql.WeatherMonth;

@Mapper
public interface WeatherMonthMapper {

	// 插入某一天的天气
	void insertWeatherMonth(WeatherMonth weatherMonth);
	
	void insertBatchWeatherMonth(List<WeatherMonth> weatherMonths);
	
	// 查询某一月的天气
	List<WeatherMonth> findWeatherMonthsByMonthArea(String month, String area);
	
	List<WeatherMonth> findWeatherMonthsByCondition(Map<String, Object> condition);
	
	Long countWeatherMonthsByCondition(Map<String, Object> condition);
	
	void deleteWeatherMonthByMonthArea(String month, String areaName);
	
	void updateWeatherMonthByMonthArea(WeatherMonth weatherMonth);
}
