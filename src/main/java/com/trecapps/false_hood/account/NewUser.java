package com.trecapps.false_hood.account;

import java.sql.Date;
import java.util.Calendar;

import javax.validation.constraints.Email;

import org.springframework.stereotype.Component;

@Component
public class NewUser {

	String firstName;
	String lastName;
	String username;
	String mainEmail;
	String trecEmail;
	String backupEmail;
	String password;
	Date birthday;
	
	String clientId;
	
	// Security features
	byte passwordMonthReset;  // How many months before Password needs to be Changed
	byte timeForValidToken;   // How long after login that the token should last (by 10 minutes)
	byte validTimeFromActivity; // Whether apps should update the token every activity
	
	byte maxLoginAttempts; // How many login attempts per hour 
	byte lockTime; // How long to lock the account for 
	
	
	public NewUser(String firstName, String lastName, String username, @Email String mainEmail, String trecEmail,
			@Email String backupEmail, String password, Date birthday, String clientId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.mainEmail = mainEmail;
		this.trecEmail = trecEmail;
		this.backupEmail = backupEmail;
		this.password = password;
		this.birthday = birthday;
		
		this.passwordMonthReset = (byte)0;
		this.timeForValidToken = (byte)0;
		this.maxLoginAttempts = (byte)5;
		this.lockTime = (byte)6;
		
		this.clientId = clientId;
	}
	
	
	
	
	/**
	 * @param firstName
	 * @param lastName
	 * @param username
	 * @param mainEmail
	 * @param trecEmail
	 * @param backupEmail
	 * @param password
	 * @param birthday
	 * @param passwordMonthReset
	 * @param timeForValidToken
	 * @param validTimeFromActivity
	 * @param maxLoginAttempts
	 * @param lockTime
	 */
	public NewUser(String firstName, String lastName, String username, @Email String mainEmail, String trecEmail,
			@Email String backupEmail, String password, Date birthday, byte passwordMonthReset, byte timeForValidToken,
			byte validTimeFromActivity, byte maxLoginAttempts, byte lockTime, String clientId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.mainEmail = mainEmail;
		this.trecEmail = trecEmail;
		this.backupEmail = backupEmail;
		this.password = password;
		this.birthday = birthday;
		this.passwordMonthReset = passwordMonthReset;
		this.timeForValidToken = timeForValidToken;
		this.validTimeFromActivity = validTimeFromActivity;
		this.maxLoginAttempts = maxLoginAttempts;
		this.lockTime = lockTime;
		
		this.clientId = clientId;
	}




	/**
	 * @return the clientId
	 */
	public String getClientId() {
		return clientId;
	}




	/**
	 * @param clientId the clientId to set
	 */
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}




	public NewUser() {
		super();
	}
	
	
	
	/**
	 * @return the passwordMonthReset
	 */
	public byte getPasswordMonthReset() {
		return passwordMonthReset;
	}

	/**
	 * @param passwordMonthReset the passwordMonthReset to set
	 */
	public void setPasswordMonthReset(byte passwordMonthReset) {
		this.passwordMonthReset = passwordMonthReset;
	}

	/**
	 * @return the timeForValidToken
	 */
	public byte getTimeForValidToken() {
		return timeForValidToken;
	}

	/**
	 * @param timeForValidToken the timeForValidToken to set
	 */
	public void setTimeForValidToken(byte timeForValidToken) {
		this.timeForValidToken = timeForValidToken;
	}

	/**
	 * @return the validTimeFromActivity
	 */
	public byte getValidTimeFromActivity() {
		return validTimeFromActivity;
	}

	/**
	 * @param validTimeFromActivity the validTimeFromActivity to set
	 */
	public void setValidTimeFromActivity(byte validTimeFromActivity) {
		this.validTimeFromActivity = validTimeFromActivity;
	}

	/**
	 * @return the maxLoginAttempts
	 */
	public byte getMaxLoginAttempts() {
		return maxLoginAttempts;
	}

	/**
	 * @param maxLoginAttempts the maxLoginAttempts to set
	 */
	public void setMaxLoginAttempts(byte maxLoginAttempts) {
		this.maxLoginAttempts = maxLoginAttempts;
	}

	/**
	 * @return the lockTime
	 */
	public byte getLockTime() {
		return lockTime;
	}

	/**
	 * @param lockTime the lockTime to set
	 */
	public void setLockTime(byte lockTime) {
		this.lockTime = lockTime;
	}

	@Override
	public String toString() {
		return "NewUser [firstName=" + firstName + ", lastName=" + lastName + ", username=" + username + ", mainEmail="
				+ mainEmail + ", trecEmail=" + trecEmail + ", backupEmail=" + backupEmail + ", password=" + password
				+ ", birthday=" + birthday + ", passwordMonthReset=" + passwordMonthReset + ", timeForValidToken="
				+ timeForValidToken + ", validTimeFromActivity=" + validTimeFromActivity + ", maxLoginAttempts="
				+ maxLoginAttempts + ", lockTime=" + lockTime + "]";
	}

	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getMainEmail() {
		return mainEmail;
	}
	public void setMainEmail(String mainEmail) {
		this.mainEmail = mainEmail;
	}
	public String getTrecEmail() {
		return trecEmail;
	}
	public void setTrecEmail(String trecEmail) {
		this.trecEmail = trecEmail;
	}
	public String getBackupEmail() {
		return backupEmail;
	}
	public void setBackupEmail(String backupEmail) {
		this.backupEmail = backupEmail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Date getBirthday() {
		return birthday;
	}
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}
	
	
}
