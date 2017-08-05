package com.gzwanhong.utils;

import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

import com.gzwanhong.dao.SynFileOptionDao;
import com.gzwanhong.domain.ChangeFile;

public class MyFileListener extends FileAlterationListenerAdaptor {

	private SynFileOptionDao synFileOptionDao;
	private String backupDir;
	private String replaceDir;

	public MyFileListener(SynFileOptionDao synFileOptionDao, String backupDir) {
		this.synFileOptionDao = synFileOptionDao;
		this.backupDir = backupDir;
		this.replaceDir = backupDir.substring(0, backupDir.lastIndexOf(File.separator)+1);
	}

	@Override
	public void onDirectoryChange(File directory) {
		super.onDirectoryChange(directory);
	}

	@Override
	public void onDirectoryCreate(File directory) {
		super.onDirectoryCreate(directory);
		try {
			ChangeFile changeFile = new ChangeFile();
			changeFile.setFileName(directory.getName());
			changeFile.setFilePath(directory.getPath());
			changeFile.setBackupDir(backupDir);
			changeFile.setTargetPath(directory.getPath().replace(replaceDir, ""));
			changeFile.setFileOrDir(2);
			changeFile.setChangeType(1);
			changeFile.setStatus(0);

			synFileOptionDao.save(changeFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onDirectoryDelete(File directory) {
		super.onDirectoryDelete(directory);

		try {
			ChangeFile changeFile = new ChangeFile();
			changeFile.setFileName(directory.getName());
			changeFile.setFilePath(directory.getPath());
			changeFile.setBackupDir(backupDir);
			changeFile.setTargetPath(directory.getPath().replace(replaceDir, ""));
			changeFile.setFileOrDir(2);
			changeFile.setChangeType(3);
			changeFile.setStatus(0);

			synFileOptionDao.save(changeFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFileChange(File file) {
		super.onFileChange(file);

		try {
			ChangeFile changeFile = new ChangeFile();
			changeFile.setFileName(file.getName());
			changeFile.setFilePath(file.getPath());
			changeFile.setFileSize(file.length());
			changeFile.setBackupDir(backupDir);
			changeFile.setTargetPath(file.getPath().replace(replaceDir, ""));
			changeFile.setFileOrDir(1);
			changeFile.setChangeType(2);
			changeFile.setStatus(0);

			synFileOptionDao.save(changeFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFileCreate(File file) {
		super.onFileCreate(file);

		try {
			ChangeFile changeFile = new ChangeFile();
			changeFile.setFileName(file.getName());
			changeFile.setFilePath(file.getPath());
			changeFile.setFileSize(file.length());
			changeFile.setBackupDir(backupDir);
			changeFile.setTargetPath(file.getPath().replace(replaceDir, ""));
			changeFile.setFileOrDir(1);
			changeFile.setChangeType(1);
			changeFile.setStatus(0);

			synFileOptionDao.save(changeFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onFileDelete(File file) {
		super.onFileDelete(file);

		try {
			ChangeFile changeFile = new ChangeFile();
			changeFile.setFileName(file.getName());
			changeFile.setFilePath(file.getPath());
			changeFile.setFileSize(file.length());
			changeFile.setBackupDir(backupDir);
			changeFile.setTargetPath(file.getPath().replace(replaceDir, ""));
			changeFile.setFileOrDir(1);
			changeFile.setChangeType(3);
			changeFile.setStatus(0);

			synFileOptionDao.save(changeFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onStart(FileAlterationObserver observer) {
		super.onStart(observer);
	}

	@Override
	public void onStop(FileAlterationObserver observer) {
		super.onStop(observer);
	}

}
