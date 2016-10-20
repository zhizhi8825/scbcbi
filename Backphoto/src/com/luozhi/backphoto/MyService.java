package com.luozhi.backphoto;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentFilter.MalformedMimeTypeException;
import android.os.IBinder;

public class MyService extends Service {
	private static MyBroadcastReceiver myBroadcastReceiver = new MyBroadcastReceiver();

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		try {
			// 启动拍照监听
			IntentFilter intentFilter = new IntentFilter(
					"android.hardware.action.NEW_PICTURE");
			intentFilter.addDataType("image/*");
			intentFilter.setPriority(9999);
			registerReceiver(myBroadcastReceiver, intentFilter);

			// 启动录相监听
			intentFilter = new IntentFilter("android.hardware.action.NEW_VIDEO");
			intentFilter.addDataType("video/*");
			intentFilter.setPriority(9999);
			registerReceiver(myBroadcastReceiver, intentFilter);

			Notification notification = new Notification(R.drawable.sony,
					getString(R.string.app_name), System.currentTimeMillis());

			PendingIntent pendingintent = PendingIntent.getActivity(this, 0,
					new Intent(this, MainActivity.class), 0);
			notification.setLatestEventInfo(this, "", "Sony Home",
					pendingintent);
			startForeground(0x111, notification);
		} catch (MalformedMimeTypeException e) {
			e.printStackTrace();
		}

		flags = START_STICKY;

		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		stopForeground(true);
		// 启动服务
		Intent serviceIntent = new Intent(this, MyService.class);
		startService(serviceIntent);

		super.onDestroy();
	}
}
