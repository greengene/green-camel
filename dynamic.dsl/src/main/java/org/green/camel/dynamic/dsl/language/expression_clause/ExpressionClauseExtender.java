package org.green.camel.dynamic.dsl.language.expression_clause;

import org.apache.camel.builder.ExpressionClause;
import org.green.camel.dynamic.dsl.language.LanguageDefinitionExtender;

public class ExpressionClauseExtender extends LanguageDefinitionExtender<ExpressionClause<?>>
{
	private ExpressionClause<?> expressionClause;
	
	private LoopDefinitionExtender type;
	
	public ExpressionClauseExtender(ExpressionClause<?> expressionClause)
	{
		setDefinition(expressionClause);
	}
	
	@Override
	public ExpressionClause<?> getPureDefinition()
	{
		return expressionClause;
	}

	@Override
	public void setPureDefinition(ExpressionClause<?> expressionClause)
	{
		this.expressionClause = expressionClause;		
	}

	public LoopDefinitionExtender getType()
	{
		return type;
	}

	public void setType(LoopDefinitionExtender type)
	{
		this.type = type;
	}

	public LoopDefinitionExtender simple(String text)
	{
		return new LoopDefinitionExtender(getDefinition().simple(text));
	}
}
