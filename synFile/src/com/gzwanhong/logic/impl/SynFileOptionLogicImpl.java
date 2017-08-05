package com.gzwanhong.logic.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.views.velocity.components.CheckBoxDirective;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.alibaba.druid.util.Base64;
import com.ctc.wstx.sr.ElemAttrs;
import com.gzwanhong.dao.ConfigurationDao;
import com.gzwanhong.dao.SynFileOptionDao;
import com.gzwanhong.domain.ChangeFile;
import com.gzwanhong.domain.Configuration;
import com.gzwanhong.domain.SynRecord;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.RequestEntity;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.SynFileOptionLogic;
import com.gzwanhong.mapper.ChangeFileMapper;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.MyFileListener;
import com.gzwanhong.utils.MyJNotifyListener;
import com.gzwanhong.utils.WhCommon;
import com.gzwanhong.utils.WhUtil;

import net.contentobjects.jnotify.JNotify;
import net.contentobjects.jnotify.JNotifyException;

@Service
@Scope(value = "prototype")
public class SynFileOptionLogicImpl implements SynFileOptionLogic {
	@Autowired
	private SynFileOptionDao synFileOptionDao;
	@Autowired
	private ConfigurationDao configurationDao;
	private Logger log = Logger.getLogger(getClass());

	public ConfigurationDao getConfigurationDao() {
		return configurationDao;
	}

	public void setConfigurationDao(ConfigurationDao configurationDao) {
		this.configurationDao = configurationDao;
	}

	public SynFileOptionDao getSynFileOptionDao() {
		return synFileOptionDao;
	}

	public void setSynFileOptionDao(SynFileOptionDao synFileOptionDao) {
		this.synFileOptionDao = synFileOptionDao;
	}

	/**
	 * 查出所有配置信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryConfig() throws Exception {
		Map<String, Object> optionMap = new HashMap<String, Object>();

		List<Configuration> list = configurationDao.queryByExample(new Configuration());
		if (!WhUtil.isEmpty(list) && list.size() > 0) {
			for (Configuration configuration : list) {
				optionMap.put(configuration.getName(), configuration.getVal());
			}
		}

		return optionMap;
	}

	/**
	 * 添加监控目录
	 * 
	 * @param dir
	 * @param backupDir
	 * @return
	 * @throws JNotifyException
	 */
	public void addWatch(String dir) throws JNotifyException {

		// 监控用户的操作，增，删，改，重命名
		int mask = JNotify.FILE_CREATED | JNotify.FILE_DELETED | JNotify.FILE_MODIFIED | JNotify.FILE_RENAMED;

		// 是否监控子目录
		boolean subTree = true;

		// 开始监控
		int watchID = JNotify.addWatch(dir, mask, subTree, new MyJNotifyListener(synFileOptionDao, dir));

		if (WhUtil.isEmpty(WhCommon.watchIdList)) {
			WhCommon.watchIdList = new ArrayList<Integer>();
		}

		WhCommon.watchIdList.add(watchID);

	}

	/**
	 * 关闭所有的目录监控
	 * 
	 * @throws JNotifyException
	 */
	public void removeAllWatch() throws JNotifyException {
		if (!WhUtil.isEmpty(WhCommon.watchIdList) && WhCommon.watchIdList.size() > 0) {
			for (Integer wathcId : WhCommon.watchIdList) {
				JNotify.removeWatch(wathcId);
			}
		}
	}

	/**
	 * 开启或关闭同步
	 * 
	 * @param paramEntity
	 * @return
	 * @throws Exception
	 */
	public ResultEntity onOff(ParamEntity paramEntity) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		if (!WhUtil.isEmpty(paramEntity) && !WhUtil.isEmpty(paramEntity.getStatus())) {
			if (paramEntity.getStatus().intValue() == 1) {
				// 取出配置信息
				Map<String, Object> optionMap = queryConfig();

				// 取出配置的备份目录字符串
				String dirStr = WhUtil.toString(optionMap.get("dirs"));

				if (!WhUtil.isEmpty(dirStr)) {
					// 把目录字符串按回车分解成多个
					String[] dirs = dirStr.split("\n");

					// 把目录字符集体处理成map
					Map<String, String> dirMap = new HashMap<String, String>();

					for (String dir : dirs) {
						dirMap.put(dir, dir);
					}

					// 关闭监控
					removeAllWatch();

					// 开始把一个个的目录添加进监控里
					for (String dir : dirs) {
						addWatch(dir);
					}

					// 开启
					WhCommon.isOn = true;
				} else {
					resultEntity.setResult(false);
					resultEntity.setError("备份目录为空，请先配置");
				}
			} else if (paramEntity.getStatus().intValue() == 0) {
				// 关闭
				WhCommon.isOn = false;

				// 关闭监控
				removeAllWatch();
			} else {
				resultEntity.setResult(false);
				resultEntity.setError("未知的状态标识，无法操作");
			}
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("开启或关闭参数为空，无法操作");
		}

		return resultEntity;
	}

	public DatagridEntity queryDatagrid(PageInfo pageInfo, ParamEntity paramEntity) throws Exception {
		DatagridEntity datagridEntity = synFileOptionDao.queryBySqlToDatagrid(ChangeFileMapper.class, "querySynFile",
				pageInfo, paramEntity);

		return datagridEntity;
	}

	/**
	 * webservice 获取变动的文件做备份
	 * 
	 * @param hours
	 * @return
	 * @throws Exception
	 */
	public ResultEntity getBackupFile(String json) throws Exception {
		RequestEntity requestEntity = (RequestEntity) JsonUtil.jsonToBean(json, RequestEntity.class);
		Map<String, Object> param = (Map<String, Object>) requestEntity.getParam();

		ResultEntity resultEntity = new ResultEntity();
		Map<String, Object> resultMap = new HashMap<String, Object>();

		// 取出最多一百条发生改变的文件记录
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("startTime", DateUtils.addHours(new Date(), -WhUtil.toInteger(param.get("hours"))));
		paramMap.put("flag", WhUtil.toString(param.get("flag")));
		List<ChangeFile> changeFileList = synFileOptionDao.queryBySql(ChangeFileMapper.class, "getBackupFile", paramMap,
				ChangeFile.class);

		// 循环处理，把这一百条汇总成map,key为文件路径
		Map<String, Map<String, Object>> fileMap = new HashMap<String, Map<String, Object>>();
		Map<String, Object> fileMapTemp;
		List<String> idList;
		for (ChangeFile changeFileTemp : changeFileList) {
			// 把 changeFile 装进去
			fileMapTemp = fileMap.get(changeFileTemp.getFilePath());

			if (WhUtil.isEmpty(fileMapTemp)) {
				fileMapTemp = new HashMap<String, Object>();
			}

			fileMapTemp.put("changeFile", changeFileTemp);

			// 把id追加进去
			idList = (List<String>) fileMapTemp.get("idList");
			if (WhUtil.isEmpty(idList)) {
				idList = new ArrayList<String>();
			}

			idList.add(changeFileTemp.getId());

			fileMapTemp.put("idList", idList);

			fileMap.put(changeFileTemp.getFilePath(), fileMapTemp);
		}

		// 把上面的map转移到另一个map，累计将要传输的文件大小，总大小超过50M的就跳出，以限制一次传输的最大量不超过50M，除了单个文件就超过50M的
		Map<String, Map<String, Object>> resultFileMap = new HashMap<String, Map<String, Object>>();
		ChangeFile changeFileTemp;
		Long fileSizeSum = 0L;
		FileInputStream fis;
		byte[] fileData;
		File file;
		for (String key : fileMap.keySet()) {
			fileMapTemp = fileMap.get(key);
			changeFileTemp = (ChangeFile) fileMapTemp.get("changeFile");

			// 是删除操作、更名操作或目录操作的，直接放进去
			if (changeFileTemp.getFileOrDir().intValue() == 2 || changeFileTemp.getChangeType().intValue() == 3
					|| changeFileTemp.getChangeType().intValue() == 4) {
				resultFileMap.put(key, fileMapTemp);
				continue;
			}

			resultFileMap.put(key, fileMapTemp);

			// 超过50M的就跳出
			// if ((WhUtil.toLong(changeFileTemp.getFileSize()).longValue() +
			// fileSizeSum) > (50 * 1024 * 1024)
			// && fileSizeSum.longValue() > 0) {
			// break;
			// }

			// 开始把读取文件并转为字符
			// file = new File(changeFileTemp.getFilePath());
			//
			// if (file.exists()) {
			// fis = new FileInputStream(file);
			// fileData = new byte[fis.available()];
			// fis.read(fileData);
			// fis.close();
			//
			// fileMapTemp.put("fileData", Base64.byteArrayToBase64(fileData));
			//
			// resultFileMap.put(key, fileMapTemp);
			//
			// fileSizeSum += changeFileTemp.getFileSize();
			// }
		}

		resultMap.put("fileMap", resultFileMap);

		resultEntity.setObj(resultMap);

		return resultEntity;
	}

	/**
	 * 添加备份记录
	 */
	public ResultEntity addRecord(String json) throws Exception {
		ResultEntity resultEntity = new ResultEntity();
		RequestEntity requestEntity = (RequestEntity) JsonUtil.jsonToBean(json, RequestEntity.class);
		Map<String, Object> param = (Map<String, Object>) requestEntity.getParam();

		// 取出参数
		List<String> idList = (List<String>) param.get("idList");
		String flag = WhUtil.toString(param.get("flag"));
		String clientName = WhUtil.toString(param.get("clientName"));

		if (!WhUtil.isEmpty(idList) && idList.size() > 0) {
			// 查出changeFile数据
			List<ChangeFile> changeFileList = synFileOptionDao.queryByIds(idList, ChangeFile.class);

			if (!WhUtil.isEmpty(changeFileList) && changeFileList.size() > 0) {
				// 修改changeFile的状态与最后一次同步主机，并生成同步记录
				List<SynRecord> synRecordList = new ArrayList<SynRecord>();
				SynRecord synRecordTemp;
				for (ChangeFile changeFile : changeFileList) {
					changeFile.setStatus(1);
					changeFile.setClientName(clientName);

					synRecordTemp = new SynRecord();
					synRecordTemp.setFlag(flag);
					synRecordTemp.setName(clientName);
					synRecordTemp.setChangeFileId(changeFile.getId());
					synRecordList.add(synRecordTemp);
				}

				// 保存
				synFileOptionDao.saveOrUpdateAll(changeFileList);
				synFileOptionDao.saveAll(synRecordList);
			}
		}

		return resultEntity;
	}

	/**
	 * 客户端触发，开始扫描文件目录下所有文件
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public ResultEntity startCheckFile(String json) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		RequestEntity requestEntity = (RequestEntity) JsonUtil.jsonToBean(json, RequestEntity.class);
		Map<String, Object> param = (Map<String, Object>) requestEntity.getParam();
		String flag = WhUtil.toString(param.get("flag"));

		// 取出配置信息
		Map<String, Object> optionMap = queryConfig();

		// 取出配置的备份目录字符串
		String dirStr = WhUtil.toString(optionMap.get("dirs"));

		if (!WhUtil.isEmpty(WhCommon.checkFileThreadMap.get(flag))) {
			// 如果之前有正在扫描，那就把它停了
			WhCommon.checkFileThreadMap.get(flag).stop();
		}

		if (!WhUtil.isEmpty(dirStr)) {
			Thread checkFileThread = new CheckFileThread(dirStr, flag);
			checkFileThread.start();
			WhCommon.checkFileThreadMap.put(flag, checkFileThread);
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("服务器端没有配置要备份的目录");
		}

		return resultEntity;
	}

	class CheckFileThread extends Thread {
		private String dirStr;
		private String flag;

		public CheckFileThread(String dirStr, String flag) {
			this.dirStr = dirStr;
			this.flag = flag;
		}

		@Override
		public void run() {
			// 把目录字符串按回车分解成多个
			String[] dirs = dirStr.split("\n");

			if (WhUtil.isEmpty(WhCommon.checkFileMap)) {
				WhCommon.checkFileMap = new HashMap<String, List<Map<String, Object>>>();
			}

			List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
			WhCommon.checkFileMap.put(flag, list);

			// 循环并递归的扫描文件
			File dirFile = null;
			String replaceDir;
			for (String dir : dirs) {
				dirFile = new File(dir);
				replaceDir = dir.trim().substring(0, dir.trim().lastIndexOf(File.separator) + 1);

				if (dirFile.exists()) {
					// 开始递归处理
					checkDir(dirFile, flag, replaceDir);
				}
			}
		}
	}

	public void checkDir(File dir, String flag, String replaceDir) {
		// 获取这个目录下的所有文件与目录
		File[] files = dir.listFiles();

		Map<String, Object> filePathTemp = null;
		if (!WhUtil.isEmpty(files) && files.length > 0) {
			// 有文件，那就遍历它们
			for (File file : files) {
				if (file.isDirectory()) {
					// 是目录，继续递归
					checkDir(file, flag, replaceDir);
				} else {
					// 是文件
					filePathTemp = new HashMap<String, Object>();
					filePathTemp.put("filePath", file.getPath());
					filePathTemp.put("fileSize", file.length());
					filePathTemp.put("targetPath", file.getPath().replace(replaceDir, ""));
					filePathTemp.put("fileOrDir", 1);
					filePathTemp.put("status", 0);
					WhCommon.checkFileMap.get(flag).add(filePathTemp);
				}

				// 当集合大于1000的话，就停住不进行先，直到集合里的数据被取走
				if (WhCommon.checkFileMap.get(flag).size() >= 1000) {
					while (true) {
						if (WhCommon.checkFileMap.get(flag).size() >= 1000) {
							try {
								Thread.sleep(3000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						} else {
							break;
						}
					}
				}
			}
		} else {
			// 没有文件，是个空目录
			filePathTemp = new HashMap<String, Object>();
			filePathTemp.put("filePath", dir.getPath());
			filePathTemp.put("fileSize", 0);
			filePathTemp.put("targetPath", dir.getPath().replace(replaceDir, ""));
			filePathTemp.put("fileOrDir", 2);
			filePathTemp.put("status", 0);
			WhCommon.checkFileMap.get(flag).add(filePathTemp);
		}
	}

	/**
	 * 获取备份目录下的所有文件路径
	 */
	public ResultEntity getAllFile(String json) throws Exception {
		ResultEntity resultEntity = new ResultEntity();
		log.info("开始整站备份获取所有文件");

		RequestEntity requestEntity = (RequestEntity) JsonUtil.jsonToBean(json, RequestEntity.class);
		Map<String, Object> param = (Map<String, Object>) requestEntity.getParam();
		String flag = WhUtil.toString(param.get("flag"));

		List<Map<String, Object>> filePathList = new ArrayList<Map<String, Object>>();
		if (!WhUtil.isEmpty(WhCommon.checkFileMap.get(flag)) && WhCommon.checkFileMap.get(flag).size() > 0) {
			// 有文件,那就取出所有的
			int count = WhCommon.checkFileMap.get(flag).size();
			filePathList = new ArrayList(WhCommon.checkFileMap.get(flag).subList(0, count));

			// 删除掉取出来的那些
			for (int i = 0; i < count; i++) {
				WhCommon.checkFileMap.get(flag).remove(0);
			}
		}

		resultEntity.setObj(filePathList);

		// // 取出配置信息
		// Map<String, Object> optionMap = queryConfig();
		//
		// // 取出配置的备份目录字符串
		// String dirStr = WhUtil.toString(optionMap.get("dirs"));
		//
		// if (!WhUtil.isEmpty(dirStr)) {
		// String[] dirStrs = dirStr.split("\n");
		//
		// // 循环备份目录，获取所有的文件路径
		// List<Map<String, Object>> filePathList = new ArrayList<Map<String,
		// Object>>();
		// Map<String, Object> filePathTemp;
		// File dirTemp;
		// Collection<File> fileCollection;
		// String replaceDir;
		// for (String dir : dirStrs) {
		// dirTemp = new File(dir.trim());
		// replaceDir = dir.trim().substring(0,
		// dir.trim().lastIndexOf(File.separator) + 1);
		//
		// if (dirTemp.exists()) {
		// fileCollection = FileUtils.listFiles(dirTemp, null, true);
		//
		// if (!WhUtil.isEmpty(fileCollection) && fileCollection.size() > 0) {
		// for (File file : fileCollection) {
		// filePathTemp = new HashMap<String, Object>();
		// filePathTemp.put("filePath", file.getPath());
		// filePathTemp.put("targetPath", file.getPath().replace(replaceDir,
		// ""));
		// filePathTemp.put("fileOrDir", 1);
		// filePathTemp.put("status", 0);
		// filePathList.add(filePathTemp);
		// }
		// }
		// }
		// }
		//
		// resultEntity.setObj(filePathList);
		// log.info("结束整站备份获取所有文件:" + filePathList.size());
		// } else {
		// resultEntity.setResult(false);
		// resultEntity.setError("服务端没有配置备份目录");
		// }

		return resultEntity;
	}

	/**
	 * 下载指定路径的文件,webservice用的
	 * 
	 * @param json
	 * @return
	 * @throws Exception
	 */
	public ResultEntity downloadFile(String json) throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		RequestEntity requestEntity = (RequestEntity) JsonUtil.jsonToBean(json, RequestEntity.class);
		Map<String, Object> param = (Map<String, Object>) requestEntity.getParam();

		String filePath = WhUtil.toString(param.get("filePath"));

		// 开始把读取文件并转为字符
		File file = new File(filePath);
		FileInputStream fis;
		byte[] fileData;

		if (file.exists()) {
			fis = new FileInputStream(file);

			if (fis.available() != 0) {
				fileData = new byte[fis.available()];
			} else {
				fileData = new byte[1];
			}

			fis.read(fileData);
			fis.close();

			resultEntity.setObj(Base64.byteArrayToBase64(fileData));
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("没有找到文件");
		}

		return resultEntity;
	}

	/**
	 * action用的下载指定路径的文件
	 */
	public void downloadFile(ServletOutputStream servletOutputStream, String filePath) {
		File file = new File(filePath);
		FileInputStream fis = null;

		try {
			if (file.exists() && !file.isDirectory()) {
				// 有这个文件并且不是文件夹
				fis = new FileInputStream(file);

				int b = -1;
				while ((b = fis.read()) != -1) {
					servletOutputStream.write(b);
				}

				servletOutputStream.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
