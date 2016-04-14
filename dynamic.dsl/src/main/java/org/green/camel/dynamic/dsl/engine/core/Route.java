package org.green.camel.dynamic.dsl.engine.core;

import java.util.List;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.spi.UnitOfWork;
import org.green.camel.dynamic.dsl.engine.utility.TraceHelper;
import org.green.camel.dynamic.dsl.language.route_definition.RouteDefinitionExtender;

public abstract class Route extends RouteBuilder 
{
	String routeBuilderNameSpace;
	
	List<Route> parameterizedChildRoutes;

	public static String fromHere;
	
	private String methodName;
	private int instance;
	
	public Route()
	{
		super(EnhancedRouteBuilder.camelContext);
	}
	
	protected final RouteDefinitionExtender fromHere()
	{
		int instance;
		
		if (!EnhancedRouteBuilder.startConfiguringParameterizedRoute)
		{
			instance = 0;
		}
		else
		{
			instance = EnhancedRouteBuilder.currentlyConfiguredRoute.getInstance();
		}		

		fromHere = getDefaultLabel(EnhancedRouteBuilder.FROM_HERE_STACK_DEPTH, instance);

		return new RouteDefinitionExtender(fromLabel(fromHere));
	}
	
	final RouteDefinition fromLabel(String routeLabel)
	{
		return
			from(routeLabel)
			.process(new Processor()
			{
				@Override
				public void process(Exchange exchange) throws Exception
				{
					exchange.setUnitOfWork(exchange.getProperty("rootUnitOfWork", UnitOfWork.class));
				}
			})
			.routeId(routeLabel);				
	}
	
	final String getDefaultLabel(int depth, int instance)
	{		
		String methodName;
		
		if (!EnhancedRouteBuilder.startConfiguringParameterizedRoute)
		{
			methodName = TraceHelper.getMethodName(depth);
		}
		else
		{
			methodName = EnhancedRouteBuilder.currentlyConfiguredRoute.getMethodName();
			
			EnhancedRouteBuilder.startConfiguringParameterizedRoute = false;
		}

		String label = new StringBuilder("direct:").append(routeBuilderNameSpace).append(methodName).append(instance > 0 ? "." + instance : "").toString();
		
		System.out.print("URI: " + label);
		
		return label;
	}

	final String getMethodName()
	{
		return methodName;
	}

	final void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}

	final int getInstance()
	{
		return instance;
	}

	final void setInstance(int instance)
	{
		this.instance = instance;
	}
}
