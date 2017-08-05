package com.gzwanhong.dao.impl;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.gzwanhong.dao.TrackRecordDao;

@Repository
@Scope(value = "prototype")
public class TrackRecordDaoImpl extends BaseDaoImpl implements TrackRecordDao {

}
