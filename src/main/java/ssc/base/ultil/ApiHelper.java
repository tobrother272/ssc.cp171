/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.ultil;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import ssc.base.global.TC;

/**
 *
 * @author PC
 */
public class ApiHelper {

    /**
     * Map<String, String> hashMap = new HashMap<String, String>(); // add
     * elements to hashMap hashMap.put("key", "value");
     *
     * @param url
     * @param postValues
     * @return
     */
    public static JSONObject postData(String url, Map<String, String> postValues) {

        try {
            String query = TC.getInts().api_url + url;
            JSONObject postObj = new JSONObject();
            Set<String> keySet = postValues.keySet();
            for (String key : keySet) {
                postObj.put(key, postValues.get(key));

            }

            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON, postObj.toJSONString());
            //System.out.println("xxxxxxxxxxxxxxxx 1");
            request = new Request.Builder()
                    .url(query)
                    .post(requestBody)
                    .build();
            Response response = client.newCall(request).execute();
            //System.out.println("xxxxxxxxxxxxxxxx 2");
            String resultJson = response.body().string();

            System.out.println("ApiHelper.postData " + resultJson);
            JSONObject jo = (JSONObject) new JSONParser().parse(resultJson);
            return jo;
        } catch (Exception e) {
            e.printStackTrace();
            //System.out.println("asdasdsasads");
        }
        //System.out.println("dassadadsadsad");
        JSONObject jo = new JSONObject();
        jo.put("message", "Lỗi login không xác định");
        return jo;
    }

    public static JSONObject postDataWitAccessToken(String url, Map<String, String> postValues) {
        try {
            String query = TC.getInts().api_url + url;
            JSONObject postObj = new JSONObject();
            //System.out.println("query " + query);
            Set<String> keySet = postValues.keySet();
            for (String key : keySet) {
                postObj.put(key, postValues.get(key));
                //System.out.println(key + " " + postValues.get(key));
            }

            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON, postObj.toJSONString());
            request = new Request.Builder()
                    .url(query)
                    .post(requestBody)
                    .addHeader("Authorization", TC.getInts().getAccessToken())
                    .addHeader("token", TC.getInts().gologin_token)
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();
            //System.out.println("getDataWithAccessToken " + TC.getInts().getAccessToken());
            //System.out.println("ApiHelper.postDataWitAccessToken " + resultJson);
            JSONObject jo = (JSONObject) new JSONParser().parse(resultJson);
            return jo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getDataWitAccessTokenAndUrl(String url, String token) {
        try {
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", token)
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();
            System.out.println(url);
            System.out.println("getDataWithAccessToken " + TC.getInts().getAccessToken());
            System.out.println("ApiHelper.postDataWitAccessToken " + resultJson);
            JSONObject jo = (JSONObject) new JSONParser().parse(resultJson);
            return jo;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    public static JSONObject postDataWitAccessTokenDataUrl(String url, String Token, Map<String, String> postValues) {
        try {

            JSONObject postObj = new JSONObject();
            //System.out.println("query " + query);
            Set<String> keySet = postValues.keySet();
            for (String key : keySet) {
                postObj.put(key, postValues.get(key));
                //System.out.println(key + " " + postValues.get(key));
            }

            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            RequestBody requestBody = RequestBody.create(JSON, postObj.toJSONString());
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .addHeader("Authorization", Token)
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();
            //System.out.println("getDataWithAccessToken " + TC.getInts().getAccessToken());
            //System.out.println("ApiHelper.postDataWitAccessToken " + resultJson);
            JSONObject jo = (JSONObject) new JSONParser().parse(resultJson);
            return jo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getDataWithAccessToken(String url) {
        try {

            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;
      
            request = new Request.Builder()
                    .url(TC.getInts().api_url + url)
                    .get()
                    .addHeader("Authorization", TC.getInts().getAccessToken())
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();

            //System.out.println("getDataWithAccessToken " + resultJson);
            JSONObject jo = (JSONObject) new JSONParser().parse(resultJson);
            return jo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getDataWithAccessTokenAndURl(String url, String token) {
        try {

            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;
            //System.out.println(TS.getInstance().getServiceUrl() + "/oauth/verify_token");
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", token)
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();

            System.out.println("getDataWithAccessToken " + resultJson);
            JSONObject jo = (JSONObject) new JSONParser().parse(resultJson);
            return jo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getDataWithAccessTokenAndURl2(String url, String token, String token2) {
        try {

            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;
            //System.out.println(TS.getInstance().getServiceUrl() + "/oauth/verify_token");
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", token)
                    .addHeader("token", token2)
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();

            System.out.println("getDataWithAccessToken " + resultJson);
            JSONObject jo = (JSONObject) new JSONParser().parse(resultJson);
            return jo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getArryData(String url) {
        try {

            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;
            //System.out.println(TS.getInstance().getServiceUrl() + "/oauth/verify_token");
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();

            //System.out.println("getDataWithAccessToken " + resultJson);
            JSONArray jo = (JSONArray) new JSONParser().parse(resultJson);
            return jo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getDataWithAccessTokenAndUrl(String url, String token) {
        try {

            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;
            //System.out.println(TS.getInstance().getServiceUrl() + "/oauth/verify_token");
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("Authorization", token)
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();

            //System.out.println("getDataWithAccessToken " + resultJson);
            JSONObject jo = (JSONObject) new JSONParser().parse(resultJson);
            return jo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getDataWithUrl(String url) {
        try {

            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;
            //System.out.println(TS.getInstance().getServiceUrl() + "/oauth/verify_token");
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();

            //System.out.println("getDataWithAccessToken " + resultJson);
            JSONObject jo = (JSONObject) new JSONParser().parse(resultJson);
            return jo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject getDataWithoutAccessToken(String url) {
        try {

            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(240, TimeUnit.SECONDS);
            Request request = null;
            request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();
            JSONObject jo = (JSONObject) new JSONParser().parse(resultJson);
            return jo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Boolean downloadFileWithToken(String url, String savePath) {
        try {
            Response response = null;
            Request request = null;
            OkHttpClient client = new OkHttpClient();
            client.setReadTimeout(120, TimeUnit.SECONDS);
            request = new Request.Builder().url(TC.getInts().api_url + url)
                    .addHeader("Authorization", TC.getInts().getAccessToken())
                    .addHeader("Content-Type", "application/json")
                    .build();
            response = client.newCall(request).execute();
            FileOutputStream fos = new FileOutputStream(savePath);
            InputStream inputStream = response.body().byteStream();
            try ( BufferedInputStream input = new BufferedInputStream(inputStream)) {
                byte[] dataBuffer = new byte[1024];
                int readBytes;
                long totalBytes = 0;
                while ((readBytes = input.read(dataBuffer)) != -1) {
                    totalBytes += readBytes;
                    fos.write(dataBuffer, 0, readBytes);
                    //System.out.println("downloadFileWithToken totalBytes "+totalBytes);
                }
            }
            fos.close();
            response.body().close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static final okhttp3.MediaType MEDIA_TYPE_ZIP = okhttp3.MediaType.parse("application/x-zip-compressed");

    public static Boolean uploadFileWithToken(File file, String url) {

        try {
            okhttp3.OkHttpClient client = new okhttp3.OkHttpClient();

            okhttp3.RequestBody requestBody = new okhttp3.MultipartBody.Builder().setType(okhttp3.MultipartBody.FORM)
                    .addFormDataPart("file", file.getName(), okhttp3.RequestBody.create(MEDIA_TYPE_ZIP, file))
                    .build();
            okhttp3.Request request = new okhttp3.Request.Builder().url(TC.getInts().api_url + url)
                    .post(requestBody).addHeader("Authorization", TC.getInts().getAccessToken()).build();

            okhttp3.Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
