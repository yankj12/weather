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
import com.yan.weather.schema.mysql.WeatherCity;
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
		System.out.println(content);
		
		if(content != null) {
			Document document=Jsoup.parse(content);
			Element cityAllDivElement = document.select("div#cityall").first();
			List<Element> cityliElements = cityAllDivElement.select("li");
			
			List<WeatherCity> weatherCities = new ArrayList<WeatherCity>();
			
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

	@Override
	public void crawlWeatherHistoryByCity(WeatherCity weatherCity) {
		// TODO Auto-generated method stub
		// http://lishi.tianqi.com/beijing/index.html
	}

	@Override
	public void crawlWeatherHistoryByMonth(WeatherMonth weatherMonth) {
		// TODO Auto-generated method stub
		// http://lishi.tianqi.com/beijing/201810.html
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
		weatherHistoryServiceImpl.crawlWeatherCity();
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
}
