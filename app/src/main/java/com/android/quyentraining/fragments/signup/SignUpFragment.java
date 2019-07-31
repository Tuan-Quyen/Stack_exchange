package com.android.quyentraining.fragments.signup;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quyentraining.R;
import com.android.quyentraining.activities.main.MainActivity;
import com.android.quyentraining.base.BaseFragment;
import com.android.quyentraining.fragments.signupstackexchange.SignUpStackExchangeFragment;
import com.android.quyentraining.interfaces.ClickConnectionDialog;
import com.android.quyentraining.models.User;
import com.android.quyentraining.ultis.AppConstain;
import com.android.quyentraining.ultis.AppDialog;
import com.facebook.CallbackManager;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpFragment extends BaseFragment implements GoogleApiClient.OnConnectionFailedListener, ClickConnectionDialog, SignUpView {
    @BindView(R.id.titleLogin_tv)
    TextView titleBar;
    @BindView(R.id.frsignup_btnsignupgoogle)
    SignInButton btnSignUpGG;
    @BindView(R.id.notifyLogin)
    TextView tvNotify;
    boolean LoginGG;
    private GoogleSignInClient mGoogleSignInClient;
    private CallbackManager callbackManager;
    private int RC_SIGN_IN = 001;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDataBaseReference;
    AppDialog appDialog = new AppDialog();
    SignUpPresenter signUpPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        bindView();
        return view;
    }

    public void bindView() {
        signUpPresenter = new SignUpPresenter(this.getContext(),this.getActivity(),this);
        titleBar.setText(getString(R.string.btn_signuplowercase));
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        btnSignUpGG.setSize(SignInButton.SIZE_STANDARD);
        signUpPresenter.checkConnection();
    }

    @OnClick({R.id.frsignup_tvlinkpolicy, R.id.frsignup_tvlinkterm, R.id.titleLogin_imv, R.id.frsignup_btnsignupse, R.id.frsignup_btnsignupgoogle,R.id.frsignup_btnsignupfb})
    public void onClick(View view) {
        Intent polyIntent;
        switch (view.getId()) {
            case R.id.frsignup_tvlinkpolicy:
                String policylink = AppConstain.POLYCY_LINK;
                polyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(policylink));
                startActivity(polyIntent);
                break;
            case R.id.frsignup_tvlinkterm:
                String termlink = AppConstain.TERM_OF_USE;
                polyIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(termlink));
                startActivity(polyIntent);
                break;
            case R.id.titleLogin_imv:
                getActivity().onBackPressed();
                break;
            case R.id.frsignup_btnsignupse:
                replaceFragment(new SignUpStackExchangeFragment(), true, R.id.actLogin_clContent);
                break;
            case R.id.frsignup_btnsignupgoogle:
                onClickSignUpGoogle();
                break;
            case R.id.frsignup_btnsignupfb:
                LoginGG = false;
                callbackManager = CallbackManager.Factory.create();
                signUpPresenter.facebookService(callbackManager);
                LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(AppConstain.GET_FB_DATA_PUBLIC_PROFILE, AppConstain.GET_FB_DATA_USER_FRIEND, AppConstain.GET_FB_DATA_EMAIL));
                break;
        }
    }

    public void onClickSignUpGoogle() {
        LoginGG = true;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this.getContext());
        if (account != null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    tvNotify.setVisibility(View.VISIBLE);
                }
            }, 2000);
        } else {
            signIn();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (LoginGG) {
            // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            if (requestCode == RC_SIGN_IN) {
                // The Task returned from this call is always completed, no need to attach
                // a listener.
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    GoogleSignInAccount account = task.getResult(ApiException.class);
                    signUpPresenter.firebaseAuthWithGoogle(account,mAuth);

                } catch (ApiException e) {
                    updateUI(null);
                }
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }


    public void addUserFireBase(String userID, String username, long timeactivity, String typeaccount) {
        User user = new User(timeactivity, username, typeaccount,timeactivity);
        mDataBaseReference = firebaseDatabase.getReference().child(AppConstain.KEY_CHILD_USERS).child(userID);
        mDataBaseReference.setValue(user);
    }

    public void updateUI(GoogleSignInAccount account) {
        if (account != null) {
            Intent intent = new Intent(this.getContext(), MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(getContext(), connectionResult.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setOnClickConnectionDialog() {
        this.getActivity().onBackPressed();
    }

    @Override
    public void signUpFBSuccess(LoginResult loginResult) {
        signUpPresenter.handleFacebookAccessToken(loginResult.getAccessToken(),mAuth);
    }

    @Override
    public void signUpFBFail(String error) {
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
        addUserFireBase(user.getUid(), user.getDisplayName(), now, AppConstain.TYPE_ACCOUNT_FACEBOOK);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void handleGoogleSuccess(GoogleSignInAccount account, String userID, String userName, long now) {
        addUserFireBase(userID, userName, now, AppConstain.TYPE_ACCOUNT_GOOGLE);
        updateUI(account);
    }

    @Override
    public void handleFail() {
        updateUI(null);
    }
}
