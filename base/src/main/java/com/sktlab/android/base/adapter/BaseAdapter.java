package com.sktlab.android.base.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

public abstract class BaseAdapter<T extends ViewBinding> extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder<T>> {
    @NonNull
    @Override
    public BaseViewHolder<T> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        T binding = getBinding(inflater, parent);
        return new BaseViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
//        holder.setIsRecyclable(false);
        onBindViewHolder((T) holder.binding, position);
    }

    protected abstract T getBinding(LayoutInflater inflater, @NonNull ViewGroup parent);

    protected abstract void onBindViewHolder(T binding, int position);

    public static class BaseViewHolder<T extends ViewBinding> extends RecyclerView.ViewHolder {
        T binding;

        public BaseViewHolder(T binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
