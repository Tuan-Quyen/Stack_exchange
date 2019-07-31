package com.android.quyentraining.fragments.tag;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quyentraining.R;
import com.android.quyentraining.adapters.TagMainItemAdapter;
import com.android.quyentraining.base.BaseFragment;
import com.android.quyentraining.fragments.question.QuestionFragment;
import com.android.quyentraining.interfaces.ItemFeedCallBack;
import com.android.quyentraining.interfaces.ItemFeedClickListener;
import com.android.quyentraining.models.tag.ItemTagModels;
import com.android.quyentraining.models.tag.TagsModels;
import com.android.quyentraining.ultis.AppConstain;
import com.android.quyentraining.ultis.AppDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnTextChanged;

public class TagFragment extends BaseFragment implements ItemFeedClickListener, ItemFeedCallBack, TagView {
    @BindView(R.id.frMain_rv)
    RecyclerView rvTag;
    @BindView(R.id.frMain_tvSearchDialog)
    TextView tvSearchDialog;
    @BindView(R.id.frMain_etSearch)
    EditText etSearch;
    @BindView(R.id.frMain_imvSearch)
    ImageView imvSearch;
    @BindView(R.id.frMain_srl)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.frMain_imvClearText)
    ImageView clearText;
    int totalItem = 0, currentItem = 0, scrollOutItem = 0, page = 1;
    String order = AppConstain.DESCENDING, sortSite, valueSort = AppConstain.POPULAR;
    boolean isRefresh, hasMore;
    CharSequence textChange = "";
    ArrayList<TagsModels> tagsArrayList = new ArrayList<>();
    TagMainItemAdapter tagMainItemAdapter;
    AppDialog appDialog = new AppDialog();
    TagPresenter tagPresenter;

    public static TagFragment newInstance(String sortSite) {
        Bundle args = new Bundle();
        args.putString(AppConstain.KEY_SITENAME, sortSite);
        TagFragment fragment = new TagFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        bindView();
        scrollHideKeyBoard(rvTag);
        return view;
    }

    public void bindView() {
        tagPresenter = new TagPresenter(this.getContext(), this);
        etSearch.setHint(AppConstain.SEARCH_TAG);
        tvSearchDialog.setText(AppConstain.POPULAR_UPPER);
        Bundle bundle = getArguments();
        if (bundle != null) {
            sortSite = bundle.getString(AppConstain.KEY_SITENAME, AppConstain.API_SITE_STACKOVERFLOW);
        }
        tagMainItemAdapter = new TagMainItemAdapter(tagsArrayList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        tagMainItemAdapter.setItemTagClick(this);
        rvTag.setLayoutManager(linearLayoutManager);
        rvTag.setAdapter(tagMainItemAdapter);
        showLoading();
        swipeRefresh.setRefreshing(true);
        tagPresenter.loadData(tagsArrayList, sortSite, page, valueSort, order, isRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                page = 1;
                isRefresh = true;
                tagPresenter.swipeCheck(String.valueOf(textChange));
            }
        });
        loadMore(linearLayoutManager);
    }

    public void loadMore(final LinearLayoutManager linearLayoutManager) {
        rvTag.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = linearLayoutManager.getChildCount();
                totalItem = linearLayoutManager.getItemCount();
                scrollOutItem = linearLayoutManager.findFirstVisibleItemPosition();
                if ((currentItem + scrollOutItem) >= totalItem && hasMore) {
                    showLoading();
                    page++;
                    isRefresh = false;
                    if(textChange.equals("")) {
                        tagPresenter.loadData(tagsArrayList, sortSite, page, valueSort, order, isRefresh);
                    }else{
                        tagPresenter.loadSearchData(tagsArrayList, sortSite, page, valueSort, order,textChange, isRefresh);
                    }
                }
            }
        });
    }

    @Override
    public void loadData(ItemTagModels itemTagModels) {
        hasMore = itemTagModels.isHasMore();
        tagsArrayList.addAll(itemTagModels.getItems());
        tagMainItemAdapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
        hideLoading();
    }

    @Override
    public void loadDataFail(String errorString) {
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
        swipeRefresh.setRefreshing(false);
        hideLoading();
    }

    @Override
    public void swipeHasText(String textChange) {
        tagPresenter.loadSearchData(tagsArrayList, sortSite, page, valueSort, order, textChange, isRefresh);
    }

    @Override
    public void swipeNoText() {
        tagPresenter.loadData(tagsArrayList, sortSite, page, valueSort, order, isRefresh);
    }

    @OnClick({R.id.frMain_btnSearchDialog, R.id.frMain_imvClearText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frMain_btnSearchDialog:
                appDialog.searchTagDialog(this.getContext(), this, tvSearchDialog);
                break;
            case R.id.frMain_imvClearText:
                etSearch.setText("");
                break;
        }
    }

    @OnTextChanged(R.id.frMain_etSearch)
    public void onTextChange(CharSequence sequence) {
        textChange = String.valueOf(sequence.toString().toLowerCase());
        switch (sequence.length()) {
            case 0:
                swipeRefresh.setRefreshing(true);
                closeKeyBoard(this.getActivity().getCurrentFocus());
                isRefresh = true;
                tagPresenter.loadData(tagsArrayList, sortSite, page, valueSort, order, isRefresh);
                clearText.setVisibility(View.GONE);
                imvSearch.setVisibility(View.VISIBLE);
                break;
            default:
                clearText.setVisibility(View.VISIBLE);
                imvSearch.setVisibility(View.GONE);
                break;
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
            tagPresenter.loadSearchData(tagsArrayList, sortSite, page, valueSort, order, textChange, isRefresh);
            closeKeyBoard(this.getActivity().getCurrentFocus());
            return true;
        }
        return false;
    }

    @Override
    public void onItemFeedClickListener(int position, List<String> list) {
        valueSort = list.get(position);
        switch (valueSort) {
            case AppConstain.POPULAR:
                order = AppConstain.DESCENDING;
                break;
            case AppConstain.NAME_TEXT:
                order = AppConstain.ASCENDING;
                break;
        }
        isRefresh = true;
        closeKeyBoard(this.getActivity().getCurrentFocus());
        page = 1;
        showLoading();
        swipeRefresh.setRefreshing(true);
        tagPresenter.swipeCheck(String.valueOf(textChange));
        tvSearchDialog.setText(list.get(position));
    }

    @Override
    public void onItemFeedCallBack(int position) {
        replaceFragment(QuestionFragment.newInstance(sortSite, null, tagsArrayList.get(position).getNameTag()), true, R.id.actHome_frContent);
    }
}
