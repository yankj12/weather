package com.yan.weather.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yan.weather.schema.mysql.WeatherDay;

@Mapper
public interface WeatherDayMapper {

	// 插入某一天的天气
	void insertWeatherDay(WeatherDay weatherDay);
	
	void insertBatchWeatherDay(List<WeatherDay> weatherDays);
	
	// 查询某一天的天气
	List<WeatherDay> findWeatherDaysByDate(String date, String area);
	
	// 查询某一年的天气
	List<WeatherDay> findWeatherDaysByYear(String year, String area);
	
	// 查询某一月的天气
	List<WeatherDay> findWeatherDaysByMonth(String month, String area);
	
	List<WeatherDay> findWeatherDaysByCondition(Map<String, Object> condition);
	
	Long countWeatherDaysByCondition(Map<String, Object> condition);
	
	void deleteWeatherDayByDateArea(String date, String area);
	
	void updateWeatherDayByDateArea(WeatherDay weatherDay);
}
