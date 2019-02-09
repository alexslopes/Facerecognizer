package com.ifba.ads.Facerecognizer.teste;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/teste")
public class Teste {

	@GET
	@Produces({MediaType.APPLICATION_JSON})
	public String teste() {
		return("Teste rest");
	}
}
