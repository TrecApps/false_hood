package com.trecapps.false_hood.publicFalsehoods;

import com.trecapps.false_hood.miscellanous.Severity;
import com.trecapps.false_hood.publicFigure.PublicFigure;

import java.sql.Date;
import java.util.List;

public class SearchPublicFalsehood
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
     * Regions
     */
    List<Region> regions;

    /**
     * Institution
     */
    List<Institution> institutions;

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
    List<PublicFigure> authors;

    public SearchPublicFalsehood() {
    }

    public SearchPublicFalsehood(String terms, Date to, Date from,
        List<Region> regions, List<Institution> institutions, int numberOfEntries,
        Severity minimum, Severity maximum, List<PublicFigure> authors)
    {
        this.terms = terms;
        this.to = to;
        this.from = from;
        this.regions = regions;
        this.institutions = institutions;
        this.numberOfEntries = numberOfEntries;
        this.minimum = minimum;
        this.maximum = maximum;
        this.authors = authors;
    }

    public String getTerms() {
        return terms;
    }

    public void setTerms(String terms) {
        this.terms = terms;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public List<Region> getRegions() {
        return regions;
    }

    public void setRegions(List<Region> regions) {
        this.regions = regions;
    }

    public List<Institution> getInstitutions() {
        return institutions;
    }

    public void setInstitutions(List<Institution> institutions) {
        this.institutions = institutions;
    }

    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    public void setNumberOfEntries(int numberOfEntries) {
        this.numberOfEntries = numberOfEntries;
    }

    public Severity getMinimum() {
        return minimum;
    }

    public void setMinimum(Severity minimum) {
        this.minimum = minimum;
    }

    public Severity getMaximum() {
        return maximum;
    }

    public void setMaximum(Severity maximum) {
        this.maximum = maximum;
    }

    public List<PublicFigure> getAuthors() {
        return authors;
    }

    public void setAuthors(List<PublicFigure> authors) {
        this.authors = authors;
    }
}
