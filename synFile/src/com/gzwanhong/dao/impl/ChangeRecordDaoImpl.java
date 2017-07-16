package com.gzwanhong.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.gzwanhong.dao.ChangeRecordDao;

@Repository
@Scope(value = "prototype")
public class ChangeRecordDaoImpl extends BaseDaoImpl implements ChangeRecordDao {

}
