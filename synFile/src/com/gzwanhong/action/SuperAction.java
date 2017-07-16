package com.gzwanhong.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.gzwanhong.domain.Department;
import com.gzwanhong.domain.User;
import com.gzwanhong.entity.PageInfo;
import com.opensymphony.xwork2.ActionSupport;

public class SuperAction extends ActionSupport {
	private static final long serialVersionUID = 1L;
	protected Integer rows;
	protected Integer page;

	public HttpServletRequest getRequest() {
		return ServletActionContext.getRequest();
	}

	public HttpServletResponse getResponse() {
		return ServletActionContext.getResponse();
	}

	public HttpSession getSession() {
		return getRequest().getSession();
	}

	public Integer getRows() {
		return rows;
	}

	public void setRows(Integer rows) {
		this.rows = rows;
	}

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public void ajaxResponse(Object obj) {
		getResponse().setCharacterEncoding("utf-8");
		getResponse().setHeader("Content-type", "text/html; charset=utf-8");
		try {
			getResponse().getWriter().print(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getWebPath() {
		return ServletActionContext.getServletContext().getRealPath("/");
	}

	public PageInfo getPageInfo() {
		return new PageInfo(rows, page);
	}

	public User getUser() {
		return (User) getSession().getAttribute("user");
	}

	public Department getDepartment() {
		return (Department) getSession().getAttribute("department");
	}

}
