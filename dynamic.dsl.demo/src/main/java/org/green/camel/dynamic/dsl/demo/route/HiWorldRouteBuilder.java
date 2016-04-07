package org.green.camel.dynamic.dsl.demo.route;

import org.green.camel.dynamic.dsl.engine.DefaultRouteBuilder;
import org.green.camel.dynamic.dsl.engine.core.Route;
import org.springframework.stereotype.Component;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

@Component
public class HiWorldRouteBuilder extends DefaultRouteBuilder
{
	public static String someResult;
	
	public String sayHi_1()
	{
		return configureRoute(
				new Route()
				{
					@Override
					public void configure()
					{
						fromHere()
						.print("Hi 1")
						.returnHere()
						;
					}					
				}				
				);
	}

	public String sayHi_2()
	{
		return configureRoute(
				new Route()
				{
					@Override
					public void configure()
					{
						fromHere()
						.print("Hi 2")
						.returnHere()
						;
					}					
				}				
				);
	}
}
