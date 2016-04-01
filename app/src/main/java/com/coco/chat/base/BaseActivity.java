package com.coco.chat.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by coco.zhou on 2016/3/28.
 */
public abstract class BaseActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewResId());
        initView();
        initData();
        initEvent();
    }

    protected abstract int getContentViewResId();

    protected abstract void initView();

    protected abstract void initEvent();

    protected abstract void initData();
}
