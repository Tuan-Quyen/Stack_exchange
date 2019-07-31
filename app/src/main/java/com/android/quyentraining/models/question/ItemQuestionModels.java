package com.android.quyentraining.models.question;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class ItemQuestionModels implements Serializable {
    @SerializedName("items")
    private ArrayList<TopicModels> topicModelsArrayList = null;
    @SerializedName("has_more")
    private boolean hasMore;

    public boolean isHasMore() {
        return hasMore;
    }

    public ArrayList<TopicModels> getTopicModelsArrayList() {
        return topicModelsArrayList != null ? topicModelsArrayList : new ArrayList<TopicModels>();
    }
}
