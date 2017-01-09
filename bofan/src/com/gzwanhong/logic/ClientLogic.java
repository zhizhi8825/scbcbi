package com.gzwanhong.logic;

import java.io.File;

import com.gzwanhong.domain.Client;
import com.gzwanhong.domain.User;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.ResultEntity;

public interface ClientLogic {
	public DatagridEntity queryDatagrid(ParamEntity paramEntity, PageInfo pageInfo, User user) throws Exception;

	public ResultEntity saveOrUpdateClient(Client client, User user) throws Exception;

	public ResultEntity deleteClient(String[] ids, User user) throws Exception;

	public DatagridEntity queryDayReport(ParamEntity paramEntity, User user) throws Exception;

	public ResultEntity saveChange(ParamEntity paramEntity, User user) throws Exception;

	public ResultEntity importClient(File file, String webPath, User user) throws Exception;

	public ResultEntity importClientIntention(File file, String webPath, User user, String optionStr,
			String sheetMapJson) throws Exception;
}
