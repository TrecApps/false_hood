package com.trecapps.false_hood.miscellanous;

public enum FalsehoodStatus {
	
	SUBMITTED((byte)0),
	VERIFIED((byte)1),
	CHALLENGED((byte)2),
	REVERIFIED((byte)3),
	MODIFIED((byte)4),
	OVERTURNED((byte)5),
	REJECTED((byte)6);
	
	
	FalsehoodStatus(byte value)
	{
		this.value = value;
	}
	
	byte value;
	
	public byte GetValue()
	{
		return value;
	}
}
