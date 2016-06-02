package org.green.camel.dynamic.dsl.engine;

public class Variable
{
	private String reference;
	
	@Override
	public String toString()
	{
		return reference;
	}

	public String getReference()
	{
		return reference;
	}

	public void setReference(String reference)
	{
		this.reference = reference;
	}
}
