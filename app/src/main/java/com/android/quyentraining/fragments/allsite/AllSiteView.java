package com.android.quyentraining.fragments.allsite;

import com.android.quyentraining.models.site.YourSiteModels;

import java.util.List;

public interface AllSiteView {
    void showLoading();
    void isHasMore(boolean isHasMore);
    void getAllSiteSuccess(List<YourSiteModels> list);
    void getAllSiteError(Throwable e);
}
