package com.android.quyentraining.fragments.tag;

import com.android.quyentraining.models.tag.ItemTagModels;

public interface TagView {
    void loadData(ItemTagModels itemTagModels);

    void loadDataFail(String errorString);

    void swipeHasText(String textChange);

    void swipeNoText();
}
