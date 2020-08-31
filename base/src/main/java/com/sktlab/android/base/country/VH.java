package com.sktlab.android.base.country;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.sktlab.android.base.R;

class VH extends RecyclerView.ViewHolder {

    TextView tvName;
    TextView tvCode;
    ImageView ivFlag;

    VH(View itemView) {
        super(itemView);
        ivFlag = (ImageView) itemView.findViewById(R.id.iv_flag);
        tvName = (TextView) itemView.findViewById(R.id.tv_name);
        tvCode = (TextView) itemView.findViewById(R.id.tv_code);
    }
}
