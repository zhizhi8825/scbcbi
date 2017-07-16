package com.gzwanhong.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gzwanhong.domain.Dictionary;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.logic.DictionaryLogic;
import com.gzwanhong.utils.JsonUtil;

@Controller
@Scope(value = "prototype")
public class DictionaryAction extends SuperAction {

	private static final long serialVersionUID = 1L;

	@Autowired
	private DictionaryLogic dictionaryLogic;
	private ParamEntity paramEntity;

	public ParamEntity getParamEntity() {
		return paramEntity;
	}

	public void setParamEntity(ParamEntity paramEntity) {
		this.paramEntity = paramEntity;
	}

	public DictionaryLogic getDictionaryLogic() {
		return dictionaryLogic;
	}

	public void setDictionaryLogic(DictionaryLogic dictionaryLogic) {
		this.dictionaryLogic = dictionaryLogic;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String queryByType() throws Exception {
		List<Dictionary> list = dictionaryLogic.queryByType(paramEntity);
		ajaxResponse(JsonUtil.beanToJson(list));
		return NONE;
	}
}
