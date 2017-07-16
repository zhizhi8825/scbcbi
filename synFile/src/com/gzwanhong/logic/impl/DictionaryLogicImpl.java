package com.gzwanhong.logic.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.gzwanhong.dao.DictionaryDao;
import com.gzwanhong.domain.Dictionary;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.logic.DictionaryLogic;
import com.gzwanhong.utils.WhUtil;

@Service
@Scope(value = "prototype")
public class DictionaryLogicImpl implements DictionaryLogic {
	@Autowired
	private DictionaryDao dictionaryDao;

	public DictionaryDao getDictionaryDao() {
		return dictionaryDao;
	}

	public void setDictionaryDao(DictionaryDao dictionaryDao) {
		this.dictionaryDao = dictionaryDao;
	}

	public List<Dictionary> queryByType(ParamEntity paramEntity) throws Exception {
		List<Dictionary> list = new ArrayList<Dictionary>();
		if (!WhUtil.isEmpty(paramEntity) && !WhUtil.isEmpty(paramEntity.getType())) {
			Dictionary example = new Dictionary();
			example.setType(paramEntity.getType());
			list = dictionaryDao.queryByExample(example,"seq");
		}

		return list;
	}
}
