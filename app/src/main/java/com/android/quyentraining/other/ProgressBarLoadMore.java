package com.android.quyentraining.other;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.android.quyentraining.R;

public class ProgressBarLoadMore extends Dialog {
    public ProgressBarLoadMore(Context context) {
        super(context);
        onCreate(context);
    }
    public void onCreate(Context context){
        View view = LayoutInflater.from(context).inflate(R.layout.progress_bar,null);
        setContentView(view);
        setCanceledOnTouchOutside(false);
        this.getWindow().setBackgroundDrawableResource(R.color.colorTrans);
        this.getWindow().setDimAmount((float) 0.4);
    }
}
