package com.gzwanhong.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtil implements ApplicationContextAware {
	private static ApplicationContext context;// 声明一个静态变量保存

	@Override
	public void setApplicationContext(ApplicationContext ac)
			throws BeansException {
		context = ac;
	}

	public static ApplicationContext getContext() {
		return context;
	}

	public static <T> T getBean(Class<T> clzz) {
		return context.getBean(clzz);
	}

}
