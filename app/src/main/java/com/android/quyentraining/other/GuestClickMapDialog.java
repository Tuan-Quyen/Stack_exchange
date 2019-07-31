package com.android.quyentraining.other;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.android.quyentraining.R;
import com.android.quyentraining.activities.login.LoginActivity;
import com.android.quyentraining.ultis.AppConstain;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GuestClickMapDialog extends Dialog {
    @BindView(R.id.dialog_save_delete_item_tv)
    TextView tvTitle;
    @BindView(R.id.dialog_save_delete_item_tvmain)
    TextView textView;
    @BindView(R.id.dialog_save_delete_item_btnOK)
    TextView btnLogin;
    Context mContext;
    public GuestClickMapDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_save_delete_item);
        ButterKnife.bind(this);
        tvTitle.setText(AppConstain.TITLE_DIALOG_GUEST_CLICK_TITLE);
        textView.setText(AppConstain.TITLE_DIALOG_GUEST_CLICK);
        btnLogin.setText(AppConstain.LOGIN);
    }
    @OnClick(R.id.dialog_save_delete_item_btnCancel)
    public void onClickCancel(){
        dismiss();
    }
    @OnClick(R.id.dialog_save_delete_item_btnOK)
    public void onClickLogin(){
        Intent intent = new Intent(getContext(),LoginActivity.class);
        getContext().startActivity(intent);
        ((Activity) mContext).finish();
    }
}
