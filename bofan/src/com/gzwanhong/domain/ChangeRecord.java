package com.gzwanhong.domain;

import java.io.Serializable;
import java.util.Date;

public class ChangeRecord implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String clientId;
	private String sourceUserId;
	private String sourceShowName;
	private String targetUserId;
	private String targetShowName;
	private String updateId;
	private Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public String getSourceUserId() {
		return sourceUserId;
	}

	public void setSourceUserId(String sourceUserId) {
		this.sourceUserId = sourceUserId;
	}

	public String getSourceShowName() {
		return sourceShowName;
	}

	public void setSourceShowName(String sourceShowName) {
		this.sourceShowName = sourceShowName;
	}

	public String getTargetUserId() {
		return targetUserId;
	}

	public void setTargetUserId(String targetUserId) {
		this.targetUserId = targetUserId;
	}

	public String getTargetShowName() {
		return targetShowName;
	}

	public void setTargetShowName(String targetShowName) {
		this.targetShowName = targetShowName;
	}

	public String getUpdateId() {
		return updateId;
	}

	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}