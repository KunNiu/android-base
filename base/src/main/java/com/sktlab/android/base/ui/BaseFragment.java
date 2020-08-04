package com.sktlab.android.base.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment<T extends ViewBinding> extends Fragment {
    protected T binding;
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = getBinding(inflater, container);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, binding.getRoot());
        createView();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewCreated();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
        binding = null;
        super.onDestroyView();
    }

    protected abstract T getBinding(@NonNull LayoutInflater inflater, @Nullable ViewGroup container);

    protected void createView() {
    }

    protected void viewCreated() {
    }

    @SuppressWarnings("unused")
    @Subscribe
    public void onEvent(Object event) {
    }

    public void toast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }

    @ColorInt
    public int getColor(@ColorRes int id) {
        return getContext().getColor(id);
    }
}
