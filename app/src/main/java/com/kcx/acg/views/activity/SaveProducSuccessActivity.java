package com.kcx.acg.views.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;

/**
 * Created by zjb on 2018/11/30.
 */
public class SaveProducSuccessActivity extends BaseActivity {
    private LinearLayout ll_back;
    private TextView tv_title;
    private Button btn;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_save_produc_success,null);
    }

    @Override
    public void initView() {
        super.initView();
        ll_back = findViewById(R.id.ll_back);
        ll_back.setVisibility(View.INVISIBLE);
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText(R.string.contribute_title);
        btn =findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}
