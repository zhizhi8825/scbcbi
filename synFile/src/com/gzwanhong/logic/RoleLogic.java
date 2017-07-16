package com.gzwanhong.logic;

import java.util.List;

import com.gzwanhong.domain.Role;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ResultEntity;

public interface RoleLogic {
	public DatagridEntity queryDatagrid(PageInfo pageInfo) throws Exception;

	public ResultEntity saveOrUpdateRole(Role role) throws Exception;

	public ResultEntity deleteRole(String[] ids) throws Exception;

	public ResultEntity queryComboboxByDepartmentId(String id) throws Exception;

	public List<Role> queryCombobox() throws Exception;
}
