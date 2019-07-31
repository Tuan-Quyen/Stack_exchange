package com.android.quyentraining.activities.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.android.quyentraining.R;
import com.android.quyentraining.activities.main.MainActivity;
import com.android.quyentraining.base.BaseActivity;
import com.android.quyentraining.fragments.login.LoginFragment;
import com.android.quyentraining.fragments.signup.SignUpFragment;
import com.android.quyentraining.ultis.AppDialog;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements LoginView {
    private LoginPresenter loginPresenter;
    private AppDialog appDialog = new AppDialog();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        loginPresenter = new LoginPresenter(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    closeKeyBoard(v);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @OnClick({R.id.actLogin_btnlogin, R.id.actLogin_btnsignup, R.id.actLogin_btnguestlogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.actLogin_btnlogin:
                addFragment(new LoginFragment(), true, R.id.actLogin_clContent);
                break;
            case R.id.actLogin_btnsignup:
                addFragment(new SignUpFragment(), true, R.id.actLogin_clContent);
                break;
            case R.id.actLogin_btnguestlogin:
                loginPresenter.guestionLogin(this);
        }
    }

    @Override
    public void guestLoginHasInternet() {
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
        this.finish();
    }

    @Override
    public void guestLoginNoInternet() {
        appDialog.connectionErrorDialog(this, null);
    }
}
