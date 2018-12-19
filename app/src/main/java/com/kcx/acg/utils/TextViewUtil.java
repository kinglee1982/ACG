package com.kcx.acg.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.kcx.acg.impl.ClickListener;
import java.util.ArrayList;
import java.util.List;

public class TextViewUtil {
	public static void setText(Context context, TextView tv,
							   List<String> str, List<Integer> color,
							   ClickListener clickListener) {
		// 累加数组所有的字符串为一个字符串
		StringBuffer long_str = new StringBuffer();
		for (int i = 0; i < str.size(); i++) {
			long_str.append(str.get(i));
		}
		SpannableString builder = new SpannableString(long_str.toString());
		// 设置不同字符串的点击事件
		for (int i = 0; i < str.size(); i++) {
			int p = i;
			int star = long_str.toString().indexOf(str.get(i));
			int end = star + str.get(i).length();
			builder.setSpan(new Clickable(clickListener, p), star, end,
					Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}

		// 设置不同字符串的颜色

		ArrayList<ForegroundColorSpan> foregroundColorSpans = new ArrayList<ForegroundColorSpan>();
		for (int i = 0; i < color.size(); i++) {
			foregroundColorSpans.add(new ForegroundColorSpan(color.get(i)));
		}
		for (int i = 0; i < str.size(); i++) {
			int star = long_str.toString().indexOf(str.get(i));
			int end = star + str.get(i).length();
			builder.setSpan(foregroundColorSpans.get(i), star, end,
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		// 设置不同字符串的字号
		//		ArrayList<AbsoluteSizeSpan> absoluteSizeSpans = new ArrayList<AbsoluteSizeSpan>();
		//
		//		for (int i = 0; i < str.size(); i++) {
		//			int star = long_str.toString().indexOf(str.get(i));
		//			int end = star + str.get(i).length();
		//			builder.setSpan(absoluteSizeSpans.get(i), star, end,
		//					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//		}
		// 设置点击后的颜色为透明，否则会一直出现高亮
		tv.setHighlightColor(Color.TRANSPARENT);
		tv.setClickable(true);
		tv.setMovementMethod(LinkMovementMethod.getInstance());
		tv.setText(builder);
	}

	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}
}

class Clickable extends ClickableSpan implements OnClickListener {
	private final ClickListener clickListener;
	private int position;

	public Clickable(ClickListener clickListener, int position) {
		this.clickListener = clickListener;
		this.position = position;
	}

	@Override
	public void onClick(View v) {
		clickListener.click(position);
	}

	@Override
	public void updateDrawState(TextPaint ds) {
		// TODO Auto-generated method stub
		super.updateDrawState(ds);
		ds.setColor(Color.WHITE); // 设置文件颜色
		ds.setUnderlineText(false);
	}
}