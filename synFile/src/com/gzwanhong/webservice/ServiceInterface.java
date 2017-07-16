package com.gzwanhong.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "serviceInterface", targetNamespace = "http://webservice.gzwanhong.com", serviceName = "serviceInterface")
public interface ServiceInterface {

	@WebMethod(operationName = "testMethod", action = "testMethod")
	@WebResult
	public String testMethod(@WebParam(name = "json") String json)
			throws Exception;

}
