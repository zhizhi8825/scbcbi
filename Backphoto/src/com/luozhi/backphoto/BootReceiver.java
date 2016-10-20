package com.luozhi.backphoto;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// 开机启动service
		// 启动服务
		Intent serviceIntent = new Intent(context, MyService.class);
		context.startService(serviceIntent);
	}

}
