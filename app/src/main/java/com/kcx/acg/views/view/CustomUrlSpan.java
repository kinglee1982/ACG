package com.kcx.acg.views.view;

import android.content.Context;
import android.content.Intent;
import android.text.style.ClickableSpan;
import android.view.View;

import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.views.activity.WebViewActivity;

/**
 * Created by zjb on 2018/11/15.
 */

public class CustomUrlSpan extends ClickableSpan {

    private Context context;
    private String url;

    public CustomUrlSpan(Context context, String url) {
        this.context = context;
        this.url = url;
        LogUtil.e("url===",url);
    }

    @Override
    public void onClick(View widget) {
        // 在这里可以做任何自己想要的处理
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(WebViewActivity.URL, url+"#app");
        context.startActivity(intent);
    }
}
