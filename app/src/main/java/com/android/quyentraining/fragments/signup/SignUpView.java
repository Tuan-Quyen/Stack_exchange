package com.android.quyentraining.fragments.signup;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

public interface SignUpView {
    void signUpFBSuccess(LoginResult loginResult);

    void signUpFBFail(String error);

    void hasConnection();

    void noConnection();

    void handleFacebookSuccess(FirebaseUser user, long now);

    void handleGoogleSuccess(GoogleSignInAccount account, String userID, String userName, long now);

    void handleFail();
}
