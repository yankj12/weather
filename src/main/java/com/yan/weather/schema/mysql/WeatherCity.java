package com.yan.weather.schema.mysql;

import java.io.Serializable;
import java.util.Date;

public class WeatherCity implements Serializable{

	public static final String CRAWL_FLAG_INITIAL = "0";
	public static final String CRAWL_FLAG_SUCCESS = "1";
	public static final String CRAWL_FLAG_FAIL = "2";
	
	
	private static final long serialVersionUID = 1L;

	private Long id;
	
	// 地区
	private String areaName;

	private String areaCode;

	// 索引字母
	private String indexLetter;
	
	private String url;
	
	// 0未下载；1已下载；2错误；
	private String crawlFlag;
	
	private Integer crawlCount;
	
	private String remark;
	
	/**
	 * 有效状态
	 */
	private String validStatus;

	/**
	 * 插入时间
	 */
	private Date insertTime;
	
	/**
	 * 修改时间
	 */
	private Date updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAreaName() {
		return areaName;
	}

	public void setAreaName(String areaName) {
		this.areaName = areaName;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getIndexLetter() {
		return indexLetter;
	}

	public void setIndexLetter(String indexLetter) {
		this.indexLetter = indexLetter;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCrawlFlag() {
		return crawlFlag;
	}

	public void setCrawlFlag(String crawlFlag) {
		this.crawlFlag = crawlFlag;
	}

	public Integer getCrawlCount() {
		return crawlCount;
	}

	public void setCrawlCount(Integer crawlCount) {
		this.crawlCount = crawlCount;
	}

	public String getValidStatus() {
		return validStatus;
	}

	public void setValidStatus(String validStatus) {
		this.validStatus = validStatus;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	
}
