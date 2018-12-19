package com.kcx.acg.views.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.bean.MessageBean;
import com.kcx.acg.bean.ReadNoticeBean;
import com.kcx.acg.net.InterceptorUtil;
import com.kcx.acg.utils.InterceptUtil;
import com.kcx.acg.utils.LogUtil;
import com.kcx.acg.views.activity.WebViewActivity;
import com.kcx.acg.views.view.CustomUrlSpan;

import java.util.List;

/**
 * 消息通知
 * Created by jb on 2018/2/6.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private final Context context;
    private List<ReadNoticeBean.ReturnDataBean.ListBean> list;
    public OnItemClickListener onItemClickListener;

    public MessageAdapter(Context context, List<ReadNoticeBean.ReturnDataBean.ListBean> list) {
        this.context = context;
        this.list = list;
    }

    public void update(List<ReadNoticeBean.ReturnDataBean.ListBean> list) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message, parent, false);
        //实例化viewHolder
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.tv_title.setText(list.get(position).getNoticeTypeName());
        holder.tv_time.setText(list.get(position).getNoticeTime());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tv_content.setText(Html.fromHtml(list.get(position).getNoticeMessage(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tv_content.setText(Html.fromHtml(list.get(position).getNoticeMessage()));
        }
        InterceptUtil.interceptHyperLink(context, holder.tv_content);

        if (list.get(position).isNoticeRead()) {
            holder.mImageView.setVisibility(View.INVISIBLE);
        } else {
            holder.mImageView.setVisibility(View.VISIBLE);
        }

        //        holder.itemView.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View view) {
        //                if (onItemClickListener != null) {
        //                    onItemClickListener.onItemClick(holder.itemView, position);
        //                }
        //            }
        //        });

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title, tv_time, tv_content;
        ImageView mImageView;

        public ViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            mImageView = view.findViewById(R.id.mImageView);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
