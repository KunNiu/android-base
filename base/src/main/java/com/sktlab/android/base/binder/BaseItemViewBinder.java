package com.sktlab.android.base.binder;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.List;

import me.drakeet.multitype.ItemViewBinder;

public abstract class BaseItemViewBinder<T extends ViewBinding, ITEM> extends ItemViewBinder<ITEM, BaseItemViewBinder.BaseViewHolder<T>> {

    @NonNull
    @Override
    protected BaseViewHolder<T> onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        T binding = getBinding(inflater, parent);
        return new BaseViewHolder<>(binding);
    }

    @Override
    final protected void onBindViewHolder(@NonNull BaseViewHolder<T> holder, @NonNull ITEM item) {
        onBindViewHolder(holder.binding, item, holder.getAbsoluteAdapterPosition());
    }

    @Override
    final protected void onBindViewHolder(@NonNull BaseViewHolder<T> holder, @NonNull ITEM item, @NonNull List<Object> payloads) {
        if (payloads.isEmpty())
            onBindViewHolder(holder, item);
        else
            onBindViewHolder(holder.binding, item, holder.getAbsoluteAdapterPosition(), payloads);
    }

    protected abstract T getBinding(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent);

    protected abstract void onBindViewHolder(@NonNull T binding, @NonNull ITEM item, int position);

    protected void onBindViewHolder(@NonNull T binding, @NonNull ITEM item, int position, @NonNull List<Object> payloads) {
    }

    static class BaseViewHolder<T extends ViewBinding> extends RecyclerView.ViewHolder {
        T binding;

        public BaseViewHolder(T binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
