package com.android.quyentraining.other;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.android.quyentraining.R;
import com.android.quyentraining.adapters.SearchDialogItemAdapter;
import com.android.quyentraining.interfaces.ItemFeedCallBack;
import com.android.quyentraining.interfaces.ItemFeedClickListener;
import com.android.quyentraining.ultis.AppConstain;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchQuestionDialog extends PopupWindow implements ItemFeedCallBack {
    @BindView(R.id.dialogsearch_rv)
    RecyclerView rvDialogSearch;
    ArrayList<String> feedArrayList;
    SearchDialogItemAdapter itemAdapter;
    ItemFeedClickListener itemFeedClickListener;

    public void setItemFeedQuestionClickListener(ItemFeedClickListener itemFeedClickListener) {
        this.itemFeedClickListener = itemFeedClickListener;
    }

    public SearchQuestionDialog(Context context) {
        super(context);
        OnCreate(context);
    }

    public void OnCreate(Context context) {
        View layoutInflater = LayoutInflater.from(context).inflate(R.layout.dialogsearch_spinner, null);
        setContentView(layoutInflater);
        ButterKnife.bind(this, layoutInflater);
        feedArrayList = new ArrayList<>();
        feedArrayList.add(AppConstain.ACTIVE);
        feedArrayList.add(AppConstain.NEWEST);
        feedArrayList.add(AppConstain.HOT);
        feedArrayList.add(AppConstain.VOTE);
        feedArrayList.add(AppConstain.UNANSWERED_NEWEST);
        feedArrayList.add(AppConstain.UNANSWERED_TAG);
        itemAdapter = new SearchDialogItemAdapter(feedArrayList);
        rvDialogSearch.setLayoutManager(new LinearLayoutManager(context));
        rvDialogSearch.setAdapter(itemAdapter);
        itemAdapter.setItemFeedCallBack(this);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void onItemFeedCallBack(int position) {
        if (itemFeedClickListener != null) {
            itemFeedClickListener.onItemFeedClickListener(position,feedArrayList);
        }
        dismiss();
    }
}
