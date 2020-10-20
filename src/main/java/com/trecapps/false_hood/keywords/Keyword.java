package com.trecapps.false_hood.keywords;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.trecapps.false_hood.commonLie.CommonLie;
import com.trecapps.false_hood.falsehoods.Falsehood;

@Entity
@Table
public class Keyword implements Comparable<Keyword>{

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
	

	@Override
	public int compareTo(Keyword o) {
		return word.compareTo(o.getWord());
	}
}
