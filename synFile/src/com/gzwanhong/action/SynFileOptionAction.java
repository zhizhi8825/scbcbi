package com.gzwanhong.action;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

@Controller
@Scope(value = "prototype")
public class SynFileOptionAction extends SuperAction {
	private static final long serialVersionUID = 1L;

	public String synFileOption() throws Exception {
		return SUCCESS;
	}
}
