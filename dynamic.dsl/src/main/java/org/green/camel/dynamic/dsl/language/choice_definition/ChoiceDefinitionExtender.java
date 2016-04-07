package org.green.camel.dynamic.dsl.language.choice_definition;

import org.apache.camel.Expression;
import org.apache.camel.Predicate;
import org.apache.camel.model.ChoiceDefinition;
import org.green.camel.dynamic.dsl.language.LanguageDefinitionExtender;
import org.green.camel.dynamic.dsl.language.processor_definition.ProcessorDefinitionExtender;

//TODO create full hierarchy
public class ChoiceDefinitionExtender extends LanguageDefinitionExtender<ChoiceDefinition>
{
	public ChoiceDefinitionExtender(ChoiceDefinition choiceDefinition)
	{
		setDefinition(choiceDefinition);
	}

	private ChoiceDefinition choiceDefinition;

	@Override
	public ChoiceDefinition getPureDefinition()
	{
		return choiceDefinition;
	}

	@Override
	public void setPureDefinition(ChoiceDefinition choiceDefinition)
	{
		this.choiceDefinition = choiceDefinition;		
	}


	public ChoiceDefinitionExtender when(Predicate predicate)
	{
		setDefinition(getDefinition().when(predicate));		
		return this;
	}


	public ChoiceDefinitionExtender setHeader(String name, Expression expression)
	{
		setDefinition(getDefinition().setHeader(name, expression));		
		return this;
	}

	public ProcessorDefinitionExtender end()
	{
		return new ProcessorDefinitionExtender(getDefinition().end());		
	}

	public ChoiceDefinitionExtender to(String uri)
	{
		setDefinition(getDefinition().to(uri));		
		return this;
	}

	public ChoiceDefinitionExtender bean(Class<?> beanType, String method)
	{
		setDefinition(getDefinition().bean(beanType, method));		
		return this;
	}

	public ChoiceDefinitionExtender otherwise()
	{
		setDefinition(getDefinition().otherwise());		
		return this;
	}

	public ChoiceDefinitionExtender log(String message)
	{
		setDefinition(getDefinition().log(message));		
		return this;
	}

	public DelayDefinitionExtender delay(long delay)
	{
		return new DelayDefinitionExtender(getDefinition().delay(delay));		
	}
}
