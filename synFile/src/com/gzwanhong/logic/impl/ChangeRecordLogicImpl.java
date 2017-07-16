package com.gzwanhong.logic.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.gzwanhong.dao.ChangeRecordDao;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.logic.ChangeRecordLogic;
import com.gzwanhong.mapper.ChangeRecordMapper;
import com.gzwanhong.utils.JsonUtil;

@Service
@Scope(value = "prototype")
public class ChangeRecordLogicImpl implements ChangeRecordLogic {
	@Autowired
	private ChangeRecordDao changeRecordDao;

	public ChangeRecordDao getChangeRecordDao() {
		return changeRecordDao;
	}

	public void setChangeRecordDao(ChangeRecordDao changeRecordDao) {
		this.changeRecordDao = changeRecordDao;
	}

	public DatagridEntity queryDatagrid(ParamEntity paramEntity, PageInfo pageInfo) throws Exception {
		DatagridEntity datagridEntity = changeRecordDao.queryBySqlToDatagrid(ChangeRecordMapper.class, "queryDatagrid",
				pageInfo, JsonUtil.beanToMap(paramEntity));

		return datagridEntity;
	}

}
