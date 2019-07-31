package com.android.quyentraining.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.quyentraining.R;
import com.android.quyentraining.interfaces.ItemFeedCallBack;
import com.android.quyentraining.models.tag.TagsModels;
import com.android.quyentraining.ultis.AppUltis;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TagMainItemAdapter extends RecyclerView.Adapter<TagMainItemAdapter.ViewHolder> {
    ArrayList<TagsModels> arrayList;
    ItemFeedCallBack itemFeedCallBack;

    public void setItemTagClick(ItemFeedCallBack itemFeedCallBack) {
        this.itemFeedCallBack = itemFeedCallBack;
    }

    public TagMainItemAdapter(ArrayList<TagsModels> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_frtag, viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TagsModels tags = arrayList.get(i);
        AppUltis appUltis = new AppUltis();
        viewHolder.tvList.get(0).setText(tags.getNameTag());
        appUltis.tagNumer(tags.getNumberTag(),viewHolder.tvList.get(1));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindViews({R.id.itemfrtag_tvtag,R.id.itemfrtag_tvnumbertag})
        List<TextView> tvList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
        @OnClick(R.id.itembtn_frtag)
        public void onClick(){
            if(itemFeedCallBack!=null){
                itemFeedCallBack.onItemFeedCallBack(getAdapterPosition());
            }
        }
    }
}
