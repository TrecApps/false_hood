package com.trecapps.false_hood.publicFalsehoods;

import org.springframework.stereotype.Component;

@Component
public class InstitutionEntry {

	Institution insitution;
	
	String contents;

	/**
	 * 
	 */
	public InstitutionEntry() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param insitution
	 * @param contents
	 */
	public InstitutionEntry(Institution insitution, String contents) {
		super();
		this.insitution = insitution;
		this.contents = contents;
	}

	/**
	 * @return the insitution
	 */
	public Institution getInsitution() {
		return insitution;
	}

	/**
	 * @param insitution the insitution to set
	 */
	public void setInsitution(Institution insitution) {
		this.insitution = insitution;
	}

	/**
	 * @return the contents
	 */
	public String getContents() {
		return contents;
	}

	/**
	 * @param contents the contents to set
	 */
	public void setContents(String contents) {
		this.contents = contents;
	}
	
	
}
