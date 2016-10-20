package com.gzwanhong.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gzwanhong.domain.User;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.UserLogic;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.WhUtil;

@Controller
@Scope(value = "prototype")
public class UserAction extends SuperAction {
	private static final long serialVersionUID = 1L;
	@Autowired
	private UserLogic userLogic;
	private User user;
	private String id;
	private String[] ids;
	private String userName;
	private String password;
	private String oldPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public UserLogic getUserLogic() {
		return userLogic;
	}

	public void setUserLogic(UserLogic userLogic) {
		this.userLogic = userLogic;
	}

	public String login() throws Exception {
		if (!WhUtil.isEmpty(userName) && !WhUtil.isEmpty(password)) {
			Map<String, Object> result = userLogic.login(userName, password);
			if (!WhUtil.isEmpty(result)) {
				super.getSession().setAttribute("user", result.get("user"));
				super.getSession().setAttribute("department", result.get("department"));
				return "main";
			} else {
				super.getRequest().setAttribute("error", "用户名或密码错误");
				return "admin";
			}
		} else {
			super.getRequest().setAttribute("error", "用户名密码不能为空");
			return "admin";
		}
	}

	public String logout() throws Exception {
		super.getSession().removeAttribute("user");
		super.getSession().removeAttribute("department");
		return "admin";
	}

	public String main() throws Exception {
		return SUCCESS;
	}

	public String user() throws Exception {
		return SUCCESS;
	}

	public String queryDatagrid() throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		if (!WhUtil.isEmpty(user)) {
			paramMap = JsonUtil.beanToMap(user);
		}
		DatagridEntity datagridEntity = userLogic.queryDatagrid(super.getUser(), paramMap, getPageInfo());
		ajaxResponse(JsonUtil.beanToJson(datagridEntity));
		return NONE;
	}

	public String saveOrUpdateUser() throws Exception {
		ResultEntity resultEntity = userLogic.saveOrUpdateUser(user);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String deleteUser() throws Exception {
		ResultEntity resultEntity = userLogic.deleteUser(ids);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	/**
	 * 根据权限来查该用户所能看到的user
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryUserCombobox() throws Exception {
		List<Map<String, Object>> list = userLogic.queryUserCombobox(super.getUser());
		ajaxResponse(JsonUtil.beanToJson(list));
		return NONE;
	}

	public String test() throws Exception {
		userLogic.test();
		return NONE;
	}

	public String updatePassword() throws Exception {
		ResultEntity resultEntity = userLogic.updatePassword(super.getUser(), oldPassword, password);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}
}
