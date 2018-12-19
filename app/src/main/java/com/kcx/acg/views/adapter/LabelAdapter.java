package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.donkingliang.labels.LabelsView;
import com.kcx.acg.R;
import com.kcx.acg.bean.VIPSettingListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 标签配置列表适配器
 * Created by jb on 2018/2/6.
 */

public class LabelAdapter extends RecyclerView.Adapter<LabelAdapter.ViewHolder> {

    private final Context context;
    private List<VIPSettingListBean.ReturnDataBean.ListBean> list;
    public OnItemClickListener onItemClickListener;
    ArrayList<String> label;

    public LabelAdapter(Context context) {
        this.context = context;

        label = new ArrayList<>();
        label.add("Android");
        label.add("IOS");
        label.add("超级牛逼的技术");
        label.add("前端");
        label.add("后台");
        label.add("微信开发");
        label.add("游戏开发");
        label.add("java");
        label.add("php");
        label.add(".net");
        label.add(".net");
        label.add(".net");
        label.add(".net");
        label.add("Android");
        label.add("IOS");
        label.add("超级牛逼的技术");
        label.add("前端");
        label.add("后台");
        label.add("微信开发");
        label.add("游戏开发");
        label.add("java");
        label.add("php");
        label.add(".net");
        label.add(".net");
        label.add(".net");
        label.add(".net");
        label.add("Android");
        label.add("IOS");
        label.add("超级牛逼的技术");
        label.add("前端");
        label.add("后台");
        label.add("微信开发");
        label.add("游戏开发");
        label.add("java");
        label.add("php");
        label.add(".net");
        label.add(".net");
        label.add(".net");
        label.add(".net");
    }

    public void upData() {

        notifyDataSetChanged();
    }

    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @Override
    public int getItemCount() {
        return label.size();
        //        return list == null ? 0 : list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_label, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        holder.mLabelsView.setLabels(label);
//        holder.mLabelsView.setSelects(0);

        holder.textView.setText(label.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.itemView, position);
                }
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        LabelsView mLabelsView;
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            mLabelsView = (LabelsView) view.findViewById(R.id.mLabelsView);
            textView = view.findViewById(R.id.textView);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);

    }
}
