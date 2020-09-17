package com.trecapps.false_hood.model;

import org.springframework.stereotype.Component;

@Component
public class FullFalsehood {

	String contents;
	
	Falsehood metadata;

	/**
	 * @param contents
	 * @param metadata
	 */
	public FullFalsehood(String contents, Falsehood metadata) {
		super();
		this.contents = contents;
		this.metadata = metadata;
	}

	/**
	 * 
	 */
	public FullFalsehood() {
		super();
		// TODO Auto-generated constructor stub
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

	/**
	 * @return the metadata
	 */
	public Falsehood getMetadata() {
		return metadata;
	}

	/**
	 * @param metadata the metadata to set
	 */
	public void setMetadata(Falsehood metadata) {
		this.metadata = metadata;
	}
	
	
}
