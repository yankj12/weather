package com.yan.weather.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yan.weather.service.facade.WeatherHistoryService;

@RestController
public class WeatherRestController {
	
	private static Logger logger = LoggerFactory.getLogger(WeatherRestController.class);
	
	@Autowired
	WeatherHistoryService weatherHistoryService;
	
	@RequestMapping("/crawlCityList") 
	public String crawlCityList(@RequestParam(required = false) String areaCode){ 
		logger.debug("================= crawlCityList =================");
		try {
			weatherHistoryService.crawlWeatherCity();
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}
		return "success";
	}
	
	@GetMapping("/crawlWeatherMonth") 
	public String crawlWeatherMonth(@RequestParam(required = false) String areaCode){
		
		return "success";
	}
}
