/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.theta.app.api;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ssc.base.global.TC;
import ssc.theta.app.model.ProfileG;
import ssc.theta.app.model.User;
import ssc.base.ultil.ApiHelper;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.json.simple.parser.JSONParser;
import ssc.base.task.BaseTask;
import ssc.base.ui.components.SSCAlertModal;
import ssc.theta.app.model.Profile;
import ssc.theta.app.model.TaskLog;

/**
 *
 * @author PC
 */
public class MetaApi {

    public static JSONObject auth(String username, String password) {
        try {
            Map<String, String> datas = new HashMap<String, String>();
            datas.put("username", username);
            datas.put("password", password);
            JSONObject res = ApiHelper.postData("/oauth/login", datas);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public static ObservableList<ProfileG> getProfiles() {
        ObservableList<ProfileG> data = FXCollections.observableArrayList();
        String url = "";
        if (TC.getInts().role.equals("ROLE_USER")) {
            url = "/profile/list";
        } else {
            url = "/profile/subuser/list";
        }
        JSONObject res = ApiHelper.getDataWithAccessToken(url);
        if (res.get("profiles") != null) {
            JSONArray arrProfiles = (JSONArray) res.get("profiles");
            for (Object jsonObjItem : arrProfiles) {
                JSONObject profileObj = (JSONObject) jsonObjItem;
                data.add(new ProfileG(profileObj, arrProfiles.indexOf(jsonObjItem)));
            }
        }
        return data;
    }
    
    
    public static List<String> getListUserName(String url) {
        List<String> data=new ArrayList<>();
        JSONArray arr = ApiHelper.getArryData(url+"/account/getListImport");
        if (arr != null) {
            for (Object jsonObjItem : arr) {
               data.add(jsonObjItem.toString());
            }
        }
        return data;
    }
    

    public static ProfileG getProfileInfo(String profile_id) {
        try {
            String url = "/profile/profileInfo?profile_id=" + profile_id;
           // System.out.println("url "+url);
            JSONObject res = ApiHelper.getDataWithAccessToken(url);
            System.out.println(""+res.toJSONString());
            if (res.get("profile") != null) {
                JSONObject profileObj = (JSONObject) res.get("profile");
                return new ProfileG(profileObj, 0);
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static ObservableList<User> getSubUsers(String profile_id) {
        ObservableList<User> data = FXCollections.observableArrayList();
        String url = "/user/profile_list?profile_id=" + profile_id;
        JSONObject res = ApiHelper.getDataWithAccessToken(url);
        if (res.get("sub_users") != null) {
            JSONArray arrProfiles = (JSONArray) res.get("sub_users");
            for (Object jsonObjItem : arrProfiles) {
                JSONObject profileObj = (JSONObject) jsonObjItem;
                User user = new User(profileObj, arrProfiles.indexOf(jsonObjItem));
                data.add(user);
            }
        }
        return data;
    }

    public static ProfileG insertProfile(ProfileG _profile) {
        try {
            String url = "/profile/insert";
            JSONObject res = ApiHelper.postDataWitAccessToken(url, _profile.getPostData());
            //System.out.println(res.toJSONString());
            if (res != null && res.get("profile") != null) {
                return new ProfileG((JSONObject) res.get("profile"), _profile.getStt());
            }
            return null;
        } catch (Exception e) {
        }
        return null;
    }

    public static boolean deleteProfile(String profile_id) {
        try {
            String url = "/profile/delete?profile_id=" + profile_id;
            JSONObject res = ApiHelper.getDataWithAccessToken(url);
            if (res != null && res.get("data") != null) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static boolean insertUserProfile(String profile_id, int subuser_id) {
        try {
            String url = "/profile/user/insert?subuser_id=" + subuser_id + "&profile_id=" + profile_id;
            JSONObject res = ApiHelper.getDataWithAccessToken(url);
            if (res != null && res.get("status").toString().contains("success")) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    
    public static boolean deleteUserProfile(String profile_id, int subuser_id) {
        try {
            String url = "/profile/user/delete?subuser_id=" + subuser_id + "&profile_id=" + profile_id;
            JSONObject res = ApiHelper.getDataWithAccessToken(url);
            if (res != null && res.get("status").toString().contains("success")) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static User inviteProfileShare(String profile_id, String username) {
        try {
            String url = "/profile/share/invite?username=" + username + "&profile_id=" + profile_id;
            JSONObject res = ApiHelper.getDataWithAccessToken(url);
            if (res != null && res.get("status").toString().contains("success")) {
                JSONObject userObj = (JSONObject) res.get("user");
                return new User(userObj, 0);
            } else {
                SSCAlertModal sCAlertModal = new SSCAlertModal(3, 120, res.get("message").toString());
                sCAlertModal.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean deleteProfileShare(String profile_id, int subuser_id) {
        try {
            String url = "/profile/share/delete?admin_share_id=" + subuser_id + "&profile_id=" + profile_id;
            JSONObject res = ApiHelper.getDataWithAccessToken(url);
            if (res != null && res.get("status").toString().contains("success")) {
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    public static String[] getPhoneNumber(String keyApi) {
        String data[] = new String[2];
        try {
            String url = "https://api.viotp.com/request/getv2?token=" + keyApi + "&serviceId=3";
            JSONObject res = ApiHelper.getDataWithUrl(url);
            if (res != null && res.get("data") != null) {
                JSONObject dataObj = (JSONObject) res.get("data");
                data[0] = dataObj.get("phone_number").toString();
                data[1] = dataObj.get("request_id").toString();
                return data;
            }
        } catch (Exception e) {
        }
        return null;
    }

    
    public static String[] getPhoneNumberChoThueSimCode(String keyApi) {
        String data[] = new String[2];
        try {
            String url = "https://chothuesimcode.com/api?act=number&apik="+keyApi+"&appId=1005&carrier=Viettel";
            JSONObject res = ApiHelper.getDataWithUrl(url);
            
            //System.out.println(res.toJSONString());
            
            if (res != null && res.get("Result") != null) {
                JSONObject dataObj = (JSONObject) res.get("Result");
                data[0] = dataObj.get("Number").toString();
                data[1] = dataObj.get("Id").toString();
                return data;
            }
        } catch (Exception e) {
        }
        return null;
    }
    public static String getSMSChoThueSimCode(String keyApi,String id) {
     
        try {
            String url = "https://chothuesimcode.com/api?act=code&apik="+keyApi+"&id="+id;
            JSONObject res = ApiHelper.getDataWithUrl(url);
            
            System.out.println(res.toJSONString());
            
            if (res != null && res.get("Result") != null) {
                JSONObject dataObj = (JSONObject) res.get("Result");
                return dataObj.get("Code").toString();
            }
        } catch (Exception e) {
        }
        return "";
    }
    
    
    
    
    
    
    public static long createRequest(String keyApi) {

        try {
            String url = "https://2ndline.io/apiv1/order?serviceId=292&apikey=" + keyApi + "&networkId=1";
            JSONObject res = ApiHelper.getDataWithUrl(url);
            System.out.println(res.toJSONString());
            if (res != null) {
                return Long.parseLong(res.get("id").toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static String getPhoneNumber(String keyApi, long order_id) {
        try {
            String url = "https://2ndline.io/apiv1/ordercheck?id=" + order_id + "&apikey=" + keyApi+"&networkId=1";
            JSONObject res = ApiHelper.getDataWithUrl(url);
            System.out.println("get phone "+ res.toJSONString());
            if (res != null && res.get("data") != null) {
                JSONObject dataObj = (JSONObject) res.get("data");
                return dataObj.get("phone").toString();
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static String get2LineSms(String keyApi, long order_id) {
        try {
            String url = "https://2ndline.io/apiv1/ordercheck?id=" + order_id + "&apikey=" + keyApi;
            JSONObject res = ApiHelper.getDataWithUrl(url);
            System.out.println("get sms "+ res.toJSONString());
            if (res != null && res.get("data") != null) {
                JSONObject dataObj = (JSONObject) res.get("data");
                return dataObj.get("code").toString();
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static String getSMSCode(String keyApi, String id) {
        try {
            String url = "https://api.viotp.com/session/getv2?requestId=" + id + "&token=" + keyApi;
            JSONObject res = ApiHelper.getDataWithUrl(url);
            if (res != null && res.get("data") != null) {
                JSONObject dataObj = (JSONObject) res.get("data");
                return dataObj.get("Code").toString();
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static String downloadFile(String linkDown, String path, BaseTask cloneVideoTask, int fileSize, String fileName) {
        OutputStream out = null;
        InputStream in = null;
        HttpURLConnection httpcon = null;
        BufferedImage image = null;
        try {
            URL url = new URL(linkDown);
            out = new BufferedOutputStream(new FileOutputStream(path));
            httpcon = (HttpURLConnection) (url).openConnection();
            httpcon.setConnectTimeout(240000);
            httpcon.setReadTimeout(240000);
            httpcon.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/99.0.4844.84 Safari/537.36");
            in = httpcon.getInputStream();
            byte[] buffer = new byte[1024];
            int numRead;
            long numWritten = 0;
            while ((numRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, numRead);
                numWritten += numRead;
                cloneVideoTask.updateMyMessage(fileName + " " + (((int) (numWritten / 1024 / 1024) + "/" + fileSize)));
                //cloneVideoTask.updateMyProgress((int) (numWritten / 1024 / 1024), total);
            }
            long mbFileDown = new File(path).length() / 1024;
            long mbFileSize = fileSize / 1024;
            if (mbFileDown + 100 < mbFileSize) {
                return fileName + " Downloaded " + mbFileDown + "/" + mbFileSize;
            }
            return "";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return fileName + " " + e.getMessage();
        } catch (MalformedURLException e) {
            return fileName + " " + e.getMessage();
        } catch (IOException e) {
            return fileName + " " + e.getMessage();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (httpcon != null) {
                    httpcon.disconnect();
                }
                if (image != null) {
                    //image.re 
                }
            } catch (IOException e) {
                //error = true;
                e.printStackTrace();
            }
        }
    }

    public static boolean downloadProfile(String profile_id, String path, ObservableList<TaskLog> log) {
        try {
            JSONObject ob = getProfileZipInfo(profile_id);
            if (ob == null) {
                log.add(new TaskLog(System.currentTimeMillis(), "Không thấy info profile " + profile_id, "", "", 0));
                return false;
            }
            ApiHelper.downloadFileWithToken("/profile/download?profile_id=" + profile_id, path);
            FileTime fileTime = FileTime.fromMillis(Long.parseLong(ob.get("dateTime").toString()));
            Files.setAttribute(new File(path).toPath(), "basic:lastModifiedTime", fileTime, NOFOLLOW_LINKS);
            return true;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return false;
    }

    public static JSONObject getProfileZipInfo(String profile_id) {
        String url = "/profile/info?profile_id=" + profile_id;
        JSONObject res = ApiHelper.getDataWithAccessToken(url);
        if (res != null) {
            return res;
        }
        return null;
    }

    public static boolean uploadProfileToServer(String path, String profile_id) {
        try {
            ApiHelper.uploadFileWithToken(new File(path), "/profile/upload?profile_id=" + profile_id);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
    
    
    public static JSONObject getFingerprint(Profile profile, String key) {
        try {
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;

            request = new Request.Builder()
                    .url("https://api.gologin.com/browser/fingerprint?os=" + profile.getPlatform() + "&resolution=" + profile.getScreen_solution())
                    .get()
                    .addHeader("User-Agent", "gologin-api")
                    .addHeader("Authorization", "Bearer " + key)
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();
            JSONParser jsonParser = new JSONParser();
            //System.out.println("------resultJson getFingerprint------");
            //System.out.println(resultJson);
            //System.out.println("------end getFingerprint------");
            return (JSONObject) jsonParser.parse(resultJson);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
     
     
    public static boolean removeGologinProfile(String profileId, String key) {
        try {
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;
            request = new Request.Builder()
                    .url("https://api.gologin.com/browser/" + profileId)
                    .delete()
                    .addHeader("Authorization", "Bearer " + key)
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();

            //System.out.println("" + resultJson);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
        
    public static String createGologinProfile(Profile profile, String key) {
        try {
            //System.out.println("key "+key);

            //String key="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiI2MGExZjZlZDJhMWRmOGU1ZTc1NzhmMTkiLCJ0eXBlIjoiZGV2Iiwiand0aWQiOiI2MmFjNTc0MTgwNTEwNTczMDJmYzVhODEifQ.RGHroUH2htM9g0_Uo4lb6zNFUiD7mltb-3s2tQh7TSc";
            JSONObject fingerPrintObj = getFingerprint(profile, key);



            if (fingerPrintObj.get("error") != null) {
                System.out.println(fingerPrintObj.get("error").toString());
                return "";
            }

            if (fingerPrintObj == null) {
                System.out.println("Không thể lấy dữ liệu fingerPrintObj");
                return "";
            }
            if (fingerPrintObj.get("message") != null) {
                if (fingerPrintObj.get("message").toString().contains("Payment required")) {
                    System.out.println("-----------------------------");
                    System.out.println("------Key chưa đóng tiền-----");
                    System.out.println("-----------------------------");
                    return "";
                }
            }

            //System.out.println(fingerPrintObj);
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;

            JSONObject JSONDrawOBJ = new JSONObject();
            
      
            
            JSONObject createObj = new JSONObject();
            createObj.put("name", profile.getName());
            createObj.put("os", profile.getPlatform());
            createObj.put("notes", "auto generated");
            createObj.put("browserType", "chrome");
            createObj.put("startUrl", "https://google.com");
            createObj.put("googleServicesEnabled", true);
            createObj.put("lockEnabled", false);

            //createObj.put("canvas", (JSONObject)fingerPrintObj.get("canvas"));
            createObj.put("mediaDevices", (JSONObject) fingerPrintObj.get("mediaDevices"));

            JSONObject navigatorObj = (JSONObject) fingerPrintObj.get("navigator");
            navigatorObj.put("userAgent", profile.getUser_agent());
            navigatorObj.put("hardwareConcurrency", profile.getCpu());
            navigatorObj.put("platform", profile.getPlatform());
            navigatorObj.put("deviceMemory", profile.getRam());

            createObj.put("navigator", navigatorObj);

            createObj.put("devicePixelRatio", Double.parseDouble(fingerPrintObj.get("devicePixelRatio").toString()));

            JSONObject webglObj = new JSONObject();
            webglObj.put("mode", profile.getOpengl());
            createObj.put("webGL", webglObj);
            //
            createObj.put("webglParams", (JSONObject) fingerPrintObj.get("webglParams"));
            //
            JSONObject webGLMetadataObj = (JSONObject) fingerPrintObj.get("webGLMetadata");
            webGLMetadataObj.put("mode", "mask");
            createObj.put("webGLMetadata", webGLMetadataObj);
            //
            JSONObject fontsObj = new JSONObject();
            fontsObj.put("enableDomRect", true);
            fontsObj.put("enableMasking", true);
            fontsObj.put("families", (JSONArray) fingerPrintObj.get("fonts"));
            createObj.put("fonts", fontsObj);
            //
            JSONObject webRTCObj = new JSONObject();
            webRTCObj.put("mode", "disabled");
            webRTCObj.put("enabled", false);
            webRTCObj.put("customize", true);
            webRTCObj.put("fillBasedOnIp", true);
            webRTCObj.put("webRTC", webRTCObj);
            //    
            JSONObject audioObj = new JSONObject();
            audioObj.put("mode", profile.getAudio());
            createObj.put("audioContext", audioObj);
            //
            createObj.put("screenHeight", profile.getScreen_solution().split("x")[1]);
            createObj.put("screenWidth", profile.getScreen_solution().split("x")[0]);
            createObj.put("proxyEnabled", true);
            //
            JSONObject canvasObj = new JSONObject();
            canvasObj.put("mode", profile.getCanvas());
            createObj.put("canvas", canvasObj);

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON, createObj.toJSONString());
            request = new Request.Builder()
                    .url("https://api.gologin.com/browser")
                    .post(requestBody)
                    .addHeader("User-Agent", "gologin-api")
                    .addHeader("Authorization", "Bearer " + key)
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();
           //System.out.println(resultJson);
            //System.out.println("---x---result--x--");
            //System.out.println(createObj.toJSONString());
       
            JSONParser jsonParser = new JSONParser();
            JSONObject result = (JSONObject) jsonParser.parse(resultJson);
            //System.out.println("---x---create profile--x--");
            
            //System.out.println("---x---create profile--x--");
            if (result.toString().contains("Payment required")) {
                System.out.println("-----------------------------");
                System.out.println("------Key chưa đóng tiền-----");
                System.out.println("-----------------------------");
                return "";
            }
            if (result == null || result.get("id") == null) {
                return "";
            }
            return result.get("id").toString();
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("" + createObj.toJSONString());
        }
        return "";
    }
    

}
