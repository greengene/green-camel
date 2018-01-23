package org.green.camel.dynamic.dsl.demo.route;

import org.green.camel.dynamic.dsl.engine.DefaultRouteBuilder;
import org.green.camel.dynamic.dsl.engine.core.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GreetingAcrossRoutesRouteBuilder extends DefaultRouteBuilder
{
	@Autowired
	HiWorldRouteBuilder hiWorldRouteBuilder;

	@Autowired
	GreetingRouteBuilder greetingRouteBuilder;

	public String giveHoller()
	{
		return configureRoute(
				new Route()
				{
					@Override
					public void configure()
					{
						fromHere()
						.print("Looking up a result of another route:")
						.to(hiWorldRouteBuilder.sayHi_3(greetingRouteBuilder.holler))
						;
					}
				}
				);
	}

}
