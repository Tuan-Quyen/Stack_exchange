package com.android.quyentraining.fragments.question;

import com.android.quyentraining.models.question.ItemQuestionModels;

import java.util.List;

public interface QuestionView {
    void loadData(ItemQuestionModels itemQuestionModels,boolean isRefresh);
    void loadDataFail(String errorString);
}
