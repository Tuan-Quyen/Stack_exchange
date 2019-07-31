package com.android.quyentraining.activities.main;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quyentraining.R;
import com.android.quyentraining.activities.splashscreen.SplashScreenActivity;
import com.android.quyentraining.adapters.NavigationItemAdapter;
import com.android.quyentraining.base.BaseActivity;
import com.android.quyentraining.fragments.allsite.AllSiteFragment;
import com.android.quyentraining.fragments.map.MapFragment;
import com.android.quyentraining.fragments.question.QuestionFragment;
import com.android.quyentraining.interfaces.ItemAllSiteCallBack;
import com.android.quyentraining.interfaces.ItemFeedClickBackMainListener;
import com.android.quyentraining.interfaces.ItemTitleDialogCallBack;
import com.android.quyentraining.interfaces.ItemYourSiteListener;
import com.android.quyentraining.models.User;
import com.android.quyentraining.models.site.YourSiteModels;
import com.android.quyentraining.ultis.AppConstain;
import com.android.quyentraining.ultis.AppDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity implements ItemYourSiteListener, ItemFeedClickBackMainListener,
        ItemAllSiteCallBack, ItemTitleDialogCallBack, MainView {
    @BindViews({R.id.titleHome_tvTitleSite, R.id.titleHome_tvTitleDialog, R.id.navHome_tvLogin})
    List<TextView> tvList;
    @BindViews({R.id.titleHome_imvDropDownTitleDialog, R.id.navHome_imvLogin})
    List<ImageView> imvList;
    @BindViews({R.id.actHome_btnTitleDialog, R.id.navHome_btnSignout, R.id.navHome_btnLogin})
    List<ConstraintLayout> clList;
    @BindView(R.id.actHome_drawer)
    DrawerLayout drawerLayout;
    @BindView(R.id.navHome_rv)
    RecyclerView rvNavHome;
    @BindView(R.id.actHome_frContent)
    FrameLayout frameContent;
    ArrayList<YourSiteModels> siteArrayList = new ArrayList<>();
    NavigationItemAdapter navigationItemAdapter;
    private String keySite;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference mDataBaseReference;
    private FirebaseUser user;
    AppDialog appDialog = new AppDialog();
    MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        googleService();
        bindView();
    }

    public void bindView() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        tvList.get(0).setMaxLines(1);
        tvList.get(0).setEllipsize(TextUtils.TruncateAt.END);
        tvList.get(0).setText(AppConstain.STACK_OVERFLOW);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        //question fragment
        replaceFragment(new QuestionFragment(), false, R.id.actHome_frContent);
        //navigation
        rvNavHome.setLayoutManager(new LinearLayoutManager(this));
        navigationItemAdapter = new NavigationItemAdapter(siteArrayList);
        navigationItemAdapter.setListener(this);
        rvNavHome.setAdapter(navigationItemAdapter);
        //user
        user = mAuth.getCurrentUser();
        //presenter
        mainPresenter = new MainPresenter(this, this);
        mainPresenter.bindUser(user);
        mainPresenter.addNewItemNav();
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

    @Override
    public void bindUser(FirebaseUser user, boolean SEAccount) {
        if (!SEAccount && user != null) {
            tvList.get(2).setText(user.getDisplayName());
            if (user.getPhotoUrl() != null) {
                Picasso.get().load(user.getPhotoUrl()).into(imvList.get(1));
            } else {
                imvList.get(1).setImageDrawable(getResources().getDrawable(R.drawable.contacts));
            }
            clList.get(1).setVisibility(View.VISIBLE);
            clList.get(2).setClickable(false);
        } else if (SEAccount && user != null) {
            mDataBaseReference = firebaseDatabase.getReference().child(AppConstain.KEY_CHILD_USERS).child(user.getUid());
            ValueEventListener valueName = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User userObject = dataSnapshot.getValue(User.class);
                    tvList.get(2).setText(userObject.getUsername());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getBaseContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            };
            mDataBaseReference.addValueEventListener(valueName);
            imvList.get(1).setImageDrawable(getResources().getDrawable(R.drawable.contacts));
            clList.get(1).setVisibility(View.VISIBLE);
            clList.get(2).setClickable(false);
        } else {
            tvList.get(2).setText(getResources().getString(R.string.tv_navHomeLogin));
            imvList.get(1).setImageDrawable(getResources().getDrawable(R.drawable.launcher));
            clList.get(1).setVisibility(View.GONE);
            clList.get(2).setClickable(true);
        }
    }

    @Override
    public void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent mainIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void addNewItemNavigation(List<YourSiteModels> list) {
        siteArrayList.addAll(list);
        navigationItemAdapter.notifyDataSetChanged();
    }

    @Override
    public void addNavigationError(Throwable e) {
        Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickMapLocation() {
        replaceFragment(new MapFragment(), true, R.id.actHome_drawer);
    }

    @OnClick({R.id.navHome_btnSignout, R.id.titleHome_imvMenu, R.id.navHome_btnLogin, R.id.navHome_btnAllSite, R.id.navHome_btnMap, R.id.actHome_btnTitleDialog})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.navHome_btnSignout:
                mainPresenter.signOut(mGoogleSignInClient);
                break;
            case R.id.titleHome_imvMenu:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.navHome_btnLogin:
                Intent intent = new Intent(this, SplashScreenActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.navHome_btnAllSite:
                onClickBtnAllSite();
                break;
            case R.id.navHome_btnMap:
                mainPresenter.clickLocationMap(appDialog, drawerLayout, user);
                break;
            case R.id.actHome_btnTitleDialog:
                appDialog.titleSiteDialog(this, this, tvList.get(1), keySite);
                break;
        }
    }

    //google
    public void googleService() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.token_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    public void onClickBtnAllSite() {
        tvList.get(0).setPadding(0, 15, 0, 0);
        tvList.get(0).setText(AppConstain.STACK_EXCHANGE_SITE);
        clList.get(0).setClickable(false);
        imvList.get(0).setVisibility(View.GONE);
        tvList.get(1).setVisibility(View.GONE);
        drawerLayout.closeDrawer(GravityCompat.START);
        addFragment(new AllSiteFragment(), true, R.id.actHome_frContent);
    }

    //navigation click
    @Override
    public void onItemClickListener(int position) {
        tvList.get(0).setPadding(0, 7, 0, 0);
        tvList.get(0).setText(siteArrayList.get(position).getSiteName());
        tvList.get(1).setVisibility(View.VISIBLE);
        tvList.get(1).setText(AppConstain.ACTIVE_QUESTION);
        clList.get(0).setClickable(true);
        imvList.get(0).setVisibility(View.VISIBLE);
        keySite = siteArrayList.get(position).getApi_site();
        replaceFragment(QuestionFragment.newInstance(keySite, siteArrayList.get(position).getSiteName(), null), true, R.id.actHome_frContent);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    //searchdialog click
    @Override
    public void CallBackItemFeed(String titleText) {
        switch (titleText) {
            case AppConstain.UNANSWERED_NEWEST:
                tvList.get(1).setText(AppConstain.CHANGED_UNANSWERED_NEWEST);
                break;
            case AppConstain.UNANSWERED_TAG:
                tvList.get(1).setText(AppConstain.CHANGE_UNANSWERED_TAG);
                break;
            case AppConstain.ACTIVE:
                tvList.get(1).setText(AppConstain.ACTIVE_QUESTION);
                break;
            default:
                tvList.get(1).setText(titleText);
                break;
        }
    }

    //allsite fragment click
    @Override
    public void CallBackListener(String titleString, String keySite) {
        this.keySite = keySite;
        tvList.get(0).setPadding(0, 7, 0, 0);
        tvList.get(1).setVisibility(View.VISIBLE);
        tvList.get(1).setText(AppConstain.ACTIVE_QUESTION);
        tvList.get(0).setText(titleString);
        clList.get(0).setClickable(true);
        imvList.get(0).setVisibility(View.VISIBLE);
    }

    //titledialog click
    @Override
    public void itemCallBack(Fragment fragment, String textTitle) {
        tvList.get(1).setText(textTitle);
        replaceFragment(fragment, true, R.id.actHome_frContent);
    }
}
