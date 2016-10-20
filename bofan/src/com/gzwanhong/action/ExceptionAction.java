package com.gzwanhong.action;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;

import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.utils.JsonUtil;

@Controller
public class ExceptionAction extends SuperAction {
	private static final long serialVersionUID = 1L;
	protected final Logger log = Logger.getLogger(getClass());

	public String exception() {
		Exception exception = (Exception) getRequest()
				.getAttribute("exception");

		exception.printStackTrace();
		log.error(exception, exception.fillInStackTrace());

		ResultEntity result = new ResultEntity(false);
		result.setError(exception.getMessage());

		StringBuffer sp = new StringBuffer();
		for (StackTraceElement ste : exception.getStackTrace()) {
			sp.append(ste.toString() + "\n");
		}
		result.setErrorDetail(sp.toString());

		ajaxResponse(JsonUtil.beanToJson(result));

		return NONE;
	}
}
