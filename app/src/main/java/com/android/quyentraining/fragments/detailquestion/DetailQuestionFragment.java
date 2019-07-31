package com.android.quyentraining.fragments.detailquestion;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.quyentraining.R;
import com.android.quyentraining.adapters.AnswerAdapter;
import com.android.quyentraining.base.BaseFragment;
import com.android.quyentraining.models.question.TopicModels;
import com.android.quyentraining.models.user.UsersModels;
import com.android.quyentraining.ultis.AppConstain;
import com.android.quyentraining.ultis.AppUltis;
import com.nex3z.flowlayout.FlowLayout;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailQuestionFragment extends BaseFragment implements DetailQuestionView {
    @BindViews({R.id.frDetailQuestion_tvQuestion, R.id.frDetailQuestion_tvVote, R.id.titleLogin_tv, R.id.frDetailQuestion_tvusername, R.id.frDetailQuestion_tvuserreputation, R.id.frDetailQuestion_tvuserbronze, R.id.frDetailQuestion_tvusergold,
            R.id.frDetailQuestion_tvusersilver, R.id.frDetailQuestion_tvTotalAnswer, R.id.frDetailQuestion_tvstatusedit, R.id.frDetailQuestion_tvdaypost})
    List<TextView> tvList;
    @BindViews({R.id.frDetailQuestion_imvuser, R.id.frDetailQuestion_imvusergold, R.id.frDetailQuestion_imvuserbronze, R.id.frDetailQuestion_imvusersilver})
    List<ImageView> imvList;
    @BindView(R.id.frDetailQuestion_wvQuestion)
    WebView wvDetail;
    @BindView(R.id.frDetailQuestion_lnTag)
    FlowLayout flowLayout;
    @BindView(R.id.frDetailQuestion_rvAnswer)
    RecyclerView rvAnswer;
    @BindView(R.id.frDetailQuestion_clAnswer)
    ConstraintLayout clTotalAnswer;
    ArrayList<String> tagsTopicsArrayList = new ArrayList<>();
    ArrayList<UsersModels> ownersArray = new ArrayList<>();
    ArrayList<TopicModels> answerArray = new ArrayList<>();
    int ownerID = 0, questionID = 0;
    String sortSite = AppConstain.API_SITE_STACKOVERFLOW, body, url = "";
    AnswerAdapter answerAdapter;
    boolean isRefresh;
    TopicModels topicModels = new TopicModels();
    AppUltis appUltis;
    DetailQuestionPresenter detailQuestionPresenter;

    public static DetailQuestionFragment newInstance(TopicModels topicModels, String keySite, String titleSite) {
        Bundle args = new Bundle();
        args.putSerializable(AppConstain.KEY_TOPIC_DETAIL, topicModels);
        args.putString(AppConstain.KEY_SITENAME, keySite);
        args.putString(AppConstain.KEY_TITLE_SITE, titleSite);
        DetailQuestionFragment fragment = new DetailQuestionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detailquestion, container, false);
        ButterKnife.bind(this, view);
        initBundle();
        detailQuestionPresenter.addTagType(tagsTopicsArrayList);
        showLoading();
        detailQuestionPresenter.loadOwnerData(ownersArray, sortSite, ownerID, isRefresh);
        WebViewDetail(url, body);
        return view;
    }

    public void initBundle() {
        appUltis = new AppUltis();
        detailQuestionPresenter = new DetailQuestionPresenter(this.getContext(), this);
        Bundle bundle = getArguments();
        if (bundle != null) {
            topicModels = (TopicModels) bundle.getSerializable(AppConstain.KEY_TOPIC_DETAIL);
            sortSite = bundle.getString(AppConstain.KEY_SITENAME);
            init(topicModels, bundle.getString(AppConstain.KEY_TITLE_SITE));

        }
        answerAdapter = new AnswerAdapter(answerArray);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvAnswer.setLayoutManager(linearLayoutManager);
        rvAnswer.setAdapter(answerAdapter);
        rvAnswer.setNestedScrollingEnabled(true);
        isRefresh = true;
    }

    public void init(TopicModels topicModels, String titleSite) {
        url = topicModels.getUrlTopic();
        body = topicModels.getBody();
        questionID = topicModels.getQuestionID();
        tagsTopicsArrayList.addAll(topicModels.getTagsArrayList());
        ownerID = topicModels.ownerQuestionModels.getUserID();
        int numberReputation = topicModels.ownerQuestionModels.getReputation();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        tvList.get(4).setText(decimalFormat.format(numberReputation));
        tvList.get(3).setText(topicModels.ownerQuestionModels.getName());
        tvList.get(0).setText(topicModels.getTopicName());
        tvList.get(2).setText(titleSite);
        Integer numberVoteTemp = topicModels.getTopicVote();
        appUltis.topicVote(numberVoteTemp, tvList.get(1));
        detailQuestionPresenter.lastEdited(topicModels);
    }

    @Override
    public void loadOwnerData(List<UsersModels> list) {
        Picasso.get().load(ownersArray.get(0).getUserImage()).into(imvList.get(0));
        Integer numberGold = ownersArray.get(0).getUserBadgeModels().getGold();
        Integer numberBronze = ownersArray.get(0).getUserBadgeModels().getBronze();
        Integer numberSliver = ownersArray.get(0).getUserBadgeModels().getSilver();
        tvList.get(6).setText(String.valueOf(numberGold));
        tvList.get(5).setText(String.valueOf(numberBronze));
        tvList.get(7).setText(String.valueOf(numberSliver));
        if (numberGold <= 0) {
            imvList.get(1).setVisibility(View.GONE);
            tvList.get(6).setVisibility(View.GONE);
        }
        if (numberBronze <= 0) {
            tvList.get(2).setVisibility(View.GONE);
            tvList.get(5).setVisibility(View.GONE);
        }
        if (numberSliver <= 0) {
            imvList.get(3).setVisibility(View.GONE);
            tvList.get(7).setVisibility(View.GONE);
        }
        //recycleview answer
        hideLoading();
        answerAdapter.notifyDataSetChanged();
        detailQuestionPresenter.loadAnswerData(answerArray, sortSite, questionID, isRefresh);
    }

    @Override
    public void loadError(Throwable e) {
        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        hideLoading();
    }

    @Override
    public void loadAnswerData(List<TopicModels> list) {
        switch (answerArray.size()) {
            case 0:
                clTotalAnswer.setVisibility(View.GONE);
                tvList.get(8).setVisibility(View.GONE);
                break;
            default:
                clTotalAnswer.setVisibility(View.VISIBLE);
                tvList.get(8).setVisibility(View.VISIBLE);
                tvList.get(8).setText(answerArray.size() + " " + AppConstain.ANSWERED);
                isRefresh = true;
                break;
        }
        answerAdapter.notifyDataSetChanged();
        hideLoading();
    }

    @Override
    public void addTagView(TextView tagView) {
        if (flowLayout != null) {
            flowLayout.addView(tagView);
        }
    }

    @Override
    public void topicLastEdited(long time, String edited) {
        tvList.get(9).setText(edited);
        appUltis.convertTime(time, tvList.get(10), false);
    }


    public void WebViewDetail(String url, String body) {
        wvDetail.getSettings().setJavaScriptEnabled(true);
        wvDetail.getSettings().setUseWideViewPort(true);
        wvDetail.getSettings().setLoadWithOverviewMode(true);
        wvDetail.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }
        });
        wvDetail.loadDataWithBaseURL(url, "<font size = 7>" + body + "</font>", "text/html", "UTF-8", null);
    }

    @OnClick(R.id.titleLogin_imv)
    public void onClickBack() {
        getActivity().onBackPressed();
    }
}
