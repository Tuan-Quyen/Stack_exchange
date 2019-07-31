package com.android.quyentraining.fragments.tag;

import android.content.Context;

import com.android.quyentraining.connections.APIConnection;
import com.android.quyentraining.connections.ConnectionRequest;
import com.android.quyentraining.models.tag.ItemTagModels;
import com.android.quyentraining.models.tag.TagsModels;
import com.android.quyentraining.ultis.AppConstain;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TagPresenter {
    Context context;
    TagView tagView;

    public TagPresenter(Context context, TagView tagView) {
        this.context = context;
        this.tagView = tagView;
    }

    public void loadData(final List<TagsModels> tagsArrayList, String sortSite, int page, String valueSort, String order, final boolean isRefresh) {
        APIConnection apiConnection = ConnectionRequest.getRetrofit().create(APIConnection.class);
        Observable<ItemTagModels> getAllTag = apiConnection.getPageTag(sortSite, String.valueOf(page), valueSort, order).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        getAllTag.subscribe(new Observer<ItemTagModels>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ItemTagModels itemTagModels) {
                if (isRefresh) {
                    tagsArrayList.clear();
                }
                if(itemTagModels != null) {
                    tagView.loadData(itemTagModels);
                }else {
                    tagView.loadDataFail(AppConstain.TOAST_NO_TAG_FOUND);
                }
            }

            @Override
            public void onError(Throwable e) {
                tagView.loadDataFail(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void loadSearchData(final List<TagsModels> tagsArrayList, String sortSite, int page, String valueSort, String order, CharSequence sequence, final boolean isRefresh) {
        APIConnection apiConnection = ConnectionRequest.getRetrofit().create(APIConnection.class);
        Observable<ItemTagModels> searchTag = apiConnection.getSearchTag(sortSite, String.valueOf(page), valueSort, order, String.valueOf(sequence)).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread());
        searchTag.subscribe(new Observer<ItemTagModels>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(ItemTagModels itemTagModels) {
                if (isRefresh) {
                    tagsArrayList.clear();
                }
                if(itemTagModels != null) {
                    tagView.loadData(itemTagModels);
                }else{
                    tagView.loadDataFail(AppConstain.TOAST_NO_TAG_FOUND);
                }
            }

            @Override
            public void onError(Throwable e) {
                tagView.loadDataFail(e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });
    }

    public void swipeCheck(String textChange){
        switch (textChange.length()) {
            case 0:
                tagView.swipeNoText();
                break;
            default:
                tagView.swipeHasText(textChange);
                break;
        }
    }
}
