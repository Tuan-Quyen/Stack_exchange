package com.android.quyentraining.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.android.quyentraining.R;
import com.android.quyentraining.ultis.AppConstain;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DeleteWayDialog extends Dialog {
    @BindView(R.id.dialog_save_delete_item_tv)
    TextView tvTitle;
    @BindView(R.id.dialog_save_delete_item_tvmain)
    TextView textView;
    List<String> list;
    int position;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    public DeleteWayDialog(Context context,List<String> list,int position) {
        super(context);
        this.list = list;
        this.position =position;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_save_delete_item);
        ButterKnife.bind(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        tvTitle.setText(AppConstain.TITLE_DIALOG_DELETE_WAY_TITLE);
        textView.setText(AppConstain.TITLE_DIALOG_DELETE_WAY);
    }
    @OnClick(R.id.dialog_save_delete_item_btnCancel)
    public void onClickCancel(){
        dismiss();
    }
    @OnClick(R.id.dialog_save_delete_item_btnOK)
    public void onClickOK(){
        FirebaseUser user = mAuth.getCurrentUser();
        String userID = user.getUid();
        mDatabaseReference = database.getReference().child(AppConstain.KEY_CHILD_USERS).child(userID).child(AppConstain.KEY_CHILD_TRACKING).child(list.get(position));
        mDatabaseReference.removeValue();
        dismiss();
    }
}
