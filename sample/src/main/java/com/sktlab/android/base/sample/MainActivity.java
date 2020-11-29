package com.sktlab.android.base.sample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sktlab.android.base.sample.databinding.ActivityMainBinding;
import com.sktlab.android.base.ui.BaseActivity;
import com.sktlab.android.base.util.Utils;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showLoading();
    }

    @Override
    protected ActivityMainBinding getBinding() {
        return ActivityMainBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void setListeners() {

    }
}