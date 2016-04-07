package org.green.camel.dynamic.dsl.language.route_definition;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.RouteDefinition;

public class UnofficialRouteDefinitionExtender extends OfficialRouteDefinitionExtender
{
	public UnofficialRouteDefinitionExtender(RouteDefinition routeDefinition)
	{
		super(routeDefinition);
	}

	public RouteDefinitionExtender beep()
	{
		setDefinition(
			getDefinition().process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					System.out.println("Beep!");
				}
			})
		);

		return (RouteDefinitionExtender) this;
	}
	
	public RouteDefinitionExtender print(final String text)
	{
		setDefinition(
			getDefinition().process(new Processor() {
				@Override
				public void process(Exchange exchange) throws Exception {
					System.out.println(text);
				}
			})
		);

		return (RouteDefinitionExtender) this;
	}
}
