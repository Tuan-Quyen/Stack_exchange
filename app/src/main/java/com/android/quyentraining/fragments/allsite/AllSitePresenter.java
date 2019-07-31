package com.android.quyentraining.fragments.allsite;

import android.content.Context;
import android.widget.Toast;

import com.android.quyentraining.connections.APIConnection;
import com.android.quyentraining.connections.ConnectionRequest;
import com.android.quyentraining.models.site.ItemSiteModels;
import com.android.quyentraining.models.site.YourSiteModels;
import com.android.quyentraining.ultis.AppConstain;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AllSitePresenter {
    Context context;
    AllSiteView allSiteView;

    public AllSitePresenter(Context context, AllSiteView allSiteView) {
        this.context = context;
        this.allSiteView = allSiteView;
    }

    public void loadPage(final int page) {
        allSiteView.showLoading();
        final APIConnection apiConnection = ConnectionRequest.getRetrofit().create(APIConnection.class);
        Observable<ItemSiteModels> getSitePage = apiConnection.getSitePage(String.valueOf(page)).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        getSitePage.subscribe(new Observer<ItemSiteModels>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ItemSiteModels itemSiteModels) {
                boolean hasMore = itemSiteModels.isHas_more();
                allSiteView.isHasMore(hasMore);
                List<YourSiteModels> list = new ArrayList<>();
                list.addAll(itemSiteModels.getItems());
                if(list.size() == 0){
                    Toast.makeText(context, AppConstain.TOAST_NO_SITE_FOUND, Toast.LENGTH_SHORT).show();
                }
                allSiteView.getAllSiteSuccess(list);
            }

            @Override
            public void onError(Throwable e) {
                allSiteView.getAllSiteError(e);
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
