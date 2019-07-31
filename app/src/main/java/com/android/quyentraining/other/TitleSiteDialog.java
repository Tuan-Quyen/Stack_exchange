package com.android.quyentraining.other;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.android.quyentraining.R;
import com.android.quyentraining.fragments.question.QuestionFragment;
import com.android.quyentraining.fragments.tag.TagFragment;
import com.android.quyentraining.fragments.user.UserFragment;
import com.android.quyentraining.interfaces.ItemTitleDialogCallBack;
import com.android.quyentraining.ultis.AppConstain;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class TitleSiteDialog extends PopupWindow {
    ItemTitleDialogCallBack itemCallBackTitle;
    AppConstain textConstain;
    String keySite;


    public void setItemQuestionListener(ItemTitleDialogCallBack itemCallBackTitle) {
        this.itemCallBackTitle = itemCallBackTitle;
    }

    public TitleSiteDialog(Context context, String keySite) {
        super(context);
        OnCreate(context);
        this.keySite = keySite;
    }

    public void OnCreate(Context context) {
        View layoutInflater = LayoutInflater.from(context).inflate(R.layout.dialogtitle_spinner, null);
        setContentView(layoutInflater);
        ButterKnife.bind(this, layoutInflater);
    }

    @OnClick({R.id.dialogTitle_tvQuestion, R.id.dialogTitle_tvTag, R.id.dialogTitle_tvUser})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.dialogTitle_tvQuestion:
                onClickQuestion();
                break;
            case R.id.dialogTitle_tvTag:
                onClickTag();
                break;
            case R.id.dialogTitle_tvUser:
                onClickUser();
                break;
        }
    }

    public void onClickQuestion() {
        if (itemCallBackTitle != null) {
            itemCallBackTitle.itemCallBack(QuestionFragment.newInstance(keySite, null, null), textConstain.QUESTION_UPPER);
        }
        dismiss();
    }

    public void onClickTag() {
        if (itemCallBackTitle != null) {
            itemCallBackTitle.itemCallBack(TagFragment.newInstance(keySite), textConstain.TAG_UPPER);
        }
        dismiss();
    }

    public void onClickUser() {
        if (itemCallBackTitle != null) {
            itemCallBackTitle.itemCallBack(UserFragment.newInstance(keySite), textConstain.USER_UPPER);
        }
        dismiss();
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }
}
