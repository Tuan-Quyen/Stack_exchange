package com.android.quyentraining.fragments.question;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quyentraining.R;
import com.android.quyentraining.adapters.QuestionMainItemAdapter;
import com.android.quyentraining.base.BaseFragment;
import com.android.quyentraining.fragments.detailquestion.DetailQuestionFragment;
import com.android.quyentraining.interfaces.ItemFeedClickBackMainListener;
import com.android.quyentraining.interfaces.ItemFeedClickListener;
import com.android.quyentraining.interfaces.ItemQuestionListener;
import com.android.quyentraining.models.question.ItemQuestionModels;
import com.android.quyentraining.ultis.AppConstain;
import com.android.quyentraining.ultis.AppDialog;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

public class QuestionFragment extends BaseFragment implements ItemQuestionListener, ItemFeedClickListener, QuestionView {
    @BindViews({R.id.frMain_imvSearch, R.id.frMain_imvClearText})
    List<ImageView> imvList;
    @BindView(R.id.frMain_rv)
    RecyclerView rvMain;
    @BindView(R.id.frMain_tvSearchDialog)
    TextView tvSearchDialog;
    @BindView(R.id.frMain_etSearch)
    EditText etSearch;
    @BindView(R.id.frMain_srl)
    SwipeRefreshLayout swipeRefresh;

    /*@Inject*/
    QuestionPresenter presenter;

    QuestionMainItemAdapter topicMainItemAdapter;
    ItemFeedClickBackMainListener itemFeedClickBackMainListener;
    String sortSite = AppConstain.API_SITE_STACKOVERFLOW, valueSort = AppConstain.ACTIVITY, titleSite = AppConstain.STACK_OVERFLOW, valueSortTemp = AppConstain.ACTIVE;
    boolean isRefresh, hasMore;
    CharSequence textChange = "";
    int totalItem = 0, currentItem = 0, scrollOutItem = 0, page = 1, position = 0;
    AppDialog appDialog = new AppDialog();

    public static QuestionFragment newInstance(String keysite, @Nullable String name, @Nullable String textChange) {
        Bundle args = new Bundle();
        args.putString(AppConstain.KEY_SITENAME, keysite);
        args.putString(AppConstain.KEY_TITLE_SITE, name);
        args.putString(AppConstain.KEY_SEARCH_TAG, textChange);
        QuestionFragment fragment = new QuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        showLoading();
        bindView();
        searchTagChange(textChange);
        return view;
    }

    public void bindView() {
        presenter = new QuestionPresenter(this.getContext(), this);
        tvSearchDialog.setText(AppConstain.ACTIVE);
        etSearch.setHint(AppConstain.SEARCH_QUESTION);
        Bundle bundle = getArguments();
        if (bundle != null) {
            sortSite = bundle.getString(AppConstain.KEY_SITENAME, AppConstain.API_SITE_STACKOVERFLOW);
            titleSite = bundle.getString(AppConstain.KEY_TITLE_SITE, AppConstain.STACK_EXCHANGE_SITE);
            textChange = bundle.getString(AppConstain.KEY_SEARCH_TAG, "");
        }
        topicMainItemAdapter = new QuestionMainItemAdapter(/*topicArr*/);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        rvMain.setLayoutManager(linearLayoutManager);
        rvMain.setAdapter(topicMainItemAdapter);
        topicMainItemAdapter.setItemQuestionListener(this);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                isRefresh = true;
                page = 1;
                if (textChange.length() > 0) {
                    presenter.loadSearchData(sortSite, page, valueSort, textChange, isRefresh);
                } else {
                    presenter.loadData(sortSite, page, valueSort, isRefresh);
                }
            }
        });
        loadMore(linearLayoutManager);
        scrollHideKeyBoard(rvMain);
    }


    public void loadMore(final LinearLayoutManager linearLayoutManager) {
        rvMain.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = linearLayoutManager.getChildCount();
                totalItem = linearLayoutManager.getItemCount();
                scrollOutItem = linearLayoutManager.findFirstVisibleItemPosition();
                if ((currentItem + scrollOutItem) >= totalItem && hasMore) {
                    showLoading();
                    isRefresh = false;
                    page++;
                    if (textChange.equals("")) {
                        presenter.loadData(sortSite, page, valueSort, isRefresh);
                    } else {
                        presenter.loadSearchData(sortSite, page, valueSort, textChange, isRefresh);
                    }
                }
            }
        });
    }

    private void searchTagChange(CharSequence tagText) {
        if (!tagText.equals("")) {
            etSearch.setText(tagText);
            isRefresh = true;
            showLoading();
            swipeRefresh.setRefreshing(true);
            presenter.loadSearchData(sortSite, page, valueSort, textChange, isRefresh);
            closeKeyBoard(this.getActivity().getCurrentFocus());
            if (itemFeedClickBackMainListener != null) {
                itemFeedClickBackMainListener.CallBackItemFeed(AppConstain.SEARCH_FOR + String.valueOf(tagText));
            }
        } else {
            showLoading();
            swipeRefresh.setRefreshing(true);
            presenter.loadData(sortSite, page, valueSort, isRefresh);
        }
    }

    @Override
    public void loadData(ItemQuestionModels itemQuestionModels, boolean isRefresh) {
        hasMore = itemQuestionModels.isHasMore();
        topicMainItemAdapter.setList(itemQuestionModels.getTopicModelsArrayList(), isRefresh);
        topicMainItemAdapter.notifyDataSetChanged();
        hideLoading();
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void loadDataFail(String errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
        swipeRefresh.setRefreshing(false);
        hideLoading();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity = (Activity) context;
        try {
            itemFeedClickBackMainListener = (ItemFeedClickBackMainListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        itemFeedClickBackMainListener = null;
    }

    @OnTextChanged(R.id.frMain_etSearch)
    public void onTextChange(CharSequence sequence) {
        textChange = String.valueOf(sequence.toString().toLowerCase());
        if (sequence.length() > 0) {
            if (valueSort.equalsIgnoreCase(AppConstain.HOT)) {
                valueSort = AppConstain.RELEVANCE;
            }
            imvList.get(0).setVisibility(View.GONE);
            imvList.get(1).setVisibility(View.VISIBLE);
        } else {
            if (valueSort.equalsIgnoreCase(AppConstain.RELEVANCE)) {
                valueSort = AppConstain.HOT;
            }
            showLoading();
            swipeRefresh.setRefreshing(true);
            closeKeyBoard(this.getActivity().getCurrentFocus());
            isRefresh = true;
            presenter.loadData(sortSite, page, valueSort, isRefresh);
            imvList.get(1).setVisibility(View.GONE);
            imvList.get(0).setVisibility(View.VISIBLE);
            if (itemFeedClickBackMainListener != null) {
                itemFeedClickBackMainListener.CallBackItemFeed(valueSortTemp);
            }
        }
    }

    @OnFocusChange(R.id.frMain_etSearch)
    public void onFocusChange(boolean hasFocus) {
        if (!hasFocus) {
            closeKeyBoard(this.getActivity().getCurrentFocus());
        }
    }

    @OnEditorAction(R.id.frMain_etSearch)
    public boolean onEditAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            isRefresh = true;
            showLoading();
            swipeRefresh.setRefreshing(true);
            searchTagChange(String.valueOf(textChange));
            closeKeyBoard(this.getActivity().getCurrentFocus());
            return true;
        }
        return false;
    }

    @OnClick({R.id.frMain_imvClearText, R.id.frMain_btnSearchDialog})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frMain_imvClearText:
                etSearch.setText("");
                break;
            case R.id.frMain_btnSearchDialog:
                appDialog.searchQuestionDialog(this.getContext(), this, tvSearchDialog);
                break;
        }
    }

    @Override
    public void onClickItemQuestionListener(int position) {
        closeKeyBoard(this.getActivity().getCurrentFocus());
        replaceFragment(DetailQuestionFragment.newInstance(topicMainItemAdapter.getTopicArrayList().get(position), sortSite, titleSite), true, R.id.actHome_drawer);
    }

    @Override
    public void onItemFeedClickListener(int position, List<String> list) {
        this.position = position;
        isRefresh = true;
        page = 1;
        closeKeyBoard(this.getActivity().getCurrentFocus());
        valueSort = list.get(position);
        valueSortTemp = list.get(position);
        if (itemFeedClickBackMainListener != null) {
            itemFeedClickBackMainListener.CallBackItemFeed(valueSortTemp);
        }
        switch (valueSort) {
            case AppConstain.ACTIVE:
                valueSort = AppConstain.ACTIVITY;
                break;
            case AppConstain.NEWEST:
                valueSort = AppConstain.CREATION;
                break;
            case AppConstain.UNANSWERED_NEWEST:
                valueSort = AppConstain.CREATION;
                break;
        }
        showLoading();
        swipeRefresh.setRefreshing(true);
        if (textChange.length() > 0) {
            if (valueSort.equalsIgnoreCase(AppConstain.HOT)) {
                valueSort = AppConstain.RELEVANCE;
            }
            presenter.loadSearchData(sortSite, page, valueSort, textChange, isRefresh);
        } else {
            presenter.loadData(sortSite, page, valueSort, isRefresh);
        }
        tvSearchDialog.setMaxLines(1);
        tvSearchDialog.setEllipsize(TextUtils.TruncateAt.END);
        tvSearchDialog.setText(list.get(position));
    }
}
