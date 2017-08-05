package com.gzwanhong.logic;

import com.gzwanhong.domain.TrackRecord;
import com.gzwanhong.domain.User;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.ResultEntity;

public interface TrackRecordLogic {
	public DatagridEntity queryDatagrid(ParamEntity paramEntity, PageInfo pageInfo, User user) throws Exception;

	public ResultEntity saveOrUpdate(TrackRecord trackRecord, User user) throws Exception;

	public ResultEntity deleteTrackRecord(ParamEntity paramEntity) throws Exception;
}
