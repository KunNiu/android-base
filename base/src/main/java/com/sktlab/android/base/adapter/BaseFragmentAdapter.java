package com.sktlab.android.base.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import java.util.ArrayList;
import java.util.List;

public class BaseFragmentAdapter extends FragmentStateAdapter {
    private List<Fragment> fragments;
    private List<Long> ids = new ArrayList<>();

    public BaseFragmentAdapter(@NonNull Fragment fragment, List<Fragment> fragments) {
        super(fragment);
        this.fragments = fragments;
    }

    public BaseFragmentAdapter(@NonNull FragmentActivity fragmentActivity, @NonNull List<Fragment> fragments) {
        super(fragmentActivity);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        ids.add(getItemId(position));
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    @Override
    public long getItemId(int position) {
        return fragments.get(position).hashCode();
    }

    @Override
    public boolean containsItem(long itemId) {
        return ids.contains(itemId);
    }
}
