package org.green.camel.dynamic.dsl.engine;

import javax.ws.rs.core.UriBuilder;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.green.camel.dynamic.dsl.route.setup.SetupRouteBuilder;


/**
 * Extend me to create routes that implement web service interfaces.
 * 
 * This class creates routes that redirect SOAP and REST requests to routes implemented by the extension of this class. 
 *
 */
abstract public class ServiceImplementerRouteBuilder extends DefaultRouteBuilder
{	
	private Class<?> serviceClass;
	private String soapServicePath;
	private String restServicePath;
	
	abstract protected void initialize();
	
	protected ServiceImplementerRouteBuilder()
	{		
		super();
		initialize();
		
		if (serviceClass == null || StringUtils.isEmpty(soapServicePath))
		{
			throw new IllegalArgumentException("Initialize " + getClass().getName());
		}
	}
	
	protected final void defaultInitialize(Class<?> serviceClass, String servicePath)
	{
		this.serviceClass = serviceClass;
		this.soapServicePath = StringUtils.deleteWhitespace(WordUtils.capitalizeFully(servicePath));
		this.restServicePath = servicePath.toLowerCase().trim().replaceAll(" +", "-");
	}
	
	protected final void defaultInitialize(Class<?> serviceClass, String soapServicePath, String restServicePath)
	{
		this.serviceClass = serviceClass;
		this.soapServicePath = soapServicePath;
		this.restServicePath = restServicePath;
	}
	
	private String rootSoapUri()
	{		
		// cxf:/" + servicePath + "?serviceClass=" + serviceClass.getName()
		
		return UriBuilder.
				fromPath("cxf:/")
	            .path("{soapServicePath}")
	            .queryParam("serviceClass", serviceClass.getName())
	            .build(soapServicePath)
	            .toString();
	}
	
	private String rootRestUri()
	{		
		// cxfrs:/rest/SomeService?resourceClasses=org.green.camel.dynamic.dsl.service.SomeService&bindingStyle=SimpleConsumer&providers=#restJacksonProviderList
		
		return UriBuilder.
				fromPath("cxfrs://")
	            .path("{restServicePath}")
	            .queryParam("resourceClasses", serviceClass.getName())
	            .queryParam("bindingStyle", "SimpleConsumer")
	            .queryParam("providers=").fragment("restJacksonProviderList")
	            .build(restServicePath)
	            .toString();
	}
	
	/**
	 * You may want to override me.
	 */
	protected String setup()
	{
		return new SetupRouteBuilder().defaultSetUp();
	}
	
	private void mainRouter()
	{		
		//TODO ???
		String routeBuilderNameSpace = StringUtils.uncapitalize(this.getClass().getSimpleName()) + ".";
		
		String rootUri = "direct:" + StringUtils.uncapitalize(serviceClass.getSimpleName());
		
		from(rootSoapUri())
		.to(rootUri);
		
		from(rootRestUri())
		.to(rootUri);
		
		from(rootUri)
		.to("log:input")
		.process(new Processor()
		{
			@Override
			public void process(Exchange exchange) throws Exception
			{
				exchange.setProperty("rootUnitOfWork", exchange.getUnitOfWork());
			}
		})

		.to(setup())
		
		//TODO toD
		//http://camel.apache.org/message-endpoint.html
		.recipientList(simple(new StringBuilder("direct:").append(routeBuilderNameSpace).append("${header.operationName}").toString()))/*.shareUnitOfWork()*/
		.to("log:output");
	}

	@Override
	protected final void beforeLoadingRoutes()
	{		
			super.beforeLoadingRoutes();
			mainRouter();
	}
}
