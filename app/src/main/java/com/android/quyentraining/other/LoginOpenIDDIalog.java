package com.android.quyentraining.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.android.quyentraining.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginOpenIDDIalog extends Dialog {
    @BindView(R.id.dialogconnection_tvmain)
    TextView tv;
    @BindView(R.id.dialogconnection_tv)
    TextView tvTitle;

    public LoginOpenIDDIalog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_checkconnection);
        ButterKnife.bind(this);
        String stringTitle = this.getContext().getString(R.string.btn_openidlogin);
        String stringtext = this.getContext().getResources().getString(R.string.error_openid) + "\n" + this.getContext().getResources().getString(R.string.error_openid2);
        tvTitle.setText(stringTitle);
        tv.setText(stringtext);
    }
    @OnClick(R.id.dialogconnection_btnok)
    public void onClick(){
        dismiss();
    }
}
