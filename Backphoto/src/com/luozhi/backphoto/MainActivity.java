package com.luozhi.backphoto;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {
	private Button button1 = null;
	private EditText dir = null;
	private EditText endWith = null;
	private EditText targetText = null;
	private File dirFile = null;
	private LinearLayout beforGame = null;
	private LinearLayout gameing = null;
	private int count = 0;
	private long firClick = 0;
	private long secClick = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.main_layout);

		// 启动服务
		Intent serviceIntent = new Intent(this, MyService.class);
		startService(serviceIntent);

		button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new Button1Click());

		dir = (EditText) findViewById(R.id.dir);
		dir.setText(MyBroadcastReceiver.targetDir);

		endWith = (EditText) findViewById(R.id.endWith);
		targetText = (EditText) findViewById(R.id.targetText);

		gameing = (LinearLayout) findViewById(R.id.gameing);
		beforGame = (LinearLayout) findViewById(R.id.beforGame);
		beforGame.setOnTouchListener(new onDoubleClick());
	}

	class Button1Click implements OnClickListener {

		@Override
		public void onClick(View v) {
			if ("".equals(dir.getText().toString())) {
				Toast.makeText(MainActivity.this, "路径不能为空", Toast.LENGTH_SHORT)
						.show();
			} else if (endWith.getText().toString()
					.equals(targetText.getText().toString())) {
				Toast.makeText(MainActivity.this, "要修改的后缀都一样，不做处理",
						Toast.LENGTH_SHORT).show();
			} else {
				dirFile = new File(dir.getText().toString());
				if (dirFile.exists()) {
					String endWithStr = endWith.getText().toString();
					String targetStr = targetText.getText().toString();
					File[] files = dirFile.listFiles();
					String fileNameTemp = null;
					int count = 0;

					// 循环目录下的文件
					for (File file : files) {
						if (file.isFile()) {
							// 修改文件名
							if (!"".equals(endWithStr)) {
								if (file.getName().endsWith(endWithStr)) {
									fileNameTemp = file.getPath().substring(
											0,
											file.getPath().lastIndexOf(
													endWithStr))
											+ targetStr;
									file.renameTo(new File(fileNameTemp));

									count++;
								}
							} else {
								file.renameTo(new File(file.getPath()
										+ targetStr));
								count++;
							}
						}
					}

					Toast.makeText(MainActivity.this, "修改了 " + count + " 个文件",
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "找不到该目录",
							Toast.LENGTH_SHORT).show();
				}
			}
		}

	}

	class onDoubleClick implements View.OnTouchListener {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (MotionEvent.ACTION_DOWN == event.getAction()) {
				count++;
				if (count == 1) {
					firClick = System.currentTimeMillis();

				} else if (count == 4) {
					secClick = System.currentTimeMillis();
					if (secClick - firClick < 1000) {
						// 双击事件
						beforGame.setVisibility(LinearLayout.GONE);
						gameing.setVisibility(LinearLayout.VISIBLE);
					}
					count = 0;
					firClick = 0;
					secClick = 0;
				}
			}
			return true;
		}

	}
}
