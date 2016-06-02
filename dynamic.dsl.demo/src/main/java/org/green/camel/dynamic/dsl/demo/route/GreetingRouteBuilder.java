package org.green.camel.dynamic.dsl.demo.route;

import org.green.camel.dynamic.dsl.demo.service.GreetingService;
import org.green.camel.dynamic.dsl.engine.ServiceImplementerRouteBuilder;
import org.green.camel.dynamic.dsl.engine.Variable;
import org.green.camel.dynamic.dsl.engine.core.Route;
import org.springframework.beans.factory.annotation.Autowired;

public class GreetingRouteBuilder extends ServiceImplementerRouteBuilder
{
	@Autowired
	HiWorldRouteBuilder hiWorldRouteBuilder;

	@Override
	protected void initialize()
	{
		defaultInitialize(GreetingService.class, "greeting");		
	}
	
	public String saySomething()
	{
		return configureRoute(
				new Route()
				{
					Variable someResult = new Variable();
					
					@Override
					public void configure()
					{
						fromHere()
						.print("Hello there!")
						.to(hiWorldRouteBuilder.sayHi_1()).as(someResult)
						.to(hiWorldRouteBuilder.sayHi_2())
						.print("Reusing a result of a  previous route invocation in the current scope:")
						.to(hiWorldRouteBuilder.sayHi_3(someResult))
						;
					}
				}
				);
	}

}
