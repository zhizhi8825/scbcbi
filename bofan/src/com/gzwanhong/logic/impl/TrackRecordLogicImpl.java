package com.gzwanhong.logic.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.gzwanhong.dao.DepartmentDao;
import com.gzwanhong.dao.TrackRecordDao;
import com.gzwanhong.domain.TrackRecord;
import com.gzwanhong.domain.User;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.TrackRecordLogic;
import com.gzwanhong.mapper.TrackRecordMapper;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.WhUtil;

@Service
@Scope(value = "prototype")
public class TrackRecordLogicImpl implements TrackRecordLogic {
	@Autowired
	private TrackRecordDao trackRecordDao;
	@Autowired
	private DepartmentDao departmentDao;

	public DepartmentDao getDepartmentDao() {
		return departmentDao;
	}

	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	public TrackRecordDao getTrackRecordDao() {
		return trackRecordDao;
	}

	public void setTrackRecordDao(TrackRecordDao trackRecordDao) {
		this.trackRecordDao = trackRecordDao;
	}

	public DatagridEntity queryDatagrid(ParamEntity paramEntity, PageInfo pageInfo, User user) throws Exception {
		if (WhUtil.isEmpty(paramEntity)) {
			paramEntity = new ParamEntity();
		}

		// 先根据该user的权限，设置它所能查询的部门或用户，如果是admin的话就不做这些限制，能查所有
		if (!"admin".equals(user.getUserName())) {
			if (WhUtil.isEmpty(user.getLimitsLevel()) || user.getLimitsLevel().intValue() == 0) {
				// 最小权限，只能查自己的数据
				if (WhUtil.isEmpty(paramEntity.getClientId())) {
					paramEntity.setUserId(user.getId());
				}
			} else if (user.getLimitsLevel().intValue() == 1) {
				// 横向权限，能查本部门所有人的数据
				paramEntity.setDeptIds(new String[] { user.getDepartmentId() });
			} else if (user.getLimitsLevel().intValue() == 2) {
				// 纵向，能查下级部门所有人数据
				List<String> idList = departmentDao.querySubDeptIdNotOwn(user.getDepartmentId());
				paramEntity.setDeptIds((String[]) idList.toArray());
			} else if (user.getLimitsLevel().intValue() == 3) {
				// 纵向，能查本部门跟下级部门所有人数据
				List<String> idList = departmentDao.querySubDeptId(user.getDepartmentId());
				paramEntity.setDeptIds((String[]) idList.toArray());
			}
		}

		DatagridEntity datagridEntity = trackRecordDao.queryBySqlToDatagrid(TrackRecordMapper.class, "queryDatagrid",
				pageInfo, JsonUtil.beanToMap(paramEntity));

		return datagridEntity;
	}

	public ResultEntity saveOrUpdate(TrackRecord trackRecord, User user) throws Exception {
		ResultEntity resultEntity = new ResultEntity();
		Date now = new Date();

		if (!WhUtil.isEmpty(trackRecord)) {
			if (WhUtil.isEmpty(trackRecord.getId())) {
				// 保存
				trackRecord.setStatus(1);
				trackRecord.setUserId(user.getId());
				trackRecordDao.save(trackRecord);
			} else {
				// 修改
				trackRecord.setUpdateId(user.getId());
				trackRecord.setUpdateTime(now);

				trackRecordDao.update(trackRecord);
			}
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("数据不能为空");
			return resultEntity;
		}

		return resultEntity;
	}

	public ResultEntity deleteTrackRecord(ParamEntity paramEntity) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		if (!WhUtil.isEmpty(paramEntity) && !WhUtil.isEmpty(paramEntity.getIds()) && paramEntity.getIds().length > 0) {
			List<TrackRecord> list = trackRecordDao.queryByIds(Arrays.asList(paramEntity.getIds()), TrackRecord.class);

			for (TrackRecord trackRecord : list) {
				trackRecord.setStatus(0);
			}

			trackRecordDao.updateAll(list);
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("请选择要删除的跟踪记录");
			return resultEntity;
		}

		return resultEntity;
	}
}
