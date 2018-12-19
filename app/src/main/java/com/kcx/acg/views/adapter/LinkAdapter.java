package com.kcx.acg.views.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.kcx.acg.R;

import java.util.List;

/**
 */

public class LinkAdapter extends DelegateAdapter.Adapter {

    private Context context;
    private List<String> list;
    private LayoutHelper layoutHelper;
    private String keyWord;

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord.replace(" ", "");
    }


    public LinkAdapter(Context context, List<String> list, LayoutHelper layoutHelper, String keyWord) {
        this.context = context;
        this.list = list;
        this.layoutHelper = layoutHelper;
//        this.keyWord = keyWord.replace(" ", "");
        this.keyWord = keyWord;
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TextHolder(LayoutInflater.from(context).inflate(R.layout.search_link_item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        TextHolder textHolder = (TextHolder) holder;
        if (list != null && list.size() > 0) {
            final String result = list.get(position);
            SpannableString spannableString = new SpannableString(result);
            ForegroundColorSpan span = new ForegroundColorSpan(ContextCompat.getColor(context, R.color.pink_ff8));
            spannableString.setSpan(span, result.toLowerCase().indexOf(keyWord.toLowerCase()), result.toLowerCase().indexOf(keyWord.toLowerCase()) + keyWord.toLowerCase().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            textHolder.linkTv.setText(spannableString);
            textHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClickListener.onItemClick(view, position, result);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    class TextHolder extends RecyclerView.ViewHolder {
        private TextView linkTv;

        public TextHolder(final View itemView) {
            super(itemView);
            linkTv = itemView.findViewById(R.id.search_link_item_tv);

        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position, String searchString);
    }
}
