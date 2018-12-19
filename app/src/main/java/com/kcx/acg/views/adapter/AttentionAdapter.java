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
import com.bumptech.glide.request.RequestOptions;
import com.kcx.acg.R;
import com.kcx.acg.api.AttentionMemberApi;
import com.kcx.acg.base.BusEvent;
import com.kcx.acg.bean.AttentionMemberBean;
import com.kcx.acg.bean.UserAttentionListBean;
import com.kcx.acg.https.HttpManager;
import com.kcx.acg.https.RetryWhenNetworkException;
import com.kcx.acg.impl.HttpOnNextListener;
import com.kcx.acg.utils.BottomDialogUtil;
import com.kcx.acg.views.view.CustomToast;
import com.kcx.acg.views.view.MyStateButton;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 关注
 * Created by jb on 2018/2/6.
 */

public class AttentionAdapter extends RecyclerView.Adapter<AttentionAdapter.ViewHolder> {

    private ViewHolder viewHolder;
    private RequestOptions options;
    private Context context;
    private List<UserAttentionListBean.ReturnDataBean.ListBean> list;
    public OnItemClickListener onItemClickListener;
    private boolean isAttention = false;
    private String attentionStr;

    public interface OnItemClickListener {
        void onItemClick(View view, int position, int memberID);
    }

    public AttentionAdapter(Context context, List<UserAttentionListBean.ReturnDataBean.ListBean> list) {
        this.context = context;
        this.list = list;

        options = new RequestOptions()
                .placeholder(R.mipmap.placehold_head)  //预加载图片
                .error(R.mipmap.placehold_head);
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
        viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getPhoto()).apply(options).into(holder.iv_touxiang);
        if (1== list.get(position).getUserIdentify()){
            holder.iv_v.setVisibility(View.VISIBLE);
        }else {
            holder.iv_v.setVisibility(View.INVISIBLE);
        }
        holder.tv_nickname.setText(list.get(position).getUserName());
        isAttention = list.get(position).isAttentioned();
        if (isAttention) {
            holder.btn_attention.setBackgroundResource(R.drawable.shape_be_attention_bg);
            holder.btn_attention.setText(R.string.browse_followed_msg);
        } else {
            holder.btn_attention.setBackgroundResource(R.drawable.shape_pink_bg);
            holder.btn_attention.setText(R.string.browse_follow_msg);

        }

        holder.myStateButton.setOnInnerClickeListener(new MyStateButton.ButtonClickListener() {
            @Override
            public void innerClick() {
                isAttention = list.get(position).isAttentioned();
                attentionStr = holder.btn_attention.getText().toString().trim();
                if (!isAttention) {
                    holder.myStateButton.setOnInnerStart();
                    AttentionMemberApi(holder, position, list.get(position).getMemberID(), 1);

                } else {
                    new BottomDialogUtil(context,
                            R.layout.dialog_bottom,
                            context.getString(R.string.attentionAndFans_hint1),
                            context.getString(R.string.attentionAndFans_hint2),
                            context.getString(R.string.attentionAndFans_hint3),
                            new BottomDialogUtil.BottonDialogListener() {
                                @Override
                                public void onItemListener() {
                                    holder.myStateButton.setOnInnerStart();
                                    AttentionMemberApi(holder, position, list.get(position).getMemberID(), 0);
                                }
                            });
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder.itemView, position, list.get(position).getMemberID());
                }
            }
        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_nickname;
        Button btn_attention;
        ImageView iv_touxiang,iv_v;
        MyStateButton myStateButton;

        public ViewHolder(View view) {
            super(view);
            tv_nickname = (TextView) view.findViewById(R.id.tv_nickname);
            myStateButton = view.findViewById(R.id.myStateButton);
            iv_touxiang = (ImageView) view.findViewById(R.id.iv_touxiang);
            btn_attention = view.findViewById(R.id.btn_attention);
            iv_v = view.findViewById(R.id.iv_v);
        }
    }


    /**
     * @param attentionedMemberID 被关注用户ID;
     * @param type                0:取消关注；1：关注
     */
    private void AttentionMemberApi(final ViewHolder holder, final int position, int attentionedMemberID, final int type) {
        AttentionMemberApi attentionMemberApi = new AttentionMemberApi((RxAppCompatActivity) context, new HttpOnNextListener<AttentionMemberBean>() {
            @Override
            public RetryWhenNetworkException.Wrapper onNext(AttentionMemberBean attentionMemberBean) {
                if (attentionMemberBean.getErrorCode() == 200) {
                    if (type == 0) {
                        holder.btn_attention.setBackgroundResource(R.drawable.shape_attention_pink_bg);
                        holder.btn_attention.setText(R.string.addAttention);
                        list.get(position).setAttentioned(false);

                    } else {
                        holder.btn_attention.setBackgroundResource(R.drawable.shape_be_attention_bg);
                        holder.btn_attention.setText(R.string.beAttention);
                        list.get(position).setAttentioned(true);

                    }
                } else {
                    holder.btn_attention.setText(attentionStr);
                }
                CustomToast.showToast( attentionMemberBean.getErrorMsg());
                holder.myStateButton.setOnInnerResult();
                EventBus.getDefault().post(new BusEvent(BusEvent.ATTENTION, true));
                return null;
            }

            @Override
            public RetryWhenNetworkException.Wrapper onError(Throwable e) {
                super.onError(e);
                holder.myStateButton.setOnInnerResult();
                holder.btn_attention.setText(attentionStr);
                return null;
            }
        });
        attentionMemberApi.setMemberID(attentionedMemberID);
        attentionMemberApi.setType(type);
        HttpManager.getInstance().doHttpDeal(context, attentionMemberApi);

    }


}
