package com.android.quyentraining.fragments.user;

import com.android.quyentraining.models.user.ItemUserModels;

public interface UserView {
    void loadData(ItemUserModels itemUserModels);

    void loadDataFail(String errorString);

    void swipeHasText(String textChange);

    void swipeEmptyText();
}
