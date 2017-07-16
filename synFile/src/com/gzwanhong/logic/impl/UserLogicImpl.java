package com.gzwanhong.logic.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.gzwanhong.dao.DepartmentDao;
import com.gzwanhong.dao.UserDao;
import com.gzwanhong.domain.Department;
import com.gzwanhong.domain.User;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.UserLogic;
import com.gzwanhong.mapper.UserMapper;
import com.gzwanhong.utils.WhUtil;

@Service
@Scope(value = "prototype")
public class UserLogicImpl implements UserLogic {
	@Autowired
	private UserDao userDao;
	@Autowired
	private DepartmentDao departmentDao;

	public DepartmentDao getDepartmentDao() {
		return departmentDao;
	}

	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public DatagridEntity queryDatagrid(User user, Map<String, Object> paramMap, PageInfo pageInfo) throws Exception {
		if (!"admin".equals(user.getUserName())) {
			if (WhUtil.isEmpty(user.getLimitsLevel()) || user.getLimitsLevel().intValue() == 0) {
				// 普票用户，只查出自己的
				paramMap.put("id", user.getId());
			} else if (user.getLimitsLevel().intValue() == 1) {
				// 横向权限的用户，能查出同部门的所有user
				paramMap.put("deptId", user.getDepartmentId());
			} else if (user.getLimitsLevel().intValue() == 2) {
				// 纵向权限的用户，能查下级部门的所有user
				List<String> idList = departmentDao.querySubDeptIdNotOwn(user.getDepartmentId());
				paramMap.put("deptIds", idList);
			} else if (user.getLimitsLevel().intValue() == 3) {
				// 横纵向权限的用户，能查同部门和下级部门的所有user
				List<String> idList = departmentDao.querySubDeptId(user.getDepartmentId());
				paramMap.put("deptIds", idList);
			} else {
				// 普票用户，只查出自己的
				paramMap.put("id", user.getId());
			}
		}

		DatagridEntity datagridEntity = userDao.queryBySqlToDatagrid(UserMapper.class, "queryDatagrid", pageInfo,
				paramMap);

		return datagridEntity;
	}

	public ResultEntity saveOrUpdateUser(User user) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		if (!WhUtil.isEmpty(user)) {
			// if (WhUtil.isEmpty(user.getId())) {
			userDao.saveOrUpdate(user);
			// } else {
			// User temp = userDao.queryById(user.getId(), User.class);
			// user.setPassword(temp.getPassword());
			// userDao.saveOrUpdate(user);
			// }
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("数据不能为空");
		}

		return resultEntity;
	}

	public ResultEntity deleteUser(String[] ids) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		if (!WhUtil.isEmpty(ids) && ids.length > 0) {
			userDao.removeByIds(Arrays.asList(ids), User.class);
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("数据不能为空");
		}

		return resultEntity;
	}

	public Map<String, Object> login(String userName, String password) throws Exception {
		Map<String, Object> result = null;
		User user = new User();
		user.setUserName(userName);
		user.setPassword(password);
		List<User> list = userDao.queryByExample(user);

		if (!WhUtil.isEmpty(list) && list.size() > 0) {
			// 把部门查出来
			result = new HashMap<String, Object>();
			Department department = userDao.queryById(list.get(0).getDepartmentId(), Department.class);
			result.put("user", list.get(0));
			result.put("department", department);
		}

		return result;
	}

	/**
	 * 根据权限来查该用户所能看到的user
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> queryUserCombobox(User user) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();

		if (!"admin".equals(user.getUserName())) {
			if (WhUtil.isEmpty(user.getLimitsLevel()) || user.getLimitsLevel().intValue() == 0) {
				// 普票用户，只查出自己的
				paramMap.put("id", user.getId());
			} else if (user.getLimitsLevel().intValue() == 1) {
				// 横向权限的用户，能查出同部门的所有user
				paramMap.put("deptId", user.getDepartmentId());
			} else if (user.getLimitsLevel().intValue() == 2) {
				// 纵向权限的用户，能查下级部门的所有user
				List<String> idList = departmentDao.querySubDeptIdNotOwn(user.getDepartmentId());
				paramMap.put("deptIds", idList);
			} else if (user.getLimitsLevel().intValue() == 3) {
				// 横纵向权限的用户，能查同部门和下级部门的所有user
				List<String> idList = departmentDao.querySubDeptId(user.getDepartmentId());
				paramMap.put("deptIds", idList);
			} else {
				// 普票用户，只查出自己的
				paramMap.put("id", user.getId());
			}
		}

		List<Map<String, Object>> userList = userDao.queryBySql(UserMapper.class, "queryUserCombobox", paramMap);

		return userList;
	}

	public void test() throws Exception {
		userDao.test();
	}

	/**
	 * 修改密码
	 * 
	 * @param user
	 * @param oldPassword
	 * @param password
	 * @return
	 * @throws Exception
	 */
	public ResultEntity updatePassword(User user, String oldPassword, String password) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		// 先验证看旧密码对不对
		User example = new User();
		example.setUserName(user.getUserName());
		example.setPassword(oldPassword);

		List<User> userList = userDao.queryByExample(example);

		if (!WhUtil.isEmpty(userList) && userList.size() > 0) {
			example = userList.get(0);
			example.setPassword(password);
			userDao.update(example);
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("旧密码不正确");
		}

		return resultEntity;
	}
}
