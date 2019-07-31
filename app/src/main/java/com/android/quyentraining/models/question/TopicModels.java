package com.android.quyentraining.models.question;

import com.android.quyentraining.models.user.OwnerQuestionModels;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class TopicModels implements Serializable {
    @SerializedName("owner")
    public OwnerQuestionModels ownerQuestionModels;
    @SerializedName("title")
    public String topicName;
    @SerializedName("creation_date")
    public Long topicDate;
    @SerializedName("tags")
    public ArrayList<String> tagsArrayList;
    @SerializedName("score")
    public Integer topicVote;
    @SerializedName("answer_count")
    public Integer topicAnswer;
    @SerializedName("link")
    public String urlTopic;
    @SerializedName("question_id")
    public Integer questionID;
    @SerializedName("body")
    public String body;
    @SerializedName("last_edit_date")
    public Long lastEdit;
    @SerializedName("is_answered")
    public Boolean hasAnswer;
    @SerializedName("accepted_answer_id")
    public Integer isAnswered;
    @SerializedName("is_accepted")
    public Boolean isAccepted;

    public Boolean getHasAnswer() {
        return hasAnswer;
    }

    public Integer getIsAnswered() {
        return isAnswered;
    }

    public Boolean getAccepted() {
        return isAccepted;
    }

    public Long getLastEdit() {
        return lastEdit;
    }

    public String getBody() {
        return body;
    }

    public Integer getQuestionID() {
        return questionID;
    }

    public OwnerQuestionModels getOwnerQuestionModels() {
        return ownerQuestionModels;
    }

    public String getTopicName() {
        return topicName;
    }


    public Long getTopicDate() {
        return topicDate;
    }


    public ArrayList<String> getTagsArrayList() {
        return tagsArrayList;
    }


    public Integer getTopicVote() {
        return topicVote;
    }

    public Integer getTopicAnswer() {
        return topicAnswer;
    }

    public String getUrlTopic() {
        return urlTopic;
    }
}

