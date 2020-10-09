package com.trecapps.false_hood.falsehoods;

import java.math.BigInteger;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.trecapps.false_hood.commonLie.CommonLie;
import com.trecapps.false_hood.publicFigure.PublicFigure;



	

@Entity
@Table
public class Falsehood {

	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	BigInteger id;
	
	@ManyToOne
	@JoinColumn
	MediaOutlet outlet;
	
	@ManyToOne
	@JoinColumn
	CommonLie commonLie;
	
	// Verification Status
	@Transient 
	public static final byte SUBMITTED = 0;	// Has been submitted, but not verified as a false hood
	@Transient 
	public static final byte VERIFIED = 1;	// Has been verified by staff
	@Transient 
	public static final byte CHALLENGED = 2;	// Has been verified, but is now being challenged
	@Transient 
	public static final byte REVERIFIED = 3;	// Has been verified and the challenge has failed
	@Transient 
	public static final byte MODIFIED = 4;	// Has been modified from an earlier version
	@Transient 
	public static final byte OVERTURNED = 5;	// Has previously been verified, but was overturned
	@Transient
	public static final byte REJECTED = 6;
	
	
	@Column
	byte status;
	
	// Media Type
	@Transient 
	public static final byte VIDEO = 0;	
	@Transient 
	public static final byte ARTICLE = 1;
	
	@Column
	byte mediaType;
	
	
	@Transient 
	public static final byte LIE = 0;			// Falsehood cites a direct lie
	@Transient 
	public static final byte FABRICATION = 1;	// Falsehood cites a likely lie
	@Transient 
	public static final byte MISLEADING = 2;		// No direct lie but point relies heavily on omission
	@Transient 
	public static final byte DOUBLE_STANDARD = 3;// Exposes a double Standard utilized by the entity
	@Transient 
	public static final byte BIAS = 4;			// Could be truthful, but reveals a bias that entity does not acknowledge
	@Transient 
	public static final byte HONEST_BIAS = 5;		// Could be truthful, based on a bias that entity acknowledges
	
	@Column
	byte severity;
	
	@ManyToOne
	@JoinColumn
	PublicFigure author1;
	
	@ManyToOne
	@JoinColumn
	PublicFigure author2;
	
	@Column
	String source;
	
	@Column
	Date dateMade;
	
	@Column
	String contentId;


	public Falsehood(BigInteger id, MediaOutlet outlet, byte status, byte mediaType, CommonLie commonLie, byte severity, PublicFigure author1, PublicFigure author2,
			 String source, Date dateMade, String contentId) {
		super();
		this.id = id;
		this.outlet = outlet;
		this.status = status;
		this.mediaType = mediaType;
		this.severity = severity;
		this.author1 = author1;
		this.author2 = author2;
		this.contentId = contentId;
		this.source = source;
		this.dateMade = dateMade;
		this.commonLie = commonLie;
	}

	
	
	public CommonLie getCommonLie() {
		return commonLie;
	}



	public void setCommonLie(CommonLie commonLie) {
		this.commonLie = commonLie;
	}



	public Falsehood() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
		this.id = id;
	}


	public MediaOutlet getOutlet() {
		return outlet;
	}

	public void setOutlet(MediaOutlet outlet) {
		this.outlet = outlet;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(byte status) {
		this.status = status;
	}
	
	public byte getMediaType() {
		return mediaType;
	}

	public void setMediaType(byte mediaType) {
		this.mediaType = mediaType;
	}
	
	public byte getSeverity() {
		return severity;
	}

	public void setSeverity(byte severity) {
		this.severity = severity;
	}

	public PublicFigure getAuthor1() {
		return author1;
	}

	public void setAuthor1(PublicFigure author1) {
		this.author1 = author1;
	}

	public PublicFigure getAuthor2() {
		return author2;
	}

	public void setAuthor2(PublicFigure author2) {
		this.author2 = author2;
	}

	public String getContentId() {
		return contentId;
	}

	public void setContentId(String details) {
		this.contentId = details;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getDateMade() {
		return dateMade;
	}

	public void setDateMade(Date dateMade) {
		this.dateMade = dateMade;
	}



	@Override
	public String toString() {
		return "Falsehood [id=" + id + ", outlet=" + outlet + ", commonLie=" + commonLie + ", status=" + status
				+ ", mediaType=" + mediaType + ", severity=" + severity + ", author1=" + author1 + ", author2="
				+ author2 + ", source=" + source + ", dateMade=" + dateMade + ", contentId=" + contentId + "]";
	}
	

}
