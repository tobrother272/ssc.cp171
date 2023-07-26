/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.proxy.tmproxy;

import javafx.concurrent.Task;
import okhttp3.MediaType;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author simplesolution.co
 */
public class GetTMProxy extends Task<String> {
    private String key;
    public GetTMProxy(String key) {
        this.key = key;
    }
    public static String GetTMProxy(String key) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://tmproxy.com/api/proxy/get-new-proxy")
                    .post(RequestBody.create(MediaType
                        .parse("application/json"),
                            "{\"api_key\": \""+key+"\"}"
                    ))
                    .addHeader("Accept", "application/json")
                    .build();
            
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();
            System.out.println("resultJson "+resultJson);
            JSONParser parser = new JSONParser();
            JSONObject jSONObject = (JSONObject) parser.parse(resultJson);
            String proxyString =((JSONObject)jSONObject.get("data")).get("https").toString();
            return proxyString;
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return "";
    }
    public static Object GetTMProxy2(String key) {
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://proxy.tinsoftsv.com/api/changeProxy.php?key="+key)
                    .get()
                    .addHeader("Accept", "application/json")
                    .build();
            
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();
            System.out.println("resultJson "+resultJson);
            JSONParser parser = new JSONParser();
            JSONObject jSONObject = (JSONObject) parser.parse(resultJson);
            if(jSONObject.get("success").toString().contains("false")){
                return jSONObject;
            }
            String proxyString =jSONObject.get("proxy").toString();
            return proxyString;
     
            // 
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return "";
    }
    public static int getTMProxyLastTime(String key) {
        int lastTime = 120;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("http://backend.simplesolution.co/TiktokApi/tmproxycurrent/"+key)
                    .get()
                    .build();
            Response response = client.newCall(request).execute();
            String resultJson = response.body().string();
            JSONParser parser = new JSONParser();
            JSONObject jSONObject = (JSONObject) parser.parse(resultJson);
            String proxyString =((JSONObject)jSONObject.get("data")).get("next_request").toString();

            return Integer.parseInt(proxyString);
            // 
        } catch (Exception e) {
            //e.printStackTrace();
        }
        return lastTime;
    }

  
    @Override
    protected String call() throws Exception {
        return GetTMProxy(key);
    }
    public void start() {
        Thread t = new Thread(this);
        t.setDaemon(true);
        t.start();
    }
}
