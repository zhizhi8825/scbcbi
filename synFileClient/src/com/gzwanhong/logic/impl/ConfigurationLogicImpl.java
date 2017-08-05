package com.gzwanhong.logic.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.gzwanhong.dao.ConfigurationDao;
import com.gzwanhong.domain.Configuration;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.ConfigurationLogic;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.WhUtil;

@Service
@Scope(value = "prototype")
public class ConfigurationLogicImpl implements ConfigurationLogic {
	@Autowired
	private ConfigurationDao configurationDao;

	public ConfigurationDao getConfigurationDao() {
		return configurationDao;
	}

	public void setConfigurationDao(ConfigurationDao configurationDao) {
		this.configurationDao = configurationDao;
	}

	public ResultEntity saveOrUpdate(ParamEntity paramEntity) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		if (!WhUtil.isEmpty(paramEntity) && !WhUtil.isEmpty(paramEntity.getJsonStr())) {
			Map<String, Object> optionMap = JsonUtil.jsonToMap(paramEntity.getJsonStr(), false);

			Configuration configuration = null;
			List<Configuration> list = null;
			Configuration example = null;
			List<Configuration> saveList = new ArrayList<Configuration>();
			for (String key : optionMap.keySet()) {
				example = new Configuration();
				example.setName(key);
				list = configurationDao.queryByExample(example);

				// 如果已有那就修改，否则新增
				if (!WhUtil.isEmpty(list) && list.size() > 0) {
					configuration = list.get(0);
					configuration.setVal(WhUtil.toString(optionMap.get(key)));
					saveList.add(configuration);
				} else {
					configuration = new Configuration();
					configuration.setName(key);
					configuration.setVal(WhUtil.toString(optionMap.get(key)));
					saveList.add(configuration);
				}
			}

			configurationDao.saveOrUpdateAll(saveList);
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("配置信息为空");
		}

		return resultEntity;
	}

	public ResultEntity queryConfig() throws Exception {
		ResultEntity resultEntity = new ResultEntity();
		Map<String, Object> optionMap = new HashMap<String, Object>();

		List<Configuration> list = configurationDao.queryByExample(new Configuration());
		if (!WhUtil.isEmpty(list) && list.size() > 0) {
			for (Configuration configuration : list) {
				optionMap.put(configuration.getName(), configuration.getVal());
			}
		}

		resultEntity.setObj(optionMap);
		return resultEntity;
	}
}
