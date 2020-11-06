package com.trecapps.false_hood.publicFalsehoods;

import org.springframework.stereotype.Component;

@Component
public class FullPublicFalsehood {

    String contents;

    PublicFalsehood metadata;

    String keywords;

    public FullPublicFalsehood() {
    }

    public FullPublicFalsehood(String contents, PublicFalsehood metadata, String keywords) {
        this.contents = contents;
        this.metadata = metadata;
        this.keywords = keywords;
    }
    
    public FullPublicFalsehood clone()
    {
    	return new FullPublicFalsehood(contents, metadata.clone(), keywords);
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

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
