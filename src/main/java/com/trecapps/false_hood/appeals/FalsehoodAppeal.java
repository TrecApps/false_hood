package com.trecapps.false_hood.appeals;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.trecapps.false_hood.falsehoods.Falsehood;
import com.trecapps.false_hood.users.FalsehoodUser;

@Entity
@Table
public class FalsehoodAppeal 
{
	@Id
	BigInteger id;
	
	@ManyToOne
	@JoinColumn
	Falsehood falsehood;
	
	@Column
	String desiredState;
	
	@ManyToOne
	@JoinColumn
	FalsehoodUser petitioner;

	@Column
	byte ratified;   // Has this appeal gotten enough verified signatures to be worth looking at?

	/**
	 * 
	 */
	public FalsehoodAppeal() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param id
	 * @param falsehood
	 * @param desiredState
	 * @param petitioner
	 */
	public FalsehoodAppeal(BigInteger id, Falsehood falsehood, String desiredState, FalsehoodUser petitioner) {
		super();
		this.id = id;
		this.falsehood = falsehood;
		this.desiredState = desiredState;
		this.petitioner = petitioner;
		this.ratified = (byte)0;
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
	 * @return the falsehood
	 */
	public Falsehood getFalsehood() {
		return falsehood;
	}

	/**
	 * @param falsehood the falsehood to set
	 */
	public void setFalsehood(Falsehood falsehood) {
		this.falsehood = falsehood;
	}

	/**
	 * @return the desiredState
	 */
	public String getDesiredState() {
		return desiredState;
	}

	/**
	 * @param desiredState the desiredState to set
	 */
	public void setDesiredState(String desiredState) {
		this.desiredState = desiredState;
	}

	/**
	 * @return the petitioner
	 */
	public FalsehoodUser getPetitioner() {
		return petitioner;
	}

	/**
	 * @param petitioner the petitioner to set
	 */
	public void setPetitioner(FalsehoodUser petitioner) {
		this.petitioner = petitioner;
	}

	/**
	 * @return the ratified
	 */
	public byte getRatified() {
		return ratified;
	}

	/**
	 * @param ratified the ratified to set
	 */
	public void setRatified(byte ratified) {
		this.ratified = ratified;
	}
	
	
}
