package com.gzwanhong.webservice;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "serviceInterface", targetNamespace = "http://webservice.gzwanhong.com", serviceName = "serviceInterface")
public interface ServiceInterface {

	@WebMethod(operationName = "testMethod", action = "testMethod")
	@WebResult
	public String testMethod(@WebParam(name = "json") String json) throws Exception;

	@WebMethod(operationName = "getBackupFile", action = "getBackupFile")
	@WebResult
	public String getBackupFile(@WebParam(name = "json") String json) throws Exception;

	@WebMethod(operationName = "addRecord", action = "addRecord")
	@WebResult
	public String addRecord(@WebParam(name = "json") String json) throws Exception;

	@WebMethod(operationName = "getAllFile", action = "getAllFile")
	@WebResult
	public String getAllFile(@WebParam(name = "json") String json) throws Exception;
	
	@WebMethod(operationName = "startCheckFile", action = "startCheckFile")
	@WebResult
	public String startCheckFile(@WebParam(name = "json") String json) throws Exception;

	@WebMethod(operationName = "downloadFile", action = "downloadFile")
	@WebResult
	public String downloadFile(@WebParam(name = "json") String json) throws Exception;

}
