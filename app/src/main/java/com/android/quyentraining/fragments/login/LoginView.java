package com.android.quyentraining.fragments.login;

import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;

public interface LoginView {
    void loginFBSuccess(LoginResult loginResult);

    void loginFBFail(String error);

    void hasConnection();

    void noConnection();

    void handleFacebookSuccess(FirebaseUser user, long now);

    void handleGoogleSuccess(GoogleSignInAccount account, String userID, String userName, long now);

    void handleFail();
}
