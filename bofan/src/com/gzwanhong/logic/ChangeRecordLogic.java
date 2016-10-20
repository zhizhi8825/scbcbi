package com.gzwanhong.logic;

import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ParamEntity;

public interface ChangeRecordLogic {
	public DatagridEntity queryDatagrid(ParamEntity paramEntity, PageInfo pageInfo) throws Exception;
}
