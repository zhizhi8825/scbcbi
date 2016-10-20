package com.gzwanhong.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.gzwanhong.dao.DictionaryDao;

@Repository
@Scope(value = "prototype")
public class DictionaryDaoImpl extends BaseDaoImpl implements DictionaryDao {

}
