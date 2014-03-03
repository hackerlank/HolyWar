package com.holywar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.database.Cursor;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

public class LoginActivity extends AppActivity {
	protected static final String TAG = "LoginActivity";

	private String serverArea[] = { "炎黄争霸", "龙行天下" };

	private Button loginButton = null;
	private Button exitButton = null;
	private EditText passwordEditText = null;
	private EditText usernameEditText = null;
	private Spinner spinner = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		usernameEditText = (EditText) findViewById(R.id.login_EditTextUsername);
		passwordEditText = (EditText) findViewById(R.id.login_EditTextPassword);

		loginButton = (Button) findViewById(R.id.login_btnlogin);
		loginButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Log.v(TAG, "loginButton");
				try {
					String username = "";
					String password = "";

					JSONArray array = new JSONArray();
					array.put(0, username);
					array.put(1, password);

					JSONObject object = new JSONObject();
					object.put("actioncode", 1);
					object.put("data", array);

					LoginActivity.this.requestURL(object.toString(),
							"login.php");
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

		exitButton = (Button) findViewById(R.id.login_btnexit);
		exitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				System.exit(0);
			}
		});

		spinner = (Spinner) findViewById(R.id.login_splongin);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, serverArea);
		spinner.setAdapter(adapter);

		createShortCut();
		Log.v("main", "test");
	}

	@Override
	protected void handleResult(String jsonStr) {
		try {
			JSONObject object = new JSONObject(jsonStr);
			if (object != null) {
				String loginflag = object.getString("success");
				AppUtil.verifyCode = object.getString("result");
				AppUtil.faction = object.getString("faction");
				AppUtil.heroname = object.getString("heroname");
				AppUtil.herolevel = object.getString("herolevel");

				if (loginflag.equals("true")) {
					AppUtil.dialog(this, "登录成功");
				} else {
					AppUtil.dialog(this, "登录失败，请重新登录");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		release();
	}

	/**
	 * 获取系统的SDK版本号
	 * 
	 * @return
	 */
	private int getSystemVersion() {
		return Build.VERSION.SDK_INT;
	}

	protected void createShortCut() {
		Log.v("main", String.valueOf(hasShortcut()));
		// if (!hasShortcut()) {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.setFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		intent.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.setClass(this, AppActivity.class);

		Intent shortcutIntent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		// 快捷方式的标题
		shortcutIntent.putExtra(
				Intent.EXTRA_SHORTCUT_NAME,
				this.getApplicationContext().getString(
						com.holywar.R.string.app_name));
		// 快捷方式的图标
		ShortcutIconResource iconResource = Intent.ShortcutIconResource
				.fromContext(this, com.holywar.R.drawable.ic_launcher);
		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				iconResource);
		// 是否允许重复创建（不一定有效）
		shortcutIntent.putExtra("duplicate", false);

		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

		sendBroadcast(shortcutIntent);
		// }

	}

	private boolean hasShortcut() {
		boolean isInstall = false;
		final ContentResolver contentResolver = this.getContentResolver();

		String url = "";

		if (getSystemVersion() < 8) {
			url = "content://com.android.launcher.settings/favorites?notify=true";
		} else {
			url = "content://com.android.launcher2.settings/favorites?notify=true";
		}

		final Uri CONTENT_URI = Uri.parse(url);

		Cursor cursor = contentResolver
				.query(CONTENT_URI,
						new String[] { "title", "iconResource" },
						"title=?",
						new String[] { getString(com.holywar.R.string.app_name) },
						null);
		if (cursor != null && cursor.getCount() > 0) {
			Log.v(this.getClass().getName(), "已创建");
			return true;
		}
		return false;
	}
}