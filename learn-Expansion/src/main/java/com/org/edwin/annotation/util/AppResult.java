package com.org.edwin.annotation.util;

public class AppResult {

	public String code;
	public String msg;
	public String getCode() {
		return code;
	}
	public AppResult setCode(String code) {
		this.code = code;
		return new AppResult();
	}
	public String getMsg() {
		return msg;
	}
	public AppResult setMsg(String msg) {
		this.msg = msg;
		return new AppResult();
	}
}
