package com.soongwei.meow.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.soongwei.meow.logics.TPBFunctions;
import com.soongwei.meow.objects.json.Request.SearchRequest;
import com.soongwei.meow.objects.json.Respond.TPBResults;
import com.soongwei.meow.services.interceptors.SessionBinding;


@Path("/TPBService")
public class TPBService {


	
	@GET
	@Path("/test")
	@Produces(MediaType.APPLICATION_JSON)
	public String test() {
		String testWord = "";

	

		testWord = "Hello";

		return testWord;
	}
	
	@POST
	@Path("/searchPOST")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> searchPOST(SearchRequest searchRequest) {
		 Map<String, Object> respondMap = new HashMap<String, Object>();
		List<TPBResults> tpbResults = new ArrayList<>();
		
		tpbResults = TPBFunctions.search(searchRequest);
		
		respondMap.put("results", tpbResults);
		return respondMap;
	}
	
	@SessionBinding
	@GET
	@Path("/searchGET")
	@Produces(MediaType.APPLICATION_JSON)
	public Map<String, Object> searchGET(@QueryParam("type") int type,@QueryParam("search") String search) {
		 Map<String, Object> respondMap = new HashMap<String, Object>();
		 
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.setType(type);
		searchRequest.setSearch(search);
		List<TPBResults> tpbResults = new ArrayList<>();


		tpbResults = TPBFunctions.search(searchRequest);
		
		respondMap.put("results", tpbResults);

		return respondMap;
	}
	
	
}