package com.gzwanhong.utils;

import java.io.File;

import com.gzwanhong.dao.SynFileOptionDao;
import com.gzwanhong.domain.ChangeFile;

import net.contentobjects.jnotify.JNotifyListener;

public class MyJNotifyListener implements JNotifyListener {
	private SynFileOptionDao synFileOptionDao;
	private String backupDir;
	private String replaceDir;

	public MyJNotifyListener(SynFileOptionDao synFileOptionDao, String backupDir) {
		this.synFileOptionDao = synFileOptionDao;
		this.backupDir = backupDir;
		this.replaceDir = backupDir.substring(0, backupDir.lastIndexOf(File.separator) + 1);
	}

	@Override
	public void fileCreated(int wd, String rootPath, String name) {
		try {
			File file = new File(new File(rootPath), name);

			ChangeFile changeFile = new ChangeFile();
			changeFile.setFileName(name);
			changeFile.setFilePath(file.getPath());
			changeFile.setBackupDir(backupDir);
			changeFile.setTargetPath(file.getPath().replace(replaceDir, ""));
			changeFile.setChangeType(1);
			changeFile.setStatus(0);

			if (file.isDirectory()) {
				changeFile.setFileOrDir(2);
			} else {
				changeFile.setFileOrDir(1);
				changeFile.setFileSize(file.length());
			}

			synFileOptionDao.save(changeFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fileDeleted(int wd, String rootPath, String name) {
		try {
			File file = new File(new File(rootPath), name);

			ChangeFile changeFile = new ChangeFile();
			changeFile.setFileName(name);
			changeFile.setFilePath(file.getPath());
			changeFile.setBackupDir(backupDir);
			changeFile.setTargetPath(file.getPath().replace(replaceDir, ""));
			changeFile.setChangeType(3);
			changeFile.setStatus(0);

			if (file.isDirectory()) {
				changeFile.setFileOrDir(2);
			} else {
				changeFile.setFileOrDir(1);
				changeFile.setFileSize(file.length());
			}

			synFileOptionDao.save(changeFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fileModified(int wd, String rootPath, String name) {
		try {
			File file = new File(new File(rootPath), name);

			ChangeFile changeFile = new ChangeFile();
			changeFile.setFileName(name);
			changeFile.setFilePath(file.getPath());
			changeFile.setBackupDir(backupDir);
			changeFile.setTargetPath(file.getPath().replace(replaceDir, ""));
			changeFile.setChangeType(2);
			changeFile.setStatus(0);

			if (file.isDirectory()) {
				changeFile.setFileOrDir(2);
			} else {
				changeFile.setFileOrDir(1);
				changeFile.setFileSize(file.length());
			}

			synFileOptionDao.save(changeFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fileRenamed(int wd, String rootPath, String oldName, String newName) {
		try {
			File file = new File(new File(rootPath), newName);

			ChangeFile changeFile = new ChangeFile();
			changeFile.setFileName(newName);
			changeFile.setOldName(oldName);
			changeFile.setFilePath(file.getPath());
			changeFile.setBackupDir(backupDir);
			changeFile.setTargetPath(file.getPath().replace(replaceDir, ""));
			changeFile.setChangeType(4);
			changeFile.setStatus(0);

			if (file.isDirectory()) {
				changeFile.setFileOrDir(2);
			} else {
				changeFile.setFileOrDir(1);
				changeFile.setFileSize(file.length());
			}

			synFileOptionDao.save(changeFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
