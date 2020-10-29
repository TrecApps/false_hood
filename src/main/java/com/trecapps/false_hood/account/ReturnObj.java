package com.trecapps.false_hood.account;

public class ReturnObj {

	String token;
	
	String username;
	
	String firstname;
	
	String lastname;
	
	String color;

	/**
	 * @param token
	 * @param username
	 * @param firstname
	 * @param lastname
	 * @param color
	 */
	public ReturnObj(String token, String username, String firstname, String lastname, String color) {
		super();
		this.token = token;
		this.username = username;
		this.firstname = firstname;
		this.lastname = lastname;
		this.color = color;
	}

	/**
	 * 
	 */
	public ReturnObj() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the token
	 */
	public String getToken() {
		return token;
	}

	/**
	 * @param token the token to set
	 */
	public void setToken(String token) {
		this.token = token;
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the firstname
	 */
	public String getFirstname() {
		return firstname;
	}

	/**
	 * @param firstname the firstname to set
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * @return the lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * @param lastname the lastname to set
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * @return the color
	 */
	public String getColor() {
		return color;
	}

	/**
	 * @param color the color to set
	 */
	public void setColor(String color) {
		this.color = color;
	}
	
	
}
