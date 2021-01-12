package com.trecapps.false_hood.publicFalsehoods;

import java.util.List;

import org.springframework.stereotype.Component;

import com.trecapps.false_hood.json.EventObj;
import com.trecapps.false_hood.json.VerdictObj;

@Component
public class FullPublicFalsehood {

    String contents;

    PublicFalsehood metadata;

	List<VerdictObj> verdicts;
	List<EventObj> events;

    public FullPublicFalsehood() {
    }
    
    /**
	 * @param contents
	 * @param metadata
	 * @param keywords
	 * @param events
	 */
	public FullPublicFalsehood(String contents, PublicFalsehood metadata, List<VerdictObj> verdicts,
			List<EventObj> events) {
		super();
		this.contents = contents;
		this.metadata = metadata;
		this.verdicts = verdicts;
		this.events = events;
	}

	public FullPublicFalsehood clone()
    {
    	return new FullPublicFalsehood(contents, metadata.clone(), verdicts, events);
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public PublicFalsehood getMetadata() {
        return metadata;
    }

    public void setMetadata(PublicFalsehood metadata) {
        this.metadata = metadata;
    }

	/**
	 * @return the verdicts
	 */
	public List<VerdictObj> getVerdicts() {
		return verdicts;
	}

	/**
	 * @param verdicts the verdicts to set
	 */
	public void setVerdicts(List<VerdictObj> verdicts) {
		this.verdicts = verdicts;
	}

	/**
	 * @return the events
	 */
	public List<EventObj> getEvents() {
		return events;
	}

	/**
	 * @param events the events to set
	 */
	public void setEvents(List<EventObj> events) {
		this.events = events;
	}

    
}
