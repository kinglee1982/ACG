package com.kcx.acg.views.activity;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
import com.google.common.collect.Lists;
import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;
import com.kcx.acg.views.adapter.LanguageAdapter;
import com.kcx.acg.views.view.TitleBarView;

import java.util.List;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Created by  o on 2018/11/7.
 */

public class LanguageActivity extends BaseActivity implements CancelAdapt {
    private static final Integer[] LANGUAGES = {
            R.string.setting_language_ja_jp,
            R.string.setting_language_zh_cn,
            R.string.setting_language_zh_tw,
            R.string.setting_language_en_us,
            R.string.setting_language_ko_kr
    };

    private View rootView;
    private TitleBarView titleBarView;
    private ImageView backIv;
    private RecyclerView contentRv;

    @Override
    public View setInitView() {
        rootView = LayoutInflater.from(this).inflate(R.layout.activity_language, null);
        titleBarView = rootView.findViewById(R.id.language_titlebar);
        backIv = titleBarView.getIv_in_title_back();

        contentRv = rootView.findViewById(R.id.language_rv);
        contentRv.setRecycledViewPool(viewPool);
        contentRv.setLayoutManager(layoutManager);
        bindView(contentRv);

        return rootView;
    }

    @Override
    public void initData() {
        List<String> list = Lists.newArrayList();
        for (Integer language :
                LANGUAGES) {
            list.add(getString(language));
        }
        LinearLayoutHelper linearLayoutHelper = new LinearLayoutHelper();
        linearLayoutHelper.setDividerHeight(2);
        linearLayoutHelper.setMarginTop(1);
        linearLayoutHelper.setBgColor(R.color.white);
        LanguageAdapter adapter = new LanguageAdapter(this, linearLayoutHelper, list);
        DelegateAdapter delegateAdapter = new DelegateAdapter(layoutManager);
        delegateAdapter.addAdapter(adapter);
        contentRv.setAdapter(delegateAdapter);
    }

    @Override
    public void setListener() {
        backIv.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_in_title_back:
                finish();
                break;
        }
    }
}
