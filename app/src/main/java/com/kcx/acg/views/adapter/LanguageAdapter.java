package com.kcx.acg.views.adapter;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.kcx.acg.R;
import com.kcx.acg.manager.AccountManager;
import com.kcx.acg.manager.ConfigManager;

import java.util.List;
import java.util.Locale;

/**
 * Created by  o on 2018/11/7.
 */

public class LanguageAdapter extends DelegateAdapter.Adapter {
    private static final String[] LANGUAGE_CODE = {"ja_JP","zh_CN", "zh_TW", "en_US", "ko_KR"};
    private static final Integer[] LANGUAGE = {
            R.string.setting_language_ja_jp,
            R.string.setting_language_zh_cn,
            R.string.setting_language_zh_tw,
            R.string.setting_language_en_us,
            R.string.setting_language_ko_kr
    };


    private Context context;
    private LayoutHelper layoutHelper;
    private List<String> stringList;

    public LanguageAdapter(Context context, LayoutHelper layoutHelper, List<String> list) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.stringList = list;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.language_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.languageTv.setText(stringList.get(position));
        if(TextUtils.equals(LANGUAGE_CODE[position], AccountManager.getInstances().getLocaleCode())){
            viewHolder.checkedIv.setVisibility(View.VISIBLE);
            viewHolder.languageTv.setTextColor(ContextCompat.getColor(context, R.color.pink_ff8));
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewHolder.checkedIv.setVisibility(View.VISIBLE);
                viewHolder.languageTv.setTextColor(ContextCompat.getColor(context, R.color.pink_ff8));
                String[] localeCode = LANGUAGE_CODE[position].split("_");
                AccountManager.getInstances().setLocaleCode(LANGUAGE_CODE[position]);
                AccountManager.getInstances().setLanguage(context.getString(LANGUAGE[position]));
                Resources res = context.getResources();
                Configuration config = res.getConfiguration();
                config.locale = new Locale(localeCode[0], localeCode[1]);
                ConfigManager.getInstance(context).updateConfigByNotActivity(config);
            }
        });
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public TextView languageTv;
        public ImageView checkedIv;

        public ViewHolder(View itemView) {
            super(itemView);
            languageTv = itemView.findViewById(R.id.language_tv);
            checkedIv = itemView.findViewById(R.id.language_checked_iv);
        }
    }
}
