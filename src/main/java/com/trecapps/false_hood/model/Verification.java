package com.trecapps.false_hood.model;
public enum Verification
{
	SUBMITTED,	// Has been submitted, but not verified as a false hood
	VERIFIED,	// Has been verified by staff
	CHALLENGED,	// Has been verified, but is now being challenged
	REVERIFIED,	// Has been verified and the challenge has failed
	MODIFIED,	// Has been modified from an earlier version
	OVERTURNED	// Has previously been verified, but was overturned
}