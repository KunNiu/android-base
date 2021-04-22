package com.sktlab.android.base.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
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
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.viewbinding.ViewBinding;

import com.sktlab.android.base.R;
import com.sktlab.android.base.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.ref.WeakReference;

public abstract class BaseActivity<T extends ViewBinding> extends AppCompatActivity {
    protected T binding;
    private AlertDialog loadingDialog;
    private AppCompatTextView loadingText;
    private ProgressBar loadingPb;
    private WeakReference<ResultCallback> resultCallbackWeak;

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

    public void replace(BaseFragment fragment, boolean addToBackStack) {
        if (getContainerId() != -1)
            fragment.replace(this, getContainerId(), addToBackStack);
    }

    public void replace(BaseFragment fragment) {
        replace(fragment, true);
    }

    protected @IdRes
    int getContainerId() {
        return -1;
    }

    protected @ColorInt
    int getStatusBarColor() {
        return Color.WHITE;
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

    public void startActivityForResult(@SuppressLint("UnknownNullness") Intent intent, int requestCode, @Nullable ResultCallback resultCallback) {
        if (resultCallback != null) {
            if (resultCallbackWeak != null) {
                resultCallbackWeak.clear();
            }
            resultCallbackWeak = new WeakReference<>(resultCallback);
        }
        super.startActivityForResult(intent, requestCode);
    }

    public void startActivityForResult(Class<?> cls, int requestCode) {
        this.startActivityForResult(cls, requestCode, null);
    }

    public void startActivityForResult(Class<?> cls, int requestCode, Bundle bundle) {
        this.startActivityForResult(cls, requestCode, bundle, null);
    }

    public void startActivityForResult(Class<?> cls, int requestCode, Bundle bundle, @Nullable ResultCallback resultCallback) {
        if (resultCallback != null) {
            if (resultCallbackWeak != null) {
                resultCallbackWeak.clear();
            }
            resultCallbackWeak = new WeakReference<>(resultCallback);
        }
        Intent intent = new Intent(this, cls);
        if (null != bundle) {
            intent.putExtra("bundle", bundle);
        }
        super.startActivityForResult(intent, requestCode);
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, @Nullable @org.jetbrains.annotations.Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, @Nullable ResultCallback resultCallback) throws IntentSender.SendIntentException {
        this.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, null, resultCallback);
    }

    public void startIntentSenderForResult(IntentSender intent, int requestCode, @Nullable @org.jetbrains.annotations.Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, @Nullable @org.jetbrains.annotations.Nullable Bundle options, @Nullable ResultCallback resultCallback) throws IntentSender.SendIntentException {
        if (resultCallback != null) {
            if (resultCallbackWeak != null) {
                resultCallbackWeak.clear();
            }
            resultCallbackWeak = new WeakReference<>(resultCallback);
        }
        super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCallbackWeak != null && resultCallbackWeak.get() != null) {
            resultCallbackWeak.get().onResult(requestCode, resultCode, data);
        }
    }

    public Bundle getBundle() {
        return getIntent().getBundleExtra("bundle");
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

    public interface ResultCallback {
        void onResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data);
    }
}
