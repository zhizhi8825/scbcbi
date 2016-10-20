package com.gzwanhong.logic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.gzwanhong.dao.MenuDao;
import com.gzwanhong.domain.Menu;
import com.gzwanhong.domain.User;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.MenuLogic;
import com.gzwanhong.utils.TreeUtil;
import com.gzwanhong.utils.WhUtil;

@Service
@Scope(value = "prototype")
public class MenuLogicImpl implements MenuLogic {
	@Autowired
	private MenuDao menuDao;

	public MenuDao getMenuDao() {
		return menuDao;
	}

	public void setMenuDao(MenuDao menuDao) {
		this.menuDao = menuDao;
	}

	/**
	 * 查询菜单
	 * 
	 * @param roleId
	 * @return
	 * @throws Exception
	 */
	public ResultEntity initUserMenuTree(User user) throws Exception {
		ResultEntity resultEntity = new ResultEntity();
		List<Menu> list = menuDao.initUserMenuTree(user);

		// 把集合处理成map
		Map<String, List<Menu>> tMap = new HashMap<String, List<Menu>>();
		List<Menu> mList = null;
		for (Menu m : list) {
			mList = tMap.get(m.getParentId());
			if (WhUtil.isEmpty(mList)) {
				mList = new ArrayList<Menu>();
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
		Map<String, String> attributesMap = new HashMap<String, String>();
		attributesMap.put("url", "getUrl");
		for (Menu m : list) {
			if ("-1".equals(m.getParentId())) {
				treeMap = new HashMap<String, Object>();
				treeMap.put("id", m.getId());
				treeMap.put("text", m.getName());

				treeMap = TreeUtil.dealTree(treeMap, tMap, Menu.class, elementsMap, attributesMap, false);
				tree.add(treeMap);
			}
		}

		resultEntity.setObj(tree);

		return resultEntity;
	}

	public ResultEntity queryTreegrid() throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		User user = new User();
		user.setUserName("admin");
		List<Menu> list = menuDao.initUserMenuTree(user);

		// 把集合处理成map
		Map<String, List<Menu>> tMap = new HashMap<String, List<Menu>>();
		List<Menu> mList = null;
		for (Menu m : list) {
			mList = tMap.get(m.getParentId());
			if (WhUtil.isEmpty(mList)) {
				mList = new ArrayList<Menu>();
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
		elementsMap.put("url", "getUrl");
		elementsMap.put("parentId", "getParentId");
		elementsMap.put("seq", "getSeq");
		elementsMap.put("remark", "getRemark");
		elementsMap.put("createTime", "getCreateTime");
		for (Menu m : list) {
			if ("-1".equals(m.getParentId())) {
				treeMap = new HashMap<String, Object>();
				treeMap.put("id", m.getId());
				treeMap.put("name", m.getName());
				treeMap.put("url", m.getUrl());
				treeMap.put("parentId", m.getParentId());
				treeMap.put("seq", m.getSeq());
				treeMap.put("remark", m.getRemark());
				treeMap.put("createTime", m.getCreateTime());

				treeMap = TreeUtil.dealTree(treeMap, tMap, Menu.class, elementsMap, null, false);
				tree.add(treeMap);
			}
		}

		resultEntity.setObj(tree);

		return resultEntity;
	}

	public ResultEntity saveOrUpdateMenu(Menu menu) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		if (!WhUtil.isEmpty(menu)) {
			menuDao.saveOrUpdate(menu);
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
	public ResultEntity deleteMenu(String id) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		if (!WhUtil.isEmpty(id)) {
			menuDao.removeById(id, Menu.class);
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("数据不能为空");
		}

		return resultEntity;
	}
}
