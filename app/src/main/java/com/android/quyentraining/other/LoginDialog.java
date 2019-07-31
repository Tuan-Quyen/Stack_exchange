package com.android.quyentraining.other;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quyentraining.R;
import com.android.quyentraining.activities.main.MainActivity;
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
import butterknife.OnTextChanged;

public class LoginDialog extends Dialog {
    @BindView(R.id.dialogloginse_btnLogin)
    TextView btnLogin;
    @BindView(R.id.dialogloginse_etEmail)
    EditText etEmail;
    @BindView(R.id.dialogloginse_etPass)
    EditText etPass;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDatabaseReference;
    Context mContext;

    public LoginDialog(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setContentView(R.layout.dialog_loginstackexchange);
        ButterKnife.bind(this);
    }

    @OnTextChanged({R.id.dialogloginse_etEmail, R.id.dialogloginse_etPass})
    public void textChange() {
        etEmail.setHintTextColor(getContext().getResources().getColor(R.color.colorDarkWhite));
        etPass.setHintTextColor(getContext().getResources().getColor(R.color.colorDarkWhite));
    }

    @OnClick(R.id.dialogloginse_btnLogin)
    public void onClickBtnLogin() {
        String email = etEmail.getText().toString();
        String pass = etPass.getText().toString();
        if (email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(getContext(), AppConstain.EMAIL_OR_PASS_IS_EMPTY, Toast.LENGTH_SHORT).show();
            etEmail.setHintTextColor(getContext().getResources().getColor(R.color.colorRed));
            etPass.setHintTextColor(getContext().getResources().getColor(R.color.colorRed));
        } else {
            final ProgressBarLoadMore progressBarLoadMore = new ProgressBarLoadMore(this.getContext());
            progressBarLoadMore.show();
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        String userID = user.getUid();
                        if (user.isEmailVerified()) {
                            long now = System.currentTimeMillis() / 1000;
                            firebaseDatabase = FirebaseDatabase.getInstance();
                            mDatabaseReference = firebaseDatabase.getReference().child(AppConstain.KEY_CHILD_USERS).child(userID).child(AppConstain.KEY_CHILD_TIME_ACTIVITY);
                            mDatabaseReference.setValue(now);
                            Intent intent = new Intent(getContext(), MainActivity.class);
                            getContext().startActivity(intent);
                            ((Activity) mContext).finish();
                            dismiss();
                        } else {
                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(getContext(), AppConstain.TOAST_MUST_VERIFIED_EMAIL, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                    progressBarLoadMore.hide();
                    progressBarLoadMore.dismiss();
                }
            });
        }
    }
}
