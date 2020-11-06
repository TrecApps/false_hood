package com.trecapps.false_hood.falsehoods;

import org.springframework.stereotype.Component;

@Component
public class FullFalsehood {

	String contents;
	
	Falsehood metadata;
	
	String keywords;

	/**
	 * @param contents
	 * @param metadata
	 */
	public FullFalsehood(String contents, Falsehood metadata, String keywords) {
		super();
		this.contents = contents;
		this.metadata = metadata;
		this.keywords = keywords;
	}
	
	public FullFalsehood clone()
	{
		return new FullFalsehood(contents, metadata.clone(), keywords);
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

	/**
	 * @return the keywords
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * @param keywords the keywords to set
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	
}
