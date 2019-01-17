package com.yan.weather.disruptor;

import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.yan.weather.schema.mysql.WeatherMonth;

public class WeatherMonthEventProducerWithTranslator {

	private final RingBuffer<WeatherMonthEvent> ringBuffer;

	public WeatherMonthEventProducerWithTranslator(RingBuffer<WeatherMonthEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }

	private static final EventTranslatorOneArg<WeatherMonthEvent, WeatherMonth> TRANSLATOR = new EventTranslatorOneArg<WeatherMonthEvent, WeatherMonth>() {
		public void translateTo(WeatherMonthEvent event, long sequence, WeatherMonth weatherMonth) {
			event.setWeatherMonth(weatherMonth);
		}
	};

	public void onData(WeatherMonth weatherMonth) {
		ringBuffer.publishEvent(TRANSLATOR, weatherMonth);
	}
}
