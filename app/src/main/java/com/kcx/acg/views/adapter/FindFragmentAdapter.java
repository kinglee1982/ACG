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

import org.w3c.dom.Text;

import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 */

public class FindFragmentAdapter extends DelegateAdapter.Adapter {

    private List<String> list;
    // 用于存放数据列表

    private Context context;
    private LayoutHelper layoutHelper;
    private RecyclerView.LayoutParams layoutParams;
    private int count = 0;

//    private MyItemClickListener myItemClickListener;


    public FindFragmentAdapter(Context context, LayoutHelper layoutHelper, int count, List<String> list) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.count = count;
        this.list = list;
    }

    public FindFragmentAdapter(Context context, LayoutHelper layoutHelper, int count, @NonNull RecyclerView.LayoutParams layoutParams, List<String> list) {
        this.context = context;
        this.layoutHelper = layoutHelper;
        this.count = count;
        this.layoutParams = layoutParams;
        this.list = list;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.find_fragment, null));
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.find_fragment_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.find_fragment_item_tv);
        textView.setText("123" + position);
    }


    @Override
    public int getItemCount() {
        return count;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.find_fragment_item_tv);

        }

        public TextView getText(){
            return textView;
        }
    }
}
