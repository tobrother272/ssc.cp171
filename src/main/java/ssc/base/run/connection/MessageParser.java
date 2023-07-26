/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssc.base.run.connection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/**
 *
 * @author PC
 */
public class MessageParser {

    private long code;
    private String message;
    private String error;
    private String name;
    private int count;
    private String value;
    private String port;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
    

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public MessageParser(String message) {
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(message);
            this.code = Long.parseLong(jsonObject.get("code").toString());
            this.message = jsonObject.get("message").toString();
            try {
                this.error = jsonObject.get("error").toString();
            } catch (Exception e) {

            }
            try {
                this.name = jsonObject.get("name").toString();
            } catch (Exception e) {

            }
            try {
                this.port = jsonObject.get("port").toString();
            } catch (Exception e) {

            }
            try {
                this.count = Integer.parseInt(jsonObject.get("count").toString());
            } catch (Exception e) {
                this.count = -1;
            }
            try {
                this.value = jsonObject.get("value").toString();
            } catch (Exception e) {
                this.value ="null";
                //e.printStackTrace();
            }
        } catch (Exception e) {
            System.out.println("---------------start message lỗi--------------");
            System.out.println(message);
            System.out.println("---------------end message lỗi--------------");
            e.printStackTrace();
        }

    }

}
