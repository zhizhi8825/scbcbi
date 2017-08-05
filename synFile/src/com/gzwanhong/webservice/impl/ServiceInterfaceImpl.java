package com.gzwanhong.webservice.impl;

import javax.jws.WebService;

import org.springframework.stereotype.Component;

import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.SynFileOptionLogic;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.SpringUtil;
import com.gzwanhong.webservice.ServiceInterface;

@Component
@WebService(name = "serviceInterface", targetNamespace = "http://webservice.gzwanhong.com", serviceName = "serviceInterface")
public class ServiceInterfaceImpl implements ServiceInterface {

	public String testMethod(String json) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		return JsonUtil.beanToJson(resultEntity);
	}

	public String getBackupFile(String json) throws Exception {
		SynFileOptionLogic synFileOptionLogic = SpringUtil.getBean(SynFileOptionLogic.class);
		ResultEntity resultEntity = new ResultEntity();
		resultEntity = synFileOptionLogic.getBackupFile(json);

		return JsonUtil.beanToJson(resultEntity);
	}

	public String addRecord(String json) throws Exception {
		SynFileOptionLogic synFileOptionLogic = SpringUtil.getBean(SynFileOptionLogic.class);
		ResultEntity resultEntity = new ResultEntity();
		resultEntity = synFileOptionLogic.addRecord(json);

		return JsonUtil.beanToJson(resultEntity);
	}

	public String startCheckFile(String json) throws Exception {
		SynFileOptionLogic synFileOptionLogic = SpringUtil.getBean(SynFileOptionLogic.class);
		ResultEntity resultEntity = new ResultEntity();
		resultEntity = synFileOptionLogic.startCheckFile(json);

		return JsonUtil.beanToJson(resultEntity);
	}

	public String getAllFile(String json) throws Exception {
		SynFileOptionLogic synFileOptionLogic = SpringUtil.getBean(SynFileOptionLogic.class);
		ResultEntity resultEntity = new ResultEntity();
		resultEntity = synFileOptionLogic.getAllFile(json);

		return JsonUtil.beanToJson(resultEntity);
	}

	public String downloadFile(String json) throws Exception {
		SynFileOptionLogic synFileOptionLogic = SpringUtil.getBean(SynFileOptionLogic.class);
		ResultEntity resultEntity = new ResultEntity();
		resultEntity = synFileOptionLogic.downloadFile(json);

		return JsonUtil.beanToJson(resultEntity);
	}
}
