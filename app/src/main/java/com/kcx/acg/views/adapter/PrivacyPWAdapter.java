package com.kcx.acg.views.adapter;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kcx.acg.R;

/**
 * Created by jb .
 */

public class PrivacyPWAdapter extends RecyclerView.Adapter<PrivacyPWAdapter.ViewHolder> {

    private final String[] stringArray;
    private final int classType;
    private  boolean isShowDel =false;
    private Context context;
    private OnItemClickListener onItemClickListener;
    private Vibrator vibrator;

    public PrivacyPWAdapter(Context context, String[] stringArray,int classType) {
        this.context = context;
        this.stringArray = stringArray;
        this.classType = classType;
        vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void update(boolean isShowDel){
        this.isShowDel =isShowDel;
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return stringArray == null ? 0 : stringArray.length;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_privacy_pw, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PrivacyPWAdapter.ViewHolder holder, final int position) {
        if (position ==9){ //取消
            if (classType==0){ //设置隐私密码
                holder.tv_content.setTextColor(context.getResources().getColor(R.color.black_333));
                holder.tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                holder.rl_bg.setBackgroundResource(R.color.transparent);
            }else if(classType==1){//校验隐私密码holder.rl_bg.setBackgroundResource(R.color.transparent);
               holder.itemView.setVisibility(View.GONE);
            }

        }else if (position ==11){  //删除
            holder.rl_bg.setBackgroundResource(R.color.transparent);
            holder.tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
            holder.tv_content.setTextColor(context.getResources().getColor(R.color.pink_hint));
            if (isShowDel){
                holder.itemView.setVisibility(View.VISIBLE);
            }else {
                holder.itemView.setVisibility(View.GONE);
            }

        }else {
            holder.itemView.setVisibility(View.VISIBLE);
            holder.rl_bg.setBackgroundResource(R.drawable.shape_cricle_light_pink_bg);
            holder.tv_content.setTextColor(context.getResources().getColor(R.color.pink_hint));
            holder.tv_content.setTextSize(TypedValue.COMPLEX_UNIT_SP,26);
        }
        holder.tv_content.setText(stringArray[position]);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(30); //按钮点击震动反馈效果
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position+1);
                }
            }
        });

    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_content;
        RelativeLayout rl_bg;

        public ViewHolder(View view) {
            super(view);
            tv_content = view.findViewById(R.id.tv_content);
            rl_bg = view.findViewById(R.id.rl_bg);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
