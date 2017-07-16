package com.gzwanhong.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.gzwanhong.dao.MenuDao;
import com.gzwanhong.domain.Menu;
import com.gzwanhong.domain.User;

@Repository
@Scope(value = "prototype")
public class MenuDaoImpl extends BaseDaoImpl implements MenuDao {

	public List<Menu> initUserMenuTree(User user) throws Exception {
		List<Menu> list = new ArrayList<Menu>();
		String sql = "";

		if ("admin".equals(user.getUserName())) {
			sql = "select m.* from menu m order by m.seq";
		} else {
			sql += " SELECT                                                    ";
			sql += " 	m.*                                                    ";
			sql += " FROM                                                      ";
			sql += " 	menu m                                                 ";
			sql += " INNER JOIN relation_role_menu rrm ON m.id = rrm.menu_id   ";
			sql += " INNER JOIN role r ON rrm.role_id = r.id                   ";
			sql += " WHERE r.id = '" + user.getRoleId()
					+ "' order by m.seq                    ";
		}

		System.out.println(sql);
		list = this.queryBySql(sql, Menu.class);
		return list;
	}
}
