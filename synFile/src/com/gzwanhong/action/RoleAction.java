package com.gzwanhong.action;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gzwanhong.domain.Role;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.RoleLogic;
import com.gzwanhong.utils.JsonUtil;

@Controller
@Scope(value = "prototype")
public class RoleAction extends SuperAction {
	private static final long serialVersionUID = 1L;
	@Autowired
	private RoleLogic roleLogic;
	private Role role;
	private String id;
	private String[] ids;

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public RoleLogic getRoleLogic() {
		return roleLogic;
	}

	public void setRoleLogic(RoleLogic roleLogic) {
		this.roleLogic = roleLogic;
	}

	public String role() throws Exception {
		return SUCCESS;
	}

	public String queryDatagrid() throws Exception {
		DatagridEntity datagridEntity = roleLogic.queryDatagrid(getPageInfo());
		ajaxResponse(JsonUtil.beanToJson(datagridEntity));
		return NONE;
	}

	public String saveOrUpdateRole() throws Exception {
		ResultEntity resultEntity = roleLogic.saveOrUpdateRole(role);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String deleteRole() throws Exception {
		ResultEntity resultEntity = roleLogic.deleteRole(ids);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String queryComboboxByDepartmentId() throws Exception {
		ResultEntity resultEntity = roleLogic.queryComboboxByDepartmentId(id);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String queryCombobox() throws Exception {
		List<Role> list = roleLogic.queryCombobox();
		ajaxResponse(JsonUtil.beanToJson(list));
		return NONE;
	}
}
