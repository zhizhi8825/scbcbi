package com.gzwanhong.action;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gzwanhong.domain.TrackRecord;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.TrackRecordLogic;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.WhUtil;

@Controller
@Scope(value = "prototype")
public class TrackRecordAction extends SuperAction {

	private static final long serialVersionUID = 1L;
	@Autowired
	private TrackRecordLogic trackRecordLogic;
	private ParamEntity paramEntity;
	private TrackRecord trackRecord;

	public TrackRecord getTrackRecord() {
		return trackRecord;
	}

	public void setTrackRecord(TrackRecord trackRecord) {
		this.trackRecord = trackRecord;
	}

	public ParamEntity getParamEntity() {
		return paramEntity;
	}

	public void setParamEntity(ParamEntity paramEntity) {
		this.paramEntity = paramEntity;
	}

	public TrackRecordLogic getTrackRecordLogic() {
		return trackRecordLogic;
	}

	public void setTrackRecordLogic(TrackRecordLogic trackRecordLogic) {
		this.trackRecordLogic = trackRecordLogic;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String queryDatagrid() throws Exception {
		DatagridEntity datagridEntity = trackRecordLogic.queryDatagrid(paramEntity, getPageInfo(), super.getUser());

		ajaxResponse(JsonUtil.beanToJson(datagridEntity, WhUtil.YYYY_MM_DD_HH_MM_SS));
		return NONE;
	}

	public String saveOrUpdate() throws Exception {
		ResultEntity resultEntity = trackRecordLogic.saveOrUpdate(trackRecord, super.getUser());
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}

	public String deleteTrackRecord() throws Exception {
		ResultEntity resultEntity = trackRecordLogic.deleteTrackRecord(paramEntity);
		ajaxResponse(JsonUtil.beanToJson(resultEntity));
		return NONE;
	}
}
