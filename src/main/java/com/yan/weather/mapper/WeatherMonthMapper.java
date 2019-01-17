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
	List<WeatherMonth> findWeatherMonthsByAreaCodeAndYearMonth(WeatherMonth weatherMonth);
	
	List<WeatherMonth>  findWeatherMonthsUnCrawledFirst(int first);
	
	List<WeatherMonth> findWeatherMonthsByCondition(Map<String, Object> condition);
	
	Long countWeatherMonthsByCondition(Map<String, Object> condition);
	
	void deleteWeatherMonthByAreaCodeAndYearMonth(Map<String, Object> condition);
	
	void updateWeatherMonthByAreaCodeAndYearMonth(WeatherMonth weatherMonth);
	
	void updateCrawlFlagByAreaCodeAndYearMonth(WeatherMonth weatherMonth);
	
	void updateCrawlFlagToPreparedByIds(List<Long> ids);
}
