package com.gzwanhong.logic;

import java.util.List;
import java.util.Map;

import com.gzwanhong.domain.User;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ResultEntity;

public interface UserLogic {
	public DatagridEntity queryDatagrid(User user, Map<String, Object> paramMap, PageInfo pageInfo) throws Exception;

	public ResultEntity saveOrUpdateUser(User user) throws Exception;

	public ResultEntity deleteUser(String[] ids) throws Exception;

	public Map<String, Object> login(String userName, String password) throws Exception;

	public void test() throws Exception;

	public List<Map<String, Object>> queryUserCombobox(User user) throws Exception;

	public ResultEntity updatePassword(User user, String oldPassword, String password) throws Exception;
}
