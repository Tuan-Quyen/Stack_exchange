package com.android.quyentraining.adapters;

import android.support.annotation.NonNull;
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

import com.android.quyentraining.R;
import com.android.quyentraining.models.question.TopicModels;
import com.android.quyentraining.ultis.AppConstain;
import com.android.quyentraining.ultis.AppUltis;
import com.squareup.picasso.Picasso;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {
    ArrayList<TopicModels> topicArrayList;

    public AnswerAdapter(ArrayList<TopicModels> topicArrayList) {
        this.topicArrayList = topicArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_frdetailquestion, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AppUltis appUltis = new AppUltis();
        TopicModels topicModels = topicArrayList.get(i);
        Integer numberTemp = 0;
        appUltis.drawNumberVote(topicModels.getTopicVote(), viewHolder.tvList.get(0));
        viewHolder.tvList.get(1).setText(topicModels.getOwnerQuestionModels().getName());
        if (topicModels.getOwnerQuestionModels().getReputation() != null) {
            numberTemp = topicModels.getOwnerQuestionModels().getReputation();
        }
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.tvList.get(2).setText(decimalFormat.format(numberTemp));
        Picasso.get().load(topicModels.getOwnerQuestionModels().getImage()).into(viewHolder.imvList.get(0));
        loadWebView(viewHolder, topicModels);
        if (topicArrayList.get(i).getLastEdit() != null) {
            viewHolder.tvList.get(3).setText(AppConstain.ANSWERED);
            appUltis.convertTime(topicArrayList.get(i).getLastEdit(), viewHolder.tvList.get(4), false);
        } else {
            viewHolder.tvList.get(3).setText(AppConstain.EDITED);
            appUltis.convertTime(topicArrayList.get(i).getTopicDate(), viewHolder.tvList.get(4), false);
        }
        if (topicModels.getAccepted()) {
            viewHolder.imvList.get(1).setVisibility(View.VISIBLE);
        }
    }

    public void loadWebView(ViewHolder viewHolder, TopicModels topicModels) {
        viewHolder.wvAnswer.getSettings().setJavaScriptEnabled(true);
        viewHolder.wvAnswer.getSettings().setUseWideViewPort(true);
        viewHolder.wvAnswer.getSettings().setLoadWithOverviewMode(true);
        viewHolder.wvAnswer.setWebViewClient(new WebViewClient() {
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
        viewHolder.wvAnswer.loadDataWithBaseURL(topicModels.getUrlTopic(), "<font size = 7>" + topicModels.getBody() + "</font>", "text/html", "UTF-8", null);
    }

    @Override
    public int getItemCount() {
        return topicArrayList != null ? topicArrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindViews({R.id.itemDetailQuestion_tvVote, R.id.itemDetailQuestion_tvusername, R.id.itemDetailQuestion_tvuserreputation, R.id.itemDetailQuestion_tvstatusedit, R.id.itemDetailQuestion_tvdaypost})
        List<TextView> tvList;
        @BindViews({R.id.itemDetailQuestion_imvuser, R.id.itemDetailQuestion_btnAccepted})
        List<ImageView> imvList;
        @BindView(R.id.itemDetailQuestion_wv)
        WebView wvAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
