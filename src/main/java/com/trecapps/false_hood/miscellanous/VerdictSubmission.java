package com.trecapps.false_hood.miscellanous;

import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
public class VerdictSubmission {

    String comment;

    BigInteger falsehood;

    public VerdictSubmission() {
    }

    public VerdictSubmission(String comment, BigInteger falsehood) {
        this.comment = comment;
        this.falsehood = falsehood;
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
