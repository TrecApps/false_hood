package com.trecapps.false_hood.publicFalsehoods;

import org.springframework.stereotype.Component;

@Component
public class RegionEntry {

	Region region;
	
	String contents;

	
	
	/**
	 * 
	 */
	public RegionEntry() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param region
	 * @param contents
	 */
	public RegionEntry(Region region, String contents) {
		super();
		this.region = region;
		this.contents = contents;
	}

	/**
	 * @return the region
	 */
	public Region getRegion() {
		return region;
	}

	/**
	 * @param region the region to set
	 */
	public void setRegion(Region region) {
		this.region = region;
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
