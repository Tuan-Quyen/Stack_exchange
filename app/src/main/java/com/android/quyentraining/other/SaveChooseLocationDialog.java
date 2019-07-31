package com.android.quyentraining.other;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.android.quyentraining.R;
import com.android.quyentraining.interfaces.SaveChooseCallBack;
import com.android.quyentraining.ultis.AppConstain;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class SaveChooseLocationDialog extends Dialog {
    @BindView(R.id.dialogsavechoose_et)
    EditText et;
    LatLng latLng;
    private FirebaseDatabase database;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    SaveChooseCallBack saveChooseCallBack;

    public void setSaveChooseCallBack(SaveChooseCallBack saveChooseCallBack) {
        this.saveChooseCallBack = saveChooseCallBack;
    }

    public SaveChooseLocationDialog(Context context, LatLng latLng) {
        super(context);
        this.latLng = latLng;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_savechooselocation);
        ButterKnife.bind(this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }
    @OnClick(R.id.dialogsavechoose_btnCancel)
    public void onClickCancel(){
        if(saveChooseCallBack!= null){
            saveChooseCallBack.setOnClickBackSaveChoose(null,false);
        }
        dismiss();
    }

    @OnTextChanged(R.id.dialogsavechoose_et)
    public void onTextChange(){
        et.setHint(AppConstain.HINT_ET_SAVE);
        et.setHintTextColor(getContext().getResources().getColor(R.color.colorDarkWhite));
    }

    @OnClick(R.id.dialogsavechoose_btnOK)
    public void onClickOK(){
        if(et.getText().length() == 0){
            et.setHint(AppConstain.HINT_ET_SAVE_ERROR);
            et.setHintTextColor(getContext().getResources().getColor(R.color.colorRed));
        }else{
            String userID = mAuth.getUid();
            mDatabaseReference = database.getReference().child(AppConstain.KEY_CHILD_USERS).child(userID).child(AppConstain.KEY_CHILD_LOCATION).child(String.valueOf(et.getText()));
            mDatabaseReference.setValue(latLng);
            Toast.makeText(getContext(),AppConstain.TOAST_CHOOSE_PLACE + et.getText() + AppConstain.LOCATION,Toast.LENGTH_SHORT).show();
            dismiss();
            if(saveChooseCallBack!= null){
                saveChooseCallBack.setOnClickBackSaveChoose(String.valueOf(et.getText()),true);
            }
        }
    }
}
