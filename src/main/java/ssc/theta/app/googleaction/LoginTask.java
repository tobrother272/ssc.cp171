/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.googleaction;

import java.io.File;
import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import ssc.base.global.TC;
import ssc.base.run.GoogleInteractiveChildTask;
import ssc.base.ultil.ApiHelper;
import ssc.base.ultil.MyFileUtils;
import ssc.base.ultil.StringUtils;
import static ssc.base.ultil.ThreadUtils.Sleep;
import static ssc.theta.app.api.MetaApi.createRequest;
import static ssc.theta.app.api.MetaApi.get2LineSms;
import static ssc.theta.app.api.MetaApi.getPhoneNumber;
import static ssc.theta.app.api.MetaApi.getPhoneNumberChoThueSimCode;
import static ssc.theta.app.api.MetaApi.getSMSCode;
import static ssc.theta.app.api.MetaApi.getSMSChoThueSimCode;
import ssc.theta.app.model.GoogleAccount.ACCOUNT_LOGIN_STATUS;
import ssc.theta.app.model.GoogleAccount.ACCOUNT_STATUS;

/**
 * @author simpl
 */
public class LoginTask extends GoogleInteractiveChildTask {

    public static String RECOVER_URL = "https://accounts.google.com/signin/v2/challenge/selection";
    public static String NOT_NOW_URL = "https://gds.google.com/web/chi";
    public static String ADD_BIRTHDAY_URL = "myaccount.google.com/interstitials/birthday";
    public static String CHANNEL_DEAD_URL = "upport.google.com/accounts/answer/";
    public static String ACCEPT_COLLECTION_URL = "myaccount.google.com/signinoptions/recovery-options-collection";
    public static String IPP_URL = "https://accounts.google.com/signin/v2/challenge/ipp";
    public static String LOGIN_REJECTED = "https://accounts.google.com/signin/v2/deniedsigninrejected";
    public static String KPE = "https://accounts.google.com/signin/v2/challenge/kpe";
    public static String DP = "https://accounts.google.com/signin/v2/challenge/dp";
    public static String IAP = "https://accounts.google.com/signin/v2/challenge/iap";
    public static String CHANGE_PASS = "https://accounts.google.com/signin/v2/changepassword";
    public static String DISABALED = "https://accounts.google.com/signin/v2/disabled";
    public static String WEBREAUTH = "https://accounts.google.com/ServiceLogin/webreauth";
    public static String unknownerror = "https://accounts.google.com/info/unknownerror";
    public static String newFeature = "https://accounts.google.com/signin/newfeatures";

    //  
    public static String BTN_CONFIRM_RECOVER = "//div[@role='presentation']//div[@data-challengetype='12']";
    public static String BTN_CONFIRM_PHONE_RECOVER = "//div[@role='presentation']//div[@data-challengetype='13']";

    public static String BTN_NOT_NOW = "//c-wiz//span[contains(text(),'Not now')]";
    public static String TXT_EMAIL_RECOVER = "//input[contains(@type,'email')]";
    public static String TXT_PHONE_RECOVER = "//input[contains(@type,'tel')]";
    public static String BTN_NEXT = "//button[child::span[contains(text(),'Tiếp theo') or contains(text(),'Next') or contains(text(),'다음') or contains(text(),'下一步') or contains(text(),'Напред') ]]";
    public static String TXT_USERNAME = "//input[contains(@id,'identifierId')]";
    public static String TXT_PASSWORD = "//input[@name='Passwd']";
    public static String BTN_NEXT_COLLECTION = "//div[contains(@role,'presentation')]//div[contains(@role,'button')]";
    public static String BTN_LOGIN = "//a[contains(@href,'https://accounts.google.com/ServiceLogin')]";
    public static String BTN_AVARTAR = "//button[contains(@id,'avatar-btn')]";

    //
    public static String TXT_OLD_USERNAME = "//input[contains(@id,'Email')]";

    public String er = "Lỗi : ";
    public String success = "Thành Công";
    public String complete = "Hoàn thành";
    private boolean sm = false;
    private boolean quit = true;

    public LoginTask(LoginAction task, boolean quit) {
        super(task);
        this.quit = quit;
    }

    public LoginTask(LoginAction task) {
        super(task);
        this.quit = true;
    }


    public boolean recoverByEmail() {
        try {
            if (getParentTask().getConnection().GETLENGHT(BTN_CONFIRM_RECOVER, 30, sm ? "Check Email" : "Kiểm tra nút email recover") == 0) {
                getParentTask().getAccount().setLogin_status_string(ACCOUNT_STATUS.WRONG_INFO.getValue());
                getParentTask().getAccount().updateData();
                updateMyMessage("Không có OPT email recover");
                return false;
            }
            if (!getParentTask().getConnection().clickWithPosition(BTN_CONFIRM_RECOVER, 0)) {
                return onFinishFail(sm ? "Click recover error" : "Không thể click nút gmail recover");
            }
            if (!getParentTask().getConnection().waitElementVisible(TXT_EMAIL_RECOVER, 120, sm ? "Waiting txt recover email " : "Đang chờ ô nhập recover")) {
                return onFinishFail(sm ? "Load recover text timeout" : "Chờ ô nhập recover quá thời gian");
            }
            if (!getParentTask().getConnection().sendKey(TXT_EMAIL_RECOVER, 0, getParentTask().getAccount().getGmail_recover(), 20)) {
                return onFinishFail(sm ? "Type recover error" : "Không thể nhập email recover");
            }
            if (!getParentTask().getConnection().clickWithPosition(BTN_NEXT, 0)) {
                return onFinishFail(sm ? "CLick submit recover error" : "Không thể click nút submit gmail recover");
            }
            if (!getParentTask().getConnection().waitElementInvisibale(TXT_EMAIL_RECOVER, 120, sm ? "Waiting recover invisible" : "Chờ sumit gmail recover ")) {
                return onFinishFail(sm ? "Cant recover account" : "Không thể submit gmail recover");
            }
            getParentTask().getConnection().waitForLoad(30);
            getParentTask().insertSuccessLog(sm ? "Recover account success" : "Xác mình gmail recover thành công");
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean recoverByPHONE() {
        try {
            if (getParentTask().getConnection().GETLENGHT(BTN_CONFIRM_PHONE_RECOVER, 30, "Kiểm tra nút phone recover") == 0) {
                getParentTask().getAccount().setLogin_status_string(ACCOUNT_STATUS.WRONG_INFO.getValue());
                getParentTask().getAccount().updateData();
                updateMyMessage("Không có OPT email recover");
                return false;
            }
            if (!getParentTask().getConnection().clickWithPosition(BTN_CONFIRM_PHONE_RECOVER, 0)) {
                return onFinishFail(sm ? "Click recover error" : "Không thể click nút gmail recover");
            }
            if (!getParentTask().getConnection().waitElementVisible(TXT_PHONE_RECOVER, 120, "Đang chờ ô nhập phone recover")) {
                return onFinishFail(sm ? "Load recover text timeout" : "Chờ ô nhập recover quá thời gian");
            }
            if (!getParentTask().getConnection().sendKey(TXT_PHONE_RECOVER, 0, getParentTask().getAccount().getPhone_recover(), 20)) {
                return onFinishFail("Không thể nhập phone recover");
            }
            if (!getParentTask().getConnection().clickWithPosition(BTN_NEXT, 0)) {
                return onFinishFail(sm ? "CLick submit recover error" : "Không thể click nút submit gmail recover");
            }
            if (!getParentTask().getConnection().waitElementInvisibale(TXT_PHONE_RECOVER, 120, "Chờ sumit phone recover ")) {
                return onFinishFail("Không thể submit phone recover");
            }
            getParentTask().getConnection().waitForLoad(30);
            getParentTask().insertSuccessLog("Xác mình phone recover thành công");
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean addAdvInfomation() {
        try {
            if (getParentTask().getConnection().GETLENGHT(BTN_NOT_NOW, 30, sm ? "Checking notnow" : "Kiểm tra nút notnow") == 0) {
                return false;
            }
            if (!getParentTask().getConnection().clickWithPosition(BTN_NOT_NOW, 0)) {
                return onFinishFail(sm ? "Click notnow error" : "Không thể click nút notnow");
            }
            getParentTask().getConnection().waitForLoad(30);
            getParentTask().insertSuccessLog(sm ? "Clicked notnow button" : "Đã xác nhận notnow");
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean addBirthday() {
        try {
            /*
            if (getParentTask().getConnection().GETLENGHT(BTN_NOT_NOW, 30,sm?"Checking notnow": "Kiểm tra nút notnow") == 0) {

            }
            if (!getParentTask().getConnection().clickWithPosition(BTN_NOT_NOW, 0)) {

            }
            getParentTask().getConnection().waitForLoad(30);
            getParentTask().insertSuccessLog(sm?"Birthday inserted":"Đã thêm ngày sinh");
            return true;
             */
            return true;
        } catch (Exception e) {

        }
        return false;
    }

    public boolean acceptCollection() {
        try {
            if (!getParentTask().getConnection().clickWithPosition(BTN_NEXT_COLLECTION, 1)) {

            }
            if (!getParentTask().getConnection().waitElementInvisibale(BTN_NEXT_COLLECTION, 10, sm ? "Wating submit collection" : "Chờ submit accept collect")) {

            }
            getParentTask().getConnection().waitForLoad(30);
            getParentTask().insertSuccessLog(sm ? "Clicked collection" : "Đã accept collection data");
            return true;
        } catch (Exception e) {

        }
        return true;
    }

    public boolean changePass() {
        try {

            String newPass = StringUtils.getRandomPass(10);

            if (getParentTask().getConnection().GETLENGHT("//input[@name='Passwd']", 10, "Check ổ đổi mật khẩu") == 0) {
                return false;
            }
            if (!getParentTask().getConnection().sendKey("//input[@name='Passwd']", 0, newPass, 10)) {
                return false;
            }
            if (!getParentTask().getConnection().sendKey("//input[@name='ConfirmPasswd']", 0, newPass, 10)) {
                return false;
            }
            if (!getParentTask().getConnection().clickWithPosition("//div[@id='changepasswordNext']", 0)) {
                return false;
            }
            if (!getParentTask().getConnection().waitElementInvisibale("//input[@name='ConfirmPasswd']", 10, sm ? "Wating submit new passwd" : "Chờ Lưu mật khẩu mới")) {
                return false;
            }
            getParentTask().getConnection().waitForLoad(30);
            getParentTask().insertSuccessLog(sm ? "New pass changed" : "Đã lưu mật khẩu mới");
            getParentTask().getAccount().setPassword(newPass);
            getParentTask().getAccount().updateData();
            return true;
        } catch (Exception e) {

        }
        return true;
    }

    String currentUrl = "";

    public boolean verifyAccount() {
        try {
            String verityCode = "";
            String phoneNumber = "";
            String id = "";
            int countErrorInputPhone = 0;
            do {
                if (countErrorInputPhone >= 3) {
                    updateMessage("Không thể verify sau 3 lần");
                    return false;
                }
                int numberGetPhoneError = 0;
                do {
                    if (numberGetPhoneError >= 3) {
                        updateMessage("Không thể lấy phone");
                        return false;
                    }
                    String result[] = getPhoneNumber(TC.getInts().Viotp_key);
                    phoneNumber = result[0];
                    id = result[1];
                    numberGetPhoneError++;
                    getParentTask().waitMessage("Chờ lấy số điện thoại", 6);
                } while (phoneNumber.length() == 0);
                insertSuccessLog("[" + numberGetPhoneError + "]Đã lấy phone " + phoneNumber);
                if (!getParentTask().getConnection().sendKey("//input[contains(@id,'deviceAddress')]", 0, phoneNumber, 10)) {
                    return false;
                }
                if (!getParentTask().getConnection().clickWithoutTimeout("//input[contains(@id,'next-button')]")) {

                    wait("Check code", 3000);

                    return false;
                }
                countErrorInputPhone++;
            } while (!getParentTask().getConnection().waitElementVisible("//input[contains(@id,'smsUserPin')]", 5, "Chờ xác minh phone"));

            int maxTimeGetSMS = 60;
            long startGetSMS = System.currentTimeMillis();
            long currentTime = 0;
            do {
                currentTime = (System.currentTimeMillis() - startGetSMS) / 1000;
                if (currentTime >= maxTimeGetSMS) {
                    return false;
                }
                updateMessage("Đang check sms " + currentTime + "/" + maxTimeGetSMS);
                verityCode = getSMSCode(TC.getInts().Viotp_key, id);
                wait("Chờ lấy sms xác minh", 3);
            } while (verityCode.length() == 0);

            insertSuccessLog("[" + currentTime + "]Đã lấy code " + verityCode);

            if (!getParentTask().getConnection().sendKey("//input[contains(@id,'smsUserPin')]", 0, verityCode, 10)) {
                return false;
            }
            if (!getParentTask().getConnection().click("//input[contains(@id,'next-button')]")) {
                return false;
            }
            if (!getParentTask().getConnection().waitElementInvisibale("//input[contains(@id,'smsUserPin')]", 10, "chờ submit code")) {
                return false;
            }

            //getParentTask().getAccount().setVerifyTime(System.currentTimeMillis());
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean chothuesimCodeVerify() {
        try {
            String verityCode = "";
            String phoneNumber = "";
            String id = "";
            int countErrorInputPhone = 0;
            do {
                if (countErrorInputPhone >= 3) {
                    updateMessage("Không thể verify sau 3 lần");
                    return false;
                }
                int numberGetPhoneError = 0;
                do {
                    if (numberGetPhoneError >= 3) {
                        updateMessage("Không thể lấy phone");
                        return false;
                    }
                    String result[] = getPhoneNumberChoThueSimCode(TC.getInts().simcode);
                    phoneNumber = result[0];
                    id = result[1];
                    numberGetPhoneError++;
                    getParentTask().waitMessage("Chờ lấy số điện thoại", 6);
                } while (phoneNumber.length() == 0);
                insertSuccessLog("[" + numberGetPhoneError + "]Đã lấy phone " + phoneNumber);
                if (!getParentTask().getConnection().sendKey("//input[contains(@id,'deviceAddress')]", 0, phoneNumber, 10)) {
                    return false;
                }
                if (!getParentTask().getConnection().clickWithoutTimeout("//input[contains(@id,'next-button')]")) {

                    wait("Check code", 3000);

                    return false;
                }
                countErrorInputPhone++;
            } while (!getParentTask().getConnection().waitElementVisible("//input[contains(@id,'smsUserPin')]", 5, "Chờ xác minh phone"));

            int maxTimeGetSMS = 60;
            long startGetSMS = System.currentTimeMillis();
            long currentTime = 0;
            do {
                currentTime = (System.currentTimeMillis() - startGetSMS) / 1000;
                if (currentTime >= maxTimeGetSMS) {
                    return false;
                }
                updateMessage("Đang check sms " + currentTime + "/" + maxTimeGetSMS);
                verityCode = getSMSChoThueSimCode(TC.getInts().simcode, id);
                wait("Chờ lấy sms xác minh", 3);
            } while (verityCode.length() == 0);

            insertSuccessLog("[" + currentTime + "]Đã lấy code " + verityCode);

            if (!getParentTask().getConnection().sendKey("//input[contains(@id,'smsUserPin')]", 0, verityCode, 10)) {
                return false;
            }
            if (!getParentTask().getConnection().click("//input[contains(@id,'next-button')]")) {
                return false;
            }
            if (!getParentTask().getConnection().waitElementInvisibale("//input[contains(@id,'smsUserPin')]", 10, "chờ submit code")) {
                return false;
            }

            //getParentTask().getAccount().setVerifyTime(System.currentTimeMillis());
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean verifyAccountVia2Line() {
        try {
            String verityCode = "";
            String phoneNumber = "";
            long order_id = 0;
            int countErrorInputPhone = 0;
            int maxTimeGetSMS = 120;
            do {
                phoneNumber = "";
                order_id = 0;
                if (countErrorInputPhone >= 3) {
                    updateMessage("Không thể verify sau 3 lần");
                    return false;
                }
                int numberCreateRequestError = 0;
                do {
                    if (numberCreateRequestError >= 3) {
                        updateMessage("Không thể lấy phone");
                        return false;
                    }
                    order_id = createRequest(TC.getInts().secondLine);
                    numberCreateRequestError++;
                    getParentTask().waitMessage("Chờ lấy order id", 6);

                } while (order_id == 0);

                insertSuccessLog("[" + numberCreateRequestError + "]Đã tạo order " + order_id);

                long startGetSMS = System.currentTimeMillis();
                long currentTime = 0;
                do {
                    currentTime = (System.currentTimeMillis() - startGetSMS) / 1000;
                    if (currentTime >= maxTimeGetSMS) {
                        return false;
                    }
                    updateMessage("Đang check số phone từ order " + currentTime + "/" + maxTimeGetSMS);
                    phoneNumber = getPhoneNumber(TC.getInts().secondLine, order_id);
                    wait("Chờ lấy số xác minh xác minh", 3);
                } while (phoneNumber.length() == 0);

                insertSuccessLog("[" + numberCreateRequestError + "]Đã lấy được số" + phoneNumber);

                if (!getParentTask().getConnection().sendKey("//input[contains(@id,'deviceAddress')]", 0, phoneNumber, 10)) {
                    return false;
                }
                if (!getParentTask().getConnection().clickWithoutTimeout("//input[contains(@id,'next-button')]")) {

                    wait("Check code", 3000);

                    return false;
                }
                countErrorInputPhone++;
            } while (!getParentTask().getConnection().waitElementVisible("//input[contains(@id,'smsUserPin')]", 5, "Chờ xác minh phone"));

            long startGetSMS = System.currentTimeMillis();
            long currentTime = 0;
            do {
                currentTime = (System.currentTimeMillis() - startGetSMS) / 1000;
                if (currentTime >= maxTimeGetSMS) {
                    return false;
                }
                updateMessage("Đang check sms " + currentTime + "/" + maxTimeGetSMS);
                verityCode = get2LineSms(TC.getInts().secondLine, order_id);
                wait("Chờ lấy sms xác minh", 3);
            } while (verityCode.length() == 0);

            insertSuccessLog("[" + currentTime + "]Đã lấy code " + verityCode);

            if (!getParentTask().getConnection().sendKey("//input[contains(@id,'smsUserPin')]", 0, verityCode, 10)) {
                return false;
            }
            if (!getParentTask().getConnection().click("//input[contains(@id,'next-button')]")) {
                return false;
            }
            if (!getParentTask().getConnection().waitElementInvisibale("//input[contains(@id,'smsUserPin')]", 10, "chờ submit code")) {
                return false;
            }

            //getParentTask().getAccount().setVerifyTime(System.currentTimeMillis());
            return true;
        } catch (Exception e) {
        }
        return false;
    }

    public boolean checkNext() {
        try {
            currentUrl = getParentTask().getConnection().GETURL(30);

            getParentTask().insertSuccessLog(currentUrl);
            if (currentUrl.startsWith("https://www.youtube.com/")) {
                getParentTask().insertSuccessLog(sm ? "Checking step :Youtube " : "Check Next Step : Đã vào youtube");
                return true;
            }

            Sleep(1000);
            if (currentUrl.startsWith(RECOVER_URL)) {
                getParentTask().insertSuccessLog("Cần recover tài khoản");
                if (getParentTask().getConnection().GETLENGHT(BTN_CONFIRM_PHONE_RECOVER, 30, "Kiểm tra nút phone recover") == 0) {
                    return recoverByEmail();
                } else {
                    return recoverByPHONE();
                }
            } else if (currentUrl.contains(DISABALED)) {
                getParentTask().insertSuccessLog(sm ? "Checking step :Disabled " : "Check Next Step : Tài khoản disable");
                getParentTask().getAccount().setLogin_status_string(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().updateData();
                updateMyMessage("Tài khoản chết");
                return false;
            } else if (currentUrl.startsWith(NOT_NOW_URL)) {
                getParentTask().insertSuccessLog(sm ? "Checking step :Notnow " : "Check Next Step : Cần thêm thông tin mở rộng tài khoản");
                if (!addAdvInfomation()) {
                    if (getParentTask().getConnection().loadUrl("https://www.youtube.com/", 120)) {
                        getParentTask().insertSuccessLog("Đã load trang youtube");
                    }
                    if (getParentTask().getConnection().waitElementVisible(BTN_AVARTAR, 10, "Đang check login")) {
                        return true;
                    }
                    if (!getParentTask().getConnection().clickWithWaitTimeout(BTN_LOGIN, 0, 120)) {
                        return false;
                    }
                    if (getParentTask().getConnection().waitElementVisible(BTN_AVARTAR, 10, "Đang check login")) {
                        return true;
                    }
                    return false;
                }
                return true;
            } else if (currentUrl.contains(ADD_BIRTHDAY_URL)) {
                getParentTask().insertSuccessLog(sm ? "Checking step :Birthday require " : "Check Next Step : Cần thêm ngày sinh");
                //Sleep(30000);
                return addBirthday();
            } else if (currentUrl.contains(LOGIN_REJECTED)) {
                getParentTask().insertSuccessLog(sm ? "Checking step :Cant login account" : "Check Next Step : Không thể login account");
                getParentTask().getAccount().setStatus(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().setStatus_string(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().updateData();
                updateMyMessage("Tài khoản không thể login");
                return false;
            } else if (currentUrl.contains(KPE)) {
                getParentTask().insertSuccessLog(sm ? "Checking step : Spam login" : "Check Next Step : Spam login");
                getParentTask().getAccount().setStatus(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().setStatus_string(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().updateData();
                updateMyMessage("Spam login thử lại sau");
                return false;
            } else if (currentUrl.startsWith(IPP_URL)) {
                getParentTask().insertSuccessLog(sm ? "Checking step :IPP " : "Check Next Step : Xác mình bằng sdt IPP");
                getParentTask().getAccount().setStatus(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().setStatus_string(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().updateData();
                updateMyMessage("Cần xác mình bằng phone cũ");
                return false;
            } else if (currentUrl.startsWith(DP)) {
                getParentTask().insertSuccessLog(sm ? "Checking step :Device backup " : "Check Next Step : Xác mình bằng thiết bị");
                getParentTask().getAccount().setStatus(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().setStatus_string(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().updateData();
                updateMyMessage("Cần xác mình bằng phone cũ");
                return false;
            } else if (currentUrl.contains(CHANNEL_DEAD_URL)) {
                getParentTask().getAccount().setStatus(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().setStatus_string(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().updateData();
                return onFinishFail(sm ? "Checking step :Youtube Channel Dead " : "Check Next Step : Tài khoản chết kênh");
            } else if (currentUrl.contains(ACCEPT_COLLECTION_URL)) {
                getParentTask().insertSuccessLog(sm ? "Checking step :Info Collection " : "Check Next Step : Accept recover");
                return acceptCollection();
            } else if (currentUrl.contains(CHANGE_PASS)) {
                getParentTask().insertSuccessLog(sm ? "Checking step :Change pass " : "Check Next Step : Cần đổi pass");
                if (!changePass()) {
                    return false;
                }
                return true;
            } else if (currentUrl.startsWith("https://www.youtube.com/")) {
                getParentTask().insertSuccessLog(sm ? "Checking step :Youtube " : "Check Next Step : Đã vào youtube");
                return true;
            } else if (currentUrl.contains("speedbump/idvreenable")) {
                if (TC.getInts().Viotp_key.length() != 0) {
                    if (!verifyAccount()) {
                        getParentTask().insertSuccessLog(sm ? "Account verify " : "Check Next Step : Tài khoản bị verify");
                        getParentTask().getAccount().setStatus(ACCOUNT_STATUS.VERIFY.getValue());
                        getParentTask().getAccount().setStatus_string(ACCOUNT_STATUS.VERIFY.getValue());
                        getParentTask().getAccount().updateData();
                        updateMyMessage("Tài khoản bị verify");
                        return false;
                    }
                } else if (TC.getInts().secondLine.length() != 0) {
                    if (!verifyAccountVia2Line()) {
                        getParentTask().insertSuccessLog(sm ? "Account verify " : "Check Next Step : Tài khoản bị verify");
                        getParentTask().getAccount().setStatus(ACCOUNT_STATUS.VERIFY.getValue());
                        getParentTask().getAccount().setStatus_string(ACCOUNT_STATUS.VERIFY.getValue());
                        getParentTask().getAccount().updateData();
                        updateMyMessage("Tài khoản bị verify");
                        return false;
                    }
                } else if (TC.getInts().simcode.length() != 0) {
                    if (!chothuesimCodeVerify()) {
                        getParentTask().insertSuccessLog(sm ? "Account verify " : "Check Next Step : Tài khoản bị verify");
                        getParentTask().getAccount().setStatus(ACCOUNT_STATUS.VERIFY.getValue());
                        getParentTask().getAccount().setStatus_string(ACCOUNT_STATUS.VERIFY.getValue());
                        getParentTask().getAccount().updateData();
                        updateMyMessage("Tài khoản bị verify");
                        return false;
                    }
                } else {
                    return false;
                }

                return true;
            } else if (currentUrl.startsWith(IAP)) {
                getParentTask().insertSuccessLog(sm ? "Account verify " : "Check Next Step : Tài khoản cần phone để login");
                getParentTask().getAccount().setStatus(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().setStatus_string(ACCOUNT_STATUS.VERIFY.getValue());
                getParentTask().getAccount().updateData();
                updateMyMessage("Tài khoản cần phone login");
                return false;
            } else if (currentUrl.startsWith(unknownerror)) {
                getParentTask().insertSuccessLog(sm ? "Checking step : Exeption " : "Check Next Step : Không xác định");
                updateMyMessage("Lỗi không xác định");
                getParentTask().getAccount().updateData();
                return false;
            } else if (currentUrl.startsWith(newFeature)) {
                //wait("Code di", 1000);
                try {
                    if (!getParentTask().getConnection().clickWithPosition("//form//div[@role='button']", 0)) {
                        return false;
                    }
                    if (!getParentTask().getConnection().clickWithPosition("//form//div[@role='button']", 0)) {
                        return false;
                    }
                    if (!getParentTask().getConnection().clickWithPosition("//form//div[@role='button']", 0)) {
                        //return false;
                    }
                    wait("cho hien", 5);
                    if (!getParentTask().getConnection().clickWithPosition("//form//div[@role='button']", 1)) {
                        return false;
                    }
                    wait("cho hien", 10);
                    return true;
                } catch (Exception e) {
                }

            } else if (currentUrl.startsWith("https://accounts.google.com/speedbump/gaplustos")) {
                //wait("Code di", 1000);
                try {
                    if (!getParentTask().getConnection().clickWithPosition("//form//input[@id='confirm']", 0)) {
                        return false;
                    }
                    wait("Accept speedbum gaplustos", 10);
                    return true;
                } catch (Exception e) {
                }
            } else {

                getParentTask().insertSuccessLog("Check Next Step : Không xác định");
                getParentTask().getAccount().updateData();
                updateMyMessage("Lỗi không xác định");
                //Sleep(60000 * 4);
                return false;
            }
        } catch (Exception e) {
        }
        return false;
    }

    boolean removeProfile = false;

    @Override
    protected Boolean call() {
        String cookiesFolder = System.getProperty("user.dir") + File.separator + "cookies";
        MyFileUtils.createFolder(cookiesFolder);
        String cookiesFile = cookiesFolder + File.separator + System.currentTimeMillis() + ".json";
        try {
            TC.getInts().arrOpens.remove(getParentTask().getAccount().getUsername());
            if (!getParentTask().getConnection().loadUrlWithOutTimeout("https://www.youtube.com/", 60)) {

                getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                
                if (getParentTask().getConnection().clearCache()) {

                }

                getParentTask().waitMessage("Check không thể load youtube...........", 30);

                return onFinishFail("Không thể load trang youtube");
            }

            getParentTask().waitMessage("load trang youtube", 10);

            long startTime = System.currentTimeMillis();
            long currentTime = (System.currentTimeMillis() - startTime) / 1000;

            while (currentTime < 60) {
                currentTime = (System.currentTimeMillis() - startTime) / 1000;
                Sleep(3000);
                if (getParentTask().getConnection().GETLENGHT("//ytd-logo//yt-icon[contains(@id,'logo-icon')]", 3, "") != 0) {
                    break;
                }
                updateMessage("chờ load trang youtube " + currentTime + "/60");
            }
            /*
            if (getParentTask().getConnection().waitElementVisible("//tp-yt-paper-dialog//tp-yt-paper-button", 5, "Check accept cookies 1")) {
                if (getParentTask().getConnection().runCustomJs("document.evaluate('//tp-yt-paper-dialog//tp-yt-paper-button', document, null, 7, null).snapshotItem(document.evaluate('//tp-yt-paper-dialog//tp-yt-paper-button', document, null, 7, null).snapshotLength-2).click();", "Off Accept cookies")) {
                    getParentTask().insertSuccessLog("Đã click accept cookies form vuông");
                }
            }

            if (getParentTask().getConnection().waitElementVisible("//tp-yt-paper-dialog//ytd-button-renderer", 5, "Check accept cookies 2")) {
                if (getParentTask().getConnection().runCustomJs("document.evaluate('//tp-yt-paper-dialog//ytd-button-renderer//button', document, null, 7, null).snapshotItem(1).click();", "Off Accept cookies")) {
                    getParentTask().insertSuccessLog("Đã click accept cookies form tròn");
                } else {
                    getParentTask().waitMessage("check accept cookie diiiiiiiiiiii", 120);
                }
            }

            if (getParentTask().getConnection().GETURL(5).contains("https://consent.youtube.com/")) {
                getParentTask().waitMessage("Check form google consent.youtube.com", 120);
            }

            */

            if (getParentTask().getConnection().GETLENGHT(BTN_LOGIN, 3, "") == 0) {
                getParentTask().getAccount().setStatus(ACCOUNT_STATUS.LIVE.getValue());
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGINED.getValue());
                getParentTask().getAccount().updateData();
                new File(cookiesFile);
                if (getParentTask().getConnection().exportCookies(cookiesFile, 10)) {
                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(new FileReader(cookiesFile));
                    JSONArray cookes = (JSONArray) obj;
                    JSONObject cookiesObj = new JSONObject();
                    cookiesObj.put("cookies", cookes);
                    getParentTask().getAccount().setCookies(cookiesObj.toJSONString());
                    getParentTask().getAccount().updateData();
                }
                if (!getParentTask().getConnection().loadUrl("https://www.youtube.com/premium_benefits", 120)) {
                    //return onFinishFail("Không thể load trang đăng kí");
                }

                if (getParentTask().getConnection().waitElementVisible("//ytd-member-hub-profile-renderer//div[contains(@id,'details-container')]", 5, "check premium")) {

                    String dateExpired = getParentTask().getConnection().getJs("return document.evaluate(\"//ytd-member-hub-profile-renderer//div[contains(@id,'details-container')]//span[contains(@class,'yt-formatted-strin')]\",document,null,7,null).snapshotItem(1).textContent", 5, "");
                    long et = StringUtils.getLongFromDateString("MMMM dd, yyyy", dateExpired);
                    getParentTask().getAccount().setPremium_expired(et);
                    getParentTask().getAccount().updateData();
                    getParentTask().insertSuccessLog("Đã có premium");
                }
                return onFinishSuccess();
            }

            if (!getParentTask().getConnection().clickWithoutWait(BTN_LOGIN, 0)) {
                getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                return onFinishFail(sm ? "Click login button error" : "Không thể click nút login");
            }
            //getParentTask().getConnection().waitForLoad(30);

            startTime = System.currentTimeMillis();
            currentTime = (System.currentTimeMillis() - startTime) / 1000;

            while (getParentTask().getConnection().GETLENGHT(TXT_USERNAME, 3, "") == 0) {
                currentTime = (System.currentTimeMillis() - startTime) / 1000;
                Sleep(5000);
                if (currentTime > 60) {
                    return onFinishFail("Load trang login quá thời gian");
                }
                String currentUrl = getParentTask().getConnection().GETURL(3);
                if (currentUrl.startsWith("https://www.youtube.com/") && getParentTask().getConnection().GETLENGHT(BTN_LOGIN, 3, "") == 0) {
                    getParentTask().getAccount().setStatus(ACCOUNT_STATUS.LIVE.getValue());
                    getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGINED.getValue());
                    getParentTask().getAccount().updateData();
                    new File(cookiesFile);
                    if (getParentTask().getConnection().exportCookies(cookiesFile, 10)) {
                        JSONParser parser = new JSONParser();
                        Object obj = parser.parse(new FileReader(cookiesFile));
                        JSONArray cookes = (JSONArray) obj;
                        JSONObject cookiesObj = new JSONObject();
                        cookiesObj.put("cookies", cookes);
                        getParentTask().getAccount().setCookies(cookiesObj.toJSONString());
                        getParentTask().getAccount().updateData();
                    }
                    if (currentUrl.startsWith(WEBREAUTH)) {
                        getParentTask().insertSuccessLog(sm ? "Account verify " : "Check Next Step : Tài khoản bị verify");
                        getParentTask().getAccount().setStatus_string(ACCOUNT_STATUS.VERIFY.getValue());
                        getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                        getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                        getParentTask().getAccount().updateData();

                        updateMyMessage("Tài khoản bị verify");
                        return false;
                    }
                    if (!getParentTask().getConnection().loadUrl("https://www.youtube.com/premium_benefits", 120)) {
                        //return onFinishFail("Không thể load trang đăng kí");
                    }

                    if (getParentTask().getConnection().waitElementVisible("//ytd-member-hub-profile-renderer//div[contains(@id,'details-container')]", 5, "check premium")) {

                        String dateExpired = getParentTask().getConnection().getJs("return document.evaluate(\"//ytd-member-hub-profile-renderer//div[contains(@id,'details-container')]//span[contains(@class,'yt-formatted-strin')]\",document,null,7,null).snapshotItem(1).textContent", 5, "");
                        long et = StringUtils.getLongFromDateString("MMMM dd, yyyy", dateExpired);
                        getParentTask().getAccount().setPremium_expired(et);
                        getParentTask().getAccount().updateData();
                        getParentTask().insertSuccessLog("Đã có premium");
                    }
                    return onFinishSuccess();
                }

                updateMessage("Chờ load trang nhập username " + currentTime + "/60");
            }
            if (!getParentTask().getConnection().sendKey(TXT_USERNAME, 0, getParentTask().getAccount().getUsername(), 20)) {
                getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                return onFinishFail(sm ? "Type username error" : "Không thể nhập username");
            }
            if (!getParentTask().getConnection().clickWithPosition("//div[@id='identifierNext']", 0)) {
                getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                return onFinishFail(sm ? "Click submit username error" : "Không thể xác nhận username");
            }

            getParentTask().waitMessage("submit username", 5);

            startTime = System.currentTimeMillis();
            currentTime = (System.currentTimeMillis() - startTime) / 1000;

            while (getParentTask().getConnection().GETLENGHT(TXT_PASSWORD, 3, "Check passwd") == 0) {
                currentTime = (System.currentTimeMillis() - startTime) / 1000;
                Sleep(3000);
                if (currentTime > 60) {
                    return onFinishFail("Không thể nhập username");
                }
                if (getParentTask().getConnection().getAttibute(TXT_USERNAME, "aria-invalid", "Check username", 0, 30).equals("true")) {
                    getParentTask().getAccount().setStatus_string(ACCOUNT_STATUS.WRONG_INFO.getValue());
                    getParentTask().getAccount().updateData();
                    getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                    ///getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.g etValue());
                    return onFinishFail("Sai username");
                }
                if (!getParentTask().getConnection().clickWithPosition("//div[@id='identifierNext']", 0)) {

                }
                updateMessage("Chờ submit username " + currentTime + "/60");
            }

            if (!getParentTask().getConnection().sendKey(TXT_PASSWORD, 0, getParentTask().getAccount().getPassword(), 20)) {
                getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                return onFinishFail(sm ? "Type password error " : "Không thể nhập mật khẩu");
            }
            if (!getParentTask().getConnection().clickWithPosition("//div[@id='passwordNext']", 0)) {
                getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                return onFinishFail(sm ? "CLick submit password error" : "Không thể xác nhận mật khẩu");
            }

            startTime = System.currentTimeMillis();
            currentTime = (System.currentTimeMillis() - startTime) / 1000;

            while (getParentTask().getConnection().GETLENGHT(TXT_PASSWORD, 3, "") != 0) {
                currentTime = (System.currentTimeMillis() - startTime) / 1000;
                Sleep(3000);
                if (currentTime > 60) {
                    return onFinishFail("Không thể submit mật khẩu");
                }
                if (getParentTask().getConnection().getJs("return document.evaluate(\"" + TXT_PASSWORD + "\", document, null, 7, null).snapshotItem(0).getAttribute(\"aria-invalid\")", 10, "").contains("true")) {
                    getParentTask().getAccount().setStatus_string(ACCOUNT_STATUS.WRONG_INFO.getValue());
                    getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                    getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                    return onFinishFail("Sai mật khẩu");
                }
                if (!getParentTask().getConnection().clickWithPosition("//div[@id='passwordNext']", 0)) {

                }
                updateMessage("Chờ submit passwd " + currentTime + "/60");
            }

            if (!checkNext()) {
                getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());

                if (getParentTask().getAccount().getStatus().equals(ACCOUNT_STATUS.VERIFY.getValue())) {
                    return onFinishFail("Account verify");
                }
                return onFinishFail("Không thể next");
            }
            if (!checkNext()) {
                getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                if (getParentTask().getAccount().getStatus().equals(ACCOUNT_STATUS.VERIFY.getValue())) {
                    return onFinishFail("Account verify");
                }
                return onFinishFail("Không thể next");
            }
            if (!checkNext()) {
                getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                if (getParentTask().getAccount().getStatus().equals(ACCOUNT_STATUS.VERIFY.getValue())) {
                    return onFinishFail("Account verify");
                }
                return onFinishFail("Không thể next");
            }
            if (!checkNext()) {
                getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                if (getParentTask().getAccount().getStatus().equals(ACCOUNT_STATUS.VERIFY.getValue())) {
                    return onFinishFail("Account verify");
                }
                return onFinishFail("Không thể next");
            }
            if (!checkNext()) {
                getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGOUT.getValue());
                if (getParentTask().getAccount().getStatus().equals(ACCOUNT_STATUS.VERIFY.getValue())) {
                    return onFinishFail("Account verify");
                }
                return onFinishFail("Không thể next");
            }
            getParentTask().getAccount().setStatus(ACCOUNT_STATUS.LIVE.getValue());
            getParentTask().getAccount().setStatus_string(ACCOUNT_STATUS.LIVE.getValue());
            getParentTask().getAccount().setLogin_status_string(ACCOUNT_LOGIN_STATUS.LOGINED.getValue());
            getParentTask().getAccount().setLogin_status(ACCOUNT_LOGIN_STATUS.LOGINED.getValue());

            getParentTask().getAccount().updateData();

            new File(cookiesFile);
            if (getParentTask().getConnection().exportCookies(cookiesFile, 10)) {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(cookiesFile));
                JSONArray cookes = (JSONArray) obj;
                JSONObject cookiesObj = new JSONObject();
                cookiesObj.put("cookies", cookes);
                getParentTask().getAccount().setCookies(cookiesObj.toJSONString());
                getParentTask().getAccount().updateData();
            }
            if (TC.getInts().account_api.length() != 0 && TC.getInts().account_token.length() != 0) {
                ApiHelper.getDataWithAccessTokenAndUrl(TC.getInts().account_api + "/account/live?usename=" + getParentTask().getAccount().getUsername(), TC.getInts().account_token);
                //
            }

            if (!getParentTask().getConnection().loadUrl("https://www.youtube.com/premium_benefits", 120)) {
                //return onFinishFail("Không thể load trang đăng kí");
            }

            if (getParentTask().getConnection().waitElementVisible("//ytd-member-hub-profile-renderer//div[contains(@id,'details-container')]", 5, "check premium")) {

                String dateExpired = getParentTask().getConnection().getJs("return document.evaluate(\"//ytd-member-hub-profile-renderer//div[contains(@id,'details-container')]//span[contains(@class,'yt-formatted-strin')]\",document,null,7,null).snapshotItem(1).textContent", 5, "");
                long et = StringUtils.getLongFromDateString("MMMM dd, yyyy", dateExpired);
                getParentTask().getAccount().setPremium_expired(et);
                getParentTask().getAccount().updateData();
                getParentTask().insertSuccessLog("Đã có premium");
            }

            return onFinishSuccess();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (quit) {
                if (!getParentTask().getConnection().quitBrowser(5)) {

                }
                getParentTask().getConnection().disconnect();
            }
            if (!getParentTask().getAccount().getLogin_status().equals(ACCOUNT_LOGIN_STATUS.LOGINED.getValue())) {
                MyFileUtils.deleteFolder(new File(TC.getInts().gologin_folder + File.separator + "gologin_profile_" + getParentTask().getAccount().getProfile_id()));
            }

        }
        return true;
    }

    @Override
    public void sendResultToParent() {

    }

    @Override
    public String setActionName() {
        return sm ? " Account setup step " : " giây | Đang login tài khoản";

    }

}
