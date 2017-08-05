package com.gzwanhong.webservice.impl;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.webservice.ServiceInterface;

@Component
@WebService(name = "serviceInterface", targetNamespace = "http://webservice.gzwanhong.com", serviceName = "serviceInterface")
public class ServiceInterfaceImpl implements ServiceInterface {

	public String testMethod(String json) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		return JsonUtil.beanToJson(resultEntity);
	}
}
