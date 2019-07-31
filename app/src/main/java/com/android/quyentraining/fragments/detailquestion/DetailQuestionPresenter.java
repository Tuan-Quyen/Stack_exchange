package com.android.quyentraining.fragments.detailquestion;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.quyentraining.R;
import com.android.quyentraining.connections.APIConnection;
import com.android.quyentraining.connections.ConnectionRequest;
import com.android.quyentraining.models.question.ItemQuestionModels;
import com.android.quyentraining.models.question.TopicModels;
import com.android.quyentraining.models.user.ItemUserModels;
import com.android.quyentraining.models.user.UsersModels;
import com.android.quyentraining.ultis.AppConstain;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DetailQuestionPresenter {
    Context context;
    DetailQuestionView detailQuestionView;

    public DetailQuestionPresenter(Context context, DetailQuestionView detailQuestionView) {
        this.context = context;
        this.detailQuestionView = detailQuestionView;
    }

    public void loadOwnerData(final List<UsersModels> ownersArray, final String sortSite, int ownerID, final boolean isRefresh) {
        APIConnection apiConnection = ConnectionRequest.getRetrofit().create(APIConnection.class);
        Observable<ItemUserModels> getOwnerQuestion = apiConnection.getOwnerQuestion(String.valueOf(ownerID), sortSite).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        getOwnerQuestion.subscribe(new Observer<ItemUserModels>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ItemUserModels itemUserModels) {
                if (isRefresh) {
                    ownersArray.clear();
                }
                ownersArray.addAll(itemUserModels.getItems());
                detailQuestionView.loadOwnerData(ownersArray);
            }

            @Override
            public void onError(Throwable e) {
                detailQuestionView.loadError(e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void loadAnswerData(final List<TopicModels> answerArray, final String sortSite, int questionID, final boolean isRefresh) {
        APIConnection apiConnection = ConnectionRequest.getRetrofit().create(APIConnection.class);
        Observable<ItemQuestionModels> getAnswerQuestion = apiConnection.getAnswerQuestion(String.valueOf(questionID), sortSite).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        getAnswerQuestion.subscribe(new Observer<ItemQuestionModels>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ItemQuestionModels itemQuestionModels) {
                if (isRefresh) {
                    answerArray.clear();
                }
                answerArray.addAll(itemQuestionModels.getTopicModelsArrayList());
                detailQuestionView.loadAnswerData(answerArray);
            }

            @Override
            public void onError(Throwable e) {
                detailQuestionView.loadError(e);
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void addTagType(List<String> tagsTopicsArrayList) {
        for (int i = 0; i < tagsTopicsArrayList.size(); i++) {
            TextView tagView = new TextView(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 5, 15, 5);
            tagView.setLayoutParams(layoutParams);
            tagView.setTextSize(10);
            tagView.setText(tagsTopicsArrayList.get(i));
            tagView.setPadding(10, 10, 10, 10);
            tagView.setBackground(context.getResources().getDrawable(R.drawable.view_btntagtv));
            detailQuestionView.addTagView(tagView);
        }
    }

    public void lastEdited(TopicModels topicModels){
        if(topicModels.getLastEdit() != null){
            detailQuestionView.topicLastEdited(topicModels.getLastEdit(), AppConstain.EDITED);
        }else{
            detailQuestionView.topicLastEdited(topicModels.getTopicDate(),AppConstain.ASKED);
        }
    }
}
