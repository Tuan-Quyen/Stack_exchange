package com.android.quyentraining.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.quyentraining.R;
import com.android.quyentraining.interfaces.ItemAllSiteListener;
import com.android.quyentraining.interfaces.ItemFeedCallBack;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class SelectedLocationItemAdapter extends RecyclerView.Adapter<SelectedLocationItemAdapter.ViewHolder> {
    List<String> list;
    ItemAllSiteListener itemAllSiteListener;
    ItemFeedCallBack itemFeedCallBack;
    public void setItemAllSiteListener(ItemAllSiteListener itemAllSiteListener,ItemFeedCallBack itemFeedCallBack) {
        this.itemAllSiteListener = itemAllSiteListener;
        this.itemFeedCallBack = itemFeedCallBack;
    }

    public SelectedLocationItemAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_listlocation,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.imv.setImageResource(R.drawable.ic_location_on_pink_24dp);
        viewHolder.tv.setText(list.get(i));
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_listlocation_imv)
        ImageView imv;
        @BindView(R.id.item_listlocation_tv)
        TextView tv;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick(R.id.item_listlocation_btn)
        public void onClick(){
            if(itemAllSiteListener!=null){
                itemAllSiteListener.onItemAllSiteClickListener(getAdapterPosition());
            }
        }
        @OnLongClick(R.id.item_listlocation_btn)
        public boolean onLongClick(){
            if(itemFeedCallBack!=null){
                itemFeedCallBack.onItemFeedCallBack(getAdapterPosition());
            }
            return false;
        }
    }
}
