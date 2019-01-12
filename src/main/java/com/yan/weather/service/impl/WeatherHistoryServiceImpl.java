package com.yan.weather.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yan.weather.mapper.WeatherCityMapper;
import com.yan.weather.mapper.WeatherDayMapper;
import com.yan.weather.mapper.WeatherMonthMapper;
import com.yan.weather.schema.mysql.WeatherCity;
import com.yan.weather.schema.mysql.WeatherDay;
import com.yan.weather.schema.mysql.WeatherMonth;
import com.yan.weather.service.facade.WeatherHistoryService;

@Service
public class WeatherHistoryServiceImpl implements WeatherHistoryService{

	private static Logger logger = LoggerFactory.getLogger(WeatherHistoryServiceImpl.class);
	
	@Value("${proxy.useProxy}")
	private boolean useProxy;

	@Value("${proxy.ip}")
	private String proxyIp;
	
	@Value("${proxy.port}")
	private int port;
	
	@Autowired
	WeatherCityMapper weatherCityMapper;
	
	@Autowired
	WeatherMonthMapper weatherMonthMapper;
	
	@Autowired
	WeatherDayMapper weatherDayMapper;
	
	@Override
	public void crawlWeatherCity() {
		// http://lishi.tianqi.com/#city_q
		String url = "http://lishi.tianqi.com/#city_q";
		
//		Host: lishi.tianqi.com
//		User-Agent: Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0
//		Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8
//		Accept-Language: zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2
//		Accept-Encoding: gzip, deflate
//		Connection: keep-alive
//		Cookie: cs_prov=04; cs_city=0401; ccity=101040100; Hm_lvt_ab6a683aa97a52202eab5b3a9042a8d2=1545100007,1547086544; UM_distinctid=168358b39e22db-0639973eb72783-143a7540-1fa400-168358b39e327f; CNZZDATA1275796416=1915182485-1547084074-%7C1547084074; Hm_lpvt_ab6a683aa97a52202eab5b3a9042a8d2=1547086573
//		Upgrade-Insecure-Requests: 1
//		Pragma: no-cache
//		Cache-Control: no-cache
		
		Map<String, String> requestHeaders = new HashMap<>();
		
		requestHeaders.put("Host", "lishi.tianqi.com");
		requestHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
		requestHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		requestHeaders.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		requestHeaders.put("Accept-Encoding", "gzip, deflate");
		requestHeaders.put("Connection", "keep-alive");
		requestHeaders.put("Cookie", "cs_prov=04; cs_city=0401; ccity=101040100; Hm_lvt_ab6a683aa97a52202eab5b3a9042a8d2=1545100007,1547086544; UM_distinctid=168358b39e22db-0639973eb72783-143a7540-1fa400-168358b39e327f; CNZZDATA1275796416=1915182485-1547084074-%7C1547084074; Hm_lpvt_ab6a683aa97a52202eab5b3a9042a8d2=1547086573");
		
		String content = this.requestUrlByGetMethod(url, requestHeaders, "utf-8");
//		System.out.println(content);
		
		if(content != null) {
			Document document=Jsoup.parse(content);
			Element cityAllDivElement = document.select("div#cityall").first();
			List<Element> cityliElements = cityAllDivElement.select("li");
			
//			List<WeatherCity> weatherCities = new ArrayList<WeatherCity>();
			
			if(cityliElements != null){
				for(Element lielement:cityliElements){
					Element aElement = lielement.select("a").first();
					String href = aElement.attr("href");
					
					//text​() Gets the combined text of this element and all its children. 
					//ownText​() Gets the text owned by this element only; does not get the combined text of all children. 
					String areaName = aElement.ownText();
					
					if(href != null && !"".equals(href.trim())){
						if(!href.startsWith("#")){
							WeatherCity weatherCity = new WeatherCity();
							
							// http://lishi.tianqi.com/anda/index.html
							String areaCode = this.substrAreaCodeFromUrl(href);
							
							weatherCity.setAreaName(areaName);
							weatherCity.setAreaCode(areaCode);
							// 首字母变大写作为索引字符
							weatherCity.setIndexLetter(areaCode.substring(0, 1).toUpperCase());
							weatherCity.setUrl(href);
							
							weatherCity.setCrawlCount(0);
							weatherCity.setCrawlFlag(WeatherCity.CRAWL_FLAG_INITIAL);
							weatherCity.setValidStatus("1");
							
							weatherCity.setInsertTime(new Date());
							weatherCity.setUpdateTime(new Date());
							
							WeatherCity weatherCityTemp = weatherCityMapper.findWeatherCityByAreaName(areaName);
							if(weatherCityTemp == null) {
								weatherCityMapper.insertWeatherCity(weatherCity);
								
//							ObjectMapper mapper = new ObjectMapper();
//					        String jsonString = null;
//							try {
//								jsonString = mapper.writeValueAsString(weatherCity);
//								logger.debug("保存城市信息," + jsonString);
//							} catch (JsonProcessingException e) {
//								e.printStackTrace();
//							}
								logger.debug("保存城市信息," + areaName);
							}
							
							//weatherCities.add(weatherCity);
						}
					}
				}
			}
		}
		
	}

	public void crawlWeatherMonthByAreaCode(String areaCode) {
		
		// 根据areaCode查出来areaName，当然这一步是非必须的
		WeatherCity city = weatherCityMapper.findWeatherCityByAreaCode(areaCode);
		String areaName = city != null ? city.getAreaName() : "";
		
		// http://lishi.tianqi.com/beijing/index.html
		String url = "http://lishi.tianqi.com/" + areaCode + "/index.html";
		
		Map<String, String> requestHeaders = new HashMap<>();
		
		requestHeaders.put("Host", "lishi.tianqi.com");
		requestHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
		requestHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
		requestHeaders.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
		requestHeaders.put("Accept-Encoding", "gzip, deflate");
		requestHeaders.put("Connection", "keep-alive");
		requestHeaders.put("Cookie", "cs_prov=04; cs_city=0401; ccity=101040100; Hm_lvt_ab6a683aa97a52202eab5b3a9042a8d2=1545100007,1547086544; UM_distinctid=168358b39e22db-0639973eb72783-143a7540-1fa400-168358b39e327f; CNZZDATA1275796416=1915182485-1547084074-%7C1547084074; Hm_lpvt_ab6a683aa97a52202eab5b3a9042a8d2=1547086573");
		
		String content = this.requestUrlByGetMethod(url, requestHeaders, "utf-8");
//		System.out.println(content);
		
		if(content != null) {
			Document document=Jsoup.parse(content);
			Element cityAllDivElement = document.select("div.tqtongji1").first();
			List<Element> cityMonthliElements = cityAllDivElement.select("li");
			
			if(cityMonthliElements != null){
				for(Element lielement:cityMonthliElements){
					Element aElement = lielement.select("a").first();
					String href = aElement.attr("href");
					
					//text​() Gets the combined text of this element and all its children. 
					//ownText​() Gets the text owned by this element only; does not get the combined text of all children. 
					String urlName = aElement.ownText();
					
					if(href != null && !"".equals(href.trim())){
						if(!href.startsWith("#")){
							WeatherMonth weatherMonth = new WeatherMonth();
							
							// http://lishi.tianqi.com/anda/index.html
							String yearMonth = this.substrYearMonthFromUrl(href);
							
							weatherMonth.setAreaCode(areaCode);
							weatherMonth.setAreaName(areaName);
							
							weatherMonth.setYearMonth(yearMonth);
							
							weatherMonth.setUrl(href);
							weatherMonth.setUrlName(urlName);
							
							weatherMonth.setCrawlCount(0);
							weatherMonth.setCrawlFlag(WeatherCity.CRAWL_FLAG_INITIAL);
							weatherMonth.setValidStatus("1");
							
							weatherMonth.setInsertTime(new Date());
							weatherMonth.setUpdateTime(new Date());
							
							Map<String, Object> condition = new HashMap<>();
							condition.put("yearMonth", yearMonth);
							condition.put("areaCode", areaCode);
							
							Long resultCount = weatherMonthMapper.countWeatherMonthsByCondition(condition);
							if(resultCount == 0) {
								weatherMonthMapper.insertWeatherMonth(weatherMonth);
								logger.debug("保存城市天气年月信息," + areaCode + "," + yearMonth);
							}
							
						}
					}
				}
			}
		}
	}

	@Override
	public void crawlWeatherHistoryByMonth(String areaCode, String yearMonth) {
		
		// 根据areaCode查出来areaName，当然这一步是非必须的
		WeatherCity city = weatherCityMapper.findWeatherCityByAreaCode(areaCode);
		String areaName = city != null ? city.getAreaName() : "";
		
		// http://lishi.tianqi.com/beijing/201810.html
		
		try {
			String url = "http://lishi.tianqi.com/" + areaCode + "/" + yearMonth + ".html";
			
			Map<String, String> requestHeaders = new HashMap<>();
			
			requestHeaders.put("Host", "lishi.tianqi.com");
			requestHeaders.put("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:64.0) Gecko/20100101 Firefox/64.0");
			requestHeaders.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			requestHeaders.put("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
			requestHeaders.put("Accept-Encoding", "gzip, deflate");
			requestHeaders.put("Connection", "keep-alive");
			requestHeaders.put("Cookie", "cs_prov=04; cs_city=0401; ccity=101040100; Hm_lvt_ab6a683aa97a52202eab5b3a9042a8d2=1545100007,1547086544; UM_distinctid=168358b39e22db-0639973eb72783-143a7540-1fa400-168358b39e327f; CNZZDATA1275796416=1915182485-1547084074-%7C1547084074; Hm_lpvt_ab6a683aa97a52202eab5b3a9042a8d2=1547086573");
			
			String content = this.requestUrlByGetMethod(url, requestHeaders, "utf-8");
//			System.out.println(content);
			
			if(content != null) {
				Document document=Jsoup.parse(content);
				Element weatherAllDivElement = document.select("div.tqtongji2").first();
				List<Element> weatherHisliElements = weatherAllDivElement.select("ul");
				
				if(weatherHisliElements != null){
					for(Element ulelement:weatherHisliElements){
						List<Element> liElements = ulelement.select("li");
						
						if(liElements != null && liElements.size() > 0){
							if(liElements.get(0).ownText() == null || "日期".equals(liElements.get(0).ownText())){
								// 说明是表头行
								continue;
							}else{
								// 日期	 最高气温	最低气温	天气	风向	风力
								WeatherDay weatherDay = new WeatherDay();
								
								weatherDay.setAreaCode(areaCode);
								weatherDay.setAreaName(areaName);
								
								//text​() Gets the combined text of this element and all its children. 
								//ownText​() Gets the text owned by this element only; does not get the combined text of all children. 
								String dateStr = liElements.get(0).text();
								
								weatherDay.setDate(dateStr);
								
								String[] ary = dateStr.split("-");
								
								weatherDay.setYear(ary[0]);
								weatherDay.setMonth(ary[1]);
								weatherDay.setDay(ary[2]);
								
								String temperatureMaxStr = liElements.get(1).ownText();
								weatherDay.setTemperatureMax(Double.parseDouble(temperatureMaxStr));
								
								String temperatureMinStr = liElements.get(2).ownText();
								weatherDay.setTemperatureMin(Double.parseDouble(temperatureMinStr));
								
								String weatherSummary = liElements.get(3).ownText();
								weatherDay.setSummary(weatherSummary);
								
								String windDirection = liElements.get(4).ownText();
								weatherDay.setWindDirection(windDirection);
								
								String windScale = liElements.get(5).ownText();
								weatherDay.setWindScale(windScale);
								
								weatherDay.setValidStatus("1");
								weatherDay.setInsertTime(new Date());
								weatherDay.setUpdateTime(new Date());
								
								
								Map<String, Object> condition = new HashMap<>();
								condition.put("date", dateStr);
								condition.put("areaCode", areaCode);
								
								Long resultCount = weatherDayMapper.countWeatherDaysByCondition(condition);
								if(resultCount == 0) {
									weatherDayMapper.insertWeatherDay(weatherDay);
									logger.debug("保存城市天气信息," + areaCode + "," + dateStr);
								}
								
							}
						}
								
					}
				}
			}
			
			// 更新下WeatherMonth的crawlFlag标志位
			// 更新为下载成功
			WeatherMonth monthTemp = new WeatherMonth();
			monthTemp.setAreaCode(areaCode);
			monthTemp.setYearMonth(yearMonth);
			monthTemp.setCrawlFlag(WeatherCity.CRAWL_FLAG_SUCCESS);
			monthTemp.setUpdateTime(new Date());
			
			weatherMonthMapper.updateCrawlFlagByMonthAreaCodeAndYearMonth(monthTemp);
		} catch (Exception e) {
			e.printStackTrace();
			// 更新下WeatherMonth的crawlFlag标志位
			// 更新为下载失败
			
			WeatherMonth monthTemp = new WeatherMonth();
			monthTemp.setAreaCode(areaCode);
			monthTemp.setYearMonth(yearMonth);
			monthTemp.setCrawlFlag(WeatherCity.CRAWL_FLAG_FAIL);
			monthTemp.setUpdateTime(new Date());
			
			weatherMonthMapper.updateCrawlFlagByMonthAreaCodeAndYearMonth(monthTemp);
		}
	}
	
	/**
	 * 通过get的方式请求一个url
	 * @param url
	 * @param requestHeaders
	 * @param charset
	 * @return
	 */
	public String requestUrlByGetMethod(String url, Map<String, String> requestHeaders, String charset) {
		
		CloseableHttpClient httpclient = null;
		
        //实例化CloseableHttpClient对象
        if(this.useProxy){
        	// 使用代理服务器
        	
        	//设置代理IP、端口、协议（请分别替换）
        	HttpHost proxy = new HttpHost(proxyIp, port, "http");
        	
        	//把代理设置到请求配置
        	RequestConfig defaultRequestConfig = RequestConfig.custom().setProxy(proxy).build();
        	httpclient = HttpClients.custom().setDefaultRequestConfig(defaultRequestConfig).build();
        }else{
        	// 不使用代理服务器
        	httpclient = HttpClients.createDefault();
        }
        
		HttpGet httpget = new HttpGet(url);
		
		if(requestHeaders != null && requestHeaders.size() > 0) {
			for(Entry<String, String> entry:requestHeaders.entrySet()) {
				httpget.addHeader(entry.getKey(), entry.getValue());
			}
		}
		
		String content = null;
		CloseableHttpResponse response = null;
		try {
			response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				content = EntityUtils.toString(entity, charset);
                
		    }
			
			// 请求最小间隔3s
//			Thread.sleep(3 * 1000);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
		    try {
				if(response != null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}

	
	public static void main(String[] args) {
		WeatherHistoryServiceImpl weatherHistoryServiceImpl = new WeatherHistoryServiceImpl();
//		weatherHistoryServiceImpl.crawlWeatherCity();
//		System.out.println(weatherHistoryServiceImpl.substrYearMonthFromUrl("http://lishi.tianqi.com/beijing/201812.html"));
		
		System.out.println("beijing".substring(0, 1).toUpperCase());
		
	}
	
	/**
	 * 从url链接中截取地区代码
	 * 
	 * @param href
	 * @return
	 */
	public String substrAreaCodeFromUrl(String href){
		String areaCode = null;
		
		// 从url中截取地区代码的逻辑
		// 从.com/开始到下一个/之间的内容为地区代码
		int index1 = href.contains(".com/")?href.indexOf(".com/") + ".com/".length():0;
		String subString = href.substring(index1);
		
		int index2 = subString.contains("/")?subString.indexOf("/"):subString.length();
		areaCode = subString.substring(0, index2);
		
		return areaCode;
	}
	
	/**
	 * 从url链接中截取年月
	 * yyyyMM
	 * 
	 * @param href
	 * @return
	 */
	public String substrYearMonthFromUrl(String href){
		String yearMonth = null;
		
		// http://lishi.tianqi.com/beijing/201812.html
		// 从url中截取地区代码的逻辑
		// 从最后一个/到.htm之间的为年月
		int index1 = href.contains("/")?href.lastIndexOf("/")+1:0;
		int index2 = href.contains(".htm")?href.indexOf(".htm"):href.length();
		yearMonth = href.substring(index1, index2);
		
		return yearMonth;
	}
}
