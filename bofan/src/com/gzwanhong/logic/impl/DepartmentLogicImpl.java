package com.gzwanhong.logic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.gzwanhong.dao.DepartmentDao;
import com.gzwanhong.domain.Department;
import com.gzwanhong.domain.Menu;
import com.gzwanhong.domain.User;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.DepartmentLogic;
import com.gzwanhong.mapper.DepartmentMapper;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.TreeUtil;
import com.gzwanhong.utils.WhUtil;

@Service
@Scope(value = "prototype")
public class DepartmentLogicImpl implements DepartmentLogic {
	@Autowired
	private DepartmentDao departmentDao;

	public DepartmentDao getDepartmentDao() {
		return departmentDao;
	}

	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	public ResultEntity queryTreegrid(User user) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		List<Department> list = departmentDao.queryTreegrid(user);

		// 把集合处理成map
		Map<String, List<Department>> tMap = new HashMap<String, List<Department>>();
		List<Department> mList = null;
		for (Department m : list) {
			mList = tMap.get(m.getParentId());
			if (WhUtil.isEmpty(mList)) {
				mList = new ArrayList<Department>();
			}

			mList.add(m);
			tMap.put(m.getParentId(), mList);
		}

		// 循环处理，parentId为-1的是根
		List<Object> tree = new ArrayList<Object>();
		Map<String, Object> treeMap = null;
		Map<String, String> elementsMap = new HashMap<String, String>();
		elementsMap.put("id", "getId");
		elementsMap.put("name", "getName");
		elementsMap.put("parentId", "getParentId");
		elementsMap.put("tel", "getTel");
		elementsMap.put("address", "getAddress");
		elementsMap.put("linkman", "getLinkman");
		elementsMap.put("createTime", "getCreateTime");
		for (Department m : list) {
			if ("-1".equals(m.getParentId())) {
				treeMap = new HashMap<String, Object>();
				treeMap.put("id", m.getId());
				treeMap.put("name", m.getName());
				treeMap.put("parentId", m.getParentId());
				treeMap.put("tel", m.getTel());
				treeMap.put("address", m.getAddress());
				treeMap.put("linkman", m.getLinkman());
				treeMap.put("createTime", m.getCreateTime());

				treeMap = TreeUtil.dealTree(treeMap, tMap, Menu.class,
						elementsMap, null, false);
				tree.add(treeMap);
			}
		}

		resultEntity.setObj(tree);

		return resultEntity;
	}

	public ResultEntity saveOrUpdateDepartment(Department department)
			throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		if (!WhUtil.isEmpty(department)) {
			departmentDao.saveOrUpdate(department);
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("数据不能为空");
		}

		return resultEntity;
	}

	/**
	 * 删除菜单
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public ResultEntity deleteDepartment(String id) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		if (!WhUtil.isEmpty(id)) {
			departmentDao.removeById(id, Department.class);
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("数据不能为空");
		}

		return resultEntity;
	}

	public ResultEntity queryTree(User user) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		List<Department> list = departmentDao.queryTreegrid(user);

		// 把集合处理成map
		Map<String, List<Department>> tMap = new HashMap<String, List<Department>>();
		List<Department> mList = null;
		for (Department m : list) {
			mList = tMap.get(m.getParentId());
			if (WhUtil.isEmpty(mList)) {
				mList = new ArrayList<Department>();
			}

			mList.add(m);
			tMap.put(m.getParentId(), mList);
		}

		// 循环处理，parentId为-1的是根
		List<Object> tree = new ArrayList<Object>();
		Map<String, Object> treeMap = null;
		Map<String, String> elementsMap = new HashMap<String, String>();
		elementsMap.put("id", "getId");
		elementsMap.put("text", "getName");
		for (Department m : list) {
			if ("-1".equals(m.getParentId())) {
				treeMap = new HashMap<String, Object>();
				treeMap.put("id", m.getId());
				treeMap.put("text", m.getName());

				treeMap = TreeUtil.dealTree(treeMap, tMap, Menu.class,
						elementsMap, null, false);
				tree.add(treeMap);
			}
		}

		resultEntity.setObj(tree);

		return resultEntity;
	}

	/**
	 * 查询该用记下的所有部门下拉框
	 * 
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public ResultEntity queryComboboxByUser(User user) throws Exception {
		ResultEntity resultEntity = new ResultEntity();
		List<Department> list = departmentDao.queryComboboxByUser(user);
		resultEntity.setObj(list);
		return resultEntity;
	}
}
