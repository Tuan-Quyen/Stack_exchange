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
import com.android.quyentraining.models.site.YourSiteModels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AllSiteItemAdapter extends RecyclerView.Adapter<AllSiteItemAdapter.ViewHolder>{
    public ArrayList<YourSiteModels> yourSiteArrayList;
    ItemAllSiteListener listener;

    public AllSiteItemAdapter(ArrayList<YourSiteModels> yourSiteArrayList) {
        this.yourSiteArrayList = yourSiteArrayList;
    }
    public void setListener(ItemAllSiteListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_frallsite,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        YourSiteModels itemSiteModels = yourSiteArrayList.get(i);
        viewHolder.tvList.get(0).setText(itemSiteModels.getSiteName());
        viewHolder.tvList.get(1).setText(itemSiteModels.getAudience());
        Picasso.get().load(itemSiteModels.getImvSite()).into(viewHolder.imvAllSite);
    }

    @Override
    public int getItemCount() {
        return yourSiteArrayList!=null ? yourSiteArrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindViews({R.id.itembtnfrallsite_tvallsite,R.id.itembtnfrallsite_tvdescription})
        List<TextView> tvList;
        @BindView(R.id.itembtnfrallsite_imvallsite)
        ImageView imvAllSite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
        @OnClick(R.id.itembtn_frallsite)
        public void onClickBtnItemAllSite(){
            if(listener != null){
                listener.onItemAllSiteClickListener(getAdapterPosition());
            }
        }
    }
}
