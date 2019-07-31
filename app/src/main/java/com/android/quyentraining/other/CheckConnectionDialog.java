package com.android.quyentraining.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.android.quyentraining.R;
import com.android.quyentraining.interfaces.ClickConnectionDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CheckConnectionDialog extends Dialog {
    @BindView(R.id.dialogconnection_tvmain)
    TextView tv;
    ClickConnectionDialog clickConnectionDialog;
    @BindView(R.id.dialogconnection_tv)
    TextView tvTitle;

    public void setClickConnectionDialog(ClickConnectionDialog clickConnectionDialog) {
        this.clickConnectionDialog = clickConnectionDialog;
    }

    public CheckConnectionDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_checkconnection);
        ButterKnife.bind(this);
        setCanceledOnTouchOutside(false);
        String stringTitle = this.getContext().getString(R.string.tv_connectionError);
        String stringtext = this.getContext().getResources().getString(R.string.connectionError) + "\n" + this.getContext().getResources().getString(R.string.connectionError2);
        tvTitle.setText(stringTitle);
        tv.setText(stringtext);
    }

    @OnClick(R.id.dialogconnection_btnok)
    public void onClickOK() {
        dismiss();
        if (clickConnectionDialog != null) {
            clickConnectionDialog.setOnClickConnectionDialog();
        }
    }
}
