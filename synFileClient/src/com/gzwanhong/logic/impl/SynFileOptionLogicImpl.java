package com.gzwanhong.logic.impl;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.print.DocFlavor.STRING;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.expression.spel.ast.OpMinus;
import org.springframework.stereotype.Service;

import com.alibaba.druid.support.logging.Log;
import com.gzwanhong.dao.ConfigurationDao;
import com.gzwanhong.dao.SynFileOptionDao;
import com.gzwanhong.domain.Configuration;
import com.gzwanhong.domain.DownAllFile;
import com.gzwanhong.entity.DatagridEntity;
import com.gzwanhong.entity.PageInfo;
import com.gzwanhong.entity.ParamEntity;
import com.gzwanhong.entity.RequestEntity;
import com.gzwanhong.entity.ResultEntity;
import com.gzwanhong.logic.SynFileOptionLogic;
import com.gzwanhong.mapper.BackupRecordMapper;
import com.gzwanhong.mapper.ChangeFileMapper;
import com.gzwanhong.mapper.DownAllFileMapper;
import com.gzwanhong.utils.DownAllFileThead;
import com.gzwanhong.utils.JsonUtil;
import com.gzwanhong.utils.SpringUtil;
import com.gzwanhong.utils.TimerThread;
import com.gzwanhong.utils.WhCommon;
import com.gzwanhong.utils.WhUtil;
import com.gzwanhong.webserviceClient.ServiceInterface;
import com.gzwanhong.webserviceClient.ServiceInterface_Service;

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

		// 取出配置信息，放到常量里
		WhCommon.backupDir = WhUtil.toString(optionMap.get("backupDir"));
		WhCommon.hours = WhUtil.toInteger(optionMap.get("hours"));
		WhCommon.flag = WhUtil.toString(optionMap.get("flag"));
		WhCommon.clientName = WhUtil.toString(optionMap.get("clientName"));
		WhCommon.webUrl = WhUtil.toString(optionMap.get("webserviceUrl"));
		WhCommon.theadCount = WhUtil.toInteger(optionMap.get("theadCount"));
		WhCommon.workTime = WhUtil.toString(optionMap.get("workTime"));

		return optionMap;
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

				// 取出配置信息，放到常量里
				String backupDir = WhUtil.toString(optionMap.get("backupDir"));
				String hoursStr = WhUtil.toString(optionMap.get("hours"));
				String workTimeStr = WhUtil.toString(optionMap.get("workTime"));
				String flag = WhUtil.toString(optionMap.get("flag"));
				String clientName = WhUtil.toString(optionMap.get("clientName"));
				String webserviceUrl = WhUtil.toString(optionMap.get("webserviceUrl"));

				if (!WhUtil.isEmpty(backupDir) && !WhUtil.isEmpty(hoursStr) && !WhUtil.isEmpty(workTimeStr)
						&& !WhUtil.isEmpty(flag) && !WhUtil.isEmpty(clientName) && !WhUtil.isEmpty(webserviceUrl)) {
					WhCommon.backupDir = backupDir;
					WhCommon.hours = WhUtil.toInteger(hoursStr);
					WhCommon.flag = flag;
					WhCommon.clientName = clientName;
					WhCommon.webUrl = webserviceUrl;

					// 处理时间段
					String[] workTimeStrs = workTimeStr.split("\n");
					WhCommon.workTimeList = new ArrayList<Map<String, Date>>();
					Map<String, Date> workTimeMap = null;

					String[] strsTemp;
					if (!WhUtil.isEmpty(workTimeStrs) && workTimeStrs.length > 0) {
						for (String temp : workTimeStrs) {
							strsTemp = temp.trim().split("-");

							if (!WhUtil.isEmpty(strsTemp) && strsTemp.length == 2) {
								workTimeMap = new HashMap<String, Date>();
								workTimeMap.put("startHour", WhUtil.toDate(strsTemp[0], WhUtil.HH_MM));
								workTimeMap.put("endHour", WhUtil.toDate(strsTemp[1], WhUtil.HH_MM));

								WhCommon.workTimeList.add(workTimeMap);
							} else {
								resultEntity.setResult(false);
								resultEntity.setError("工作时间段配置格式不正确，请检查");
								return resultEntity;
							}
						}

						// 开启定时器线程
						if (WhUtil.isEmpty(WhCommon.timerThead)) {
							WhCommon.timerThead = new TimerThread();
						} else {
							// 如果是已启动了的，那就先关掉再启动
							WhCommon.timerThead.stop();
							WhCommon.timerThead = new TimerThread();
						}

						WhCommon.timerThead.start();

						// 开启
						WhCommon.isOn = true;
					} else {
						resultEntity.setResult(false);
						resultEntity.setError("工作时间段没有配置，请先配置");
					}
				} else {
					resultEntity.setResult(false);
					resultEntity.setError("配置没有维护完整，请先配置");
				}
			} else if (paramEntity.getStatus().intValue() == 0) {
				// 关闭
				if (!WhUtil.isEmpty(WhCommon.timerThead)) {
					// 关闭定时器线程
					WhCommon.timerThead.stop();
					WhCommon.timerThead = null;
				}

				if (!WhUtil.isEmpty(WhCommon.backupThead)) {
					// 关闭备份文件线程
					WhCommon.backupThead.stop();
					WhCommon.backupThead = null;
				}

				WhCommon.isOn = false;
				WhCommon.isWorking = false;
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
		DatagridEntity datagridEntity = synFileOptionDao.queryBySqlToDatagrid(BackupRecordMapper.class,
				"queryBackupRecord", pageInfo, paramEntity);

		return datagridEntity;
	}

	/**
	 * 查看是否还有整站备份的任务没有完成
	 * 
	 * @return
	 * @throws Exception
	 */
	public ResultEntity hasDownTask() throws Exception {
		ResultEntity resultEntity = new ResultEntity();
		DownAllFile example = new DownAllFile();

		example.setStatus(0);
		List<DownAllFile> downAllFileList = synFileOptionDao.queryByExample(example, 1, 1);
		if (!WhUtil.isEmpty(downAllFileList) && downAllFileList.size() > 0) {
			resultEntity.setCode(1);
		} else {
			resultEntity.setCode(0);
		}

		return resultEntity;
	}

	/**
	 * 获取所有要备份的文件信息
	 */
	public ResultEntity getAllFile() throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		// 取出配置信息
		Map<String, Object> optionMap = queryConfig();
		String webserviceUrl = WhUtil.toString(optionMap.get("webserviceUrl"));
		WhCommon.flag = WhUtil.toString(optionMap.get("flag"));

		if (!WhUtil.isEmpty(webserviceUrl)) {
			WhCommon.webUrl = webserviceUrl;

			// 删除数据库里所有的数据
			synFileOptionDao.removeBySql(DownAllFileMapper.class, "removeAll");

			WhCommon.getAllFileThead = new GetAllFileThead(synFileOptionDao, true);
			WhCommon.getAllFileThead.start();
		} else {
			resultEntity.setResult(false);
			resultEntity.setError("没有配置连接地址");
		}

		return resultEntity;
	}

	/**
	 * 内部类，远程获取要整站备份的所有文件信息线程 restart 重新开始
	 * 
	 * @author Administrator
	 *
	 */
	class GetAllFileThead extends Thread {
		private SynFileOptionDao synFileOptionDao;
		private Boolean restart;

		public GetAllFileThead(SynFileOptionDao synFileOptionDao, Boolean restart) {
			this.synFileOptionDao = synFileOptionDao;
			this.restart = restart;
		}

		@Override
		public void run() {
			try {
				log.info("开始执行整站备份文件获取线程");
				RequestEntity requestEntity = new RequestEntity();
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("flag", WhCommon.flag);

				requestEntity.setSecurity("wh");
				requestEntity.setParam(paramMap);

				ServiceInterface serviceInterface = new ServiceInterface_Service(
						new URL(WhCommon.webUrl + WhCommon.webserviceUrl)).getServiceInterfacePort();

				ResultEntity resultEntity = new ResultEntity();
				String json = "";

				// 如果是重新开始的话就去触发扫描文件,不然的话就是继续
				if (restart) {
					json = serviceInterface.startCheckFile(JsonUtil.beanToJson(requestEntity));
					resultEntity = (ResultEntity) JsonUtil.jsonToBean(json, ResultEntity.class);

					log.info("已触发服务端扫描文件");
				}

				if (resultEntity.getResult()) {
					// 开始不停的去服务端取文件信息回来，每处理一次停3秒
					int emptyCount = 0;
					while (WhCommon.isDownAllFile) {
						json = serviceInterface.getAllFile(JsonUtil.beanToJson(requestEntity));
						resultEntity = (ResultEntity) JsonUtil.jsonToBean(json, ResultEntity.class);

						// 取出接口传回来的所有文件路径
						DownAllFile[] filePaths = (DownAllFile[]) JsonUtil
								.jsonToBean(JsonUtil.beanToJson(resultEntity.getObj()), DownAllFile[].class);

						if (!WhUtil.isEmpty(filePaths) && filePaths.length > 0) {
							emptyCount = 0;
							List<DownAllFile> filePathList = new ArrayList<DownAllFile>(Arrays.asList(filePaths));

							int splitNum = 5000; // 每多少第数据提交一次，以免过多数据导致保存失败
							if (filePathList.size() >= splitNum) {
								double count = Math.ceil(filePathList.size() / WhUtil.toDouble(splitNum));
								for (int i = 0; i < count; i++) {
									if (i == count - 1) {
										this.synFileOptionDao
												.saveAll(filePathList.subList(splitNum * i, filePathList.size()));
									} else {
										this.synFileOptionDao
												.saveAll(filePathList.subList(splitNum * i, splitNum * i + splitNum));
									}
								}
							} else {
								this.synFileOptionDao.saveAll(filePathList);
							}
						}
						// else {
						// Thread.sleep(3000);
						// emptyCount++;
						// }

						// if (emptyCount == 20) {
						// break;
						// }
						Thread.sleep(3000);
					}

					WhCommon.getAllFileThead = null;
				} else {
					log.error(resultEntity.getError());
				}

				log.info("结束执行整站备份文件获取线程");
			} catch (Exception e) {
				e.printStackTrace();
				log.error(e);
			}
		}

	}

	public ResultEntity getDownloadStatus() throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		// 获取总条数
		int allCount = synFileOptionDao.queryCountByExample(new DownAllFile());

		// 获取已备份的数量
		DownAllFile example = new DownAllFile();
		example.setStatus(2);
		int downCount = synFileOptionDao.queryCountByExample(example);

		if (allCount == 0) {
			if (WhCommon.isDownAllFile) {
				resultEntity.setMsg("正在获取文件信息，请耐心等候...");
			}
		} else {
			if (WhCommon.isDownAllFile) {
				resultEntity.setMsg("正在整站备份：" + downCount + "/" + allCount);
			} else {
				resultEntity.setMsg("整站备份记录：" + downCount + "/" + allCount);
			}
		}

		return resultEntity;
	}

	/**
	 * 开始整站备份
	 * 
	 * @return
	 * @throws Exception
	 */
	public ResultEntity startDownFile() throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		// 当没有在进行的时候才去打开它
		if (!WhCommon.isDownAllFile) {
			queryConfig();
			WhCommon.isDownAllFile = true;
			int threadCount = (WhCommon.theadCount.intValue() == 0) ? 1 : WhCommon.theadCount; // 定义线程数，同时下载的数量，取配置的，默认1个线程
			WhCommon.downAllFileThread = new ArrayList<Thread>();
			Thread threadTemp;

			// 看看有没开启整站备份取文件的线程，没有的话就开启
			if (WhUtil.isEmpty(WhCommon.getAllFileThead)) {
				WhCommon.getAllFileThead = new GetAllFileThead(synFileOptionDao, false);
				WhCommon.getAllFileThead.start();
			}

			// 把数据库里正在进行的记录状态改为未进行
			DownAllFile example = new DownAllFile();
			example.setStatus(1);
			List<DownAllFile> downAllFileList = synFileOptionDao.queryByExample(example);

			if (!WhUtil.isEmpty(downAllFileList) && downAllFileList.size() > 0) {
				for (DownAllFile downAllFile : downAllFileList) {
					downAllFile.setStatus(0);
				}

				synFileOptionDao.saveOrUpdateAll(downAllFileList);
			}

			// 取出配置信息
			Map<String, Object> optionMap = queryConfig();
			String webserviceUrl = WhUtil.toString(optionMap.get("webserviceUrl"));
			String backupDir = WhUtil.toString(optionMap.get("backupDir"));

			if (!WhUtil.isEmpty(webserviceUrl)) {
				WhCommon.webUrl = webserviceUrl;
				WhCommon.backupDir = backupDir;

				for (int i = 0; i < threadCount; i++) {
					threadTemp = new DownAllFileThead();
					threadTemp.start();
					WhCommon.downAllFileThread.add(threadTemp);
				}
			} else {
				resultEntity.setResult(false);
				resultEntity.setError("没有配置连接地址");
			}
		}

		return resultEntity;
	}

	/**
	 * 暂停整站备份
	 * 
	 * @return
	 * @throws Exception
	 */
	public ResultEntity stopDownFile() throws Exception {
		ResultEntity resultEntity = new ResultEntity();

		// 当有在进行的时候才去关闭它
		if (WhCommon.isDownAllFile) {
			WhCommon.isDownAllFile = false;

			// 关闭线程
			if (!WhUtil.isEmpty(WhCommon.downAllFileThread) && WhCommon.downAllFileThread.size() > 0) {
				for (Thread thread : WhCommon.downAllFileThread) {
					thread.stop();
				}

				WhCommon.downAllFileThread = null;
			}

			// 把数据库里正在进行的记录状态改为未进行
			DownAllFile example = new DownAllFile();
			example.setStatus(1);
			List<DownAllFile> downAllFileList = synFileOptionDao.queryByExample(example);

			if (!WhUtil.isEmpty(downAllFileList) && downAllFileList.size() > 0) {
				for (DownAllFile downAllFile : downAllFileList) {
					downAllFile.setStatus(0);
				}

				synFileOptionDao.saveOrUpdateAll(downAllFileList);
			}
		}

		return resultEntity;
	}

}
