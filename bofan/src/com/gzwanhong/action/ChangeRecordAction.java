package com.gzwanhong.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.logic.ChangeRecordLogic;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.WhUtil;

@Controller
@Scope(value = "prototype")
public class ChangeRecordAction extends SuperAction {
	private static final long serialVersionUID = 1L;

	@Autowired
	private ChangeRecordLogic changeRecordLogic;
	private ParamEntity paramEntity;

	public ParamEntity getParamEntity() {
		return paramEntity;
	}

	public void setParamEntity(ParamEntity paramEntity) {
		this.paramEntity = paramEntity;
	}

	public ChangeRecordLogic getChangeRecordLogic() {
		return changeRecordLogic;
	}

	public void setChangeRecordLogic(ChangeRecordLogic changeRecordLogic) {
		this.changeRecordLogic = changeRecordLogic;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String queryDatagrid() throws Exception {
		DatagridEntity datagridEntity = changeRecordLogic.queryDatagrid(paramEntity, super.getPageInfo());
		ajaxResponse(JsonUtil.beanToJson(datagridEntity, WhUtil.YYYY_MM_DD_HH_MM_SS));
		return NONE;
	}

}
