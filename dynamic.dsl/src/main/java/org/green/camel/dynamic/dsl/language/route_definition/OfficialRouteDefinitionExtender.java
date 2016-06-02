package org.green.camel.dynamic.dsl.language.route_definition;

import org.apache.camel.model.RouteDefinition;
import org.green.camel.dynamic.dsl.engine.Variable;
import org.green.camel.dynamic.dsl.engine.core.EnhancedRouteBuilder;
import org.green.camel.dynamic.dsl.language.LanguageDefinitionExtender;

public class OfficialRouteDefinitionExtender extends LanguageDefinitionExtender<RouteDefinition>
{
	private RouteDefinition routeDefinition;

	public OfficialRouteDefinitionExtender(RouteDefinition routeDefinition)
	{
		setDefinition(routeDefinition);
	}
	
	@Override
	public RouteDefinition getPureDefinition()
	{
		return routeDefinition;
	}

	@Override
	public void setPureDefinition(RouteDefinition routeDefinition)
	{
		this.routeDefinition = routeDefinition;		
	}
	
	public void returnHere()
	{
		defaultReturnHere();
	}
	
	public RouteDefinitionExtender as(Variable variable)
	{
		variable.setReference(EnhancedRouteBuilder.returnedLabel);
		return (RouteDefinitionExtender) this;
	}
}
