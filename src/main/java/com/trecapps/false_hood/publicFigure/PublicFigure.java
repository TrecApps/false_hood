package com.trecapps.false_hood.publicFigure;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trecapps.false_hood.users.FalsehoodUser;

@Table
@Entity
public class PublicFigure
{

	@Id
	Long id;
	
	@Column
	String firstname;
	
	@Column
	String middleNames;
	
	@Column
	String lastName;
	
	@Column
	byte approved;
	
	@Column
	FalsehoodUser submitter;

	/**
	 * @param id
	 * @param firstname
	 * @param middleNames
	 * @param lastName
	 */
	public PublicFigure(Long id, String firstname, String middleNames, String lastName) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.middleNames = middleNames;
		this.lastName = lastName;
		approved = (byte)0;
	}
	
	
	

	/**
	 * @return the submitter
	 */
	public FalsehoodUser getSubmitter() {
		return submitter;
	}




	/**
	 * @param submitter the submitter to set
	 */
	public void setSubmitter(FalsehoodUser submitter) {
		this.submitter = submitter;
	}




	/**
	 * @return the approved
	 */
	public byte getApproved() {
		return approved;
	}



	/**
	 * @param approved the approved to set
	 */
	public void setApproved(byte approved) {
		this.approved = approved;
	}



	/**
	 * 
	 */
	public PublicFigure() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
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
	 * @return the middleNames
	 */
	public String getMiddleNames() {
		return middleNames;
	}

	/**
	 * @param middleNames the middleNames to set
	 */
	public void setMiddleNames(String middleNames) {
		this.middleNames = middleNames;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName the lastName to set
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	
}
