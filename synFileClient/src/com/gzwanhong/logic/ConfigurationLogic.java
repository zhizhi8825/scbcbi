package com.gzwanhong.logic;

import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.ResultEntity;

public interface ConfigurationLogic {
	public ResultEntity saveOrUpdate(ParamEntity paramEntity) throws Exception;

	public ResultEntity queryConfig() throws Exception;
}
