package com.sktlab.android.base.ui;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentActivity;
import androidx.viewbinding.ViewBinding;

import com.sktlab.android.base.R;
import com.sktlab.android.base.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class BaseFragmentActivity<T extends ViewBinding> extends FragmentActivity {
    protected T binding;
    private AlertDialog loadingDialog;
    private AppCompatTextView loadingText;
    private ProgressBar loadingPb;

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
        loadingDialog = null;
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

    public void startActivity(Class<?> cls) {
        this.startActivity(cls, null);
    }

    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (null != bundle) {
            intent.putExtra("bundle", bundle);
        }
        super.startActivity(intent);
    }

    public void startActivityForResult(Class<?> cls, int requestCode) {
        this.startActivityForResult(cls, requestCode, null);
    }

    public void startActivityForResult(Class<?> cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent(this, cls);
        if (null != bundle) {
            intent.putExtra("bundle", bundle);
        }
        super.startActivityForResult(intent, requestCode);
    }


    public void showLoading() {
        showLoading(R.string.loading);
    }

    public void showLoading(@StringRes int messageId) {
        showLoading(messageId, -1);
    }

    public void showLoading(@StringRes int messageId, @ColorRes int colorRes) {
        if (loadingDialog == null || loadingText == null || loadingPb == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.dialog_loading, null);
            loadingText = view.findViewById(R.id.tv_loading);
            loadingPb = view.findViewById(R.id.pb_loading);
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setCancelable(false);
            loadingDialog = builder.create();
            loadingDialog.show();
            Window window = loadingDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = (int) Utils.dpToPx(this, 200);
                lp.height = (int) Utils.dpToPx(this, 180);
                window.setGravity(Gravity.CENTER);
                window.setAttributes(lp);
                window.setContentView(view);
            } else
                loadingDialog.setView(view);
        }
        loadingText.setText(messageId);
        if (colorRes != -1)
            loadingPb.setIndeterminateTintList(ColorStateList.valueOf(getColor(colorRes)));
        if (!loadingDialog.isShowing())
            loadingDialog.show();
    }

    public void dismissLoading() {
        if (isLoadingShowing())
            loadingDialog.dismiss();
    }

    public boolean isLoadingShowing() {
        return loadingDialog != null && loadingDialog.isShowing();
    }
}
