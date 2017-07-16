package com.gzwanhong.logic.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.gzwanhong.dao.RoleDao;
import com.gzwanhong.domain.Role;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.RoleLogic;
import com.gzwanhong.utils.WhUtil;

@Service
@Scope(value = "prototype")
public class RoleLogicImpl implements RoleLogic {
	@Autowired
	private RoleDao roleDao;

	public RoleDao getRoleDao() {
		return roleDao;
	}

	public void setRoleDao(RoleDao roleDao) {
		this.roleDao = roleDao;
	}

	public DatagridEntity queryDatagrid(PageInfo pageInfo) throws Exception {
		DatagridEntity datagridEntity = new DatagridEntity();
		Role role = new Role();
		datagridEntity = roleDao.queryByExampleToDatagrid(role, pageInfo);
		return datagridEntity;
	}

	public ResultEntity saveOrUpdateRole(Role role) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		if (!WhUtil.isEmpty(role)) {
			roleDao.saveOrUpdate(role);
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("数据不能为空");
		}

		return resultEntity;
	}

	public ResultEntity deleteRole(String[] ids) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		if (!WhUtil.isEmpty(ids) && ids.length > 0) {
			roleDao.removeByIds(Arrays.asList(ids), Role.class);
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("数据不能为空");
		}

		return resultEntity;
	}

	/**
	 * 查询该用户部门下的所有角色下拉框
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public ResultEntity queryComboboxByDepartmentId(String id) throws Exception {
		ResultEntity resultEntity = new ResultEntity();
		if (!WhUtil.isEmpty(id)) {
			Role role = new Role();
			List<Role> list = roleDao.queryByExample(role);
			resultEntity.setObj(list);
		} else {
			resultEntity.setObj(new ArrayList<Role>());
		}
		return resultEntity;
	}

	public List<Role> queryCombobox() throws Exception {
		Role role = new Role();
		List<Role> list = roleDao.queryByExample(role);
		return list;
	}
}
