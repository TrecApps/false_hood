package com.trecapps.false_hood.falsehoods;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.trecapps.false_hood.commonLie.CommonLie;
import com.trecapps.false_hood.users.FalsehoodUser;

@Entity
@Table
public class MediaOutlet implements Comparable<MediaOutlet>{


	@Override
	public int compareTo(MediaOutlet o) {
		return outletId.compareTo(o.getOutletId());
	}
	
	@Id
	Integer outletId;
	
	@Column
	short foundationYear;
	
	@Column
	String name;
	
	@Column
	byte approved;
	
	@ManyToOne
	@JoinColumn
	FalsehoodUser submitter;

	public MediaOutlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MediaOutlet(Integer outletId, short foundationYear, String name, byte approved, FalsehoodUser submitter) {
		super();
		this.outletId = outletId;
		this.foundationYear = foundationYear;
		this.name = name;
		this.approved = approved;
		this.submitter = submitter;
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

	public Integer getOutletId() {
		return outletId;
	}

	public void setOutletId(Integer outletId) {
		this.outletId = outletId;
	}

	public short getFoundationYear() {
		return foundationYear;
	}

	public void setFoundationYear(short foundationYear) {
		this.foundationYear = foundationYear;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
}
