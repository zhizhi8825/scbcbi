package com.gzwanhong.dao;

import java.util.List;

import com.gzwanhong.domain.Menu;
import com.gzwanhong.domain.User;

public interface MenuDao extends BaseDao {
	public List<Menu> initUserMenuTree(User user) throws Exception;
}
