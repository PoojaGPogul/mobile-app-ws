package com.example.app.ws.ui.model.response;

import java.util.Date;

public class ErrorMessage {
	private Date timeStamp;
	private String message;

	public ErrorMessage() {
		
	}
	public ErrorMessage(Date timeStamp, String message) {
		this.timeStamp = timeStamp;
		this.message = message;
	}
	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timStamp) {
		this.timeStamp = timStamp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
