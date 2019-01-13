package com.yan.weather.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.yan.weather.schema.mysql.WeatherConfig;

@Mapper
public interface WeatherConfigMapper {

	void insertWeatherConfig(WeatherConfig weatherConfig);
	
	WeatherConfig findWeatherConfigByConfigCode(String configCode);
	
	void updateValidStatusByConfigCode(WeatherConfig weatherConfig);
	
	void updateConfigValueByConfigCode(WeatherConfig weatherConfig);
}
