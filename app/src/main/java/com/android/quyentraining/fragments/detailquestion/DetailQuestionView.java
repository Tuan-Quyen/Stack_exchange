package com.android.quyentraining.fragments.detailquestion;

import android.widget.TextView;

import com.android.quyentraining.models.question.TopicModels;
import com.android.quyentraining.models.user.UsersModels;

import java.util.List;

public interface DetailQuestionView {
    void loadOwnerData(List<UsersModels> list);

    void loadError(Throwable e);

    void loadAnswerData(List<TopicModels> list);

    void addTagView(TextView tagView);

    void topicLastEdited(long time,String edited);
}
