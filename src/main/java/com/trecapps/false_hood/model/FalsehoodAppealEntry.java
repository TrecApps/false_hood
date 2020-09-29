package com.trecapps.false_hood.model;

import org.springframework.stereotype.Component;

@Component
public class FalsehoodAppealEntry {

	String reason;
	
	FalsehoodAppeal appeal;

	/**
	 * 
	 */
	public FalsehoodAppealEntry() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param reason
	 * @param appeal
	 */
	public FalsehoodAppealEntry(String reason, FalsehoodAppeal appeal) {
		super();
		this.reason = reason;
		this.appeal = appeal;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason to set
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/**
	 * @return the appeal
	 */
	public FalsehoodAppeal getAppeal() {
		return appeal;
	}

	/**
	 * @param appeal the appeal to set
	 */
	public void setAppeal(FalsehoodAppeal appeal) {
		this.appeal = appeal;
	}
	
	
}
