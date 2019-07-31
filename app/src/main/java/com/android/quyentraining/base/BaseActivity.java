package com.android.quyentraining.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import com.android.quyentraining.other.ProgressBarLoadMore;

public class BaseActivity extends AppCompatActivity {
    public ProgressBarLoadMore progressBarLoadMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public void addFragment(Fragment fragment, boolean addToBackStack, int resID) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(resID, fragment);
        if (addToBackStack == true) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment fragment, boolean addToBackStack, int resID) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(resID, fragment, fragment.getClass().getName());
        if (addToBackStack == true) {
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
    }
    public void showLoading() {
        if (progressBarLoadMore != null) {
            progressBarLoadMore.show();
        } else {
            progressBarLoadMore = new ProgressBarLoadMore(this);
            progressBarLoadMore.show();
        }
    }

    public void hideLoading() {
        progressBarLoadMore.hide();
        progressBarLoadMore.dismiss();
    }

    public void closeKeyBoard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    public void scrollOffKeyBoard(final RecyclerView rvMain){
        rvMain.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                closeKeyBoard(rvMain);
                return false;
            }
        });
    }
}
