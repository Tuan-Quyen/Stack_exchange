package com.android.quyentraining.activities.main;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;

import com.android.quyentraining.connections.APIConnection;
import com.android.quyentraining.connections.CheckConnection;
import com.android.quyentraining.connections.ConnectionRequest;
import com.android.quyentraining.models.site.ItemSiteModels;
import com.android.quyentraining.models.site.YourSiteModels;
import com.android.quyentraining.ultis.AppDialog;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MainPresenter {
    Context context;
    MainView mainView;
    private boolean isSignInWithGoogle = false, isSignInWithFB = false;

    public MainPresenter(Context context, MainView mainView) {
        this.context = context;
        this.mainView = mainView;
    }

    public void bindUser(FirebaseUser user) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (account != null) {//google
            mainView.bindUser(user, false);
            isSignInWithGoogle = true;
        } else if (accessToken != null) { //facebook
            mainView.bindUser(user, false);
            isSignInWithFB = true;
        } else if (user != null) { //stack exchange account
            mainView.bindUser(user, true);
        } else {
            mainView.bindUser(null, false);
        }
    }

    public void signOut(final GoogleSignInClient mGoogleSignInClient) {
        if (isSignInWithFB) {
            LoginManager.getInstance().logOut();
            AccessToken.setCurrentAccessToken(null);
        } else if (isSignInWithGoogle) {
            mGoogleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    mGoogleSignInClient.revokeAccess();
                }
            });
        }
        mainView.signOut();
    }

    public void addNewItemNav() {
        APIConnection apiNavigation = ConnectionRequest.getRetrofit().create(APIConnection.class);
        Observable<ItemSiteModels> getItem = apiNavigation.get7Size().subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        getItem.subscribe(new Observer<ItemSiteModels>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ItemSiteModels itemSiteModels) {
                ArrayList<YourSiteModels> siteArrayList = new ArrayList<>();
                siteArrayList.addAll(itemSiteModels.getItems());
                mainView.addNewItemNavigation(siteArrayList);
            }

            @Override
            public void onError(Throwable e) {
                mainView.addNavigationError(e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void clickLocationMap(AppDialog appDialog, DrawerLayout drawerLayout, FirebaseUser user) {
        if (!CheckConnection.checkConnection(context)) {
            appDialog.connectionErrorDialog(context, null);
        } else {
            if (user != null) {
                mainView.onClickMapLocation();
            } else {
                appDialog.guestDialogClickMap(context);
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }
}
