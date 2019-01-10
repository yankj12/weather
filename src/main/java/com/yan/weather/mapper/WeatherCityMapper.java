package com.yan.weather.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.yan.weather.schema.mysql.WeatherCity;

@Mapper
public interface WeatherCityMapper {

	void insertWeatherCity(WeatherCity weatherCity);
	
	void insertBatchWeatherCity(List<WeatherCity> weatherCitys);
	
	WeatherCity findWeatherCityByAreaName(String areaName);
	
	List<WeatherCity> findWeatherCitysByCondition(Map<String, Object> condition);
	
	Long countWeatherCitysByCondition(Map<String, Object> condition);
	
}
