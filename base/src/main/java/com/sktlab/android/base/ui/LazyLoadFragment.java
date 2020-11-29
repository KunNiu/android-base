package com.sktlab.android.base.ui;

import androidx.viewbinding.ViewBinding;

public abstract class LazyLoadFragment<T extends ViewBinding> extends BaseFragment<T> {
    protected boolean isFirstLoad = true;

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad) {
            loadData();
            isFirstLoad = false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstLoad = true;
    }

    protected void loadData() {
    }
}
