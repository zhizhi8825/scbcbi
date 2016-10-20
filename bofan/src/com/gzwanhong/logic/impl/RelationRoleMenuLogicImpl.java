package com.gzwanhong.logic.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.gzwanhong.dao.RelationRoleMenuDao;
import com.gzwanhong.domain.RelationRoleMenu;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.RelationRoleMenuLogic;
import com.gzwanhong.utils.WhUtil;

@Service
@Scope(value = "prototype")
public class RelationRoleMenuLogicImpl implements RelationRoleMenuLogic {
	@Autowired
	private RelationRoleMenuDao relationRoleMenuDao;

	public RelationRoleMenuDao getRelationRoleMenuDao() {
		return relationRoleMenuDao;
	}

	public void setRelationRoleMenuDao(RelationRoleMenuDao relationRoleMenuDao) {
		this.relationRoleMenuDao = relationRoleMenuDao;
	}

	public ResultEntity saveRelationRoleMenu(String roleId, String[] ids)
			throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		if (!WhUtil.isEmpty(roleId)) {
			// 处理出中间表数据
			List<RelationRoleMenu> list = new ArrayList<RelationRoleMenu>();
			if (!WhUtil.isEmpty(ids) && ids.length > 0) {
				RelationRoleMenu relationRoleMenu = null;
				for (String id : ids) {
					relationRoleMenu = new RelationRoleMenu();
					relationRoleMenu.setRoleId(roleId);
					relationRoleMenu.setMenuId(id);
					list.add(relationRoleMenu);
				}
			}

			// 先删除该角色的所有菜单
			RelationRoleMenu example = new RelationRoleMenu();
			example.setRoleId(roleId);
			relationRoleMenuDao.removeByExample(example);

			// 再保存
			relationRoleMenuDao.saveAll(list);
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("请选择角色");
		}

		return resultEntity;
	}

	public ResultEntity queryByRoleId(String roleId) throws Exception {
		ResultEntity resultEntity = new ResultEntity();
		RelationRoleMenu relationRoleMenu = new RelationRoleMenu();
		relationRoleMenu.setRoleId(roleId);
		List<RelationRoleMenu> list = relationRoleMenuDao
				.queryByExample(relationRoleMenu);

		List<String> ids = new ArrayList<String>();
		if (!WhUtil.isEmpty(list) && list.size() > 0) {
			for (RelationRoleMenu temp : list) {
				ids.add(temp.getMenuId());
			}
		}
		resultEntity.setObj(ids);
		return resultEntity;
	}
}
