package com.gzwanhong.dao;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gzwanhong.domain.Department;
import com.gzwanhong.domain.User;
import com.gzwanhong.mapper.DepartmentMapper;
import com.gzwanhong.utils.WhUtil;

public interface DepartmentDao extends BaseDao {
	public List<Department> queryTreegrid(User user) throws Exception;

	public List<Department> queryComboboxByUser(User user) throws Exception;

	public List<String> querySubDeptId(String id) throws Exception;

	public List<String> querySubDeptIdNotOwn(String id) throws Exception;
}
