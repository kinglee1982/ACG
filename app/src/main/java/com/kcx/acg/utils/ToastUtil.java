package com.kcx.acg.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
	private static Toast toast;

	private ToastUtil() {
		throw new UnsupportedOperationException("T cannot be instantiated");
	}

	public static void showShort(Context context, CharSequence text) {
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
		} else {
			toast.setText(text);
		}
		toast.show();
	}

	public static void showLong(Context context, CharSequence text) {
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		} else {
			toast.setText(text);
		}
		toast.show();
	}
}
