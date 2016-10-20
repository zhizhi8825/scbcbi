package com.gzwanhong.logic;

import com.gzwanhong.domain.Menu;
import com.gzwanhong.domain.User;
import com.gzwanhong.entity.ResultEntity;

public interface MenuLogic {
	public ResultEntity initUserMenuTree(User user) throws Exception;

	public ResultEntity queryTreegrid() throws Exception;

	public ResultEntity saveOrUpdateMenu(Menu menu) throws Exception;

	public ResultEntity deleteMenu(String id) throws Exception;
}
