package com.gzwanhong.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.ConfigurationLogic;
import com.gzwanhong.utils.JsonUtil;

@Controller
@Scope(value = "prototype")
public class ConfigurationAction extends SuperAction {

	private static final long serialVersionUID = 1L;
	@Autowired
	private ConfigurationLogic configurationLogic;
	private ParamEntity paramEntity;

	public ConfigurationLogic getConfigurationLogic() {
		return configurationLogic;
	}

	public void setConfigurationLogic(ConfigurationLogic configurationLogic) {
		this.configurationLogic = configurationLogic;
	}

	public ParamEntity getParamEntity() {
		return paramEntity;
	}

	public void setParamEntity(ParamEntity paramEntity) {
		this.paramEntity = paramEntity;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String saveOrUpdate() throws Exception {
		ResultEntity resultEntity = configurationLogic.saveOrUpdate(paramEntity);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String queryConfig() throws Exception {
		ResultEntity resultEntity = configurationLogic.queryConfig();
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}
}
