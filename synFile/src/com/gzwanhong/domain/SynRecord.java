package com.gzwanhong.domain;

import java.io.Serializable;
import java.util.Date;

public class SynRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String name;
	public String flag;
	public String changeFileId;
	public Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getChangeFileId() {
		return changeFileId;
	}

	public void setChangeFileId(String changeFileId) {
		this.changeFileId = changeFileId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
