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
	private String filePath;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

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
	 * 下载指定路径的文件
	 * 
	 * @return
	 * @throws Exception
	 */
	public String downloadFile() throws Exception {
		synFileOptionLogic.downloadFile(getResponse().getOutputStream(), filePath);
		return NONE;
	}
}
