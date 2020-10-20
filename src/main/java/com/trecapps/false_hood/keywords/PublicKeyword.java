package com.trecapps.false_hood.keywords;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.trecapps.false_hood.commonLie.CommonLie;
import com.trecapps.false_hood.publicFalsehoods.PublicFalsehood;

@Entity
@Table
public class PublicKeyword implements Comparable<PublicKeyword>{

	@Override
	public int compareTo(PublicKeyword o) {
		return word.compareTo(o.getWord());
	}
	
	@Id
	String word;
	
	@ManyToMany
	List<PublicFalsehood> falsehoods;
	
	

	/**
	 * 
	 */
	public PublicKeyword() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PublicKeyword(String word, List<PublicFalsehood> falsehoods) {
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

	public List<PublicFalsehood> getFalsehoods() {
		return falsehoods;
	}

	public void setFalsehoods(List<PublicFalsehood> falsehoods) {
		this.falsehoods = falsehoods;
	}
}
