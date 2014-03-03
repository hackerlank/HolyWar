package com.holywar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

public class AppUtil {
	public static final String URL_PREFIX_STRING = "http://androidbks.com/holywar/";
	// public static final String URL_PREFIX_STRING =
	// "http://www.androidbks.com/holywar/";
	/* 在androidbks.com上注册的邮箱 */
	public static String email = "1042282500@qq.com";
	// public static String email = "jylong06@163.com";

	/* 验证码 */
	public static String verifyCode = "";
	/* 宗派 */
	public static String faction = "";
	/* 英雄名字 */
	public static String heroname = "";
	/* 英雄等级 */
	public static String herolevel = "";

	/* 宗派标志 0 仙界 */
	public static final String FACTION_FAIRYLAND = "0";
	/* 宗派标志 1 魔界 */
	public static final String FACTION_GHOSTLAND = "1";

	public static void dialog(Context context, String message) {
		AlertDialog dlg = new AlertDialog.Builder(context)
				.setTitle(message)
				.setIcon(R.drawable.info)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).create();
		dlg.show();
	}
}
