/*
 * Base_Model.java
 * @author Andrew Lee
 * 2014-10-20 上午11:02:04
 */
package com.name.replace.domain;

import java.io.Serializable;

/**
 * Base_Model.java description:
 * 
 * @author Andrew Lee version 2014-10-20 上午11:02:04
 */
public class BaseModel<T> implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3676969309433142938L;
	private int resultCode;
	private String message;
	private T data;

	public BaseModel() {
		super();
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public boolean isSuccess() {
		if (this.getResultCode() == 1) {
			return true;
		} else {
			return false;
		}
	}

}
