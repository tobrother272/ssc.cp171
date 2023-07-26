/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.googleaction;

import ssc.base.global.TC;
import ssc.base.run.ActionWithAccountBase;
import ssc.base.run.GoogleInteractiveChildTask;
import ssc.theta.app.model.GoogleAccount.ACCOUNT_LOGIN_STATUS;
import ssc.theta.app.model.GoogleAccount.ACCOUNT_STATUS;

/**
 * @author simpl
 */
public class OutCardTask extends GoogleInteractiveChildTask {

    public static String RECOVER_URL = "https://accounts.google.com/signin/v2/challenge/selection";
    public static String NOT_NOW_URL = "https://gds.google.com/web/chi";
    public static String ADD_BIRTHDAY_URL = "myaccount.google.com/interstitials/birthday";
    public static String CHANNEL_DEAD_URL = "support.google.com/accounts/answer/";
    public static String ACCEPT_COLLECTION_URL = "myaccount.google.com/signinoptions/recovery-options-collection";
    public static String IPP_URL = "https://accounts.google.com/signin/v2/challenge/ipp";
    public static String LOGIN_REJECTED = "https://accounts.google.com/signin/v2/deniedsigninrejected";
    public static String KPE = "https://accounts.google.com/signin/v2/challenge/kpe";
    public static String DP = "https://accounts.google.com/signin/v2/challenge/dp";
    public static String IAP = "https://accounts.google.com/signin/v2/challenge/iap";
    public static String CHANGE_PASS = "https://accounts.google.com/signin/v2/changepassword";
    public static String DISABALED = "https://accounts.google.com/signin/v2/disabled";
    public static String WEBREAUTH = "https://accounts.google.com/ServiceLogin/webreauth";
    //  
    //
    public static String BTN_CONFIRM_RECOVER = "//div[@role='presentation']//div[@data-challengetype='12']";
    public static String BTN_NOT_NOW = "//c-wiz//span[contains(text(),'Not now')]";

    public static String TXT_EMAIL_RECOVER = "//input[contains(@type,'email')]";
    public static String BTN_NEXT = "//button[child::span[contains(text(),'Tiếp theo') or contains(text(),'Next') or contains(text(),'다음') or contains(text(),'下一步') or contains(text(),'Напред') ]]";
    public static String TXT_USERNAME = "//input[contains(@id,'identifierId')]";
    public static String TXT_PASSWORD = "//input[@name='password']";
    public static String BTN_NEXT_COLLECTION = "//div[contains(@role,'presentation')]//div[contains(@role,'button')]";
    public static String BTN_SIGNUP_WITH_EMAIL = "//span[contains(text(),'Sign up with phone or email')]";
    public static String BTN_AVARTAR = "//button[contains(@id,'avatar-btn')]";

    //
    public static String TXT_OLD_USERNAME = "//input[contains(@id,'Email')]";

    public OutCardTask(ActionWithAccountBase task) {
        super(task);
    }

    String currentUrl = "";
    boolean removeProfile = false;
    private String card;

    public String BTN_FOLLOW_ACCOUNT = "//div[contains(@data-testid,'placementTracking')]//div[contains(@aria-label,'@ACCOUNT_ID')]";

    @Override
    protected Boolean call() {
        TC.getInts().arrOpens.remove(getParentTask().getAccount().getUsername());

        try {
            if (!getParentTask().getConnection().loadUrl("https://www.youtube.com/premium", 120)) {
                //return onFinishFail("Không thể load trang đăng kí");
            }
            wait("Load trang premium", 10);
            if (getParentTask().getConnection().GETLENGHT("//a[contains(@href,'https://accounts.google.com/ServiceLogin')]", 3, "") >= 1) {
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                getParentTask().getAccount().updateData();
                return onFinishFail("Tài khoản đá login");
            }
            if (getParentTask().getConnection().GETLENGHT("//yt-button-renderer[contains(@id,'manage-subscription-button')]", 3, "") == 0) {
                if (getParentTask().getConnection().GETLENGHT("//a[contains(@href,'/paid_membership')]", 3, "") >= 2) {
                    getParentTask().getAccount().updateData();
                    return onFinishFail("Tài khoản đã reg premium thành công");
                }
            }

            if (!getParentTask().getConnection().loadUrl("https://pay.google.com/gp/w/u/0/home/paymentmethods", 120)) {
                //return onFinishFail("Không thể load trang gỡ thẻ");
            }

            wait("load trang gỡ thẻ", 10);
            if (getParentTask().getConnection().GETLENGHTFRAME("https://payments.google.com/payments", "//div[contains(@class,'b3-payment-methods-empty-add-instrument')]", 3, "") != 0) {
                getParentTask().getAccount().setOut_card(System.currentTimeMillis());
                getParentTask().getAccount().updateData();
                return onFinishSuccess();
            }
            //

            int countButton = getParentTask().getConnection().GETLENGHTFRAME("https://payments.google.com/payments", "//a[contains(@class,'b3id-widget-link')]//div[contains(@class,'b3id-info-message-component')]", 3, "");
            System.out.println("countButton " + countButton);
            getParentTask().insertSuccessLog("Số nút gỡ "+countButton);
            if (countButton == 3) {
                if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.google.com/payments", "//a[contains(@class,'b3id-widget-link')]//div[contains(@class,'b3id-info-message-component')]", countButton - 2)) {
                    //return onFinishFail("Không thể load trang gỡ thẻ");
                }
            } else if (countButton == 4) {
                if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.google.com/payments", "//a[contains(@class,'b3id-widget-link')]//div[contains(@class,'b3id-info-message-component')]", countButton - 3)) {
                    //return onFinishFail("Không thể load trang gỡ thẻ");
                }
            } else if (countButton == 6) {
                if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.google.com/payments", "//a[contains(@class,'b3id-widget-link')]//div[contains(@class,'b3id-info-message-component')]", countButton - 5)) {
                    //return onFinishFail("Không thể load trang gỡ thẻ");
                }
            }

            wait("comfirm gở thẻ", 10);

            countButton = getParentTask().getConnection().GETLENGHTFRAME("https://payments.google.com/payments/u/0/fix_instrument", "//div[contains(@role,'button')]", 3, "");

            //System.out.println("countButton confirm " + countButton);
            if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.google.com/payments/u/0/fix_instrument", "//div[contains(@role,'button')]", countButton - 2)) {
                //return onFinishFail("Không thể load trang gỡ thẻ");
            }
            wait("comfirm gở thẻ", 10);

            if (getParentTask().getConnection().GETLENGHTFRAME("https://payments.google.com/payments", "//div[contains(@class,'b3-payment-methods-empty-add-instrument')]", 3, "") != 0) {
                getParentTask().getAccount().setOut_card(System.currentTimeMillis());
                getParentTask().getAccount().updateData();
                return onFinishSuccess();
            }

            wait("check code", 10);

            return onFinishFail("Không thể load trang gỡ thẻ");

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //docker.remove();

            if (!getParentTask().getConnection().quitBrowser(5)) {

            }
            getParentTask().getConnection().disconnect();
            getParentTask().getAccount().removeCache();
        }
        return true;
    }

    @Override
    public void sendResultToParent() {

    }

    @Override
    public String setActionName() {
        return " phút | Đang login tài khoản";

    }

}
