package com.sktlab.android.base.action;


import androidx.annotation.Nullable;

import com.sktlab.android.base.Callback;
import com.sktlab.android.base.event.BaseEvent;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseAction {
    public void execute() {
        execute(null);
    }

    public abstract void execute(@Nullable Callback callback);

    public void post(BaseEvent event) {
        EventBus.getDefault().post(event);
    }

    public void postSticky(BaseEvent event) {
        EventBus.getDefault().postSticky(event);
    }
}
