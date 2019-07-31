package com.android.quyentraining.fragments.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quyentraining.R;
import com.android.quyentraining.activities.main.MainActivity;
import com.android.quyentraining.base.BaseFragment;
import com.android.quyentraining.connections.CheckConnection;
import com.android.quyentraining.interfaces.ClickConnectionDialog;
import com.android.quyentraining.models.User;
import com.android.quyentraining.ultis.AppConstain;
import com.android.quyentraining.ultis.AppDialog;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener, ClickConnectionDialog, LoginView {
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDataBaseReference;
    private int RC_SIGN_IN = 001;
    @BindView(R.id.titleLogin_tv)
    TextView titleBar;
    @BindView(R.id.frlogin_sign_in_button)
    SignInButton btnLoginGG;
    String typeAccount;
    boolean loginGG;
    AppDialog appDialog = new AppDialog();
    LoginPresenter loginPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ButterKnife.bind(this, view);
        bindView();
        return view;
    }

    public void bindView() {
        loginPresenter = new LoginPresenter(this.getContext(), this.getActivity(), this);
        titleBar.setText(getString(R.string.btn_loginlowercase));
        btnLoginGG.setSize(SignInButton.SIZE_STANDARD);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        loginPresenter.checkConnection();
    }

    @OnClick({R.id.titleLogin_imv, R.id.frlogin_btnloginse, R.id.frlogin_sign_in_button, R.id.frlogin_btnloginfb,R.id.frlogin_btnloginopenid})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.titleLogin_imv:
                getActivity().onBackPressed();
                break;
            case R.id.frlogin_btnloginse:
                appDialog.loginDialog(this.getContext());
                break;
            case R.id.frlogin_sign_in_button:
                loginGG = true;
                signIn();
                break;
            case R.id.frlogin_btnloginfb:
                loginGG = false;
                callbackManager = CallbackManager.Factory.create();
                loginPresenter.facebookService(callbackManager);
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(AppConstain.GET_FB_DATA_PUBLIC_PROFILE, AppConstain.GET_FB_DATA_USER_FRIEND, AppConstain.GET_FB_DATA_EMAIL));
                break;
            case R.id.frlogin_btnloginopenid:
                appDialog.loginOpenID(this.getContext());
                break;
        }
    }

    public void googleService() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.token_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this.getContext(), gso);
    }

    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (loginGG) {
            // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    loginPresenter.firebaseAuthWithGoogle(account, mAuth);

                } catch (ApiException e) {
                    updateUI(null);
                }
            }
        } else {
            //login with fb
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void addUserFireBase(String userID, String username, String type, long timeactivity) {
        mDataBaseReference = firebaseDatabase.getReference().child(AppConstain.KEY_CHILD_USERS).child(userID).child(AppConstain.KEY_CHILD_USERNAME);
        mDataBaseReference.setValue(username);
        mDataBaseReference = firebaseDatabase.getReference().child(AppConstain.KEY_CHILD_USERS).child(userID).child(AppConstain.KEY_CHILD_TIME_ACTIVITY);
        mDataBaseReference.setValue(timeactivity);
        mDataBaseReference = firebaseDatabase.getReference().child(AppConstain.KEY_CHILD_USERS).child(userID).child(AppConstain.KEY_CHILD_TYPEACCOUNT);
        mDataBaseReference.setValue(type);
    }

    public void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            Intent intent = new Intent(this.getContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void setOnClickConnectionDialog() {
        this.getActivity().onBackPressed();
    }

    @Override
    public void loginFBSuccess(LoginResult loginResult) {
        loginPresenter.handleFacebookAccessToken(loginResult.getAccessToken(), mAuth);
    }

    @Override
    public void loginFBFail(String error) {
        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hasConnection() {
        googleService();
    }

    @Override
    public void noConnection() {
        appDialog.connectionErrorDialog(this.getContext(), this);
    }

    @Override
    public void handleFacebookSuccess(FirebaseUser user, long now) {
        typeAccount = AppConstain.TYPE_ACCOUNT_FACEBOOK;
        addUserFireBase(user.getUid(), user.getDisplayName(), typeAccount, now);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void handleGoogleSuccess(GoogleSignInAccount account, String userID, String userName, long now) {
        typeAccount = AppConstain.TYPE_ACCOUNT_GOOGLE;
        addUserFireBase(userID, userName, typeAccount, now);
        updateUI(account);
    }

    @Override
    public void handleFail() {
        updateUI(null);
    }
}
