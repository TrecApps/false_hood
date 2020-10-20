package com.trecapps.false_hood.falsehoods;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.trecapps.false_hood.commonLie.CommonLie;

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
	
	

	public MediaOutlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MediaOutlet(Integer outletId, short foundationYear, String name) {
		super();
		this.outletId = outletId;
		this.foundationYear = foundationYear;
		this.name = name;
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
