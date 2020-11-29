package com.sktlab.android.base.event;


import androidx.annotation.NonNull;

import com.sktlab.android.base.result.Result;

public abstract class BaseEvent implements Result {
    Result result;

    public BaseEvent(@NonNull Result result) {
        this.result = result;
    }

    @Override
    public boolean isSuccess() {
        return result.isSuccess();
    }

    @Override
    public Code getCode() {
        return result.getCode();
    }

    @Override
    public Object getData() {
        return result.getData();
    }

    @Override
    public String getMessage() {
        return result.getMessage();
    }
}
