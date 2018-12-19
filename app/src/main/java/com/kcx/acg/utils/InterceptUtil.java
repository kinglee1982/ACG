package com.kcx.acg.utils;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.TextView;
import com.kcx.acg.views.view.CustomUrlSpan;

/**
 * Created by zjb on 2018/11/15.
 */
public class InterceptUtil {



    /**
     * 拦截超链接
     * @param tv
     */
    public static void interceptHyperLink(Context context,TextView tv) {
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        CharSequence text = tv.getText();
        if (text instanceof Spannable) {
            int end = text.length();
            Spannable spannable = (Spannable) tv.getText();
            URLSpan[] urlSpans = spannable.getSpans(0, end, URLSpan.class);
            if (urlSpans.length == 0) {
                return;
            }

            //                        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
            SpannableStringBuilder spannableStringBuilder = setTextLinkOpenByWebView(context,text.toString());
            // 循环遍历并拦截 所有http://开头的链接
            for (URLSpan uri : urlSpans) {
                String url = uri.getURL();
                if (url.indexOf("https://") == 0 || url.indexOf("http://") == 0) {
                    CustomUrlSpan customUrlSpan = new CustomUrlSpan(context, url);
                    spannableStringBuilder.setSpan(customUrlSpan, spannable.getSpanStart(uri),
                            spannable.getSpanEnd(uri), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                }
            }
            tv.setText(spannableStringBuilder);
        }
    }

    public static SpannableStringBuilder setTextLinkOpenByWebView(final Context context, String answerString) {
        if (!TextUtils.isEmpty(answerString)) {
            Spanned htmlString = Html.fromHtml(answerString);
            if (htmlString instanceof SpannableStringBuilder) {
                SpannableStringBuilder spannableStringBuilder = (SpannableStringBuilder) htmlString;
                // 取得与a标签相关的Span
                Object[] objs = spannableStringBuilder.getSpans(0, spannableStringBuilder.length(), URLSpan.class);
                if (null != objs && objs.length != 0) {
                    for (Object obj : objs) {
                        int start = spannableStringBuilder.getSpanStart(obj);
                        int end = spannableStringBuilder.getSpanEnd(obj);
                        if (obj instanceof URLSpan) {
                            //先移除这个Span，再新添加一个自己实现的Span。
                            URLSpan span = (URLSpan) obj;
                            final String url = span.getURL();
                            spannableStringBuilder.removeSpan(obj);
                            spannableStringBuilder.setSpan(new ClickableSpan() {
                                @Override
                                public void onClick(View widget) {
                                    //                                    Intent intent = new Intent(context, WebViewActivity.class);
                                    //                                    context.startActivity(intent);
                                }

                            }, start, end, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        }
                    }
                }
                return spannableStringBuilder;
            }

        }
        return new SpannableStringBuilder(answerString);
    }


}
