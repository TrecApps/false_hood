package com.trecapps.false_hood.miscellanous;

import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class VerdictSubmission {

    String comment;

    BigInteger falsehood;
    
    boolean shouldStrike;

    public VerdictSubmission() {
    	shouldStrike = false;
    }

    public VerdictSubmission(String comment, BigInteger falsehood, boolean shouldStrike) {
        this.comment = comment;
        this.falsehood = falsehood;
        this.shouldStrike = shouldStrike;
    }
    
    public VerdictSubmission(String comment, BigInteger falsehood) {
        this.comment = comment;
        this.falsehood = falsehood;
        this.shouldStrike = false;
    }
    
    

    /**
	 * @return the shouldStrike
	 */
	public boolean isShouldStrike() {
		return shouldStrike;
	}

	/**
	 * @param shouldStrike the shouldStrike to set
	 */
	public void setShouldStrike(boolean shouldStrike) {
		this.shouldStrike = shouldStrike;
	}

	public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public BigInteger getFalsehood() {
        return falsehood;
    }

    public void setFalsehood(BigInteger falsehood) {
        this.falsehood = falsehood;
    }
}
