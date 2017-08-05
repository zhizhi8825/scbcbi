package com.gzwanhong.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.gzwanhong.dao.ConfigurationDao;

@Repository
@Scope(value = "prototype")
public class ConfigurationDaoImpl extends BaseDaoImpl implements ConfigurationDao {

}
