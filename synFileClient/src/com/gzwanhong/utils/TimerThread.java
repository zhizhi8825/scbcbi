package com.gzwanhong.utils;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 定时器线程
 * 
 * @author Administrator
 *
 */
public class TimerThread extends Thread {
	private Logger log = Logger.getLogger(getClass());

	@Override
	public void run() {

		Boolean isWorking = false;
		Date nowHour = null;
		while (WhCommon.isOn) {
			isWorking = false;
			nowHour = WhUtil.getHourMin(new Date());

			for (Map<String, Date> workTime : WhCommon.workTimeList) {
				if (nowHour.compareTo(workTime.get("startHour")) >= 0
						&& nowHour.compareTo(workTime.get("endHour")) <= 0) {
					isWorking = true;
				}
			}

			if (WhCommon.isWorking != isWorking && isWorking) {
				// 开启
				log.info("开启备份线程");

				if (WhUtil.isEmpty(WhCommon.backupThead)) {
					WhCommon.backupThead = new BackupThread();
					WhCommon.backupThead.start();
				}

				WhCommon.isWorking = isWorking;
			} else if (WhCommon.isWorking != isWorking && !isWorking) {
				// 关闭工作
				log.info("关闭备份线程");

				if (!WhUtil.isEmpty(WhCommon.backupThead)) {
					WhCommon.backupThead.stop();
					WhCommon.backupThead = null;
				}

				WhCommon.isWorking = isWorking;
			}

			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void destroy() {
		System.out.println("*******destroy");
		super.destroy();
	}

}
