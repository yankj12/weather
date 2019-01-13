package com.yan.weather.common;

/**
 * 爬取状态枚举值
 * 
 * @author Yan
 *
 */
public enum CrawlFlagEnum {

	INITIAL("初始值", "0"), DOWNLOAD_SUCCESS("下载成功", "1"), DOWNLOAD_FAIL("下载失败", "2");
	
	private String name;
	
	private String code;

	private CrawlFlagEnum(String name, String code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
}
