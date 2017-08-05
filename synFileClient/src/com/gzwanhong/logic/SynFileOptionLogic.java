package com.gzwanhong.logic;

import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.ResultEntity;

public interface SynFileOptionLogic {
	public ResultEntity onOff(ParamEntity paramEntity) throws Exception;

	public DatagridEntity queryDatagrid(PageInfo pageInfo, ParamEntity paramEntity) throws Exception;

	public ResultEntity hasDownTask() throws Exception;

	public ResultEntity getAllFile() throws Exception;

	public ResultEntity getDownloadStatus() throws Exception;

	public ResultEntity startDownFile() throws Exception;

	public ResultEntity stopDownFile() throws Exception;

}
