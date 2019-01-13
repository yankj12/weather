package com.yan.weather.schema.mysql;

import java.util.Date;

public class WeatherConfig {

	private Long id;
	
	/**
	 * 开关（配置）代码
	 */
	private String configCode;

	/**
	 * 开关（配置）名称
	 */
	private String configName;
	
	/**
	 * 开关值
	 */
	private String configValue;
	
	/**
	 * 开关（配置）描述
	 */
	private String description;
	
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

	public String getConfigCode() {
		return configCode;
	}

	public void setConfigCode(String configCode) {
		this.configCode = configCode;
	}

	public String getConfigName() {
		return configName;
	}

	public void setConfigName(String configName) {
		this.configName = configName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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

	public String getConfigValue() {
		return configValue;
	}

	public void setConfigValue(String configValue) {
		this.configValue = configValue;
	}
	
}
