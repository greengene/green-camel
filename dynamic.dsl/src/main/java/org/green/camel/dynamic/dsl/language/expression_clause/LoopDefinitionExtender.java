package org.green.camel.dynamic.dsl.language.expression_clause;

import org.apache.camel.model.LoopDefinition;
import org.green.camel.dynamic.dsl.language.LanguageDefinitionExtender;
import org.green.camel.dynamic.dsl.language.choice_definition.ChoiceDefinitionExtender;

public class LoopDefinitionExtender extends LanguageDefinitionExtender<LoopDefinition>
{
	private LoopDefinition loopDefinition;
	
	public <T> LoopDefinitionExtender(T t)
	{
		setDefinition((LoopDefinition) t);
	}

	@Override
	public LoopDefinition getPureDefinition()
	{
		return loopDefinition;
	}

	@Override
	public void setPureDefinition(LoopDefinition loopDefinition)
	{
		this.loopDefinition = loopDefinition;		
	}

	public ExpressionNodeExtender to(String uri)
	{
		return new ExpressionNodeExtender(getDefinition().to(uri));
	}
	
	public ChoiceDefinitionExtender choice()
	{
		return new ChoiceDefinitionExtender(getDefinition().choice());
	}
}
