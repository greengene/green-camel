package org.green.camel.dynamic.dsl.engine;

import org.green.camel.dynamic.dsl.engine.core.EnhancedRouteBuilder;

/**
 * Extend me to create routes that are not associated with any specific web service.
 *
 */
abstract public class DefaultRouteBuilder extends EnhancedRouteBuilder
{
	/**
	 * You may override me. It will affect all routes in this concrete RouteBuilder. Call super.exceptionHandling() here if you only want to add rules.
	 * 
	 * http://camel.apache.org/exception-clause.html
	 * 
	 * Global scope for Java DSL is per RouteBuilder instance, so if you want to share among multiple RouteBuilder classes, then create a base abstract RouteBuilder class and put the error handling logic in its configure method. And then extend this class, and make sure to class super.configure(). We are just using the Java inheritance technique.
	 */
	@SuppressWarnings("unchecked")
	protected void exceptionHandling() {
		
		onException(
				java.sql.SQLException.class,
				java.lang.Exception.class
			)
		.maximumRedeliveries(0)
		.to("bean:errorProcessor");
	}

	@Override
	protected void beforeLoadingRoutes()
	{
		exceptionHandling();		
	}
}
