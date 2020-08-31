package com.sktlab.android.base.result;


import androidx.annotation.StringRes;

import com.sktlab.android.base.R;

public interface Result {
    class Code {
        public static final int UNKNOWN = -7000;
        public static final int SUCCESS = 7000;
        public static final int FAILED = 7001;
        private int code;

        protected Code(int code) {
            this.code = code;
        }

        public int getCode() {
            return code;
        }

        @StringRes
        public int getStringRes() {
            switch (code) {
                case SUCCESS:
                    return R.string.success;
                case FAILED:
                    return R.string.failed;
                default:
                    return R.string.unknown;
            }
        }
    }

    boolean isSuccess();

    Object getData();

    Code getCode();
}
