package org.green.camel.dynamic.dsl.language.route_definition;

import org.apache.camel.Expression;
import org.apache.camel.model.RouteDefinition;
import org.green.camel.dynamic.dsl.language.choice_definition.ChoiceDefinitionExtender;
import org.green.camel.dynamic.dsl.language.expression_clause.ExpressionClauseExtender;
import org.green.camel.dynamic.dsl.language.try_definition.TryDefinitionExtender;

public class RouteDefinitionExtender extends UnofficialRouteDefinitionExtender
{
	public RouteDefinitionExtender(RouteDefinition routeDefinition)
	{
		super(routeDefinition);
	}
	
	public RouteDefinitionExtender to(String uri)
	{
		setDefinition(getDefinition().to(uri));		
		return this;
	}

	public RouteDefinitionExtender bean(Class<?> beanType, String method)
	{
		setDefinition(getDefinition().bean(beanType, method));		
		return this;
	}
	
	public ChoiceDefinitionExtender choice()
	{
		return new ChoiceDefinitionExtender(getDefinition().choice());
	}
	
	public TryDefinitionExtender doTry()
	{
		return new TryDefinitionExtender(getDefinition().doTry());		
	}
	
	public RouteDefinitionExtender setHeader(String name, Expression expression)
	{
		setDefinition(getDefinition().setHeader(name, expression));		
		return this;
	}
	
	public RouteDefinitionExtender beanRef(String ref, String method)
	{
		setDefinition(getDefinition().beanRef(ref, method));		
		return this;
	}
	
	public ExpressionClauseExtender loop()
	{
		return new ExpressionClauseExtender(getDefinition().loop());
	}
}
