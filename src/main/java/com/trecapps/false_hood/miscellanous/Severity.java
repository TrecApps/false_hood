package com.trecapps.false_hood.miscellanous;
public enum Severity
{
	LIE((byte)0),			// Falsehood cites a direct lie
	FABRICATION((byte)1),	// Falsehood cites a likely lie
	MISLEADING((byte)2),		// No direct lie but point relies heavily on omission
	DOUBLE_STANDARD((byte)3),// Exposes a double Standard utilized by the entity
	BIAS((byte)4),			// Could be truthful, but reveals a bias that entity does not acknowledge
	HONEST_BIAS((byte)5);		// Could be truthful, based on a bias that entity acknowledges
	
	Severity(byte value)
	{
		this.value = value;
	}
	
	byte value;
	
	public byte GetValue()
	{
		return value;
	}
}
