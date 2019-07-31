package com.android.quyentraining.activities.main;

import com.android.quyentraining.models.site.YourSiteModels;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import io.reactivex.annotations.Nullable;

public interface MainView {
    void bindUser(@Nullable FirebaseUser user, boolean SEAccount);

    void signOut();

    void addNewItemNavigation(List<YourSiteModels> list);

    void addNavigationError(Throwable e);

    void onClickMapLocation();
}
