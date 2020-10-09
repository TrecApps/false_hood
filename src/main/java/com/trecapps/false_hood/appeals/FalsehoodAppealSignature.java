package com.trecapps.false_hood.appeals;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trecapps.false_hood.users.FalsehoodUser;

@Entity
@Table
public class FalsehoodAppealSignature {

	@Id
	BigInteger id;
	
	@Column
	FalsehoodAppeal appeal;
	
	@Column
	FalsehoodUser user;
	
	@Column
	String verificationString;
	
	@Column
	byte grantAnon;

	/**
	 * 
	 */
	public FalsehoodAppealSignature() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param appeal
	 * @param user
	 * @param verificationString
	 * @param grantAnon
	 */
	public FalsehoodAppealSignature(BigInteger id, FalsehoodAppeal appeal, FalsehoodUser user,
			String verificationString, byte grantAnon) {
		super();
		this.id = id;
		this.appeal = appeal;
		this.user = user;
		this.verificationString = verificationString;
		this.grantAnon = grantAnon;
	}

	/**
	 * @return the id
	 */
	public BigInteger getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(BigInteger id) {
		this.id = id;
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

	/**
	 * @return the user
	 */
	public FalsehoodUser getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(FalsehoodUser user) {
		this.user = user;
	}

	/**
	 * @return the verificationString
	 */
	public String getVerificationString() {
		return verificationString;
	}

	/**
	 * @param verificationString the verificationString to set
	 */
	public void setVerificationString(String verificationString) {
		this.verificationString = verificationString;
	}

	/**
	 * @return the grantAnon
	 */
	public byte getGrantAnon() {
		return grantAnon;
	}

	/**
	 * @param grantAnon the grantAnon to set
	 */
	public void setGrantAnon(byte grantAnon) {
		this.grantAnon = grantAnon;
	}
	
	
}
