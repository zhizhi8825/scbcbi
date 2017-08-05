package com.gzwanhong.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.druid.util.Base64;
import com.gzwanhong.dao.SynFileOptionDao;
import com.gzwanhong.domain.DownAllFile;
import com.gzwanhong.entity.RequestEntity;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.SynFileOptionLogic;
import com.gzwanhong.webserviceClient.ServiceInterface;
import com.gzwanhong.webserviceClient.ServiceInterface_Service;

public class DownAllFileThead extends Thread {
	private Logger log = Logger.getLogger(getClass());
	private Long time = System.currentTimeMillis();

	@Override
	public void run() {
		SynFileOptionDao synFileOptionDao = SpringUtil.getBean(SynFileOptionDao.class);
		DownAllFile downAllFile = null;
		ServiceInterface serviceInterface = null;
		try {
			serviceInterface = new ServiceInterface_Service(new URL(WhCommon.webUrl + WhCommon.webserviceUrl))
					.getServiceInterfacePort();
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		ResultEntity resultEntity;
		byte[] fileData;
		File file;
		File backupDir = new File(WhCommon.backupDir);
		FileOutputStream fos;

		while (WhCommon.isDownAllFile) {
			try {
				time = System.currentTimeMillis();
				downAllFile = DownAllFileThead.getNeedDownFile();

				if (!WhUtil.isEmpty(downAllFile)) {
					if (downAllFile.getFileOrDir().intValue() == 2) {
						// 是文件夹，那就直接创建它
						file = new File(backupDir, downAllFile.getTargetPath());
						if (!file.exists()) {
							file.mkdirs();
						}

						downAllFile.setStatus(2);
						synFileOptionDao.saveOrUpdate(downAllFile);
					} else if (downAllFile.getFileSize().intValue() > (5 * 1024 * 1024)) {
						// 文件大于5M的用HTTP下载
						WhUtil.downloadFile(WhCommon.webUrl + WhCommon.downloadUrl, downAllFile.getFilePath(),
								new File(backupDir, downAllFile.getTargetPath()).getPath());

						downAllFile.setStatus(2);
						synFileOptionDao.saveOrUpdate(downAllFile);
					} else {
						// 文件小于50M的用webservice下载
						RequestEntity requestEntity = new RequestEntity();
						Map<String, Object> paramMap = new HashMap<String, Object>();

						paramMap.put("filePath", downAllFile.getFilePath());

						requestEntity.setSecurity("wh");
						requestEntity.setParam(paramMap);

						String json = serviceInterface.downloadFile(JsonUtil.beanToJson(requestEntity));

						log.info("整站备份下载文件：" + json);

						resultEntity = (ResultEntity) JsonUtil.jsonToBean(json, ResultEntity.class);

						if (resultEntity.getResult()) {
							if (!WhUtil.isEmpty(resultEntity.getObj())) {
								// 保存文件
								fileData = Base64.base64ToByteArray(WhUtil.toString(resultEntity.getObj()));
								file = new File(backupDir, downAllFile.getTargetPath());

								// 如果没有目录的话就创建目录
								if (!file.getParentFile().exists()) {
									file.getParentFile().mkdirs();
								}

								fos = new FileOutputStream(file);
								fos.write(fileData);
								fos.flush();
								fos.close();

								downAllFile.setStatus(2);
								synFileOptionDao.saveOrUpdate(downAllFile);
							}
						} else {
							log.info(resultEntity.getError());
						}
					}
				}

				if (System.currentTimeMillis() - time < 500) {
					Thread.sleep(500);
				}
			} catch (Exception e) {
				e.printStackTrace();
				downAllFile.setStatus(0);
				try {
					synFileOptionDao.saveOrUpdate(downAllFile);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取一条要备份的文件路径
	 * 
	 * @return
	 * @throws Exception
	 */
	public static synchronized DownAllFile getNeedDownFile() throws Exception {
		SynFileOptionDao synFileOptionDao = SpringUtil.getBean(SynFileOptionDao.class);

		DownAllFile downAllFile = null;
		DownAllFile example = new DownAllFile();
		example.setStatus(0);
		List<DownAllFile> downAllFileList = synFileOptionDao.queryByExample(example,1,1);
		if (!WhUtil.isEmpty(downAllFileList) && downAllFileList.size() > 0) {
			downAllFile = downAllFileList.get(0);
			// 把这条标为正在处理
			downAllFile.setStatus(1);
			synFileOptionDao.saveOrUpdate(downAllFile);
		}

		return downAllFile;
	}

}
