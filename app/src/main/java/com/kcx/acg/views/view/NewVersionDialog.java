package com.kcx.acg.views.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.bean.UpgradeInfoBean;

/**
 */

public class NewVersionDialog extends Dialog {
    public NewVersionDialog(@NonNull Context context) {
        super(context);
    }

    public NewVersionDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected NewVersionDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder implements View.OnClickListener{
        private NewVersionDialog dialog;
        private View view;
        private Context context;
        private TextView versionNumberTv;
        private TextView versionInfoTv;
        private TextView updateTv;
        private TextView cancelTv;
        private TextView progressTv;
        private ProgressBar progressBar;

        public Builder(Context context) {
            this.context = context;
            dialog = new NewVersionDialog(context, R.style.ActionSheetDialogStyle);
        }

        public NewVersionDialog create(UpgradeInfoBean.ReturnDataBean version) {
            view = LayoutInflater.from(context).inflate(R.layout.dialog_new_version, null);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            versionNumberTv = view.findViewById(R.id.new_version_number_tv);
            versionInfoTv = view.findViewById(R.id.new_version_info_tv);
            updateTv = view.findViewById(R.id.new_version_update_tv);
            cancelTv = view.findViewById(R.id.new_version_cancel_tv);
            progressTv = view.findViewById(R.id.tv_progress);
            progressBar = view.findViewById(R.id.pb_update);

            versionNumberTv.setText("V"+version.getAppVersion());
            versionInfoTv.setText(version.getDescription().replace("\\n","\n"));
            if (version.isIsEnforceUpdate())
                cancelTv.setVisibility(View.GONE);
            else
                cancelTv.setVisibility(View.VISIBLE);

            updateTv.setOnClickListener(this);
            cancelTv.setOnClickListener(this);
            return dialog;
        }

        public void setProgress(int progress){
            progressTv.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            progressTv.setText(progress + "%");
            progressBar.setProgress(progress);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.new_version_update_tv:
                    updateTv.setEnabled(false);
                    onUpgradeListener.onUpgrade();
                    break;
                case R.id.new_version_cancel_tv:
                    dialog.cancel();
                    break;
            }
        }

        public void setOnUpgradeListener(OnUpgradeListener onUpgradeListener) {
            this.onUpgradeListener = onUpgradeListener;
        }

        private OnUpgradeListener onUpgradeListener;

        public interface OnUpgradeListener{
            void onUpgrade();
        }
    }



}
