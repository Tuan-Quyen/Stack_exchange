package com.android.quyentraining.fragments.signupstackexchange;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quyentraining.R;
import com.android.quyentraining.base.BaseFragment;
import com.android.quyentraining.fragments.signup.SignUpPresenter;
import com.android.quyentraining.models.User;
import com.android.quyentraining.ultis.AppConstain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnTextChanged;

public class SignUpStackExchangeFragment extends BaseFragment implements SignUpStackExchangeView {
    @BindView(R.id.titleLogin_tv)
    TextView titleBar;
    @BindView(R.id.frsignup_etEmail)
    EditText etEmail;
    @BindView(R.id.frsignup_etName)
    EditText etName;
    @BindView(R.id.frsignup_etPass)
    EditText etPass;
    @BindView(R.id.frsignup_etRePass)
    EditText etRePass;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference;
    SignUpStackExchangePresenter signUpStackExchangePresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signupstackexchange, container, false);
        ButterKnife.bind(this, view);
        bindView();
        return view;
    }

    public void bindView() {
        signUpStackExchangePresenter = new SignUpStackExchangePresenter(this.getContext(), this);
        titleBar.setText(getString(R.string.btn_signuplowercase));
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @OnClick({R.id.titleLogin_imv, R.id.frsignup_btnCreate})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLogin_imv:
                getActivity().onBackPressed();
                break;
            case R.id.frsignup_btnCreate:
                String email = etEmail.getText().toString();
                final String name = etName.getText().toString();
                String pass = etPass.getText().toString();
                String repass = etRePass.getText().toString();
                signUpStackExchangePresenter.onClickSignUp(mAuth, email, name, pass, repass);
                break;
        }
    }

    @OnEditorAction(R.id.frsignup_etRePass)
    public boolean onClickETOK(int actionId, KeyEvent keyEvent) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                || keyEvent.getAction() == KeyEvent.ACTION_DOWN || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
            closeKeyBoard(this.getActivity().getCurrentFocus());
        }
        return false;
    }

    @OnTextChanged({R.id.frsignup_etRePass, R.id.frsignup_etEmail, R.id.frsignup_etPass, R.id.frsignup_etName})
    public void TextChange() {
        etRePass.setTextColor(getContext().getResources().getColor(R.color.colorDark));
        etEmail.setHintTextColor(getContext().getResources().getColor(R.color.colorDarkWhite));
        etPass.setHintTextColor(getContext().getResources().getColor(R.color.colorDarkWhite));
        etName.setHintTextColor(getContext().getResources().getColor(R.color.colorDarkWhite));
    }

    public void writeUserData(String userID, String name, Long lastLogin, String typeaccount) {
        User user = new User(lastLogin, name, typeaccount, lastLogin);
        mDatabaseReference = firebaseDatabase.getReference().child(AppConstain.KEY_CHILD_USERS).child(userID);
        mDatabaseReference.setValue(user);
    }

    @Override
    public void emptyEmailOrPass(String errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
        etEmail.setHintTextColor(getContext().getResources().getColor(R.color.colorRed));
        etPass.setHintTextColor(getContext().getResources().getColor(R.color.colorRed));
    }

    @Override
    public void emptyName(String errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
        etName.setHintTextColor(getContext().getResources().getColor(R.color.colorRed));
    }

    @Override
    public void repassNotMatch(String errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
        etRePass.setTextColor(getContext().getResources().getColor(R.color.colorRed));
    }

    @Override
    public void sendEmailSuccess(String successString) {
        Toast.makeText(getContext(), successString, Toast.LENGTH_SHORT).show();
        getActivity().onBackPressed();
    }

    @Override
    public void sendEmailFail(String errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void signUpSuccess(String userID, String name, long now, String accountType) {
        writeUserData(userID, name, now, accountType);
    }

    @Override
    public void signUpFail(String errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingProgress() {
        showLoading();
    }

    @Override
    public void hideLoadingProgress() {
        hideLoading();
    }
}
