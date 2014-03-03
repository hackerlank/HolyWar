package com.holywar;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

public abstract class AppActivity extends Activity {
	private final String TAG = "AppActivity";
	/* 多线程之间通信的工具类 */
	private Handler myHandler = null;
	private NetThread myThread = null;

	private String jsonStr = "";
	private String url = "";

	protected abstract void handleResult(String jsonStr);

	@Override
	protected Dialog onCreateDialog(int id) {
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setMessage("请稍后.....");
		dialog.setIndeterminate(true);
		dialog.setCancelable(false);
		return dialog;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (!showWindowTitle()) {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
		}
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		initHandler();
	}

	private void initHandler() {
		myHandler = new MyHandler();
	}

	protected void release() {
		dismissDialog(1);
		try {
			myThread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void requestURL(String jsonStr, String url) {
		this.jsonStr = jsonStr;
		this.url = url;

		myThread = new NetThread();
		myThread.start();
		showDialog(1);
	}

	/* 是否显示窗口标题 */
	protected boolean showWindowTitle() {
		return false;
	}

	class MyHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				if (msg.obj != null) {
					handleResult(String.valueOf(msg.obj));
				} else {
					dismissDialog(1);
				}

				break;
			case -1:
				AppUtil.dialog(AppActivity.this, "网络异常错误");
				release();
				break;
			case -2:
				AppUtil.dialog(AppActivity.this,
						"请到androidbks.com注册，替换AppUtil.email");
				release();
				break;
			}
			super.handleMessage(msg);
		}
	}

	class NetThread extends Thread {

		@Override
		public void run() {
			String uri = AppUtil.URL_PREFIX_STRING + url;
			try {
				HttpClient client = new DefaultHttpClient();
				String uri1 = uri + "?email=" + AppUtil.email;
				HttpPost post = new HttpPost(uri + "?email=" + AppUtil.email);

				StringEntity stringEntity = new StringEntity(jsonStr, "utf-8");
				post.setEntity(stringEntity);

				client.getParams().setParameter(
						HttpConnectionParams.SO_TIMEOUT, 10000);
				client.getParams().setParameter(
						HttpConnectionParams.CONNECTION_TIMEOUT, 10000);

				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();

				if (entity != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "utf-8"));
					StringBuffer buffer = new StringBuffer();
					String line = reader.readLine();
					while (line != null) {
						buffer.append(line);
						line = reader.readLine();
					}
					reader.close();

					if (buffer == null && buffer.toString().equals("")) {
						myHandler.sendEmptyMessage(-2);
					} else {
						Message msg = new Message();
						msg.what = 0;
						msg.obj = (buffer == null ? "" : buffer.toString());
						myHandler.sendMessage(msg);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				myHandler.sendEmptyMessage(-1);
			}
		}
	}
}
