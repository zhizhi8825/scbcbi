package com.gzwanhong.logic.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.gzwanhong.dao.ClientDao;
import com.gzwanhong.dao.DepartmentDao;
import com.gzwanhong.domain.ChangeRecord;
import com.gzwanhong.domain.Client;
import com.gzwanhong.domain.TrackRecord;
import com.gzwanhong.domain.User;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.ClientLogic;
import com.gzwanhong.mapper.ClientMapper;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.JxlUtil;
import com.gzwanhong.utils.PinYin;
import com.gzwanhong.utils.WhCommon;
import com.gzwanhong.utils.WhUtil;

@Service
@Scope(value = "prototype")
public class ClientLogicImpl implements ClientLogic {
	@Autowired
	private ClientDao clientDao;
	@Autowired
	private DepartmentDao departmentDao;

	public DepartmentDao getDepartmentDao() {
		return departmentDao;
	}

	public void setDepartmentDao(DepartmentDao departmentDao) {
		this.departmentDao = departmentDao;
	}

	public ClientDao getClientDao() {
		return clientDao;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	public DatagridEntity queryDatagrid(ParamEntity paramEntity, PageInfo pageInfo, User user) throws Exception {
		if (WhUtil.isEmpty(paramEntity)) {
			paramEntity = new ParamEntity();
		}

		// 先根据该user的权限，设置它所能查询的部门或用户，如果是admin的话就不做这些限制，能查所有
		if (!"admin".equals(user.getUserName())) {
			if (WhUtil.isEmpty(user.getLimitsLevel()) || user.getLimitsLevel().intValue() == 0) {
				// 最小权限，只能查自己的数据
				paramEntity.setUserId(user.getId());
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

		if (!WhUtil.isEmpty(paramEntity.getIsIntention()) && "NOT NULL".equals(paramEntity.getIsIntention())) {
			paramEntity.setIsIntention("NOT NULL AND c.intent_level != '' ");
		} else if (!WhUtil.isEmpty(paramEntity.getIsIntention()) && "NULL".equals(paramEntity.getIsIntention())) {
			paramEntity.setIsIntention("NULL or c.intent_level = '' ");
		}

		DatagridEntity datagridEntity = clientDao.queryBySqlToDatagrid(ClientMapper.class, "queryDatagrid", pageInfo,
				JsonUtil.beanToMap(paramEntity));

		return datagridEntity;
	}

	public ResultEntity saveOrUpdateClient(Client client, User user) throws Exception {
		ResultEntity resultEntity = new ResultEntity();
		Date now = new Date();

		if (WhUtil.isEmpty(client.getId())) {
			// 保存

			// 如果意向不为空的话，就先做个判断，以公司名称与项目为准，看有没重复的数据
			if (!WhUtil.isEmpty(client.getIntentLevel())) {
				if (WhUtil.isEmpty(client.getProject())) {
					resultEntity.setResult(false);
					resultEntity.setError("有意向的客户，必须指定项目");
					return resultEntity;
				}

				Boolean has = clientDao.hasByNameProject(client.getName(), client.getProject());

				if (has) {
					resultEntity.setResult(false);
					resultEntity.setError("已有该名称与项目的客户，不能再增加");
					return resultEntity;
				}
			}

			client.setStatus(1);
			client.setUserId(user.getId());
			client.setPinyin(PinYin.converterToFirstSpell(client.getName()));

			clientDao.saveOrUpdate(client);
		} else {
			// 修改

			// 把这条数据先从数据库取出来
			Client oldClient = clientDao.queryById(client.getId(), Client.class);

			// 如果是原先没不是意向客户，而现在是的话，就判断是否有重复数据
			if (WhUtil.isEmpty(oldClient.getIntentLevel()) && !WhUtil.isEmpty(client.getIntentLevel())) {
				// 先看下是否设置了项目
				if (WhUtil.isEmpty(client.getProject())) {
					resultEntity.setResult(false);
					resultEntity.setError("有意向的客户，必须指定项目");
					return resultEntity;
				}

				Boolean has = clientDao.hasByNameProject(client.getName(), client.getProject());

				if (has) {
					resultEntity.setResult(false);
					resultEntity.setError("已有该名称与项目的客户，请核实");
					return resultEntity;
				}
			}

			// 如果项目有做修改，则看是否有冲突
			if (!WhUtil.isEmpty(oldClient.getIntentLevel()) && !WhUtil.isEmpty(oldClient.getProject())
					&& !oldClient.getProject().equals(client.getProject())) {
				Boolean has = clientDao.hasByNameProject(client.getName(), client.getProject());

				if (has) {
					resultEntity.setResult(false);
					resultEntity.setError("已有该名称与项目的客户，请核实");
					return resultEntity;
				}
			}

			if (!WhUtil.isEmpty(oldClient.getIntentLevel()) && !oldClient.getName().equals(client.getName())) {
				resultEntity.setResult(false);
				resultEntity.setError("公司名称不能修改");
				return resultEntity;
			}

			client.setUpdateId(user.getId());
			client.setUpdateTime(now);
			client.setPinyin(PinYin.converterToFirstSpell(client.getName()));

			clientDao.update(client);
		}

		return resultEntity;
	}

	/**
	 * 批量删除客户，其实不是真删，是把status标识改为已删，记录还在
	 * 
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	public ResultEntity deleteClient(String[] ids, User user) throws Exception {
		ResultEntity resultEntity = new ResultEntity();
		Date now = new Date();

		// 先查出来
		List<Client> clientList = clientDao.queryByIds(Arrays.asList(ids), Client.class);

		// 循环设置status状态为0
		for (Client c : clientList) {
			c.setStatus(0);
			c.setUpdateTime(now);
			c.setUpdateId(user.getId());
		}

		// 保存修改
		clientDao.updateAll(clientList);

		return resultEntity;
	}

	public DatagridEntity queryDayReport(ParamEntity paramEntity, User user) throws Exception {
		if (WhUtil.isEmpty(paramEntity)) {
			paramEntity = new ParamEntity();
		}

		// 先根据该user的权限，设置它所能查询的部门或用户，如果是admin的话就不做这些限制，能查所有
		if (!"admin".equals(user.getUserName())) {
			if (WhUtil.isEmpty(user.getLimitsLevel()) || user.getLimitsLevel().intValue() == 0) {
				// 最小权限，只能查自己的数据
				paramEntity.setUserId(user.getId());
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

		DatagridEntity datagridEntity = clientDao.queryBySqlToDatagrid(ClientMapper.class, "queryDayReport",
				JsonUtil.beanToMap(paramEntity));

		return datagridEntity;
	}

	/**
	 * 保存转移
	 * 
	 * @param ids
	 * @param id
	 * @param user
	 * @return
	 * @throws Exception
	 */
	public ResultEntity saveChange(ParamEntity paramEntity, User user) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		if (!WhUtil.isEmpty(paramEntity) && !WhUtil.isEmpty(paramEntity.getIds()) && paramEntity.getIds().length > 0
				&& !WhUtil.isEmpty(paramEntity.getId())) {
			String[] ids = paramEntity.getIds();
			String id = paramEntity.getId();

			// 查出所有用户信息
			User userExample = new User();
			List<User> userList = clientDao.queryByExample(userExample);

			// 把user处理成map
			Map<String, User> userMap = new HashMap<String, User>();
			for (User u : userList) {
				userMap.put(u.getId(), u);
			}

			// 查出所有的将要转移的客户数据
			List<Client> clientList = clientDao.queryByIds(Arrays.asList(ids), Client.class);

			// 循环的设置这些客户数据的所属用户id，同时处理出转移记录
			List<ChangeRecord> changeRecordList = new ArrayList<ChangeRecord>();
			ChangeRecord changeRecord = null;
			for (Client client : clientList) {
				changeRecord = new ChangeRecord();
				changeRecord.setClientId(client.getId());
				changeRecord.setSourceUserId(client.getUserId());
				changeRecord.setSourceShowName(userMap.get(client.getUserId()).getShowName());
				changeRecord.setTargetUserId(id);
				changeRecord.setTargetShowName(userMap.get(id).getShowName());
				changeRecord.setUpdateId(user.getId());
				changeRecordList.add(changeRecord);

				client.setUserId(id);
			}

			// 保存修改
			clientDao.updateAll(clientList);
			clientDao.saveAll(changeRecordList);
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("数据有误");
		}

		return resultEntity;
	}

	/**
	 * 导入客户信息
	 */
	@SuppressWarnings("unchecked")
	public ResultEntity importClient(File file, String webPath, User user) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		List<TrackRecord> trackRecordList = new ArrayList<TrackRecord>(); // 装跟踪记录用
		TrackRecord trackRecordTemp = null;
		String optionStr = WhUtil.getFileText(webPath + WhCommon.config.getProperty("importClientOption"));
		Map<String, Object> optionMap = JsonUtil.jsonToMap(optionStr, false);
		Map<String, Object> objMap = JxlUtil.convertToBean(file, optionMap, true);

		List<Client> clientList = (List<Client>) objMap.get("clientList");

		// 处理出名称与项目不为空的数据，去数据库查看有没重复
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>();
		Map<String, Object> paramMap = null;
		for (Client client : clientList) {
			if (!WhUtil.isEmpty(client.getIntentLevel()) && WhUtil.isEmpty(client.getProject())) {
				resultEntity.setResult(false);
				resultEntity.setError(client.getName() + " 有意向但没写明属于哪个项目");
				return resultEntity;
			}

			if (WhUtil.isEmpty(client.getIntentLevel()) && !WhUtil.isEmpty(client.getProject())) {
				resultEntity.setResult(false);
				resultEntity.setError(client.getName() + " 有所属项目但没写明意向");
				return resultEntity;
			}

			if (!WhUtil.isEmpty(client.getLinkman()) && client.getLinkman().getBytes().length > 100) {
				resultEntity.setResult(false);
				resultEntity.setError(client.getName() + " 联系人太长");
				return resultEntity;
			}

			paramMap = new HashMap<String, Object>();
			paramMap.put("name", client.getName());
			paramMap.put("project", client.getProject());

			paramList.add(paramMap);

			client.setId(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
			client.setStatus(1);
			client.setUserId(user.getId());
			client.setPinyin(PinYin.converterToFirstSpell(client.getName()));

			// 处理出跟踪明细
			if (!WhUtil.isEmpty(client.getField1())) {
				trackRecordTemp = new TrackRecord();
				trackRecordTemp.setClientId(client.getId());
				trackRecordTemp.setRecord(client.getField1());
				trackRecordTemp.setUserId(user.getId());
				trackRecordTemp.setStatus(1);

				trackRecordList.add(trackRecordTemp);
				client.setField1("");
			}
		}

		List<Client> hasClientList = clientDao.queryByNameAndProject(paramList);

		// 如果是有重复的，则抛错，不继续保存
		if (!WhUtil.isEmpty(hasClientList) && hasClientList.size() > 0) {
			// 组装有重复的信息提示
			String error = "以下客户信息在系统中有重复：<br/>";
			for (Client client : hasClientList) {
				error += client.getName() + "," + client.getProject() + "<br/>";
			}

			resultEntity.setResult(false);
			resultEntity.setError(error);
			return resultEntity;
		}

		clientDao.saveAll(clientList);
		clientDao.saveAll(trackRecordList);

		resultEntity.setMsg("成功导入 " + clientList.size() + " 条数据");

		return resultEntity;
	}

	/**
	 * 导入意向客户信息
	 */
	@SuppressWarnings("unchecked")
	public ResultEntity importClientIntention(File file, String webPath, User user, String optionStr,
			String sheetMapJson) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		// String optionStr = WhUtil.getFileText(webPath +
		// WhCommon.config.getProperty("importClientIntentionOption"));
		Map<String, Object> optionMap = JsonUtil.jsonToMap(optionStr, false);
		Map<String, Client> clientListMap = new HashMap<String, Client>(); // 用来装将要保存的客户数据MAP，key为公司名与项目名，方便后面的去重用
		List<Client> clientList = new ArrayList<Client>(); // 用来装将要保存的客户数据
		List<Client> noProjectList = new ArrayList<Client>(); // 用来装没有项目的数据
		List<Map<String, Object>> paramList = new ArrayList<Map<String, Object>>(); // 记录所有的公司与项目名称，查是否重复用
		List<TrackRecord> trackRecordList = new ArrayList<TrackRecord>(); // 装跟踪记录用
		TrackRecord trackRecordTemp = null;
		Map<String, Object> objMap = null;
		Map<String, Object> paramMap = null;
		// Map<Integer, String> sheetMap = new HashMap<Integer, String>(); //
		// 设定要读的sheet工作表集合
		// sheetMap.put(0, "A");
		// sheetMap.put(1, "B");
		// sheetMap.put(2, "C");
		// sheetMap.put(3, "D");
		// sheetMap.put(4, "O");
		Map<String, Integer> sheetMap = (Map<String, Integer>) JsonUtil.jsonToBean(sheetMapJson, Map.class);
		String nameToLong = "";
		String linkmanToLong = "";
		String remarkToLong = "";
		String recordToLong = "";
		String pinyinError = ""; // 记录处理拼音出错的公司名称

		// 循环sheetMap，从excel文件里一个个工作区间的取数据
		List<Client> clientListTemp = null;
		Integer sheetIndex = null;
		for (String intentLevel : sheetMap.keySet()) {
			sheetIndex = sheetMap.get(intentLevel);

			if (WhUtil.isEmpty(sheetIndex)) {
				continue;
			}

			optionMap.put("startSheet", sheetIndex);
			objMap = JxlUtil.convertToBeanPoi(file, optionMap, true);
			clientListTemp = (List<Client>) objMap.get("clientList");

			if (!WhUtil.isEmpty(clientListTemp) && clientListTemp.size() > 0) {
				// 处理出名称与项目不为空的数据，去数据库查看有没重复，顺便处理出跟踪明细

				for (Client client : clientListTemp) {
					if (WhUtil.isEmpty(client.getProject())) {
						noProjectList.add(client);
						continue;
					}

					// 公司名过长
					if (!WhUtil.isEmpty(client.getName()) && client.getName().length() > 100) {
						nameToLong += client.getName() + ";<br/>";
					}
					// 联系人过长
					if (!WhUtil.isEmpty(client.getLinkman()) && client.getLinkman().length() > 100) {
						linkmanToLong += client.getName() + ";<br/>";
					}
					// 备注过长
					if (!WhUtil.isEmpty(client.getRemark()) && client.getRemark().length() > 500) {
						remarkToLong += client.getName() + ";<br/>";
					}
					// 跟踪记录过长
					if (!WhUtil.isEmpty(client.getField1()) && client.getField1().length() > 1000) {
						recordToLong += client.getName() + ";<br/>";
					}

					paramMap = new HashMap<String, Object>();
					paramMap.put("name", client.getName());
					paramMap.put("project", client.getProject());

					paramList.add(paramMap);

					client.setId(UUID.randomUUID().toString().replaceAll("-", "").toUpperCase());
					client.setStatus(1);
					client.setUserId(user.getId());

					// 设置它的意向级别
					client.setIntentLevel(intentLevel);

					try {
						client.setPinyin(PinYin.converterToFirstSpell(client.getName()));
					} catch (Exception e) {
						pinyinError += client.getName() + ";<br/>";
					}

					// 处理出跟踪明细
					if (!WhUtil.isEmpty(client.getField1())) {
						trackRecordTemp = new TrackRecord();
						trackRecordTemp.setClientId(client.getId());
						trackRecordTemp.setRecord(client.getField1());
						trackRecordTemp.setUserId(user.getId());
						trackRecordTemp.setStatus(1);

						trackRecordList.add(trackRecordTemp);
						client.setField1("");
					}

					// 把处理后的客户集合放进集合里去
					clientListMap.put(client.getName() + "&" + client.getProject(), client);
				}
			}
		}

		// 看是否有字段过长，有的话中断并弹出来提示，拼音出错的也提示
		String error = "";
		if (!WhUtil.isEmpty(nameToLong) || !WhUtil.isEmpty(linkmanToLong) || !WhUtil.isEmpty(remarkToLong)
				|| !WhUtil.isEmpty(recordToLong) || !WhUtil.isEmpty(pinyinError)) {
			if (!WhUtil.isEmpty(nameToLong)) {
				error += "<br/><span>以下公司名称过长</span><br/>" + nameToLong;
			}
			if (!WhUtil.isEmpty(linkmanToLong)) {
				error += "<br/>以下公司联系人过长<br/>" + linkmanToLong;
			}
			if (!WhUtil.isEmpty(remarkToLong)) {
				error += "<br/>以下公司备注过长<br/>" + remarkToLong;
			}
			if (!WhUtil.isEmpty(recordToLong)) {
				error += "<br/>以下公司跟踪情况过长<br/>" + recordToLong;
			}
			if (!WhUtil.isEmpty(pinyinError)) {
				error += "<br/>以下公司名称格式有异常<br/>" + pinyinError;
			}

			resultEntity.setCode(2);
			resultEntity.setMsg(error);
			return resultEntity;
		}

		// 看有没数据是没填项目的，有的话就把它们显示出来提示
		if (noProjectList.size() > 0) {
			error += "<br/>以下 " + noProjectList.size() + " 条客户信息没有填写项目：<br/>";
			for (int i = 0; i < noProjectList.size(); i++) {
				// if (i <= 9) {
				// 最多只显示出10个
				error += noProjectList.get(i).getName() + ";<br/>";
				// } else {
				// // 如果是大于10个，就不显示了
				// error += "......<br/>";
				// break;
				// }
			}
		}

		List<Client> hasClientList = clientDao.queryByNameAndProject(paramList);

		// 如果是有重复的，就把重复的从要保存的集合里去掉
		if (!WhUtil.isEmpty(hasClientList) && hasClientList.size() > 0) {
			Client clientTemp = null;
			// 组装有重复的信息提示
			String hasError = "";
			Integer hasCount = 0;
			for (int i = 0; i < hasClientList.size(); i++) {
				clientTemp = clientListMap
						.get(hasClientList.get(i).getName() + "&" + hasClientList.get(i).getProject());

				// 当公司名称、项目名称相同，而业务员不同时，被视为撞单，将提示出来
				if (!WhUtil.isEmpty(clientTemp) && !clientTemp.getUserId().equals(hasClientList.get(i).getUserId())) {
					hasError += hasClientList.get(i).getName() + "," + hasClientList.get(i).getProject() + ";<br/>";
					hasCount++;
				}

				// 把 clientListMap 中把对应的客户名称跟项目相同的删掉，不保存
				clientListMap.remove(hasClientList.get(i).getName() + "&" + hasClientList.get(i).getProject());
			}

			if (!WhUtil.isEmpty(hasError)) {
				error += "<br/>以下 " + hasCount + " 条客户信息与其它业务员有重复：<br/>" + hasError;
			}
		}

		// 把clientListMap 处理成 clientList 准备保存
		if (!WhUtil.isEmpty(clientListMap) && clientListMap.size() > 0) {
			for (Client client : clientListMap.values()) {
				clientList.add(client);
			}
		}

		clientDao.saveAll(clientList);
		clientDao.saveAll(trackRecordList);

		// 组装提示信息
		if (!WhUtil.isEmpty(error)) {
			resultEntity.setCode(2);
			resultEntity.setMsg("成功导入 " + clientList.size() + " 条数据<br/>" + error);
		} else {
			resultEntity.setCode(1);
			resultEntity.setMsg("成功导入 " + clientList.size() + " 条数据");
		}

		return resultEntity;
	}
}
