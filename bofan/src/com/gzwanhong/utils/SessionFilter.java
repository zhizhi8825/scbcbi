package com.gzwanhong.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class SessionFilter implements Filter {

	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		String uri = request.getRequestURI();
		if (request.getSession().getAttribute("user") == null) {
			if (uri.endsWith(".js") || uri.endsWith(".css")
					|| uri.contains("images") || uri.endsWith(".png")
					|| uri.endsWith(".gif") || uri.endsWith(".jpg")
					|| uri.endsWith(".jpeg") || uri.endsWith("img")
					|| uri.endsWith(".xml") || uri.endsWith("logout.action")
					|| uri.endsWith("admin.action")
					|| uri.endsWith("login.action")
					|| uri.contains("service")
					|| uri.endsWith("bofan/")) {
				chain.doFilter(req, resp);
			} else {
				request.getRequestDispatcher("/pages/logout.jsp").forward(req,
						resp);
			}
		} else {
			chain.doFilter(req, resp);
		}
	}

	public void init(FilterConfig filterConfig) throws ServletException {

	}

}
