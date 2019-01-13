package com.yan.weather.common;

/**
 * 有效状态枚举类型
 * 
 * @author Yan
 *
 */
public enum ValidStatusEnum {

	VALID("有效的", "1"), INVALID("无效的", "0");
	
	private String name;
	private String code;
	private ValidStatusEnum(String name, String code) {
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
