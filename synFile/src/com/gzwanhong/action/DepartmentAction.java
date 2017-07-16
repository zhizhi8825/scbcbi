package com.gzwanhong.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gzwanhong.domain.Department;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.DepartmentLogic;
import com.gzwanhong.utils.JsonUtil;

@Controller
@Scope(value = "prototype")
public class DepartmentAction extends SuperAction {
	private static final long serialVersionUID = 1L;
	@Autowired
	private DepartmentLogic departmentLogic;
	private Department department;
	private String id;

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public DepartmentLogic getDepartmentLogic() {
		return departmentLogic;
	}

	public void setDepartmentLogic(DepartmentLogic departmentLogic) {
		this.departmentLogic = departmentLogic;
	}

	public String department() throws Exception {
		return SUCCESS;
	}

	public String queryTreegrid() throws Exception {
		ResultEntity resultEntity = departmentLogic.queryTreegrid(super
				.getUser());
		ajaxResponse(JsonUtil.beanToJson(resultEntity.getObj()));
		return NONE;
	}

	public String saveOrUpdateDepartment() throws Exception {
		ResultEntity resultEntity = departmentLogic
				.saveOrUpdateDepartment(department);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String deleteDepartment() throws Exception {
		ResultEntity resultEntity = departmentLogic.deleteDepartment(id);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String queryTree() throws Exception {
		ResultEntity resultEntity = departmentLogic.queryTree(super.getUser());
		ajaxResponse(JsonUtil.beanToJson(resultEntity.getObj()));
		return NONE;
	}

	public String queryComboboxByUser() throws Exception {
		ResultEntity resultEntity = departmentLogic.queryComboboxByUser(super
				.getUser());
		ajaxResponse(JsonUtil.beanToJson(resultEntity.getObj()));
		return NONE;
	}
}
