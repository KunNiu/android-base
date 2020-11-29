package com.sktlab.android.base.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewbinding.ViewBinding;

import com.sktlab.android.base.R;
import com.sktlab.android.base.util.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class BaseFragment<T extends ViewBinding> extends Fragment {
    protected static String TAG;
    protected T binding;
    private AlertDialog loadingDialog;
    private AppCompatTextView loadingText;
    private ProgressBar loadingPb;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        TAG = this.getClass().getSimpleName();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = getBinding(inflater, container);
            EventBus.getDefault().register(this);
            createView();
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setListeners();
        viewCreated();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        binding = null;
        super.onDestroyView();
    }

    protected abstract T getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    protected void createView() {
    }

    protected void viewCreated() {
    }

    protected abstract void setListeners();

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event) {
    }

    public void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    public void toast(@StringRes int resId) {
        Toast.makeText(getContext(), getString(resId), Toast.LENGTH_LONG).show();
    }

    @ColorInt
    public int getColor(@ColorRes int id) {
        return getContext().getColor(id);
    }

    public void replace(BaseActivity activity, @IdRes int resId, boolean addToBackStack) {
        FragmentTransaction transaction;
        transaction = activity.getSupportFragmentManager().beginTransaction();
        transaction.replace(resId, this, TAG);
        if (addToBackStack)
            transaction.addToBackStack(TAG);
        transaction.commit();
    }

    public void startActivity(Class<?> cls) {
        this.startActivity(cls, null);
    }

    public void startActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(getActivity(), cls);
        if (null != bundle) {
            intent.putExtra("bundle", bundle);
        }
        super.startActivity(intent);
    }

    public void startActivityForResult(Class<?> cls, int requestCode) {
        this.startActivityForResult(cls, requestCode, null);
    }

    public void startActivityForResult(Class<?> cls, int requestCode, Bundle bundle) {
        Intent intent = new Intent(getActivity(), cls);
        if (null != bundle) {
            intent.putExtra("bundle", bundle);
        }
        super.startActivityForResult(intent, requestCode);
    }

    public Bundle getBundle() {
        return getActivity().getIntent().getBundleExtra("bundle");
    }

    public void showLoading() {
        showLoading(R.string.loading);
    }

    public void showLoading(@StringRes int messageId) {
        showLoading(messageId, -1);
    }

    public void showLoading(@StringRes int messageId, @ColorRes int colorRes) {
        if (loadingDialog == null || loadingText == null || loadingPb == null) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_loading, null);
            loadingText = view.findViewById(R.id.tv_loading);
            loadingPb = view.findViewById(R.id.pb_loading);
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                    .setCancelable(false);
            loadingDialog = builder.create();
            loadingDialog.show();
            Window window = loadingDialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams lp = window.getAttributes();
                lp.width = (int) Utils.dpToPx(getContext(), 200);
                lp.height = (int) Utils.dpToPx(getContext(), 180);
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

    public void finishActivity() {
        requireActivity().finish();
    }
}
