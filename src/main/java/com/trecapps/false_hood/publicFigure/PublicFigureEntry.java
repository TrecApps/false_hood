package com.trecapps.false_hood.publicFigure;

import org.springframework.stereotype.Component;

@Component
public class PublicFigureEntry {

	PublicFigure figure;
	
	String text;

	/**
	 * 
	 */
	public PublicFigureEntry() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param figure
	 * @param text
	 */
	public PublicFigureEntry(PublicFigure figure, String text) {
		super();
		this.figure = figure;
		this.text = text;
	}

	/**
	 * @return the figure
	 */
	public PublicFigure getFigure() {
		return figure;
	}

	/**
	 * @param figure the figure to set
	 */
	public void setFigure(PublicFigure figure) {
		this.figure = figure;
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
