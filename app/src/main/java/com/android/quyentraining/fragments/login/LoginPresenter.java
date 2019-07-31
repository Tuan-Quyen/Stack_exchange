package com.android.quyentraining.fragments.login;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;

import com.android.quyentraining.connections.CheckConnection;
import com.android.quyentraining.ultis.AppConstain;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginPresenter {
    Context context;
    LoginView loginView;
    Activity activity;

    public LoginPresenter(Context context, Activity activity, LoginView loginView) {
        this.context = context;
        this.activity = activity;
        this.loginView = loginView;
    }

    public void facebookService(CallbackManager callbackManager) {
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                loginView.loginFBSuccess(loginResult);
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException error) {
                loginView.loginFBFail(error.getMessage());
            }
        });
    }

    public void checkConnection() {
        if (!CheckConnection.checkConnection(context)) {
            loginView.noConnection();
        } else {
            loginView.hasConnection();
        }
    }

    public void handleFacebookAccessToken(AccessToken token, final FirebaseAuth mAuth) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    long now = System.currentTimeMillis() / 1000;
                    loginView.handleFacebookSuccess(user, now);
                } else {
                    // If sign in fails, display a message to the user.
                    loginView.handleFail();
                }
            }
        });
    }

    public void firebaseAuthWithGoogle(final GoogleSignInAccount account, final FirebaseAuth mAuth) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userID = user.getUid();
                            String username = account.getDisplayName();
                            long now = System.currentTimeMillis() / 1000;
                            loginView.handleGoogleSuccess(account,userID,username,now);
                        } else {
                            // If sign in fails, display a message to the user.
                            loginView.handleFail();
                        }
                    }
                });
    }

}
