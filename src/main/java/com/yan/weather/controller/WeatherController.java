package com.yan.weather.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WeatherController {

	private static Logger logger = LoggerFactory.getLogger(WeatherController.class);
	
	@RequestMapping(value = {"/", "/index"})
	public String indexPage(){
		
		return "index";
	}

}
