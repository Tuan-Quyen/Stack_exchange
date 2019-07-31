package com.android.quyentraining.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BasePresenter{
    private CompositeDisposable compositeDisposable;

    public BasePresenter() {
        this.compositeDisposable = compositeDisposable;
    }

    protected void addSubscribe(Disposable disposable){
        compositeDisposable.add(disposable);
    }
}
