package com.gzwanhong.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.SynFileOptionLogic;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.WhCommon;
import com.gzwanhong.utils.WhUtil;

@Controller
@Scope(value = "prototype")
public class SynFileOptionAction extends SuperAction {
	private static final long serialVersionUID = 1L;
	@Autowired
	private SynFileOptionLogic synFileOptionLogic;
	private ParamEntity paramEntity;

	public ParamEntity getParamEntity() {
		return paramEntity;
	}

	public void setParamEntity(ParamEntity paramEntity) {
		this.paramEntity = paramEntity;
	}

	public SynFileOptionLogic getSynFileOptionLogic() {
		return synFileOptionLogic;
	}

	public void setSynFileOptionLogic(SynFileOptionLogic synFileOptionLogic) {
		this.synFileOptionLogic = synFileOptionLogic;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String synFileOption() throws Exception {
		getRequest().setAttribute("isOn", WhCommon.isOn);
		return SUCCESS;
	}

	public String onOff() throws Exception {
		ResultEntity resultEntity = synFileOptionLogic.onOff(paramEntity);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String queryDatagrid() throws Exception {
		DatagridEntity datagridEntity = synFileOptionLogic.queryDatagrid(getPageInfo(), paramEntity);
		ajaxResponse(JsonUtil.beanToJson(datagridEntity, WhUtil.YYYY_MM_DD_HH_MM_SSS));
		return NONE;
	}

	/**
	 * 是否还在整站备份的任务没进行完
	 * 
	 * @return
	 * @throws Exception
	 */
	public String hasDownTask() throws Exception {
		ResultEntity resultEntity = synFileOptionLogic.hasDownTask();
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String getAllFile() throws Exception {
		ResultEntity resultEntity = synFileOptionLogic.getAllFile();
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String getDownloadStatus() throws Exception {
		ResultEntity resultEntity = synFileOptionLogic.getDownloadStatus();
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String startDownFile() throws Exception {
		ResultEntity resultEntity = synFileOptionLogic.startDownFile();
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String stopDownFile() throws Exception {
		ResultEntity resultEntity = synFileOptionLogic.stopDownFile();
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}
}
