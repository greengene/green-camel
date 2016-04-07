package org.green.camel.dynamic.dsl.language.expression_clause;

import org.apache.camel.model.ExpressionNode;
import org.green.camel.dynamic.dsl.language.LanguageDefinitionExtender;
import org.green.camel.dynamic.dsl.language.choice_definition.ChoiceDefinitionExtender;
import org.green.camel.dynamic.dsl.language.processor_definition.ProcessorDefinitionExtender;

public class ExpressionNodeExtender extends LanguageDefinitionExtender<ExpressionNode>
{
	private ExpressionNode expressionNode;	

	public ExpressionNodeExtender(ExpressionNode expressionNode)
	{
		setDefinition(expressionNode);
	}

	@Override
	public ExpressionNode getPureDefinition()
	{
		return expressionNode;
	}

	@Override
	public void setPureDefinition(ExpressionNode expressionNode)
	{
		this.expressionNode = expressionNode;		
	}

	public ExpressionNodeExtender bean(Class<?> beanType, String method)
	{
		setDefinition(getDefinition().bean(beanType, method));		
		return this;
	}

	public ProcessorDefinitionExtender end()
	{
		return new ProcessorDefinitionExtender(getDefinition().end());		
	}

	public ChoiceDefinitionExtender endChoice()
	{
		return new ChoiceDefinitionExtender(getDefinition().endChoice());		
	}
}
