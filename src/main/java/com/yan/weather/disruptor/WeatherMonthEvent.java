package com.yan.weather.disruptor;

import com.yan.weather.schema.mysql.WeatherMonth;

public class WeatherMonthEvent {

	private WeatherMonth weatherMonth;

	public WeatherMonth getWeatherMonth() {
		return weatherMonth;
	}

	public void setWeatherMonth(WeatherMonth weatherMonth) {
		this.weatherMonth = weatherMonth;
	}
	
}
