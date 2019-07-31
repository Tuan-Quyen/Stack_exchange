package com.android.quyentraining.ultis;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.Gravity;
import android.view.View;

import com.android.quyentraining.interfaces.ClickConnectionDialog;
import com.android.quyentraining.interfaces.ItemFeedClickListener;
import com.android.quyentraining.interfaces.ItemTitleDialogCallBack;
import com.android.quyentraining.interfaces.SaveChooseCallBack;
import com.android.quyentraining.other.CheckConnectionDialog;
import com.android.quyentraining.other.DeleteLocationDialog;
import com.android.quyentraining.other.DeleteWayDialog;
import com.android.quyentraining.other.GuestClickMapDialog;
import com.android.quyentraining.other.LoginDialog;
import com.android.quyentraining.other.LoginOpenIDDIalog;
import com.android.quyentraining.other.SaveChooseLocationDialog;
import com.android.quyentraining.other.SaveWayDialog;
import com.android.quyentraining.other.SearchAllSiteDialog;
import com.android.quyentraining.other.SearchQuestionDialog;
import com.android.quyentraining.other.SearchTagDialog;
import com.android.quyentraining.other.SearchUserDialog;
import com.android.quyentraining.other.TitleSiteDialog;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class AppDialog {
    public void guestDialogClickMap(Context context){
        GuestClickMapDialog guestClickMapDialog = new GuestClickMapDialog(context);
        guestClickMapDialog.show();
    }
    public void loginOpenID(Context context){
        LoginOpenIDDIalog loginOpenIDDIalog = new LoginOpenIDDIalog(context);
        loginOpenIDDIalog.show();
    }
    public void connectionErrorDialog(Context context,@Nullable ClickConnectionDialog clickConnectionDialog){
        CheckConnectionDialog checkConnectionDialog = new CheckConnectionDialog(context);
        checkConnectionDialog.setClickConnectionDialog(clickConnectionDialog);
        checkConnectionDialog.show();
    }
    public void titleSiteDialog(Context context, @Nullable ItemTitleDialogCallBack itemTitleDialogCallBack, View view,String keySite){
        TitleSiteDialog titleSiteDialog = new TitleSiteDialog(context,keySite);
        titleSiteDialog.setItemQuestionListener(itemTitleDialogCallBack);
        titleSiteDialog.setOutsideTouchable(true);
        titleSiteDialog.setFocusable(true);
        titleSiteDialog.showAsDropDown(view, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, Gravity.LEFT);
    }
    public void searchAllSiteDialog(Context context, @Nullable ItemFeedClickListener itemFeedClickListener,View view){
        SearchAllSiteDialog searchAllSiteDialog = new SearchAllSiteDialog(context);
        searchAllSiteDialog.setItemFeedAllSiteClickListener(itemFeedClickListener);
        searchAllSiteDialog.setOutsideTouchable(true);
        searchAllSiteDialog.setFocusable(true);
        searchAllSiteDialog.showAsDropDown(view, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, Gravity.END);
    }
    public void loginDialog(Context context){
        LoginDialog dialog = new LoginDialog(context);
        dialog.show();
    }
    public void saveChooseDialog(Context context, LatLng latLng, @Nullable SaveChooseCallBack saveChooseCallBack){
        SaveChooseLocationDialog saveChooseLocationDialog = new SaveChooseLocationDialog(context, latLng);
        saveChooseLocationDialog.setSaveChooseCallBack(saveChooseCallBack);
        saveChooseLocationDialog.show();
    }
    public void deleteLocationDialog(Context context, List<String> locationChooseList,int position){
        DeleteLocationDialog deleteLocationDialog = new DeleteLocationDialog(context, locationChooseList, position);
        deleteLocationDialog.show();
    }
    public void saveWayDialog(Context context,List<LatLng> latLngList){
        long timenow = System.currentTimeMillis() / 1000;
        SaveWayDialog wayDialog = new SaveWayDialog(context, latLngList, timenow);
        wayDialog.show();
    }
    public void deleteWayDialog(Context context,List<String> wayList,int position){
        DeleteWayDialog deleteWayDialog = new DeleteWayDialog(context, wayList, position);
        deleteWayDialog.show();
    }
    public void searchQuestionDialog(Context context,@Nullable ItemFeedClickListener itemFeedClickListener,View view){
        SearchQuestionDialog searchQuestionDialog = new SearchQuestionDialog(context);
        searchQuestionDialog.setItemFeedQuestionClickListener(itemFeedClickListener);
        searchQuestionDialog.setOutsideTouchable(true);
        searchQuestionDialog.setFocusable(true);
        searchQuestionDialog.showAsDropDown(view, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, Gravity.TOP);
    }
    public void searchTagDialog(Context context,@Nullable ItemFeedClickListener itemFeedClickListener,View view){
        SearchTagDialog searchTagDialog = new SearchTagDialog(context);
        searchTagDialog.setItemFeedTagClickListener(itemFeedClickListener);
        searchTagDialog.setOutsideTouchable(true);
        searchTagDialog.setFocusable(true);
        searchTagDialog.showAsDropDown(view, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, Gravity.END);
    }
    public void searchUserDialog(Context context,@Nullable ItemFeedClickListener itemFeedClickListener,View view){
        SearchUserDialog searchUserDialog = new SearchUserDialog(context);
        searchUserDialog.setItemFeedUserClickListener(itemFeedClickListener);
        searchUserDialog.setOutsideTouchable(true);
        searchUserDialog.setFocusable(true);
        searchUserDialog.showAsDropDown(view, ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT, Gravity.END);
    }
}
