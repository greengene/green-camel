package org.green.camel.dynamic.dsl.demo.route;

import org.green.camel.dynamic.dsl.engine.DefaultRouteBuilder;
import org.green.camel.dynamic.dsl.engine.core.Route;

import com.google.common.collect.ContiguousSet;
import com.google.common.collect.DiscreteDomain;
import com.google.common.collect.Range;

public class HelloWorldRouteBuilder extends DefaultRouteBuilder
{
	public static String someResult;
	
	public String sayHello_1()
	{
		return configureRoute(
				new Route()
				{
					@Override
					public void configure()
					{
						fromHere()
						.print("Starting hello 1")
						.to(sayHello_2())
						.to(someResult = sayHello_3())
						.to(sayHello_4("Hello 4"))
						.to(sayHello_5())
						.to(sayHello_6())
						.print("Escaping for a while:")
						.to(sayHello_7())
						.print("Ending hello 1")
						.returnHere()
						;
					}					
				}				
				);
	}

	public String sayHello_2()
	{
		return configureRoute(
				new Route()
				{
					@Override
					public void configure()
					{
						fromHere()
						.print("Hello 2")
						;
					}					
				}				
				);
	}

	public String sayHello_3()
	{
		return configureRoute(
				new Route()
				{
					@Override
					public void configure()
					{
						fromHere()
						.print("Hello 3")
						
						;
					}					
				}				
				);
	}

	public String sayHello_4(final String text)
	{
		return configureRoute(
				new Route()
				{
					@Override
					public void configure()
					{
						fromHere()
						.print(text)
						;
					}					
				}				
				);
	}

	public String sayHello_5()
	{
		return sayHello_4("Hello 5");
	}

	public String sayHello_6()
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

	/**
	 * How to temporarily escape to native Camel language.
	 */
	public String sayHello_7()
	{
		return configureRoute(
				new Route()
				{
					@Override
					public void configure()
					{
						fromHere()
						.escape()
						.setBody(constant(ContiguousSet.create(Range.closed(0, 10), DiscreteDomain.integers())))
						.split(body()).parallelProcessing()
							.to("stream:out")
						.end()
						;
					}					
				}				
			);
	}
}
