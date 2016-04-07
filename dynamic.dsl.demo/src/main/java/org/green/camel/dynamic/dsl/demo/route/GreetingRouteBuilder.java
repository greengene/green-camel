package org.green.camel.dynamic.dsl.demo.route;

import org.green.camel.dynamic.dsl.demo.service.GreetingService;
import org.green.camel.dynamic.dsl.engine.ServiceImplementerRouteBuilder;
import org.green.camel.dynamic.dsl.engine.core.Route;
import org.springframework.beans.factory.annotation.Autowired;

public class GreetingRouteBuilder extends ServiceImplementerRouteBuilder
{
	@Autowired
	HiWorldRouteBuilder hiWorldRouteBuilder;

	@Override
	protected void initialize() {
		defaultInitialize(GreetingService.class, "greeting");		
	}
	
	public String saySomething()
	{
		return configureRoute(
				new Route()
				{
					@Override
					public void configure()
					{
						fromHere()
						.print("Hello there!")
						.to(hiWorldRouteBuilder.sayHi_1())
						.to(hiWorldRouteBuilder.sayHi_2())
						;
					}
				}
				);
	}

}
