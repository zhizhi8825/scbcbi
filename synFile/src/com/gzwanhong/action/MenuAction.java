package com.gzwanhong.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gzwanhong.domain.Menu;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.MenuLogic;
import com.gzwanhong.utils.JsonUtil;

@Controller
@Scope(value = "prototype")
public class MenuAction extends SuperAction {
	private static final long serialVersionUID = 1L;
	@Autowired
	private MenuLogic menuLogic;
	private Menu menu;
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public MenuLogic getMenuLogic() {
		return menuLogic;
	}

	public void setMenuLogic(MenuLogic menuLogic) {
		this.menuLogic = menuLogic;
	}

	public String initUserMenuTree() throws Exception {
		ResultEntity resultEntity = menuLogic.initUserMenuTree(super.getUser());
		ajaxResponse(JsonUtil.beanToJson(resultEntity.getObj()));
		return NONE;
	}

	public String menu() throws Exception {
		return SUCCESS;
	}

	public String queryTreegrid() throws Exception {
		ResultEntity resultEntity = menuLogic.queryTreegrid();
		ajaxResponse(JsonUtil.beanToJson(resultEntity.getObj()));
		return NONE;
	}

	/**
	 * 保存或修改菜单
	 * 
	 * @return
	 * @throws Exception
	 */
	public String saveOrUpdateMenu() throws Exception {
		ResultEntity resultEntity = menuLogic.saveOrUpdateMenu(menu);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String deleteMenu() throws Exception {
		ResultEntity resultEntity = menuLogic.deleteMenu(id);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}
}
