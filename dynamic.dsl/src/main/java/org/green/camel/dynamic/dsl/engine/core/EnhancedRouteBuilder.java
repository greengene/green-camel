package org.green.camel.dynamic.dsl.engine.core;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.commons.lang3.StringUtils;
import org.green.camel.dynamic.dsl.engine.utility.ReflectionsUtil;
import org.green.camel.dynamic.dsl.engine.utility.TraceHelper;

public abstract class EnhancedRouteBuilder extends RouteBuilder
{
	protected static CamelContext camelContext;
	
	private static boolean returnLabel = true;
	//TODO move returnedLabel to a global context. Language extension should not look inside here.
	public static String returnedLabel;
	private static String routeMethod = "";
	private static int time;
	
	static boolean startConfiguringParameterizedRoute;

	static final int FROM_HERE_STACK_DEPTH = 5;
	static final int CHILD_ROUTE_STACK_DEPTH = 2;
	
	static Route currentlyConfiguredRoute;
	
	protected abstract void beforeLoadingRoutes();
	
	@Override
	public void configure() throws Exception
	{	
		returnLabel = true;
		beforeLoadingRoutes();
		returnLabel = false;

		loadRoutes();
	}
	
	private void loadRoutes() throws Exception
	{		
		camelContext = (camelContext == null? getContext() : camelContext);
		
		Method[] methods = getClass().getDeclaredMethods();
		
		System.out.println("\n===============================================================================");
		System.out.println("Loading RouteBuilder: " + getClass().getCanonicalName() + "");
		System.out.print("\tRoutes to load:");
		
		for (Method method : methods)
		{
			if (Modifier.isPublic(method.getModifiers()) && method.getParameterTypes().length == 0)
			{
				System.out.print("\n\t\u25C6 Method: " + method.getName());
			}
		}

		returnLabel = false;
		
        for (Method method : methods)
        {
        	if (Modifier.isPublic(method.getModifiers()))
        	{
        		Class<?>[] parameterTypes = method.getParameterTypes();
        		
        		if (parameterTypes.length == 0)
        		{        			
        			System.out.print("\n\t---------------------------------------------------------------------------\n\tLoading route:\n\t");
        			
        			method.invoke(this, new Object[] {});
        		}
        		
        		returnLabel = false;
        	}
        }
        
        returnLabel = true;
	}
	
	private void loadParameterizeRoutes(List<Route> parameterizedRouteSnapshots) throws Exception
	{
		if (parameterizedRouteSnapshots != null)
		{
			//TODO name
			System.out.print("\t\tLoading parameterized child routes of RouteBuilder:");
			
			for (Route route : parameterizedRouteSnapshots)
			{
				startConfiguringParameterizedRoute = true;
				
				currentlyConfiguredRoute = route;
				
				System.out.println("\n\t\t.......................................................................");
				System.out.print("\t\tLoading parameterized route:\n\t\t");
				
				includeRoutes(currentlyConfiguredRoute);
				
				loadParameterizeRoutes(currentlyConfiguredRoute.parameterizedChildRoutes);
			}
		}
	}
	
	final protected String configureRoute(Route route)
	{		
		try
		{
			String label = null;
			
			Method callingMethod = null;
			
			Class<?>[] parameterTypes = {};
			
			route.routeBuilderNameSpace = StringUtils.uncapitalize(getClass().getSimpleName()) + ".";
					
			try
			{
				callingMethod = ReflectionsUtil.getMethod(TraceHelper.getStackTraceElement(1/*TODO JVM dependent?*/));
				parameterTypes = callingMethod.getParameterTypes();
			}
			catch (Exception e)
			{
				throw new IllegalStateException("Route cannot be created. Route details: Calling method: " + callingMethod + ". Parameter types: " + parameterTypes.toString() + ".", e);
			}

			if (returnLabel)
			{
				System.out.print("\n\t\t\u2022 ");
				
				routeMethod = callingMethod.getName();
				
				if (parameterTypes.length == 0)
				{					
					label = route.getDefaultLabel(CHILD_ROUTE_STACK_DEPTH, 0);
					
					System.out.print(" (0 parameters)");
				}
				else
				{					
					time++;
					
					label = route.getDefaultLabel(CHILD_ROUTE_STACK_DEPTH, time);
					
					System.out.print(" (" + parameterTypes.length + (parameterTypes.length >  1 ? " parameters" : " parameter") + ")");
					
					if (currentlyConfiguredRoute.parameterizedChildRoutes == null)
					{
						currentlyConfiguredRoute.parameterizedChildRoutes = new ArrayList<Route>();
					}
					
					route.setMethodName(routeMethod);
					route.setInstance(time);

					currentlyConfiguredRoute.parameterizedChildRoutes.add(route);
				}
			}		
			else
			{
				returnLabel = true;
	
				currentlyConfiguredRoute = route;
				
				//is root-level unparameterized route that does not immediately redirect to other route (i.e. common case)?
				//TODO do some more checks?
				if (TraceHelper.getMethodName(2).equals("invoke0"))
				{
					includeRoutes(route);
				}
				else
				{
					System.out.print("\tChanged my mind, not loading this route (" + callingMethod.toGenericString() + "). Inner route may be loaded elsewhere.");
				}
			}
			
	
			if (route == currentlyConfiguredRoute)
			{
				System.out.print("\n\t\tDetected parameterized child routes: " + (route.parameterizedChildRoutes == null ? "none" : route.parameterizedChildRoutes.size()));
				
				if (route.parameterizedChildRoutes != null && route.parameterizedChildRoutes.size() > 0)
				{
					System.out.println("\n\t\t.......................................................................");
				}
			}
			
			loadParameterizeRoutes(route.parameterizedChildRoutes);
			
			return returnedLabel = label;		
		}
		catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}
}
