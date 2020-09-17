package com.trecapps.false_hood.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
public class FalsehoodUser {
	
	@Id
	long userId;
	
	@Column
	int credit;

	/**
	 * @param userId
	 * @param credit
	 */
	public FalsehoodUser(long userId, int credit) {
		super();
		this.userId = userId;
		this.credit = credit;
	}

	/**
	 * 
	 */
	public FalsehoodUser() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * @return the credit
	 */
	public int getCredit() {
		return credit;
	}

	/**
	 * @param credit the credit to set
	 */
	public void setCredit(int credit) {
		this.credit = credit;
	}
	
	
}
