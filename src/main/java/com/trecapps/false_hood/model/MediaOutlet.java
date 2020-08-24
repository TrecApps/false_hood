package com.trecapps.false_hood.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table
public class MediaOutlet {

	@Id
	int outletId;
	
	@Column
	short foundationYear;
	
	@Column
	String name;
	
	

	public MediaOutlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MediaOutlet(int outletId, short foundationYear, String name) {
		super();
		this.outletId = outletId;
		this.foundationYear = foundationYear;
		this.name = name;
	}

	public int getOutletId() {
		return outletId;
	}

	public void setOutletId(int outletId) {
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
