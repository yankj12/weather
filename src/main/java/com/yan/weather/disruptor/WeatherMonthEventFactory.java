package com.yan.weather.disruptor;

import com.lmax.disruptor.EventFactory;

public class WeatherMonthEventFactory implements EventFactory<WeatherMonthEvent>{

	@Override
	public WeatherMonthEvent newInstance() {
		return new WeatherMonthEvent();
	}

}
