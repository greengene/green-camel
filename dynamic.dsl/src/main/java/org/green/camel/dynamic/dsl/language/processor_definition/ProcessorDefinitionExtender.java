package org.green.camel.dynamic.dsl.language.processor_definition;

import org.apache.camel.model.ProcessorDefinition;

import org.green.camel.dynamic.dsl.language.LanguageDefinitionExtender;

public class ProcessorDefinitionExtender extends LanguageDefinitionExtender<ProcessorDefinition<?>>
{
	private ProcessorDefinition<?> processorDefinition;
	
	public ProcessorDefinitionExtender(ProcessorDefinition<?> processorDefinition)
	{
		setDefinition(processorDefinition);
	}
	
	@Override
	public ProcessorDefinition<?> getPureDefinition()
	{
		return processorDefinition;
	}

	@Override
	public void setPureDefinition(ProcessorDefinition<?> processorDefinition)
	{
		this.processorDefinition = processorDefinition;		
	}

	public ProcessorDefinitionExtender to(String uri)
	{
		setDefinition(getDefinition().to(uri));		
		return this;
	}
	
	public ProcessorDefinitionExtender bean(Class<?> beanType, String method)
	{
		setDefinition(getDefinition().bean(beanType, method));		
		return this;
	}
	
	public void returnHere()
	{
		defaultReturnHere();
	}
}
