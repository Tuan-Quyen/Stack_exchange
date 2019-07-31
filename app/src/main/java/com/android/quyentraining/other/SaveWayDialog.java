package com.android.quyentraining.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.android.quyentraining.R;
import com.android.quyentraining.ultis.AppConstain;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SaveWayDialog extends Dialog {
    @BindView(R.id.dialog_save_delete_item_tv)
    TextView tvTitle;
    @BindView(R.id.dialog_save_delete_item_tvmain)
    TextView textView;
    List<LatLng> list;
    long timeSave;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    public SaveWayDialog(Context context,List<LatLng> list,long timeSave) {
        super(context);
        this.list = list;
        this.timeSave = timeSave;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_save_delete_item);
        ButterKnife.bind(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        tvTitle.setText(AppConstain.TITLE_DIALOG_SAVE_WAY_TITLE);
        textView.setText(AppConstain.TITLE_DIALOG_SAVE_WAY);
    }
    @OnClick(R.id.dialog_save_delete_item_btnCancel)
    public void onClickCancel(){
        dismiss();
    }
    @OnClick(R.id.dialog_save_delete_item_btnOK)
    public void onClickOK(){
        String userID = mAuth.getUid();
        String gson = new Gson().toJson(list);
        mDatabaseReference = database.getReference().child(AppConstain.KEY_CHILD_USERS).child(userID).child(AppConstain.KEY_CHILD_TRACKING).child(String.valueOf(timeSave));
        mDatabaseReference.setValue(gson);
        dismiss();
    }
}
