package com.kcx.acg.views.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.kcx.acg.R;

/**
 */

public class BottomDialog extends Dialog {
    private Context context;
    private TextView commitTv;
    private EditText contentEt;

    public BottomDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public BottomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected BottomDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_comment_bottom, null);
        setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        getWindow().setGravity(Gravity.BOTTOM);
        commitTv = contentView.findViewById(R.id.dialog_add_comment_tv);
        contentEt =contentView.findViewById(R.id.comment_dialog_et);
        commitTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCommitListener.onCommit(contentEt.getText().toString());
            }
        });
    }

    public void setDisable(){
        contentEt.setHint(context.getString(R.string.account_disable_1));
        contentEt.setEnabled(false);
        commitTv.setEnabled(false);
    }

    public void setReplyHint(String userNameHint){
        contentEt.setHint(userNameHint);
    }

    public void setCommitListener(OnCommitListener onCommitListener) {
        this.onCommitListener = onCommitListener;
    }

    private OnCommitListener onCommitListener;
    public interface OnCommitListener{
        void onCommit(String content);
    }
}
