package com.trecapps.false_hood.falsehoods;

import java.sql.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.trecapps.false_hood.miscellanous.Severity;

@Component
public class SearchFalsehood 
{
	/**
	 * Used to provide Search Terms Google Style
	 */
	String terms;
	
	/**
	 * Limit hits via date
	 */
	Date to, from;
	
	/**
	 * List of Media Outlets to restrict falsehoods to
	 */
	List<String> outlets;
	
	/**
	 * Max Number of Entries to attempt to retrieve
	 */
	int numberOfEntries;
	
	/**
	 * Minimum Severity vs Maximum severity
	 */
	Severity minimum, maximum;
	
	/**
	 * Authors involved in the list
	 */
	List<String> authors;
	
	

	/**
	 * @param terms
	 * @param to
	 * @param from
	 * @param outlets
	 * @param numberOfEntries
	 * @param minimum
	 * @param maximum
	 * @param authors
	 */
	public SearchFalsehood(String terms, Date to, Date from, List<String> outlets, int numberOfEntries,
			Severity minimum, Severity maximum, List<String> authors) {
		super();
		this.terms = terms;
		this.to = to;
		this.from = from;
		this.outlets = outlets;
		this.numberOfEntries = numberOfEntries;
		this.minimum = minimum;
		this.maximum = maximum;
		this.authors = authors;
	}

	public SearchFalsehood() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the terms
	 */
	public String getTerms() {
		return terms;
	}

	/**
	 * @param terms the terms to set
	 */
	public void setTerms(String terms) {
		this.terms = terms;
	}

	/**
	 * @return the to
	 */
	public Date getTo() {
		return to;
	}

	/**
	 * @param to the to to set
	 */
	public void setTo(Date to) {
		this.to = to;
	}

	/**
	 * @return the from
	 */
	public Date getFrom() {
		return from;
	}

	/**
	 * @param from the from to set
	 */
	public void setFrom(Date from) {
		this.from = from;
	}

	/**
	 * @return the outlets
	 */
	public List<String> getOutlets() {
		return outlets;
	}

	/**
	 * @param outlets the outlets to set
	 */
	public void setOutlets(List<String> outlets) {
		this.outlets = outlets;
	}

	/**
	 * @return the numberOfEntries
	 */
	public int getNumberOfEntries() {
		return numberOfEntries;
	}

	/**
	 * @param numberOfEntries the numberOfEntries to set
	 */
	public void setNumberOfEntries(int numberOfEntries) {
		this.numberOfEntries = numberOfEntries;
	}

	/**
	 * @return the minimum
	 */
	public Severity getMinimum() {
		return minimum;
	}

	/**
	 * @param minimum the minimum to set
	 */
	public void setMinimum(Severity minimum) {
		this.minimum = minimum;
	}

	/**
	 * @return the maximum
	 */
	public Severity getMaximum() {
		return maximum;
	}

	/**
	 * @param maximum the maximum to set
	 */
	public void setMaximum(Severity maximum) {
		this.maximum = maximum;
	}

	/**
	 * @return the authors
	 */
	public List<String> getAuthors() {
		return authors;
	}

	/**
	 * @param authors the authors to set
	 */
	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}
	
	
}
