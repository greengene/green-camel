package org.green.camel.dynamic.dsl.engine;

public class Variable
{
	private StringBuilder reference;

	public Variable()
	{
		this.reference = new StringBuilder();
	}
	
	@Override
	public String toString()
	{
		return reference.toString();
	}

	public String getReference()
	{
		return reference.toString();
	}

	public void setReference(String reference)
	{
		this.reference = new StringBuilder(reference);
	}	
}
