package com.android.quyentraining.fragments.signupstackexchange;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.quyentraining.ultis.AppConstain;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpStackExchangePresenter {
    Context context;
    SignUpStackExchangeView signUpStackExchangeView;

    public SignUpStackExchangePresenter(Context context, SignUpStackExchangeView signUpStackExchangeView) {
        this.context = context;
        this.signUpStackExchangeView = signUpStackExchangeView;
    }

    public void onClickSignUp(final FirebaseAuth mAuth, String email, final String name, String pass, String repass) {
        if (email.isEmpty() || pass.isEmpty()) {
            signUpStackExchangeView.emptyEmailOrPass(AppConstain.EMAIL_OR_PASS_IS_EMPTY);
        } else if (name.isEmpty()) {
            signUpStackExchangeView.emptyName(AppConstain.NAME_IS_EMPTY);
        } else {
            if (!pass.equals(repass)) {
                signUpStackExchangeView.repassNotMatch(AppConstain.PASS_NOT_MATCH);
            } else {
                mAuth.createUserWithEmailAndPassword(email, repass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        signUpStackExchangeView.showLoadingProgress();
                        if (task.isSuccessful()) {
                            final FirebaseUser user = mAuth.getCurrentUser();
                            String userID = user.getUid();
                            long now = System.currentTimeMillis() / 1000;
                            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        signUpStackExchangeView.sendEmailSuccess(AppConstain.SUCCESS_EMAIL_VERIFY + user.getEmail());
                                    } else {
                                        signUpStackExchangeView.sendEmailFail(task.getException().getMessage());
                                    }
                                }
                            });
                            signUpStackExchangeView.signUpSuccess(userID, name, now, AppConstain.TYPE_ACCOUNT_SE);

                        } else {
                            signUpStackExchangeView.signUpFail(task.getException().getMessage());
                        }
                        signUpStackExchangeView.hideLoadingProgress();
                    }
                });
            }
        }
    }
}
