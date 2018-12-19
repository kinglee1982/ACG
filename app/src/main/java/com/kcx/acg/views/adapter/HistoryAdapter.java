package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.kcx.acg.R;
import com.kcx.acg.bean.HistoryBean;
import com.kcx.acg.manager.WorkManager;
import com.kcx.acg.utils.FormatCurrentData;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

public class HistoryAdapter extends DelegateAdapter.Adapter {

    private static final int VIEW_SHOW_TITLE = 0x1001;
    private static final int VIEW_HIDE_TITLE = 0x1002;

    private Context context;
    private LayoutHelper layoutHelper;
    private List<HistoryBean> list;


    public HistoryAdapter(Context context, LayoutHelper layoutHelper, List<HistoryBean> list) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.list = list;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.history_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        final HistoryBean item = list.get(position);
//        int timeType = FormatCurrentData.getTimePosition(item.getDate());
        if (position == 0) {
            viewHolder.topLineIv.setVisibility(View.INVISIBLE);
        }else {
            viewHolder.topLineIv.setVisibility(View.VISIBLE);
        }
        if (position == list.size() - 1) {
            viewHolder.bottomLineIv.setVisibility(View.INVISIBLE);
            viewHolder.cricleIv.setVisibility(View.VISIBLE);
        }else {
            viewHolder.bottomLineIv.setVisibility(View.VISIBLE);
            viewHolder.cricleIv.setVisibility(View.INVISIBLE);
        }
        switch (getItemViewType(position)) {
            case VIEW_SHOW_TITLE:
                if (position > 0)
                    viewHolder.lineIv.setVisibility(View.VISIBLE);
                else
                    viewHolder.lineIv.setVisibility(View.GONE);
                viewHolder.cricleIv.setVisibility(View.VISIBLE);
                viewHolder.dayTv.setText(FormatCurrentData.getTimePosition(item.getBean().getCreateTime()));
                break;
            case VIEW_HIDE_TITLE:
                viewHolder.lineIv.setVisibility(View.GONE);
                if (position < list.size() - 1)
                    viewHolder.cricleIv.setVisibility(View.INVISIBLE);
                viewHolder.dayTv.setText("");
                break;
        }

        viewHolder.titleTv.setText(list.get(position).getBean().getProductTitle());
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        try {
            viewHolder.timeTv.setText(sdf2.format(sdf1.parse(item.getBean().getCreateTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WorkManager.getInstances().goToWhere(context, item.getBean().getProductID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (list.get(position).isFlag()) {
            return VIEW_SHOW_TITLE;
        } else {
            return VIEW_HIDE_TITLE;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dayTv;
        public TextView timeTv;
        public TextView titleTv;
        public ImageView cricleIv;
        public ImageView lineIv;
        public ImageView topLineIv;
        public ImageView bottomLineIv;


        public ViewHolder(View itemView) {
            super(itemView);
            dayTv = itemView.findViewById(R.id.history_item_day_tv);
            timeTv = itemView.findViewById(R.id.history_item_time_tv);
            titleTv = itemView.findViewById(R.id.history_item_title_tv);
            cricleIv = itemView.findViewById(R.id.history_item_cricle_iv);
            lineIv = itemView.findViewById(R.id.history_item_line_iv);
            topLineIv = itemView.findViewById(R.id.history_item_line_top_iv);
            bottomLineIv = itemView.findViewById(R.id.history_item_line_bottom_iv);
        }
    }
}
