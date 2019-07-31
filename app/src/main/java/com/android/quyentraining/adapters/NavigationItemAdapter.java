package com.android.quyentraining.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.quyentraining.R;
import com.android.quyentraining.interfaces.ItemYourSiteListener;
import com.android.quyentraining.models.site.YourSiteModels;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NavigationItemAdapter extends RecyclerView.Adapter<NavigationItemAdapter.ViewHolder> {
    ArrayList<YourSiteModels> yourSiteArrayList;
    ItemYourSiteListener listener;

    public void setListener(ItemYourSiteListener listener) {
        this.listener = listener;
    }

    public NavigationItemAdapter(ArrayList<YourSiteModels> yourSiteArrayList) {
        this.yourSiteArrayList = yourSiteArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_yoursiteadapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        YourSiteModels itemSiteModels = yourSiteArrayList.get(i);
        viewHolder.tvItem.setText(itemSiteModels.getSiteName());
        Picasso.get().load(itemSiteModels.getImvSite()).into(viewHolder.imvItem);
    }

    @Override
    public int getItemCount() {
        return yourSiteArrayList != null ? yourSiteArrayList.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.navHome_imvItem)
        ImageView imvItem;
        @BindView(R.id.navHome_tvItem)
        TextView tvItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @OnClick(R.id.navHome_clItem)
        public void onClickItem() {
            if (listener != null) {
                listener.onItemClickListener(getAdapterPosition());
            }
        }
    }
}
