package com.gzwanhong.logic;

import com.gzwanhong.domain.Department;
import com.gzwanhong.domain.User;
import com.gzwanhong.entity.ResultEntity;

public interface DepartmentLogic {
	public ResultEntity queryTreegrid(User user) throws Exception;

	public ResultEntity saveOrUpdateDepartment(Department department)
			throws Exception;

	public ResultEntity deleteDepartment(String id) throws Exception;

	public ResultEntity queryTree(User user) throws Exception;

	public ResultEntity queryComboboxByUser(User user) throws Exception;
}
