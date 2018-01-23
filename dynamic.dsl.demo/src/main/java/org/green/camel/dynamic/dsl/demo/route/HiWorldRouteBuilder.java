package org.green.camel.dynamic.dsl.demo.route;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.green.camel.dynamic.dsl.engine.DefaultRouteBuilder;
import org.green.camel.dynamic.dsl.engine.Variable;
import org.green.camel.dynamic.dsl.engine.core.Route;
import org.springframework.stereotype.Component;

@Component
public class HiWorldRouteBuilder extends DefaultRouteBuilder
{
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
						.setBody(constant("HELLO 1"))
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
						.setBody(constant("HELLO 2"))
						.returnHere()
						;
					}					
				}				
				);
	}
	
	public String sayHi_3(final Variable result)
	{
		return configureRoute(
				new Route()
				{
					@Override
					public void configure()
					{
						fromHere()
						.print("Variable reference: " + result)
						.process(new Processor()
						{
							@Override
							public void process(Exchange exchange) throws Exception
							{
								exchange.getOut().setBody(exchange.getProperty(result.getReference(), String.class));
							}
						})
						;
					}					
				}				
				);
	}
}
