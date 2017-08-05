package com.gzwanhong.logic;

import javax.servlet.ServletOutputStream;

import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.ResultEntity;

public interface SynFileOptionLogic {
	public ResultEntity onOff(ParamEntity paramEntity) throws Exception;

	public DatagridEntity queryDatagrid(PageInfo pageInfo, ParamEntity paramEntity) throws Exception;

	public ResultEntity getBackupFile(String json) throws Exception;

	public ResultEntity addRecord(String json) throws Exception;

	public ResultEntity startCheckFile(String json) throws Exception;

	public ResultEntity getAllFile(String json) throws Exception;

	public ResultEntity downloadFile(String json) throws Exception;

	public void downloadFile(ServletOutputStream servletOutputStream, String filePath) throws Exception;
}
