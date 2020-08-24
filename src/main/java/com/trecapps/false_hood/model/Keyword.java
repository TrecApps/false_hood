package com.trecapps.false_hood.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table
public class Keyword {

	@Id
	String word;
	
	@ManyToMany
	List<Falsehood> falsehoods;
	
	

	/**
	 * 
	 */
	public Keyword() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Keyword(String word, List<Falsehood> falsehoods) {
		super();
		this.word = word;
		this.falsehoods = falsehoods;
	}

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public List<Falsehood> getFalsehoods() {
		return falsehoods;
	}

	public void setFalsehoods(List<Falsehood> falsehoods) {
		this.falsehoods = falsehoods;
	}
	
	
}
