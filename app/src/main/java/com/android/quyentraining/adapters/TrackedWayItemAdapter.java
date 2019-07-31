package com.android.quyentraining.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.quyentraining.R;
import com.android.quyentraining.interfaces.ItemFeedClickListener;
import com.android.quyentraining.interfaces.ItemQuestionListener;
import com.android.quyentraining.interfaces.ItemYourSiteListener;
import com.android.quyentraining.ultis.AppConstain;
import com.android.quyentraining.ultis.AppUltis;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class TrackedWayItemAdapter extends RecyclerView.Adapter<TrackedWayItemAdapter.ViewHolder> {
    List<String> listWay;
    ItemYourSiteListener itemFeedClickListener;
    ItemQuestionListener itemQuestionListener;
    public void setItemClickListener(ItemYourSiteListener itemFeedClickListener,ItemQuestionListener itemQuestionListener) {
        this.itemFeedClickListener = itemFeedClickListener;
        this.itemQuestionListener =itemQuestionListener;
    }

    public TrackedWayItemAdapter(List<String> listWay) {
        this.listWay = listWay;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_listlocation,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        AppUltis appUltis = new AppUltis();
        viewHolder.imv.setImageResource(R.drawable.ic_tracking_blue);
        appUltis.convertTime(Long.parseLong(listWay.get(i)),viewHolder.tv,true);
    }

    @Override
    public int getItemCount() {
        return listWay != null ? listWay.size() : 0;
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
            if(itemFeedClickListener != null){
                itemFeedClickListener.onItemClickListener(getAdapterPosition());
            }
        }
        @OnLongClick(R.id.item_listlocation_btn)
        public boolean onLongClick(){
            if(itemQuestionListener != null){
                itemQuestionListener.onClickItemQuestionListener(getAdapterPosition());
            }
            return true;
        }
    }
}
