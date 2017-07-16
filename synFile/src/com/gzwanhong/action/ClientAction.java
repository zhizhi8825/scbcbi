package com.gzwanhong.action;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gzwanhong.domain.Client;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.ClientLogic;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.WhUtil;

@Controller
@Scope(value = "prototype")
public class ClientAction extends SuperAction {
	private static final long serialVersionUID = 1L;
	@Autowired
	private ClientLogic clientLogic;
	private Client client;
	private ParamEntity paramEntity;
	private String order;
	private String sort;
	private File file1;
	private String optionStr;
	private String sheetMap;

	public File getFile1() {
		return file1;
	}

	public void setFile1(File file1) {
		this.file1 = file1;
	}

	public ParamEntity getParamEntity() {
		return paramEntity;
	}

	public void setParamEntity(ParamEntity paramEntity) {
		this.paramEntity = paramEntity;
	}

	public String getOptionStr() {
		return optionStr;
	}

	public void setOptionStr(String optionStr) {
		this.optionStr = optionStr;
	}

	public String getOrder() {
		return order;
	}

	public String getSheetMap() {
		return sheetMap;
	}

	public void setSheetMap(String sheetMap) {
		this.sheetMap = sheetMap;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public ClientLogic getClientLogic() {
		return clientLogic;
	}

	public void setClientLogic(ClientLogic clientLogic) {
		this.clientLogic = clientLogic;
	}

	public String client() throws Exception {
		return SUCCESS;
	}

	public String clientIntention() throws Exception {
		return SUCCESS;
	}

	public String dayReport() throws Exception {
		return SUCCESS;
	}

	public String changeClient() throws Exception {
		return SUCCESS;
	}

	public String queryDatagrid() throws Exception {
		if (!WhUtil.isEmpty(sort)) {
			if (WhUtil.isEmpty(paramEntity)) {
				paramEntity = new ParamEntity();
			}

			paramEntity.setOrderBy(sort + " " + WhUtil.toString(order));
		}

		DatagridEntity datagridEntity = clientLogic.queryDatagrid(paramEntity, getPageInfo(), super.getUser());

		ajaxResponse(JsonUtil.beanToJson(datagridEntity, WhUtil.YYYY_MM_DD_HH_MM_SS));
		return NONE;
	}

	public String saveOrUpdateClient() throws Exception {
		ResultEntity resultEntity = clientLogic.saveOrUpdateClient(client, super.getUser(), paramEntity);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String deleteClient() throws Exception {
		ResultEntity resultEntity = clientLogic.deleteClient(paramEntity.getIds(), super.getUser());
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	/**
	 * 电话日报表
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryDayReport() throws Exception {
		DatagridEntity datagridEntity = clientLogic.queryDayReport(paramEntity, super.getUser());
		ajaxResponse(JsonUtil.beanToJson(datagridEntity));
		return NONE;
	}

	/**
	 * 保存转移
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveChange() throws Exception {
		ResultEntity resultEntity = clientLogic.saveChange(paramEntity, super.getUser());
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	/**
	 * 导入电话日报
	 * 
	 * @return
	 * @throws Exception
	 */
	public String importClient() throws Exception {
		ResultEntity resultEntity = clientLogic.importClient(file1, super.getWebPath(), super.getUser());
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	/**
	 * 导入意向客户
	 * 
	 * @return
	 * @throws Exception
	 */
	public String importClientIntention() throws Exception {
		ResultEntity resultEntity = clientLogic.importClientIntention(file1, super.getWebPath(), super.getUser(),
				optionStr, sheetMap);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}
}
