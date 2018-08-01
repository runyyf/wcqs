package com.yyf.wcqs.vo;

/**
 * Author: tang_botao
 * DateTime: 13-11-12 下午3:30
 * Explain:AJax数据返回模型
 */
public class AjaxData {
	//状态码---成功
	public static final Integer STATUS_SUCCESS = 0;
	//状态码---异常
	public static final Integer STATUS_ERROR = 1;

	//状态
	private Integer status;
	//消息
	private String message;
	//数据
	private Object data;

	public AjaxData() {
	}

	public AjaxData(Integer status, String message) {
		this.status = status;
		this.message = message;
	}

	public AjaxData(Integer status, String message, Object data) {
		this.status = status;
		this.message = message;
		this.data = data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
