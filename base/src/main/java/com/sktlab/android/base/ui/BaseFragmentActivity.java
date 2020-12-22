package com.sktlab.android.base.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.FragmentActivity;
import androidx.viewbinding.ViewBinding;

import com.sktlab.android.base.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class BaseFragmentActivity<T extends ViewBinding> extends FragmentActivity {
    protected T binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        binding = getBinding();
        setContentView(binding.getRoot());
        EventBus.getDefault().register(this);
        setListeners();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    protected void setStatusBar() {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().setStatusBarColor(getStatusBarColor());

        if (Utils.isLightColor(getStatusBarColor())) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
        }
    }

    protected abstract T getBinding();

    protected abstract void setListeners();

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event) {
    }

    public void toast(String message) {
        toast(message, Toast.LENGTH_LONG);
    }

    public void toast(@StringRes int resId) {
        toast(resId, Toast.LENGTH_LONG);
    }

    public void toast(String message, int duration) {
        Toast.makeText(this, message, duration).show();
    }

    public void toast(@StringRes int resId, int duration) {
        Toast.makeText(this, getString(resId), duration).show();
    }

    protected @ColorInt
    int getStatusBarColor() {
        return Color.WHITE;
    }

    public Bundle getBundle() {
        return getIntent().getBundleExtra("bundle");
    }
}
