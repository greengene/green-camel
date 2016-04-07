package org.green.camel.dynamic.dsl.route.setup;

import org.green.camel.dynamic.dsl.engine.DefaultRouteBuilder;
import org.green.camel.dynamic.dsl.engine.core.Route;

public class SetupRouteBuilder extends DefaultRouteBuilder
{
	public String defaultSetUp()
	{
		return configureRoute(
			new Route()
			{
				@Override
				public void configure()
				{
					fromHere()
					/*
			        .choice()
				        .when(header("id").isNull())
				        	.setHeader("id", simple("${body[1]}"))
				        .end()
				    */
					//.to(emptySetUp())
					.print("inside setup!")
					;
				}					
			}
		);
	}
	
	public String emptySetUp()
	{
		return configureRoute(
			new Route()
			{
				@Override
				public void configure()
				{
					fromHere()
					;
				}
			}
		);
	}
}
