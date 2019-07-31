package com.android.quyentraining.fragments.question;

import android.content.Context;

import com.android.quyentraining.base.BasePresenter;
import com.android.quyentraining.connections.APIConnection;
import com.android.quyentraining.connections.ConnectionRequest;
import com.android.quyentraining.models.question.ItemQuestionModels;
import com.android.quyentraining.ultis.AppConstain;

import java.io.IOException;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;

public class QuestionPresenter extends BasePresenter {
    Context context;
    QuestionView questionView;
    APIConnection apiConnection = ConnectionRequest.getRetrofit().create(APIConnection.class);

    @Inject
    public QuestionPresenter(Context context, QuestionView questionView) {
        this.context = context;
        this.questionView = questionView;
    }

    public void loadData(String sortSite, int page, String valueSort, final boolean isRefresh) {
        HashMap<String,Object> mHashMap = new HashMap<>();
        mHashMap.put("site",sortSite);
        mHashMap.put("page",page);
        mHashMap.put("sort",valueSort);
        /*addSubscribe(apiConnection.getPageQuestion(mHashMap).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(resData -> {
            if (resData != null) {
                questionView.loadData(resData,isRefresh);
            } else {
                questionView.loadDataFail(AppConstain.TOAST_NO_QUESTION_FOUND);
            }
        },throwable -> {
            questionView.loadDataFail(throwable.getMessage());
        }));*/
        Observable<ItemQuestionModels> getQuestion = apiConnection.getPageQuestion(mHashMap).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        getQuestion.subscribe(resData -> {
            if (resData != null) {
                questionView.loadData(resData,isRefresh);
            } else {
                questionView.loadDataFail(AppConstain.TOAST_NO_QUESTION_FOUND);
            }
        },throwable -> {
            questionView.loadDataFail(throwable.getMessage());
        });
        /*getQuestion.subscribe(new Observer<ItemQuestionModels>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ItemQuestionModels itemQuestionModels) {
                if (itemQuestionModels != null) {
                    questionView.loadData(itemQuestionModels,isRefresh);
                } else {
                    questionView.loadDataFail(AppConstain.TOAST_NO_QUESTION_FOUND);
                }
            }

            @Override
            public void onError(Throwable e) {
                questionView.loadDataFail(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });*/
    }

    public void loadSearchData(String sortSite, int page, String valueSort, CharSequence sequence, final boolean isRefresh) {
        APIConnection apiConnection = ConnectionRequest.getRetrofit().create(APIConnection.class);
        Observable<ItemQuestionModels> searchQuestion = apiConnection.getSearchQuestion(sortSite, String.valueOf(page), valueSort, String.valueOf(sequence)).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        searchQuestion.subscribe(new Observer<ItemQuestionModels>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ItemQuestionModels itemQuestionModels) {
                if (itemQuestionModels != null) {
                    questionView.loadData(itemQuestionModels,isRefresh);
                } else {
                    questionView.loadDataFail(AppConstain.TOAST_NO_QUESTION_FOUND);
                }
            }

            @Override
            public void onError(Throwable e) {
                questionView.loadDataFail(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
