package com.gzwanhong.logic;

import java.util.List;

import com.gzwanhong.domain.Dictionary;
import com.gzwanhong.entity.ParamEntity;

public interface DictionaryLogic {
	public List<Dictionary> queryByType(ParamEntity paramEntity) throws Exception;
}
