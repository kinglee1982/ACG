package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.kcx.acg.R;

public class CCTitleAdapter extends DelegateAdapter.Adapter {

    private Context context;
    private LayoutHelper layoutHelper;

    public CCTitleAdapter(Context context, LayoutHelper layoutHelper){
        this.context = context;
        this.layoutHelper = layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TitleHolder(LayoutInflater.from(context).inflate(R.layout.creative_center_title_layout, parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TitleHolder titleHolder = (TitleHolder) holder;
        titleHolder.titleTv.setText(context.getString(R.string.creative_center_title_msg));
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class TitleHolder extends RecyclerView.ViewHolder{
        private TextView titleTv;

        public TitleHolder(View itemView) {
            super(itemView);
            titleTv = itemView.findViewById(R.id.creative_center_title_tv);
        }
    }
}
