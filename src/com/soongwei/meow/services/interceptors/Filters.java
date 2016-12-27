package com.soongwei.meow.services.interceptors;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@SessionBinding
@Provider
public class Filters implements ContainerRequestFilter, ContainerResponseFilter {

	@Override
	public void filter(ContainerRequestContext reqCtx) throws IOException {

	}

	@Override
	public void filter(ContainerRequestContext reqCtx, ContainerResponseContext respCtx) throws IOException {

		respCtx.getHeaders().add("Access-Control-Allow-Origin", "http://soongwei.synology.me:9091");
		respCtx.getHeaders().add("Access-Control-Allow-Methods", "API, CRUNCHIFYGET, GET, POST, PUT, UPDATE, OPTIONS");
		respCtx.getHeaders().add("Access-Control-Max-Age", "151200");
		respCtx.getHeaders().add("Access-Control-Allow-Headers", "x-requested-with,Content-Type");

		String crunchifyRequestHeader = reqCtx.getHeaderString("Access-Control-Request-Headers");

		if (null != crunchifyRequestHeader && !crunchifyRequestHeader.equals(null)) {
			respCtx.getHeaders().add("Access-Control-Allow-Headers", crunchifyRequestHeader);
		}
	}



}
