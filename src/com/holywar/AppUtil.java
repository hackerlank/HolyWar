package com.holywar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

public class AppUtil {
	public static final String URL_PREFIX_STRING = "http://androidbks.com/holywar/";
	// public static final String URL_PREFIX_STRING =
	// "http://www.androidbks.com/holywar/";
	/* ��androidbks.com��ע������� */
	public static String email = "1042282500@qq.com";
	// public static String email = "jylong06@163.com";

	/* ��֤�� */
	public static String verifyCode = "";
	/* ���� */
	public static String faction = "";
	/* Ӣ������ */
	public static String heroname = "";
	/* Ӣ�۵ȼ� */
	public static String herolevel = "";

	/* ���ɱ�־ 0 �ɽ� */
	public static final String FACTION_FAIRYLAND = "0";
	/* ���ɱ�־ 1 ħ�� */
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
