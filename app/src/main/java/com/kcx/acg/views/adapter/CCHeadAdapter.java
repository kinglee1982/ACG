package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.kcx.acg.R;
import com.kcx.acg.views.view.NoteRadioGroup;

public class CCHeadAdapter extends DelegateAdapter.Adapter {

    private Context context;
    private LayoutHelper layoutHelper;
    private ViewHolder viewHolder;

    public CCHeadAdapter(Context context, LayoutHelper layoutHelper) {
        this.context = context;
        this.layoutHelper = layoutHelper;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.creative_center_head_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        viewHolder = (ViewHolder) holder;

        viewHolder.radioGroup.setOnCheckedChangeListener(new NoteRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(NoteRadioGroup group, int checkedId) {
                onCheckedChangeListener.onCheckedChange(checkedId);
            }
        });
    }

    public void setChecked(int viewId) {
        if (viewHolder != null)
            viewHolder.radioGroup.check(viewId);
    }

    public void setEnable(boolean enable) {
        for (int i = 0; i < viewHolder.radioGroup.getChildCount(); i++) {
            View child = viewHolder.radioGroup.getChildAt(i);
            if (child instanceof RadioButton)
                child.setEnabled(enable);
        }
    }

    @Override
    public int getItemCount() {
        return 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public NoteRadioGroup radioGroup;

        public ViewHolder(View itemView) {
            super(itemView);
            radioGroup = itemView.findViewById(R.id.creative_center_head_radio_group);
        }
    }

    private OnCheckedChangeListener onCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(int checkedId);
    }
}
