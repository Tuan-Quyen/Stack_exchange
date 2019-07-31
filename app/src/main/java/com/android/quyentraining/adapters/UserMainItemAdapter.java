package com.android.quyentraining.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.quyentraining.R;
import com.android.quyentraining.models.user.UsersModels;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;

public class UserMainItemAdapter extends RecyclerView.Adapter<UserMainItemAdapter.ViewHolderUser> {
    ArrayList<UsersModels> usersArrayList;
    public UserMainItemAdapter(ArrayList<UsersModels> usersArrayList) {
        this.usersArrayList = usersArrayList;
    }

    @NonNull
    @Override
    public ViewHolderUser onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_fruser, viewGroup, false);
        return new ViewHolderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUser viewHolder, int i) {
        UsersModels users = usersArrayList.get(i);
        viewHolder.tvList.get(0).setText(users.getUserName());
        Picasso.get().load(users.getUserImage()).into(viewHolder.imvList.get(0));
        int numberReputation = users.getReputation();
        DecimalFormat decimalFormat = new DecimalFormat("###,###,###");
        viewHolder.tvList.get(1).setText(decimalFormat.format(numberReputation));
        viewHolder.tvList.get(2).setText(String.valueOf(users.getUserBadgeModels().getGold()));
        viewHolder.tvList.get(3).setText(String.valueOf(users.getUserBadgeModels().getBronze()));
        viewHolder.tvList.get(4).setText(String.valueOf(users.getUserBadgeModels().getSilver()));
        Integer numberGold = users.getUserBadgeModels().getGold();
        Integer numberBronze = users.getUserBadgeModels().getBronze();
        Integer numberSliver = users.getUserBadgeModels().getSilver();
        if (numberGold <= 0) {
            viewHolder.imvList.get(1).setVisibility(View.GONE);
            viewHolder.tvList.get(2).setVisibility(View.GONE);
        }
        if (numberBronze <= 0) {
            viewHolder.imvList.get(2).setVisibility(View.GONE);
            viewHolder.tvList.get(3).setVisibility(View.GONE);
        }
        if (numberSliver <= 0) {
            viewHolder.imvList.get(3).setVisibility(View.GONE);
            viewHolder.tvList.get(4).setVisibility(View.GONE);
        }

    }
    @Override
    public int getItemCount() {
        return usersArrayList != null ? usersArrayList.size() : 0;
    }

    public class ViewHolderUser extends RecyclerView.ViewHolder {
        @BindViews({R.id.itembtnuser_tvusername,R.id.itembtnfruser_tvuserreputation,R.id.itembtnfruser_tvusergold,R.id.itembtnfruser_tvuserbronze,R.id.itembtnfruser_tvusersilver})
        List<TextView> tvList;
        @BindViews({R.id.itembtnfruser_imvuser,R.id.itembtnfruser_imvusergold,R.id.itembtnfruser_imvuserbronze,R.id.itembtnfruser_imvusersilver})
        List<ImageView> imvList;

        public ViewHolderUser(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
