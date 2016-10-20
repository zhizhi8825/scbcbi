package com.gzwanhong.dao;

import java.util.List;
import java.util.Map;

import com.gzwanhong.domain.Client;

public interface ClientDao extends BaseDao {
	public Boolean hasByNameProject(String name, String project) throws Exception;

	public List<Client> queryByNameAndProject(List<Map<String, Object>> paramList) throws Exception;
}
