package com.trecapps.false_hood.model;

import java.math.BigInteger;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CommonLieSubmission {

	CommonLie lie;
	
	List<BigInteger> falsehoods;

	/**
	 * @param lie
	 * @param falsehoods
	 */
	public CommonLieSubmission(CommonLie lie, List<BigInteger> falsehoods) {
		super();
		this.lie = lie;
		this.falsehoods = falsehoods;
	}

	/**
	 * 
	 */
	public CommonLieSubmission() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the lie
	 */
	public CommonLie getLie() {
		return lie;
	}

	/**
	 * @param lie the lie to set
	 */
	public void setLie(CommonLie lie) {
		this.lie = lie;
	}

	/**
	 * @return the falsehoods
	 */
	public List<BigInteger> getFalsehoods() {
		return falsehoods;
	}

	/**
	 * @param falsehoods the falsehoods to set
	 */
	public void setFalsehoods(List<BigInteger> falsehoods) {
		this.falsehoods = falsehoods;
	}
	
	
}
