package org.green.camel.dynamic.dsl.language.try_definition;

import org.apache.camel.model.TryDefinition;
import org.green.camel.dynamic.dsl.language.LanguageDefinitionExtender;

public class TryDefinitionExtender extends LanguageDefinitionExtender<TryDefinition>
{
	private TryDefinition tryDefinition;	
	
	public TryDefinitionExtender(TryDefinition tryDefinition)
	{
		setDefinition(tryDefinition);
	}

	@Override
	protected TryDefinition getPureDefinition()
	{
		return tryDefinition;
	}

	@Override
	protected void setPureDefinition(TryDefinition tryDefinition)
	{
		this.tryDefinition = tryDefinition;		
	}
	
	public TryDefinitionExtender doCatch(Class<? extends Throwable> exceptionType)
	{
		setDefinition(getDefinition().doCatch(exceptionType));		
		return this;
	}
	
	public TryDefinitionExtender bean(Class<?> beanType, String method)
	{
		setDefinition(getDefinition().bean(beanType, method));		
		return this;
	}
	
	public TryDefinitionExtender to(String uri)
	{
		setDefinition(getDefinition().to(uri));		
		return this;
	}
}
