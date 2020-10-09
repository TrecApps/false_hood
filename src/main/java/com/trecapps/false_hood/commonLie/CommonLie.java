package com.trecapps.false_hood.commonLie;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
public class CommonLie {

	@Id
	long id;
	
	@Column
	String title;
	
	

	/**
	 * 
	 */
	public CommonLie() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CommonLie(long id, String title) {
		super();
		this.id = id;
		this.title = title;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	
}
