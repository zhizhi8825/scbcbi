package com.gzwanhong.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.alibaba.druid.util.Base64;
import com.gzwanhong.dao.SynFileOptionDao;
import com.gzwanhong.domain.BackupRecord;
import com.gzwanhong.domain.ChangeFile;
import com.gzwanhong.entity.RequestEntity;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.webserviceClient.ServiceInterface;
import com.gzwanhong.webserviceClient.ServiceInterface_Service;

/**
 * 备份文件线程
 * 
 * @author Administrator
 *
 */
public class BackupThread extends Thread {
	Logger log = Logger.getLogger(BackupThread.class);

	@Override
	public void run() {
		SynFileOptionDao synFileOptionDao = SpringUtil.getBean(SynFileOptionDao.class);

		while (WhCommon.isWorking) {
			try {
				RequestEntity requestEntity = new RequestEntity();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("flag", WhCommon.flag);
				paramMap.put("hours", WhCommon.hours);

				requestEntity.setSecurity("wh");
				requestEntity.setParam(paramMap);

				ServiceInterface serviceInterface = new ServiceInterface_Service(
						new URL(WhCommon.webUrl + WhCommon.webserviceUrl)).getServiceInterfacePort();
				String json = serviceInterface.getBackupFile(JsonUtil.beanToJson(requestEntity));

				// log.info("获取同步文件接口返回：" + json);

				ResultEntity resultEntity = (ResultEntity) JsonUtil.jsonToBean(json, ResultEntity.class);

				if (resultEntity.getResult()) {
					Map<String, Object> resultMap = (Map<String, Object>) resultEntity.getObj();

					// 取出fileMap 文件集合MAP
					Map<String, Map<String, Object>> fileMap = (Map<String, Map<String, Object>>) resultMap
							.get("fileMap");

					// 循环文件集合
					ChangeFile changeFile;
					byte[] fileData;
					List<String> idList = new ArrayList<String>();
					List<String> idListTemp;
					File file;
					File oldFile;
					File backupDir = new File(WhCommon.backupDir);
					FileOutputStream fos;
					List<BackupRecord> backupRecordList = new ArrayList<BackupRecord>(); // 记录备份记录
					BackupRecord backupRecord = null;
					for (Map<String, Object> fileMapTemp : fileMap.values()) {
						changeFile = (ChangeFile) JsonUtil
								.jsonToBean(JsonUtil.beanToJson(fileMapTemp.get("changeFile")), ChangeFile.class);
						idListTemp = (List<String>) fileMapTemp.get("idList");

						file = new File(backupDir, changeFile.getTargetPath());

						if (changeFile.changeType.intValue() == 3) {
							// 删除
							if (file.exists()) {
								if (file.isDirectory()) {
									FileUtils.deleteDirectory(file);
								} else {
									file.delete();
								}
							}
						} else if (changeFile.changeType.intValue() == 4) {
							// 更名
							oldFile = new File(backupDir, changeFile.getOldName());
							if (oldFile.exists()) {
								oldFile.renameTo(file);
							}
						} else if (changeFile.changeType.intValue() == 1 && changeFile.getFileOrDir().intValue() == 2) {
							// 是目录，并且是增加
							if (!file.exists()) {
								file.mkdirs();
							}
						} else {
							// 增加、修改
							// 如果没有目录的话就创建目录
							if (!file.getParentFile().exists()) {
								file.getParentFile().mkdirs();
							}

							// 根据文件大小来判断是使用webservice还是http来下载文件，大于5M的用HTTP，小于等于的用webservice
							if (changeFile.fileSize > (5 * 1024 * 1024)) {
								WhUtil.downloadFile(WhCommon.webUrl + WhCommon.downloadUrl, changeFile.getFilePath(),
										file.getPath()); 
							} else {
								requestEntity = new RequestEntity();
								paramMap = new HashMap<String, Object>();
								paramMap.put("filePath", changeFile.getFilePath());

								requestEntity.setSecurity("wh");
								requestEntity.setParam(paramMap);

								json = serviceInterface.downloadFile(JsonUtil.beanToJson(requestEntity));

								ResultEntity resultEntityDownload = (ResultEntity) JsonUtil.jsonToBean(json,
										ResultEntity.class);

								fileData = Base64.base64ToByteArray(WhUtil.toString(resultEntityDownload.getObj()));

								fos = new FileOutputStream(file);
								fos.write(fileData);
								fos.flush();
								fos.close();
							}
						}

						// 把ID集合收集起来
						// idList.addAll(idListTemp);

						// 调用接口，标记哪些数据是已同步的
						requestEntity = new RequestEntity();
						paramMap = new HashMap<String, Object>();
						paramMap.put("idList", idListTemp);
						paramMap.put("flag", WhCommon.flag);
						paramMap.put("clientName", WhCommon.clientName);

						requestEntity.setSecurity("wh");
						requestEntity.setParam(paramMap);

						json = serviceInterface.addRecord(JsonUtil.beanToJson(requestEntity));

						// 收集备份记录
						backupRecord = new BackupRecord();
						backupRecord.setFileName(file.getName());
						backupRecord.setFilePath(file.getPath());
						backupRecord.setTargetPath(changeFile.getTargetPath());
						backupRecord.setFileOrDir(changeFile.getFileOrDir());
						backupRecord.setChangeType(changeFile.getChangeType());

						backupRecordList.add(backupRecord);
					}

					// log.info("标记接口返回：" + json);

					// 保存备份记录
					synFileOptionDao.saveAll(backupRecordList);
				} else {
					log.error(resultEntity.getError());
				}

				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destroy() {
		System.out.println("BackupThread****destroy");
		super.destroy();
	}

}
