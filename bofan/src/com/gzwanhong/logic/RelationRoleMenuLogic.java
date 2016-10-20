package com.gzwanhong.logic;

import com.gzwanhong.entity.ResultEntity;

public interface RelationRoleMenuLogic {
	public ResultEntity saveRelationRoleMenu(String roleId, String[] ids)
			throws Exception;

	public ResultEntity queryByRoleId(String roleId) throws Exception;
}
