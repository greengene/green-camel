package org.green.camel.dynamic.dsl.language.choice_definition;

import org.apache.camel.model.DelayDefinition;
import org.green.camel.dynamic.dsl.language.LanguageDefinitionExtender;
import org.green.camel.dynamic.dsl.language.expression_clause.ExpressionNodeExtender;

public class DelayDefinitionExtender extends LanguageDefinitionExtender<DelayDefinition>
{
	private DelayDefinition delayDefinition;	

	public DelayDefinitionExtender(DelayDefinition delayDefinition)
	{
		setDefinition(delayDefinition);
	}

	@Override
	protected DelayDefinition getPureDefinition()
	{
		return delayDefinition;
	}

	@Override
	protected void setPureDefinition(DelayDefinition delayDefinition)
	{
		this.delayDefinition = delayDefinition;		
	}
	
	public DelayDefinitionExtender asyncDelayed()
	{
		setDefinition(getDefinition().asyncDelayed());		
		return this;
	}
	
	public ExpressionNodeExtender to(String uri)
	{
		return new ExpressionNodeExtender(getDefinition().to(uri));		
	}

}
