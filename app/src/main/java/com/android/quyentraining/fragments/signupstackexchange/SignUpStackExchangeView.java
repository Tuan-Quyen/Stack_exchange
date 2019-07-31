package com.android.quyentraining.fragments.signupstackexchange;

public interface SignUpStackExchangeView {
    void emptyEmailOrPass(String errorString);
    void emptyName(String errorString);
    void repassNotMatch(String errorString);
    void sendEmailSuccess(String successString);
    void sendEmailFail(String errorString);
    void signUpSuccess(String userID,String name,long now,String accountType);
    void signUpFail(String errorString);
    void showLoadingProgress();
    void hideLoadingProgress();
}
