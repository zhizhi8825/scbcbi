package com.gzwanhong.utils;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;

import com.gzwanhong.entity.RequestEntity;
import com.gzwanhong.entity.ResultEntity;

public class AopAdvice {
	Logger log = Logger.getLogger(this.getClass());
	RequestEntity requestEntity = null;
	String json = null;

	/**
	 * 环绕增强
	 * 
	 * @param pjp
	 * @return
	 */
	public Object around(ProceedingJoinPoint pjp) {
		try {
			json = (String) pjp.getArgs()[0];
			System.out.println(json);
			requestEntity = (RequestEntity) JsonUtil.jsonToBean(json,
					RequestEntity.class);

			if (!"wh".equals(requestEntity.getSecurity())) {
				throw new WhException("拒绝访问");
			}

			return pjp.proceed();
		} catch (Throwable e) {
			e.printStackTrace();

			log.error(e, e.fillInStackTrace());

			ResultEntity resultEntity = new ResultEntity(false);
			resultEntity.setError(e.getMessage());

			StringBuffer sp = new StringBuffer();
			for (StackTraceElement ste : e.getStackTrace()) {
				sp.append(ste.toString() + "\n");
			}
			resultEntity.setErrorDetail(sp.toString());

			return JsonUtil.beanToJson(resultEntity);
		}
	}
}
