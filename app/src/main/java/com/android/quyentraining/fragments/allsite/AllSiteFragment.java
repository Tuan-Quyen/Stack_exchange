package com.android.quyentraining.fragments.allsite;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quyentraining.R;
import com.android.quyentraining.adapters.AllSiteItemAdapter;
import com.android.quyentraining.base.BaseFragment;
import com.android.quyentraining.fragments.question.QuestionFragment;
import com.android.quyentraining.interfaces.ItemAllSiteCallBack;
import com.android.quyentraining.interfaces.ItemAllSiteListener;
import com.android.quyentraining.interfaces.ItemFeedClickListener;
import com.android.quyentraining.models.site.YourSiteModels;
import com.android.quyentraining.ultis.AppConstain;
import com.android.quyentraining.ultis.AppDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class AllSiteFragment extends BaseFragment implements ItemAllSiteListener, ItemFeedClickListener, AllSiteView {
    @BindViews({R.id.frMain_imvSearch,R.id.frMain_imvClearText})
    List<ImageView> imvList;
    @BindView(R.id.frMain_rv)
    RecyclerView rvAllSite;
    @BindView(R.id.frMain_tvSearchDialog)
    TextView tvSearchDialog;
    @BindView(R.id.frMain_etSearch)
    EditText etSearch;
    @BindView(R.id.frMain_srl)
    SwipeRefreshLayout swipeRefresh;
    AllSiteItemAdapter allSiteItemAdapter;
    ArrayList<YourSiteModels> itemSiteModelsArrayList = new ArrayList<>();
    ItemAllSiteCallBack itemAllSiteCallBack;
    boolean isLoading, hasMore;
    int totalItem = 0, visibleThreshold = 0, scrollOutItem = 0, page = 1;
    AppDialog appDialog = new AppDialog();
    AllSitePresenter allSitePresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        showLoading();
        bindView();
        return view;
    }

    public void bindView() {
        allSitePresenter = new AllSitePresenter(this.getContext(), this);
        etSearch.setHint(AppConstain.SEARCH_SITE);
        tvSearchDialog.setText(AppConstain.MAIN_SITE);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        rvAllSite.setLayoutManager(linearLayoutManager);
        allSiteItemAdapter = new AllSiteItemAdapter(itemSiteModelsArrayList);
        rvAllSite.setAdapter(allSiteItemAdapter);
        allSiteItemAdapter.setListener(this);
        showLoading();
        swipeRefresh.setRefreshing(true);
        allSitePresenter.loadPage(page);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showLoading();
                swipeRefresh.setRefreshing(true);
                allSitePresenter.loadPage(page);
            }
        });
        loadMore(linearLayoutManager);
        scrollHideKeyBoard(rvAllSite);
    }

    @Override
    public void isHasMore(boolean isHasMore) {
        hasMore = isHasMore;
    }

    @Override
    public void getAllSiteSuccess(List<YourSiteModels> list) {
        itemSiteModelsArrayList.addAll(list);
        allSiteItemAdapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
        hideLoading();
    }

    @Override
    public void getAllSiteError(Throwable e) {
        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        swipeRefresh.setRefreshing(false);
        hideLoading();
    }

    public void loadMore(final LinearLayoutManager linearLayoutManager) {
        rvAllSite.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItem = linearLayoutManager.getItemCount();
                scrollOutItem = linearLayoutManager.findFirstVisibleItemPosition();
                visibleThreshold = linearLayoutManager.getChildCount();
                if ((visibleThreshold + scrollOutItem) >= totalItem) {
                    if (!isLoading && hasMore) {
                        page++;
                        allSitePresenter.loadPage(page);
                    }
                }
            }
        });
    }

    public void isRefreshing(boolean refresh) {
        if (refresh) {
            itemSiteModelsArrayList.clear();
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            itemAllSiteCallBack = (ItemAllSiteCallBack) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        itemAllSiteCallBack = null;
    }

    @OnClick({R.id.frMain_btnSearchDialog, R.id.frMain_imvClearText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frMain_btnSearchDialog:
                appDialog.searchAllSiteDialog(this.getContext(), this, tvSearchDialog);
                break;
            case R.id.frMain_imvClearText:
                etSearch.setText("");
                break;
        }
    }

    @OnTextChanged(R.id.frMain_etSearch)
    public void onTextChange(CharSequence sequence) {
        if (sequence.length() > 0) {
            imvList.get(1).setVisibility(View.VISIBLE);
            imvList.get(0).setVisibility(View.GONE);
            isRefreshing(true);
        } else {
            imvList.get(1).setVisibility(View.GONE);
            page = 1;
            closeKeyBoard(this.getActivity().getCurrentFocus());
            showLoading();
            swipeRefresh.setRefreshing(true);
            allSitePresenter.loadPage(page);
            imvList.get(0).setVisibility(View.VISIBLE);
        }
    }

    //interface click allsite
    @Override
    public void onItemAllSiteClickListener(int position) {
        if (itemAllSiteCallBack != null) {
            itemAllSiteCallBack.CallBackListener(itemSiteModelsArrayList.get(position).getSiteName(), itemSiteModelsArrayList.get(position).getApi_site());
        }
        closeKeyBoard(this.getActivity().getCurrentFocus());
        addFragment(QuestionFragment.newInstance(itemSiteModelsArrayList.get(position).getApi_site(), itemSiteModelsArrayList.get(position).getSiteName(), null), true, R.id.actHome_frContent);
    }

    //interface click searchdialog button
    @Override
    public void onItemFeedClickListener(int position, List<String> list) {
        tvSearchDialog.setText(list.get(position));
        closeKeyBoard(this.getActivity().getCurrentFocus());
    }
}
