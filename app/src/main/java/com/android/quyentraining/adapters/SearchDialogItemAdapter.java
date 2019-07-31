package com.android.quyentraining.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.quyentraining.R;
import com.android.quyentraining.interfaces.ItemFeedCallBack;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchDialogItemAdapter extends RecyclerView.Adapter<SearchDialogItemAdapter.ViewHolder> {
    ArrayList<String> arrayList;
    ItemFeedCallBack itemFeedCallBack;
    public SearchDialogItemAdapter(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    public void setItemFeedCallBack(ItemFeedCallBack itemFeedCallBack) {
        this.itemFeedCallBack = itemFeedCallBack;
    }

    @NonNull
    @Override
    public SearchDialogItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.itemdialogsearch_spinner,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SearchDialogItemAdapter.ViewHolder viewHolder, int i) {
        String feed = arrayList.get(i);
        viewHolder.textView.setText(feed);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.itemdialogsearch_tv) TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick(R.id.itemdialogsearch_clContent)
        public void onClickItemDialogSearch(){
            if(itemFeedCallBack !=null){
                itemFeedCallBack.onItemFeedCallBack(getAdapterPosition());
            }
        }
    }
}
