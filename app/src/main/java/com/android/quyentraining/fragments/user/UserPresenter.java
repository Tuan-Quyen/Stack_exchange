package com.android.quyentraining.fragments.user;

import android.content.Context;

import com.android.quyentraining.connections.APIConnection;
import com.android.quyentraining.connections.ConnectionRequest;
import com.android.quyentraining.models.user.ItemUserModels;
import com.android.quyentraining.models.user.UsersModels;
import com.android.quyentraining.ultis.AppConstain;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class UserPresenter {
    Context context;
    UserView userView;

    public UserPresenter(Context context, UserView userView) {
        this.context = context;
        this.userView = userView;
    }

    public void loadData(final List<UsersModels> arrayList, String sortSite, int page, String valueSort, final boolean isRefresh) {
        APIConnection apiConnection = ConnectionRequest.getRetrofit().create(APIConnection.class);
        Observable<ItemUserModels> getPageUser = apiConnection.getPageUser(sortSite, String.valueOf(page), valueSort).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        getPageUser.subscribe(new Observer<ItemUserModels>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ItemUserModels itemUserModels) {
                if (isRefresh) {
                    arrayList.clear();
                }
                if(itemUserModels != null) {
                    userView.loadData(itemUserModels);
                }else {
                    userView.loadDataFail(AppConstain.TOAST_NO_USER_FOUND);
                }
            }

            @Override
            public void onError(Throwable e) {
                userView.loadDataFail(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void loadSearchData(final List<UsersModels> arrayList,String sortSite, Integer page, String valueSort, CharSequence sequence, final boolean isRefresh) {
        APIConnection apiConnection = ConnectionRequest.getRetrofit().create(APIConnection.class);
        Observable<ItemUserModels> getSearchData = apiConnection.getSearchUser(sortSite, String.valueOf(page), valueSort, String.valueOf(sequence)).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        getSearchData.subscribe(new Observer<ItemUserModels>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ItemUserModels itemUserModels) {
                if (isRefresh) {
                    arrayList.clear();
                }
                if(itemUserModels != null) {
                    userView.loadData(itemUserModels);
                }else{
                    userView.loadDataFail(AppConstain.TOAST_NO_USER_FOUND);
                }
            }

            @Override
            public void onError(Throwable e) {
                userView.loadDataFail(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void swipeCheck(String textChange){
        switch (textChange.length()){
            case 0:
                userView.swipeEmptyText();
                break;
            default:
                userView.swipeHasText(textChange);
                break;
        }
    }
}
