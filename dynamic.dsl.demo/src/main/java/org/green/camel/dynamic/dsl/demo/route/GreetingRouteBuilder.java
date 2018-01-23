package org.green.camel.dynamic.dsl.demo.route;

import org.green.camel.dynamic.dsl.demo.service.GreetingService;
import org.green.camel.dynamic.dsl.engine.ServiceImplementerRouteBuilder;
import org.green.camel.dynamic.dsl.engine.Variable;
import org.green.camel.dynamic.dsl.engine.core.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GreetingRouteBuilder extends ServiceImplementerRouteBuilder
{
	@Autowired
	HiWorldRouteBuilder hiWorldRouteBuilder;

	@Autowired
	GreetingAcrossRoutesRouteBuilder greetingAcrossRoutesRouteBuilder;

	@Override
	protected void initialize()
	{
		defaultInitialize(GreetingService.class, "greeting");		
	}

	public Variable holler = new Variable();
	
	public String saySomething()
	{
		return configureRoute(
				new Route()
				{
					Variable hi_1 = new Variable();
					
					@Override
					public void configure()
					{
						fromHere()
						.print("Hello there!")
						.to(hiWorldRouteBuilder.sayHi_1()).as(hi_1)
						.to(hiWorldRouteBuilder.sayHi_2())
						.print("Reusing a result of a previous route invocation in the current scope:")
						.to(hiWorldRouteBuilder.sayHi_3(hi_1))
						;
					}
				}
				);
	}

	public String sayAcrossRoutes()
	{
		return configureRoute(
				new Route()
				{
					@Override
					public void configure()
					{
						fromHere()
								.print("Hello there!")
								.to(hiWorldRouteBuilder.sayHi_1()).as(holler)
								.to(hiWorldRouteBuilder.sayHi_2())
								.print("Reusing a result of a previous route invocation in the current scope:")
								.to(greetingAcrossRoutesRouteBuilder.giveHoller())
						;
					}
				}
		);
	}

}
