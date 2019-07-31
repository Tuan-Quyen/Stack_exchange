package com.android.quyentraining.fragments.user;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quyentraining.R;
import com.android.quyentraining.adapters.UserMainItemAdapter;
import com.android.quyentraining.base.BaseFragment;
import com.android.quyentraining.connections.ConnectionRequest;
import com.android.quyentraining.connections.APIConnection;
import com.android.quyentraining.interfaces.ItemFeedClickListener;
import com.android.quyentraining.models.user.ItemUserModels;
import com.android.quyentraining.models.user.UsersModels;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends BaseFragment implements ItemFeedClickListener, UserView {
    @BindView(R.id.frMain_rv)
    RecyclerView rvUser;
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
    ArrayList<UsersModels> arrayList = new ArrayList<>();
    UserMainItemAdapter userMainItemAdapter;
    boolean isRefresh, hasMore;
    int totalItem = 0, currentItem = 0, scrollOutItem = 0, page = 1;
    String sortSite, valueSort = AppConstain.REPUTATION;
    CharSequence textChange = "";
    AppDialog appDialog = new AppDialog();
    UserPresenter userPresenter;

    public static UserFragment newInstance(String keySite) {
        Bundle args = new Bundle();
        args.putString(AppConstain.KEY_SITENAME, keySite);
        UserFragment fragment = new UserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        bindView();
        scrollHideKeyBoard(rvUser);
        return view;
    }

    public void bindView() {
        userPresenter = new UserPresenter(this.getContext(), this);
        etSearch.setHint(AppConstain.SEARCH_USER);
        tvSearchDialog.setText(AppConstain.REPUTATION_UPPER);
        Bundle bundle = getArguments();
        if (bundle != null) {
            sortSite = bundle.getString(AppConstain.KEY_SITENAME, AppConstain.API_SITE_STACKOVERFLOW);
        }
        userMainItemAdapter = new UserMainItemAdapter(arrayList);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        rvUser.setLayoutManager(linearLayoutManager);
        rvUser.setAdapter(userMainItemAdapter);
        showLoading();
        swipeRefresh.setRefreshing(true);
        userPresenter.loadData(arrayList, sortSite, page, valueSort, isRefresh);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefresh.setRefreshing(true);
                page = 1;
                isRefresh = true;
                userPresenter.swipeCheck(String.valueOf(textChange));

            }
        });
        loadMore(linearLayoutManager);
    }

    public void loadMore(final LinearLayoutManager linearLayoutManager) {
        rvUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                currentItem = linearLayoutManager.getChildCount();
                scrollOutItem = linearLayoutManager.findFirstVisibleItemPosition();
                totalItem = linearLayoutManager.getItemCount();
                if ((currentItem + scrollOutItem) >= totalItem && hasMore) {
                    showLoading();
                    page++;
                    isRefresh = false;
                    if(textChange.equals("")) {
                        userPresenter.loadData(arrayList, sortSite, page, valueSort, isRefresh);
                    }else{
                        userPresenter.loadSearchData(arrayList, sortSite, page, valueSort,textChange, isRefresh);
                    }
                }
            }
        });
    }

    @Override
    public void loadData(ItemUserModels itemUserModels) {
        hasMore = itemUserModels.isHasMore();
        arrayList.addAll(itemUserModels.getItems());
        userMainItemAdapter.notifyDataSetChanged();
        swipeRefresh.setRefreshing(false);
        hideLoading();
    }

    @Override
    public void loadDataFail(String errorString) {
        swipeRefresh.setRefreshing(false);
        Toast.makeText(getContext(), errorString, Toast.LENGTH_SHORT).show();
        hideLoading();
    }

    @Override
    public void swipeHasText(String textChange) {
        userPresenter.loadSearchData(arrayList, sortSite, page, valueSort, textChange, isRefresh);
    }

    @Override
    public void swipeEmptyText() {
        userPresenter.loadData(arrayList, sortSite, page, valueSort, isRefresh);
    }

    @OnClick({R.id.frMain_btnSearchDialog, R.id.frMain_imvClearText})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.frMain_btnSearchDialog:
                appDialog.searchUserDialog(this.getContext(), this, tvSearchDialog);
                break;
            case R.id.frMain_imvClearText:
                etSearch.setText("");
                break;
        }
    }

    @OnTextChanged(R.id.frMain_etSearch)
    public void onTextChange(CharSequence sequence) {
        if (sequence.length() > 0) {
            textChange = String.valueOf(sequence.toString().toLowerCase());
            clearText.setVisibility(View.VISIBLE);
            imvSearch.setVisibility(View.GONE);
        } else {
            showLoading();
            closeKeyBoard(this.getActivity().getCurrentFocus());
            isRefresh = true;
            showLoading();
            swipeRefresh.setRefreshing(true);
            userPresenter.loadData(arrayList, sortSite, page, valueSort, isRefresh);
            clearText.setVisibility(View.GONE);
            imvSearch.setVisibility(View.VISIBLE);
        }
    }

    @OnFocusChange(R.id.frMain_etSearch)
    public void onFocusChange(boolean hasFocus) {
        if (!hasFocus) {
            closeKeyBoard(this.getActivity().getCurrentFocus());
        }
    }

    @OnEditorAction(R.id.frMain_etSearch)
    public boolean onEditorAction(int actionId) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            isRefresh = true;
            showLoading();
            swipeRefresh.setRefreshing(true);
            userPresenter.loadSearchData(arrayList, sortSite, page, valueSort, textChange, isRefresh);
            closeKeyBoard(this.getActivity().getCurrentFocus());
            return true;
        }
        return false;
    }

    @Override
    public void onItemFeedClickListener(int position, List<String> list) {
        valueSort = list.get(position);
        isRefresh = true;
        closeKeyBoard(this.getActivity().getCurrentFocus());
        page = 1;
        showLoading();
        swipeRefresh.setRefreshing(true);
        userPresenter.swipeCheck(String.valueOf(textChange));
        tvSearchDialog.setText(list.get(position));
    }
}
