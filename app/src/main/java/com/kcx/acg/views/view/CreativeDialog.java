package com.kcx.acg.views.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kcx.acg.R;

public class CreativeDialog extends Dialog {

    public CreativeDialog(@NonNull Context context) {
        super(context);
    }

    public CreativeDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public CreativeDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder implements View.OnClickListener {
        private CreativeDialog dialog;
        private Context context;
        private View view;
        private TextView titleTv;
        private TextView messageTv;
        private TextView confrimTv;
        private TextView cancelTv;

        public Builder(Context context) {
            this.context = context;
            dialog = new CreativeDialog(context, R.style.ActionSheetDialogStyle);
        }

        public CreativeDialog create(int layoutId, String... params) {
            view = LayoutInflater.from(context).inflate(layoutId, null);
            dialog.setContentView(view);
            ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
            layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
            view.setLayoutParams(layoutParams);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
            dialog.setCanceledOnTouchOutside(true);

            titleTv = view.findViewById(R.id.dialog_certification_title_tv);
            messageTv = view.findViewById(R.id.dialog_certification_message_tv);
            confrimTv = view.findViewById(R.id.dialog_certification_confirm_tv);
            cancelTv = view.findViewById(R.id.dialog_certification_cancel_tv);

            titleTv.setText(params[0]);
            messageTv.setText(params[1]);
            confrimTv.setText(params[2]);
            cancelTv.setText(params[3]);

            confrimTv.setOnClickListener(this);
            cancelTv.setOnClickListener(this);
            return dialog;

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.dialog_certification_confirm_tv:
                    onConfirmListener.onConfirm();
                    break;
                case R.id.dialog_certification_cancel_tv:
                    dialog.dismiss();
                    break;
            }
        }

        public CreativeDialog show() {
            return dialog;
        }
    }

    private static OnConfirmListener onConfirmListener;

    public void setOnConfirmListener(OnConfirmListener onConfirmListener) {
        this.onConfirmListener = onConfirmListener;
    }

    public interface OnConfirmListener {
        void onConfirm();
    }
}
