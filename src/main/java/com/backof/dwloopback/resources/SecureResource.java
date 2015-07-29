package com.backof.dwloopback.resources;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.backof.dwloopback.core.security.User;

@Path("/api")
public class SecureResource {

	private static final Logger log = LoggerFactory.getLogger(SecureResource.class);

	
	@RolesAllowed({"admin","user"})
	@Path("/secure/hello")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getSimpleHelloJson(@Auth() User user) {
			log.info("Calling Secure Resource");
			String jsonResponse = "{\"user\":\"" + user.getUsername() + "\"}";
			return Response.status(200).entity(jsonResponse).build();
	}

}
