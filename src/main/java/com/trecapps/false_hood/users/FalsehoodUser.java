package com.trecapps.false_hood.users;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trecapps.false_hood.commonLie.CommonLie;

@Table
@Entity
public class FalsehoodUser implements Comparable<FalsehoodUser>{
	
	@Id
	Long userId;
	
	@Column
	int credit;
	
	@Column
	String email;

	/**
	 * @param userId
	 * @param credit
	 */
	public FalsehoodUser(Long userId, int credit, String email) {
		super();
		this.userId = userId;
		this.credit = credit;
		this.email = email;
	}
	
	

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}



	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
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
	public Long getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(Long userId) {
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
	

	@Override
	public int compareTo(FalsehoodUser o) {
		return userId.compareTo(o.getUserId());
	}
}
