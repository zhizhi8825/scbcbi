package com.gzwanhong.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.gzwanhong.dao.ClientDao;
import com.gzwanhong.domain.Client;
import com.gzwanhong.mapper.ClientMapper;
import com.gzwanhong.utils.WhUtil;

@Repository
@Scope(value = "prototype")
public class ClientDaoImpl extends BaseDaoImpl implements ClientDao {
	public Boolean hasByNameProject(String name, String project) throws Exception {
		Boolean has = false;

		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("name", name);
		paramMap.put("project", project);

		List<Map<String, Object>> list = queryBySql(ClientMapper.class, "hasByNameProject", paramMap);
		if (!WhUtil.isEmpty(list) && list.size() > 0 && !WhUtil.isEmpty(list.get(0))) {
			has = true;
		}

		return has;
	}

	public List<Client> queryByNameAndProject(List<Map<String, Object>> paramList) throws Exception {
		List<Client> clientList;
		if (!WhUtil.isEmpty(paramList) && paramList.size() > 0) {
			clientList = this.sqlSession.selectList(ClientMapper.class.getName() + ".queryByNameAndProject", paramList);
		} else {
			clientList = new ArrayList<Client>();
		}

		return clientList;
	}
}
