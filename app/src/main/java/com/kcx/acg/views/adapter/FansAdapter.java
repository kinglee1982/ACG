package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.kcx.acg.R;
import com.kcx.acg.api.AttentionMemberApi;
import com.kcx.acg.bean.AttentionMemberBean;
import com.kcx.acg.bean.UserAttentionListBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.BottomDialogUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import java.util.List;

/**
 * 粉丝
 * Created by jb on 2018/2/6.
 */

public class FansAdapter extends RecyclerView.Adapter<FansAdapter.ViewHolder> {

    private Context context;
    private List<UserAttentionListBean.ReturnDataBean.ListBean> list;
    public OnItemClickListener onItemClickListener;
    private boolean isAttention;


    public FansAdapter(Context context, List<UserAttentionListBean.ReturnDataBean.ListBean> list) {
        this.context = context;
        this.list = list;
    }

    public void updata(List<UserAttentionListBean.ReturnDataBean.ListBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    //设置回调监听
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }


    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        //实例化展示的view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_attention, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getPhoto()).into(holder.iv_touxiang);
        holder.tv_nickname.setText(list.get(position).getUserName());
        isAttention = list.get(position).isAttentioned();
        if (isAttention) {
            holder.btn_attention.setBackgroundResource(R.drawable.shape_be_attention_bg);
            holder.btn_attention.setText("已关注");
        }else {
            holder.btn_attention.setBackgroundResource(R.drawable.shape_pink_bg);
            holder.btn_attention.setText("+关注");
        }

        //+关注点击事件
        holder.btn_attention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isAttention) {
                    holder.btn_attention.setBackgroundResource(R.drawable.shape_be_attention_bg);
                    holder.btn_attention.setText("已关注");
                    list.get(position).setAttentioned(true);
                    AttentionMemberApi(list.get(position).getMemberID(), 1);

                } else {
                    new BottomDialogUtil(context,
                            R.layout.dialog_bottom,
                            "确定不再关注此人?",
                            "取消关注",
                            "取消",
                            new BottomDialogUtil.BottonDialogListener() {
                                @Override
                                public void onItemListener() {
                                    holder.btn_attention.setBackgroundResource(R.drawable.shape_pink_bg);
                                    holder.btn_attention.setText("+关注");
                                    list.get(position).setAttentioned(false);
                                    AttentionMemberApi(list.get(position).getMemberID(), 0);
                                }
                            });

                }

            }
        });

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
        TextView tv_nickname;
        Button btn_attention;
        ImageView iv_touxiang;

        public ViewHolder(View view) {
            super(view);
            tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            btn_attention = (Button) view.findViewById(R.id.btn_attention);
            iv_touxiang = (ImageView) view.findViewById(R.id.iv_touxiang);
        }
    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    /**
     * @param attentionedMemberID 被关注用户ID;
     * @param type                0:取消关注；1：关注
     */
    private void AttentionMemberApi(int attentionedMemberID, int type) {
        AttentionMemberApi attentionMemberApi = new AttentionMemberApi((RxAppCompatActivity) context, new HttpOnNextListener<AttentionMemberBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AttentionMemberBean attentionMemberBean) {

                return null;
            }
        });
        attentionMemberApi.setMemberID(attentionedMemberID);
        attentionMemberApi.setType(type);
        HttpManager.getInstance().doHttpDeal(context,attentionMemberApi);

    }


}
