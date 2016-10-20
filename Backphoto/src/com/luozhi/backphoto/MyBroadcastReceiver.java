package com.luozhi.backphoto;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.UUID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

public class MyBroadcastReceiver extends BroadcastReceiver {
	private File targetFile = null;
	private FileInputStream fi = null;
	private FileOutputStream fo = null;
	private FileChannel in = null;
	private FileChannel out = null;
	public static String targetDir = null;

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("***** receive");
		Cursor cursor = context.getContentResolver().query(intent.getData(),
				null, null, null, null);
		cursor.moveToFirst();
		String image_video_path = cursor.getString(cursor
				.getColumnIndex("_data"));

		try {
			// 第一次进来的话看有没指定的目录，没有的话就在该照片的同级目录下新建一个
			if (targetDir == null) {
				targetDir = image_video_path.substring(
						0,
						image_video_path.lastIndexOf("/",
								image_video_path.lastIndexOf("/") - 1))
						+ File.separator + "myPicture";
				File dir = new File(targetDir);
				if (!dir.exists()) {
					dir.mkdirs();
				}
			}

			// 创建文件
			targetFile = new File(targetDir
					+ File.separator
					+ image_video_path.substring(image_video_path
							.lastIndexOf("/") + 1) + ".luo");

			// 已有该文件的话就再在名字后面加个随机字符串
			if (targetFile.exists()) {
				targetFile = new File(targetFile.getPath() + "."
						+ UUID.randomUUID());
			}

			targetFile.createNewFile();

			fi = new FileInputStream(image_video_path);
			fo = new FileOutputStream(targetFile);
			in = fi.getChannel();
			out = fo.getChannel();

			in.transferTo(0, in.size(), out);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

}
