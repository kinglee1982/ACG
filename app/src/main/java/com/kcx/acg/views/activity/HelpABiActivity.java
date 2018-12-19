package com.kcx.acg.views.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kcx.acg.R;
import com.kcx.acg.base.BaseActivity;

import org.w3c.dom.Text;

import me.jessyan.autosize.internal.CancelAdapt;

/**
 * Created by jb on 2018/9/6.
 * A币帮助
 */
public class HelpABiActivity extends BaseActivity implements CancelAdapt {
    private TextView tv_title;
    private LinearLayout ll_back;

    @Override
    public View setInitView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_abi_help,null);
    }

    @Override
    public void initView() {
        super.initView();
        tv_title =findViewById(R.id.tv_title);
        tv_title.setText("A币帮助");
        ll_back =findViewById(R.id.ll_back);
    }

    @Override
    public void setListener() {
        super.setListener();
        ll_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_back:
                    finish();
                    break;
            }
    }
}
