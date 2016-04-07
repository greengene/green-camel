package org.green.camel.dynamic.dsl.demo.service;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@WebService(name = "GreetingService", targetNamespace = "ws.camel.green.org/greeting/GreetingService")
public interface GreetingService {

	@GET
	@Path("/say/something/{text}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@WebMethod
	@WebResult(name = "saySomethingResponse")
	public List<String> saySomething(
		@PathParam("text") @WebParam(name = "text") String text);
}
