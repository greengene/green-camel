package org.green.camel.dynamic.dsl.language;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.model.ProcessorDefinition;

import org.green.camel.dynamic.dsl.engine.core.Route;


public abstract class LanguageDefinitionExtender<T>
{
	abstract protected T getPureDefinition();
	
	abstract protected void setPureDefinition(T t);
	
	protected T getDefinition()
	{
		//some logic here...
		
		return getPureDefinition();
	}
	
	protected void setDefinition(T t)
	{		
		setPureDefinition(t);
		
		//some logic here...
	}
	
	public T escape()
	{
		return getDefinition();
	}
	
	protected void defaultReturnHere()
	{
		if (getDefinition() instanceof ProcessorDefinition)
		{
			ProcessorDefinition<?> definition = (ProcessorDefinition<?>) getDefinition();

			final String label = Route.fromHere;
			
			//setRouteDefinition(
				definition.process(new Processor() {
					@Override
					public void process(Exchange exchange) throws Exception
					{
						exchange.setProperty(label, exchange.getIn().getBody());
					}
				});
			//);
		}

		//return (RouteDefinitionExtender) this;
	}
}
