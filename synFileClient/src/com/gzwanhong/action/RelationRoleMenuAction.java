package com.gzwanhong.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.RelationRoleMenuLogic;
import com.gzwanhong.utils.JsonUtil;

@Controller
@Scope(value = "prototype")
public class RelationRoleMenuAction extends SuperAction {
	private static final long serialVersionUID = 1L;
	@Autowired
	private RelationRoleMenuLogic relationRoleMenuLogic;
	private String[] ids;
	private String roleId;

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public RelationRoleMenuLogic getRelationRoleMenuLogic() {
		return relationRoleMenuLogic;
	}

	public void setRelationRoleMenuLogic(
			RelationRoleMenuLogic relationRoleMenuLogic) {
		this.relationRoleMenuLogic = relationRoleMenuLogic;
	}

	public String saveRelationRoleMenu() throws Exception {
		ResultEntity resultEntity = relationRoleMenuLogic.saveRelationRoleMenu(
				roleId, ids);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String queryByRoleId() throws Exception {
		ResultEntity resultEntity = relationRoleMenuLogic.queryByRoleId(roleId);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		System.out.println(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

}
