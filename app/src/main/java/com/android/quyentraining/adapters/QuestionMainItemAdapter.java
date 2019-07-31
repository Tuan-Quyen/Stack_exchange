package com.android.quyentraining.adapters;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.quyentraining.R;
import com.android.quyentraining.interfaces.ItemQuestionListener;
import com.android.quyentraining.models.question.TopicModels;
import com.android.quyentraining.ultis.AppUltis;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuestionMainItemAdapter extends RecyclerView.Adapter<QuestionMainItemAdapter.ViewHolder> {
    ArrayList<TopicModels> topicArrayList = new ArrayList<>();
    ItemQuestionListener listener;

    public void setItemQuestionListener(ItemQuestionListener listener) {
        this.listener = listener;
    }

    public ArrayList<TopicModels> getTopicArrayList() {
        return topicArrayList;
    }

    public void setList(ArrayList<TopicModels> list, boolean isRefresh){
        if(isRefresh) {
            topicArrayList.clear();
            topicArrayList.addAll(list);
        }else{
            topicArrayList.addAll(list);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_frquestion, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AppUltis appUltis = new AppUltis();
        viewHolder.tvList.get(2).setText(topicArrayList.get(i).getTopicName());
        String text = "";
        for (int j = 0; j < topicArrayList.get(i).getTagsArrayList().size(); j++) {
            if (j == topicArrayList.get(i).getTagsArrayList().size() - 1) {
                text = text + topicArrayList.get(i).getTagsArrayList().get(j);
            } else {
                text = text + topicArrayList.get(i).getTagsArrayList().get(j) + ", ";
            }
        }
        viewHolder.tvList.get(3).setText(text);
        appUltis.drawNumberVote(topicArrayList.get(i).getTopicVote(), viewHolder.tvList.get(0));
        appUltis.convertTime(topicArrayList.get(i).getTopicDate(), viewHolder.tvList.get(4), false);
        appUltis.drawNumberAnswere(topicArrayList.get(i).getTopicAnswer(), viewHolder.tvList.get(1), viewHolder.imvAnswerTopic);
        if (topicArrayList.get(i).getIsAnswered() != null) {
            viewHolder.clVoteAnswer.setBackgroundColor(Color.argb(85, 82, 255, 171));
            viewHolder.imvAnswerTopic.setImageResource(R.drawable.answers_with_accepted);
        } else {
            viewHolder.clVoteAnswer.setBackgroundColor(Color.argb(255, 234, 239, 243));
        }
    }

    @Override
    public int getItemCount() {
        return topicArrayList != null ? topicArrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindViews({R.id.itembtnfrMain_votenumbertopic,R.id.itembtnfrMain_answertopic,R.id.itembtnfrMain_tvnametopic,R.id.itembtnfrMain_tvtypetopic,R.id.itembtnfrMain_tvdatetopic})
        List<TextView> tvList;
        @BindView(R.id.itembtnfrMain_imvanswertopic)
        ImageView imvAnswerTopic;
        @BindView(R.id.itembtnfrMain_clvoteanswer)
        ConstraintLayout clVoteAnswer;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.itembtn_frquestion)
        public void onClickItemQuestion() {
            if (listener != null) {
                listener.onClickItemQuestionListener(getAdapterPosition());
            }
        }
    }
}
