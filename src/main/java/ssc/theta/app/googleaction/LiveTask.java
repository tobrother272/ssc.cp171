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
public class LiveTask extends GoogleInteractiveChildTask {

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

    public LiveTask(ActionWithAccountBase task, String card) {
        super(task);
        this.card = card;
    }

    String currentUrl = "";
    boolean removeProfile = false;
    private String card;

    public String BTN_FOLLOW_ACCOUNT = "//div[contains(@data-testid,'placementTracking')]//div[contains(@aria-label,'@ACCOUNT_ID')]";

    public boolean typeValue(int indexValue, int indexElement, String title) {
        getParentTask().insertSuccessLog("Nhập " + card.split("#")[indexValue] + " vào " + title);
        if (indexValue == 3) {
            String value = getParentTask().getConnection().getJsWithFrame("https://payments.youtube.com", "return document.evaluate(\"//input[contains(@type,'tel') or contains(@type,'search')]\", document, null, 7, null).snapshotItem(" + indexElement + ").value;", 3, "Lấy giá trị ô " + title).replaceAll("â", "").replaceAll(" ", "");
            if (value.length() >= 5) {
                getParentTask().insertSuccessLog("Đã có sẵn giá trị " + value + " trong ô " + title);
                return true;
            }
        }
        if (indexValue == 0) {
            String value = getParentTask().getConnection().getJsWithFrame("https://payments.youtube.com", "return document.evaluate(\"//input[contains(@type,'tel') or contains(@type,'search')]\", document, null, 7, null).snapshotItem(" + indexElement + ").value;", 3, "Lấy giá trị ô " + title).replaceAll("â", "").replaceAll(" ", "");
            if (value.trim().equals(card.split("#")[indexValue].trim())) {
                getParentTask().insertSuccessLog("Đã có sẵn giá trị " + value + " trong ô " + title);
                return true;
            }
        } else {
            String value = getParentTask().getConnection().getJsWithFrame("https://payments.youtube.com", "return document.evaluate(\"//input[contains(@type,'tel') or contains(@type,'search')]\", document, null, 7, null).snapshotItem(" + indexElement + ").value;", 3, "Lấy giá trị ô " + title).replaceAll("â", "");
            if (value.trim().equals(card.split("#")[indexValue].trim())) {
                getParentTask().insertSuccessLog("Đã có sẵn giá trị " + value + " trong ô " + title);
                return true;
            }
        }

        getParentTask().getConnection().getJsWithFrame("https://payments.youtube.com", "return document.evaluate(\"//input[contains(@type,'tel') or contains(@type,'search')]\", document, null, 7, null).snapshotItem(" + indexElement + ").value='" + card.split("#")[indexValue].trim() + "';", 3, "Lấy giá trị ô " + title).replaceAll("â", "").replaceAll(" ", "");
        wait("...", 4);
        if (!getParentTask().getConnection().sendKeyWithFrame("https://payments.youtube.com", "//input[contains(@type,'tel') or contains(@type,'search')]", indexElement, " ", 20)) {

        }
        wait("...", 1);
        if (indexElement >= 2) {
            if (!getParentTask().getConnection().sendKeyWithFrame("https://payments.youtube.com", "//input[contains(@type,'tel') or contains(@type,'search')]", indexElement, "#Backspace", 20)) {

            }
        }
        wait("...", 4);
        int countError = 0;
        if (indexElement == 0) {
            String result = getParentTask().getConnection().getJsWithFrame("https://payments.youtube.com", "return document.evaluate(\"//input[contains(@type,'tel') or contains(@type,'search')]\", document, null, 7, null).snapshotItem(" + indexElement + ").value;", 3, "Lấy giá trị ô " + title).replaceAll("â", "").replaceAll(" ", "");
            while (!result.equals(card.split("#")[indexValue])) {
                getParentTask().insertSuccessLog("Nhập " + title + " lần " + countError);
                if (countError >= 5) {
                    break;
                }
                getParentTask().getConnection().getJsWithFrame("https://payments.youtube.com", "return document.evaluate(\"//input[contains(@type,'tel') or contains(@type,'search')]\", document, null, 7, null).snapshotItem(" + indexElement + ").value='" + card.split("#")[indexValue].trim() + "';", 3, "Lấy giá trị ô " + title);
                wait("...", 4);
                if (!getParentTask().getConnection().sendKeyWithFrame("https://payments.youtube.com", "//input[contains(@type,'tel') or contains(@type,'search')]", indexElement, " ", 20)) {

                }
                wait("...", 1);
                if (indexElement >= 2) {
                    if (!getParentTask().getConnection().sendKeyWithFrame("https://payments.youtube.com", "//input[contains(@type,'tel') or contains(@type,'search')]", indexElement, "#Backspace", 20)) {

                    }
                }
                countError++;
                result = getParentTask().getConnection().getJsWithFrame("https://payments.youtube.com", "return document.evaluate(\"//input[contains(@type,'tel') or contains(@type,'search')]\", document, null, 7, null).snapshotItem(" + indexElement + ").value;", 3, "Lấy giá trị ô " + title).replaceAll("â", "").replaceAll(" ", "");
            }
        } else {
            String result = getParentTask().getConnection().getJsWithFrame("https://payments.youtube.com", "return document.evaluate(\"//input[contains(@type,'tel') or contains(@type,'search')]\", document, null, 7, null).snapshotItem(" + indexElement + ").value;", 3, "Lấy giá trị ô " + title).replaceAll("â", "");
            while (!result.equals(card.split("#")[indexValue])) {
                if (countError >= 5) {
                    break;
                }
                getParentTask().getConnection().getJsWithFrame("https://payments.youtube.com", "return document.evaluate(\"//input[contains(@type,'tel') or contains(@type,'search')]\", document, null, 7, null).snapshotItem(" + indexElement + ").value='" + card.split("#")[indexValue].trim() + "';", 3, "Lấy giá trị ô " + title);
                wait("...", 4);
                if (!getParentTask().getConnection().sendKeyWithFrame("https://payments.youtube.com", "//input[contains(@type,'tel') or contains(@type,'search')]", indexElement, " ", 20)) {

                }
                wait("...", 1);
                if (indexElement >= 2) {
                    if (!getParentTask().getConnection().sendKeyWithFrame("https://payments.youtube.com", "//input[contains(@type,'tel') or contains(@type,'search')]", indexElement, "#Backspace", 20)) {

                    }
                }
                countError++;
                result = getParentTask().getConnection().getJsWithFrame("https://payments.youtube.com", "return document.evaluate(\"//input[contains(@type,'tel') or contains(@type,'search')]\", document, null, 7, null).snapshotItem(" + indexElement + ").value;", 3, "Lấy giá trị ô " + title).replaceAll("â", "");
            }
        }
        if (indexElement == 0) {
            String result = getParentTask().getConnection().getJsWithFrame("https://payments.youtube.com", "return document.evaluate(\"//input[contains(@type,'tel') or contains(@type,'search')]\", document, null, 7, null).snapshotItem(" + indexElement + ").value;", 3, "Lấy giá trị ô " + title).replaceAll("â", "").replaceAll(" ", "");
            if (!result.equals(card.split("#")[indexValue])) {
                //wait("Lỗi info card ", 120);
                return false;
            }
        } else {
            String result = getParentTask().getConnection().getJsWithFrame("https://payments.youtube.com", "return document.evaluate(\"//input[contains(@type,'tel') or contains(@type,'search')]\", document, null, 7, null).snapshotItem(" + indexElement + ").value;", 3, "Lấy giá trị ô " + title).replaceAll("â", "");
            if (!result.equals(card.split("#")[indexValue])) {
                //wait("Lỗi info card ", 120);
                return false;
            }
        }
        return true;
    }

    @Override
    protected Boolean call() {
        TC.getInts().arrOpens.remove(getParentTask().getAccount().getUsername());

        try {

            if (!getParentTask().getConnection().loadUrl("https://www.youtube.com/premium", 120)) {
                //return onFinishFail("Không thể load trang đăng kí");
            }

            long startTime = System.currentTimeMillis();
            long currentTime = (System.currentTimeMillis() - startTime) / 1000;
            int checkLoadFullThanhToan = getParentTask().getConnection().GETLENGHT("//yt-button-renderer[contains(@id,'manage-subscription-button')]", 3, "");
            while (checkLoadFullThanhToan == 0) {
                if (currentTime >= 30) {
                    return onFinishFail("Load trang premium quá thời gian");
                }
                checkLoadFullThanhToan = getParentTask().getConnection().GETLENGHT("//yt-button-renderer[contains(@id,'manage-subscription-button')]", 3, "");
                wait("....", 3);
                currentTime = (System.currentTimeMillis() - startTime) / 1000;
            }

            if (getParentTask().getConnection().GETLENGHT("//a[contains(@href,'https://accounts.google.com/ServiceLogin')]", 3, "") >= 1) {
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                getParentTask().getAccount().updateData();
                return onFinishFail("Tài khoản đá login");
            }

            if (getParentTask().getConnection().GETLENGHT("//yt-button-renderer[contains(@id,'manage-subscription-button')]", 3, "") == 0) {
                if (getParentTask().getConnection().GETLENGHT("//a[contains(@href,'/paid_membership')]", 3, "") >= 2) {
                    getParentTask().getAccount().setPremium_expired(System.currentTimeMillis() + 2419200000L);
                    getParentTask().getAccount().updateData();
                    return onFinishSuccess();
                }
            }
            wait("...", 10);
            if (!getParentTask().getConnection().clickWithoutWait("//yt-button-renderer[contains(@id,'manage-subscription-button')]", 0)) {

                wait("Check code ...........", 3);
                if (!getParentTask().getConnection().clickWithoutWait("//yt-button-renderer[contains(@id,'manage-subscription-button')]", 0)) {
                    wait("Check code ...........", 3);
                    if (!getParentTask().getConnection().clickWithoutWait("//yt-button-renderer[contains(@id,'manage-subscription-button')]", 0)) {

                    }
                }
                if (getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//div", 3, "") == 0) {
                    wait("Check code click nút đăng kí  ...........", 120);
                    return onFinishFail("Không thể click nút đăng kí");
                }
            }


            
            wait("....", 3);

            if (getParentTask().getConnection().GETURL(3).contains("https://accounts.google.com/ServiceLogin/webreauth")) {
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGINED.getValue());
                getParentTask().getAccount().setStatus(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().updateData();
                return onFinishFail("Tài khoản die");
            }

            startTime = System.currentTimeMillis();
            currentTime = (System.currentTimeMillis() - startTime) / 1000;
            int checkFrameThanhToan = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//div", 3, "");
            while (checkFrameThanhToan == 0) {
                if (currentTime >= 30) {
                    return onFinishFail("Load trang thanh toán quá thời gian");
                }
                checkFrameThanhToan = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//div", 3, "");
                wait("....", 3);
                if(getParentTask().getConnection().GETURL(5).startsWith("https://www.youtube.com/paid_memberships?ybp=")){
                    getParentTask().getAccount().setPremium_expired(System.currentTimeMillis() +2419200000L);
                    getParentTask().getAccount().updateData();
                    return onFinishSuccess();
                }
                currentTime = (System.currentTimeMillis() - startTime) / 1000;
            }
            wait("....", 3);
            int paymentMCount = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//div[@role='radio']", 3, "");
            if (paymentMCount == 0) {
                if (getParentTask().getConnection().GETLENGHT("//a[contains(@href,'/paid_membership')]", 3, "") >= 2) {

                    wait("Check reg success ...........", 30);

                    getParentTask().getAccount().setPremium_expired(System.currentTimeMillis() + (2419200000L));
                    getParentTask().getAccount().updateData();
                    return onFinishSuccess();
                }
            }
            int countCheckHaveOldCard = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//span[contains(text(),'• • • •')]", 3, "");
            getParentTask().insertSuccessLog("Check đã gắn thẻ cũ " + countCheckHaveOldCard);

            if (countCheckHaveOldCard != 0) {

                if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.youtube.com", "//span[contains(text(),'• • • •')]", 0)) {
                    if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.youtube.com", "//button", 0)) {
                        //wait("lỗi confirm thanh toán bẳng thẻ- check lỗi", 120);
                        //return onFinishFail("Không confirm thanh toán bẳng thẻ");
                    }
                }
                wait("....", 5);

                int optionsCardCount = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//div[@role='option']", 3, "");

                getParentTask().getAccount().setCard_error(optionsCardCount - 2);
                getParentTask().getAccount().updateData();

                if (getParentTask().getAccount().getCard_error() >= 2) {
                    return onFinishFail("Đã gắn 2 thẻ lỗi");
                }

                getParentTask().insertSuccessLog("Số lựa chọn" + optionsCardCount + "/3");
                if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.youtube.com", "//div[@role='option']", optionsCardCount - 2)) {

                    wait("Check lỗi thể chọn pay bằng thẻ .......... ", 120);

                    return onFinishFail("Không thể chọn pay bằng thẻ");
                }
            } else {
                if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.youtube.com", "//div[@role='radio']", 0)) {

                    wait("Check lỗi thể chọn pay bằng thẻ .......... ", 120);

                    return onFinishFail("Không thể chọn pay bằng thẻ");
                }
                int countButton = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//button", 3, "");

                getParentTask().insertSuccessLog("Số nút submit thanh toán thẻ " + countButton);

                if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.youtube.com", "//button", 0)) {
                    //wait("lỗi confirm thanh toán bẳng thẻ- check lỗi", 120);
                    //return onFinishFail("Không confirm thanh toán bẳng thẻ");
                }

            }
            wait("....", 10);
            startTime = System.currentTimeMillis();
            currentTime = (System.currentTimeMillis() - startTime) / 1000;
            checkFrameThanhToan = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//button", 3, "");

            getParentTask().insertSuccessLog("Số frame thanh toán " + checkFrameThanhToan);
            while (checkFrameThanhToan == 1) {
                if (currentTime >= 30) {
                    wait("Check lỗi load form nhập card thời gian .......... ", 120);
                    return onFinishFail("Load form nhập card quá thời gian");
                }
                checkFrameThanhToan = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//button", 3, "");
                wait("....", 5);
                currentTime = (System.currentTimeMillis() - startTime) / 1000;
                if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.youtube.com", "//button", 0)) {
                    //wait("lỗi confirm thanh toán bẳng thẻ- check lỗi", 120);
                    //return onFinishFail("Không confirm thanh toán bẳng thẻ");
                }
            }
            wait("....", 3);

            int countTypeTell = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//input[contains(@type,'tel') or contains(@type,'search')]", 3, "");
            int countTypeSearch = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//input[contains(@type,'search')]", 3, "");
            getParentTask().insertSuccessLog("Số ô tell " + countTypeTell);
            getParentTask().insertSuccessLog("Số ô search " + countTypeSearch);

            if (!typeValue(0, 0, "Số thẻ")) {
                wait("Check lỗi nhập Số thẻ .......... ", 120);
                return onFinishFail("Không thể nhập số thẻ ");
            }

            if (!typeValue(1, 1, "Số ngày")) {
                return onFinishFail("Không thể nhập Số ngày ");
            }

            if (!typeValue(2, 2, "Số bí mật")) {
                wait("Check lỗi nhập nhập số bí mật.......... ", 120);
                return onFinishFail("Không thể nhập số bí mật ");
            }

            if (countTypeTell == 4) {

                if (!typeValue(3, 3, "Post code")) {
                    wait("Check lỗi nhập Post code.......... ", 120);
                    return onFinishFail("Không thể nhập Post code  ");
                }
            } else {

                if (!typeValue(3, countTypeTell - 1, "Post code")) {
                    wait("Check lỗi nhập Post code.......... ", 120);
                    return onFinishFail("Không thể nhập Post code  ");
                }
            }

            wait("Load trang nhập info", 3);

            int countButton = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//button", 3, "");

            getParentTask().insertSuccessLog("Số nút save thông tin thẻ " + countButton + "/2");

            if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.youtube.com", "//button", 1)) {
                wait("....", 10);
                if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.youtube.com", "//button", 1)) {
                    wait("....", 10);
                    if (getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//input[contains(@type,'tel') or contains(@type,'search')]", 3, "") >= 4) {
                        wait("Check lỗi save info card .......... ", 120);
                        return onFinishFail("Không thể save thông tin thẻ");
                    }
                }
            }

            startTime = System.currentTimeMillis();
            currentTime = (System.currentTimeMillis() - startTime) / 1000;
            int checkOInfoCard = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//input[contains(@type,'tel') or contains(@type,'search')]", 3, "");
            while (checkOInfoCard >= 4) {
                if (currentTime >= 30) {

                    if (!typeValue(0, 0, "Số thẻ lần 2")) {
                        return onFinishFail("Không thể nhập số thẻ ");
                    }

                    if (!typeValue(1, 1, "Số ngày lần 2")) {
                        return onFinishFail("Không thể nhập Số ngày ");
                    }

                    if (!typeValue(2, 2, "Số bí mật lần 2")) {
                        wait("Check lỗi nhập nhập số bí mật.......... ", 120);
                        return onFinishFail("Không thể nhập số bí mật ");
                    }
                    if (countTypeTell == 4) {
                        if (!typeValue(3, 3, "Post code lần 2")) {
                            wait("Check lỗi nhập Post code.......... ", 120);
                            return onFinishFail("Không thể nhập Post code  ");
                        }
                    } else {
                        if (!typeValue(3, countTypeTell - 1, "Post code")) {
                            wait("Check lỗi nhập Post code.......... ", 120);
                            return onFinishFail("Không thể nhập Post code  ");
                        }
                    }
                    if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.youtube.com", "//button", 1)) {
                        wait("....", 10);
                        if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.youtube.com", "//button", 1)) {
                            wait("....", 10);
                            if (getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//input[contains(@type,'tel') or contains(@type,'search')]", 3, "") >= 4) {
                                wait("Check lỗi save info card .......... ", 120);
                                return onFinishFail("Không thể save thông tin thẻ");
                            }
                        }
                    }
                    wait("Chờ save thẻ lần 2", 10);

                    if (getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//input[contains(@type,'tel') or contains(@type,'search')]", 3, "") >= 4) {
                        wait("Check lỗi save info card .......... ", 120);
                        return onFinishFail("Không thể save thông tin thẻ");
                    }
                }
                if (getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//a[contains(@href,'payment_error')]", 3, "") != 0) {
                    getParentTask().getAccount().setCard_error(getParentTask().getAccount().getCard_error() + 1);
                    getParentTask().getAccount().updateData();
                    return onFinishFail("Thẻ bị từ chối thanh toán");
                }
                checkOInfoCard = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//input[contains(@type,'tel') or contains(@type,'search')]", 3, "");
                wait("....", 3);
                currentTime = (System.currentTimeMillis() - startTime) / 1000;
            }
            wait("....", 8);

            countButton = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//button", 3, "");
            getParentTask().insertSuccessLog("Số nút submit confirm thông tin thẻ" + countButton);

            if (!getParentTask().getConnection().clickWithoutWaitWithFrame("https://payments.youtube.com", "//button", 0)) {
                wait("Không thể confirm đăng kí premium - check lỗi", 10);
                return onFinishFail("Không thể confirm chọn thẻ ");
            }

            wait("Comfirm đăng kí", 20);

            int checkLinkPaymentFail = getParentTask().getConnection().GETLENGHTFRAME("https://payments.youtube.com", "//a[contains(@href,'https://support.google.com/payments')]", 3, "");
            int checkLinkPaymentFail2 = getParentTask().getConnection().GETLENGHT("//a[contains(@href,'https://support.google.com/payments')]", 3, "");

            getParentTask().insertSuccessLog("Số link payment error " + (checkLinkPaymentFail + checkLinkPaymentFail2));
            if (checkLinkPaymentFail != 0 || checkLinkPaymentFail2 != 0) {
                getParentTask().getAccount().setCard_error(getParentTask().getAccount().getCard_error() + 1);
                getParentTask().getAccount().updateData();
                return onFinishFail("Lỗi thanh toán");
            }

            if (getParentTask().getConnection().GETLENGHT("//a[contains(@href,'/paid_membership')]", 3, "") >= 2) {
                if (!getParentTask().getConnection().clickWithoutWait("id('confirm-button')/a[@class='yt-simple-endpoint style-scope yt-button-renderer']/tp-yt-paper-button[@id='button']", 0)) {

                }
                if (!getParentTask().getConnection().loadUrl("https://www.youtube.com/premium", 120)) {
                    //return onFinishFail("Không thể load trang đăng kí");
                }
                wait("Load trang premium", 3);

                getParentTask().insertSuccessLog("Đã load trang premium");

                startTime = System.currentTimeMillis();
                currentTime = (System.currentTimeMillis() - startTime) / 1000;
                checkLoadFullThanhToan = getParentTask().getConnection().GETLENGHT("//yt-button-renderer[contains(@id,'manage-subscription-button')]", 3, "");
                while (checkLoadFullThanhToan == 0) {
                    if (currentTime >= 30) {
                        return onFinishFail("Load trang premium quá thời gian");
                    }
                    if (getParentTask().getConnection().GETLENGHT("//a[contains(@href,'/paid_membership')]", 3, "") >= 2) {
                        getParentTask().getAccount().setPremium_expired(System.currentTimeMillis() + (2419200000L));
                        getParentTask().getAccount().updateData();

                        int timeDelay  = TC.getInts().time_delay*60;
                    for (int i = 0; i < timeDelay; i++) {
                        ssc.base.ultil.ThreadUtils.Sleep(1000);
                        updateMyMessage("Chờ " + i + "/"+timeDelay+" s sau khi reg thành công");
                    }

                        return onFinishSuccess();
                    }
                    checkLoadFullThanhToan = getParentTask().getConnection().GETLENGHT("//yt-button-renderer[contains(@id,'manage-subscription-button')]", 3, "");
                    wait("....", 3);
                    currentTime = (System.currentTimeMillis() - startTime) / 1000;
                }
                getParentTask().insertSuccessLog("Đã vao trang premium check premium success");
                wait("Load trang premium lần 1", 10);

                if (getParentTask().getConnection().GETLENGHT("//a[contains(@href,'/paid_membership')]", 3, "") >= 2) {
                    getParentTask().getAccount().setPremium_expired(System.currentTimeMillis() + (2419200000L));
                    getParentTask().getAccount().updateData();
                    int timeDelay  = TC.getInts().time_delay*60;
                    for (int i = 0; i < timeDelay; i++) {
                        ssc.base.ultil.ThreadUtils.Sleep(1000);
                        updateMyMessage("Chờ " + i + "/"+timeDelay+" s sau khi reg thành công");
                    }

                    return onFinishSuccess();
                }
            }

            if (!getParentTask().getConnection().loadUrl("https://www.youtube.com/paid_memberships", 120)) {
                //return onFinishFail("Không thể load trang đăng kí");
            }
            wait("Load trang premium lần 2 ", 30);
            getParentTask().insertSuccessLog("Đã vao trang paid_memberships check premium success");
            int checkLinkSuccesss = getParentTask().getConnection().GETLENGHT("//a[contains(@href,'/paid_membership')]", 3, "");
            getParentTask().insertSuccessLog("số link paid_membership " + checkLinkSuccesss);

            if (checkLinkSuccesss != 0) {
                getParentTask().getAccount().setPremium_expired(System.currentTimeMillis() + (2419200000L));
                getParentTask().getAccount().updateData();
                 int timeDelay  = TC.getInts().time_delay*60;
                    for (int i = 0; i < timeDelay; i++) {
                        ssc.base.ultil.ThreadUtils.Sleep(1000);
                        updateMyMessage("Chờ " + i + "/"+timeDelay+" s sau khi reg thành công");
                    }
                return onFinishSuccess();
            }
            int checkLinkReg = getParentTask().getConnection().GETLENGHT("//a[contains(@href,'/premium')]", 3, "");
            getParentTask().insertSuccessLog("Số link dki lại " + checkLinkReg);
            if (checkLinkReg == 1) {

            }

            wait("Check Reggggggggggg", 120);
            return onFinishFail("Loi reg");

        } catch (Exception ex) {
            getParentTask().insertSuccessLog("Exception " + ex.getMessage());

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
