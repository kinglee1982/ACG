package com.kcx.acg.views.view;

import android.app.Dialog;
import android.content.Context;
import android.net.TrafficStats;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.bean.UpgradeInfoBean;
import com.kcx.acg.utils.DateUtil;
import com.kcx.acg.utils.FileSizeUtil;
import com.kcx.acg.utils.NetWorkSpeedUtils;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.logging.LogRecord;

public class OriginalDialog extends Dialog {

    public OriginalDialog(@NonNull Context context) {
        super(context);
    }

    public OriginalDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    public OriginalDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder implements View.OnClickListener {
        private OriginalDialog dialog;
        private Context context;
        private View view;
        private ImageView closeIv;
        private ImageView statusIv;
        private TextView speedTv;
        private TextView progressTv;
        private TextView titleTv;
        private TextView sizeTv;
        private TextView timeTv;
        private TextView retryTv;
        private ProgressBar progressBar;
        private LocalMedia media;

        public Builder(Context context) {
            this.context = context;
            dialog = new OriginalDialog(context, R.style.ActionSheetDialogStyle);

        }

        public OriginalDialog create(LocalMedia media) {
            this.media = media;
            view = LayoutInflater.from(context).inflate(R.layout.dialog_original, null);
            dialog.setContentView(view);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            closeIv = view.findViewById(R.id.dialog_original_close_iv);
            statusIv = view.findViewById(R.id.status_iv);
            titleTv = view.findViewById(R.id.title_tv);
            speedTv = view.findViewById(R.id.upload_speed_tv);
            progressTv = view.findViewById(R.id.compress_prg_tv);
            sizeTv = view.findViewById(R.id.file_size_tv);
            timeTv = view.findViewById(R.id.time_tv);
            retryTv = view.findViewById(R.id.retry_tv);
            progressBar = view.findViewById(R.id.pb_update);
            double fileSizeKb = FileSizeUtil.getFileOrFilesSize(media.getPath(), FileSizeUtil.SIZETYPE_KB);
            new NetWorkSpeedUtils(context, handler, fileSizeKb).start();

            sizeTv.setText(FileSizeUtil.getFileOrFilesSize(media.getPath(), FileSizeUtil.SIZETYPE_MB) + "M");
            closeIv.setOnClickListener(this);
            retryTv.setOnClickListener(this);
            return dialog;

        }

        public void setProgress(int progress) {
            progressBar.setProgress(progress);
            progressTv.setText(progress + "%");
            speedTv.setVisibility(View.VISIBLE);
            progressTv.setVisibility(View.GONE);
        }
        public void setCompressProgress(int progress) {
            progressBar.setProgress(progress);
            progressTv.setText(progress + "%");
            speedTv.setVisibility(View.GONE);
            progressTv.setVisibility(View.VISIBLE);
        }

        public void setTime(int secs) {
            timeTv.setVisibility(View.VISIBLE);
            timeTv.setText(DateUtil.getTime(secs));
        }

        public void setError() {
            retryTv.setVisibility(View.VISIBLE);
            timeTv.setVisibility(View.INVISIBLE);
            statusIv.setImageResource(R.mipmap.ycpop_image_suspend);
            titleTv.setText(context.getString(R.string.upload_failre_msg));
        }

        public void setSuccess() {
            statusIv.setImageResource(R.mipmap.ycpop_image_succeed);
            titleTv.setText(context.getString(R.string.upload_success_msg));
            timeTv.setText(context.getString(R.string.upload_success_hint_msg));
            timeTv.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);
            speedTv.setVisibility(View.INVISIBLE);
            progressTv.setVisibility(View.INVISIBLE);
            sizeTv.setVisibility(View.INVISIBLE);
        }

        public void setUploading() {
            statusIv.setImageResource(R.mipmap.ycpop_image_uploading);
            titleTv.setText(context.getString(R.string.original_uploading_msg));
            timeTv.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            speedTv.setVisibility(View.VISIBLE);
            sizeTv.setVisibility(View.VISIBLE);
            progressTv.setVisibility(View.GONE);
        }

        public void setTitle(String title) {
            titleTv.setText(title);
        }

        private Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                speedTv.setText(msg.obj.toString());
            }
        };

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.dialog_original_close_iv:
                    dialog.cancel();
                    break;
                case R.id.retry_tv:
                    retryTv.setVisibility(View.GONE);
                    setUploading();
                    onRetryListener.onRetry();
                    break;
            }
        }

        private OnRetryListener onRetryListener;

        public void setOnRetryListener(OnRetryListener onRetryListener) {
            this.onRetryListener = onRetryListener;
        }

        public interface OnRetryListener {
            void onRetry();
        }
    }
}
