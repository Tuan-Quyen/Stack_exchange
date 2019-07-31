package com.android.quyentraining.base;

import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;


public class BaseFragment extends Fragment{
    public void addFragment(Fragment fragment , boolean addToBackStack,int resID){
        if(getActivity()instanceof BaseActivity){
            ((BaseActivity) getActivity()).addFragment(fragment,addToBackStack,resID);
        }
    }
    public void replaceFragment(Fragment fragment, boolean addToBackStack,int resID){
        if (getActivity()instanceof BaseActivity){
            ((BaseActivity) getActivity()).replaceFragment(fragment,addToBackStack,resID);
        }
    }

    public void showLoading(){
        if(getActivity()instanceof BaseActivity){
            ((BaseActivity) getActivity()).showLoading();
        }
    }
    public void hideLoading(){
        if(getActivity()instanceof BaseActivity){
            ((BaseActivity) getActivity()).hideLoading();
        }
    }
    public void closeKeyBoard(View view){
        if(getActivity()instanceof BaseActivity){
            ((BaseActivity) getActivity()).closeKeyBoard(view);
        }
    }
    public void scrollHideKeyBoard(RecyclerView recyclerView){
        if(getActivity()instanceof BaseActivity){
            ((BaseActivity) getActivity()).scrollOffKeyBoard(recyclerView);
        }
    }
}
