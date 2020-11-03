package com.trecapps.false_hood.falsehoods;

public class MediaOutletEntry {

	MediaOutlet outlet;
	
	String text;

	
	
	/**
	 * @param outlet
	 * @param text
	 */
	public MediaOutletEntry(MediaOutlet outlet, String text) {
		super();
		this.outlet = outlet;
		this.text = text;
	}

	/**
	 * 
	 */
	public MediaOutletEntry() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the outlet
	 */
	public MediaOutlet getOutlet() {
		return outlet;
	}

	/**
	 * @param outlet the outlet to set
	 */
	public void setOutlet(MediaOutlet outlet) {
		this.outlet = outlet;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}
	
	
}
