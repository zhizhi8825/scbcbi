package com.gzwanhong.domain;

import java.io.Serializable;
import java.util.Date;

public class BackupRecord implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public String id;
	public String fileName;
	public String filePath;
	public String targetPath;
	public Integer fileOrDir;
	public Integer changeType;
	public Date createTime;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
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

	public Integer getChangeType() {
		return changeType;
	}

	public void setChangeType(Integer changeType) {
		this.changeType = changeType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
