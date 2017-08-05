package com.gzwanhong.domain;

import java.io.Serializable;
import java.util.Date;

public class DownAllFile implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id;
	private String filePath;
	private Integer fileSize;
	private String targetPath;
	private Integer fileOrDir;
	private Integer status;
	private Date updateTime;
	private Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Integer getFileSize() {
		return fileSize;
	}

	public void setFileSize(Integer fileSize) {
		this.fileSize = fileSize;
	}

	public String getTargetPath() {
		return targetPath;
	}

	public void setTargetPath(String targetPath) {
		this.targetPath = targetPath;
	}

	public Integer getFileOrDir() {
		return fileOrDir;
	}

	public void setFileOrDir(Integer fileOrDir) {
		this.fileOrDir = fileOrDir;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
