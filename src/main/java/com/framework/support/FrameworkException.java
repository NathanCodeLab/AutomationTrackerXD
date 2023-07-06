package com.framework.support;

public class FrameworkException extends RuntimeException{
	
	public String errorName;
	
	public FrameworkException(final String errorDes) {
		super(errorDes);
		this.errorName = "Error";
	}
	public FrameworkException(final String errorName, final String errorDes) {
		super(errorDes);
		this.errorName ="Error";
		this.errorName = errorName;
	}

}
