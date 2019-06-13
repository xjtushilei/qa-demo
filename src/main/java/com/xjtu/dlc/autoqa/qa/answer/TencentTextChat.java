package com.xjtu.dlc.autoqa.qa.answer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Timestamp;
import java.util.*;

public class TencentTextChat {
    private static String APP_ID = "xxxxxxxxxxxxx";
    private static String APP_KEY = "xxxxxxxxxxxx";

    public static void main(String[] args) throws UnsupportedEncodingException {

        System.out.println(chat("给我讲一个笑话"));

        System.out.println(chat("你开心吗"));

        System.out.println(chat("明天西安什么天气"));
    }

    public static String chat(String input) throws UnsupportedEncodingException {
        Map<String, Object> params = new HashMap<>();
        params.put("app_id", APP_ID);
        Timestamp d = new Timestamp(System.currentTimeMillis());

        params.put("time_stamp", d.getTime() / 1000);
        params.put("nonce_str", getRandomString(5));
        params.put("session", 1);
        params.put("question", input);

        Map<String, Object> sortMap = new TreeMap<>(String::compareTo);
        sortMap.putAll(params);

        Set<String> keySet = sortMap.keySet();
        StringBuilder sb = new StringBuilder();

        for (String key : keySet) {
            Object value = sortMap.get(key);
            sb.append("&").append(key).append("=").append(URLEncoder.encode(value + "", "UTF-8"));
        }
        sb.deleteCharAt(0);

        String md5Sign = encrypt(sb.toString() + "&app_key=" + APP_KEY);

//        System.out.println(md5Sign);
        String resp = doGet("https://api.ai.qq.com/fcgi-bin/nlp/nlp_textchat?" + sb.append("&sign=" + md5Sign).toString());

        JSONObject jsonpObject = JSON.parseObject(resp);

        if (jsonpObject.getInteger("ret") != 0) {
            System.err.println(resp);
            return null;
        } else {
            return jsonpObject.getJSONObject("data").getString("answer");
        }
    }

    private static String encrypt(String key) {
        char[] hexDigits = {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        try {
            byte[] btInput = key.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;

            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }

            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }


    private static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        Random random = new Random();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {

            int number = random.nextInt(62);
            sb.append(str.charAt(number));

        }
        return sb.toString();
    }

    private static String doGet(String httpurl) {
        HttpURLConnection connection = null;
        InputStream is = null;
        BufferedReader br = null;
        String result = null;

        try {
            URL url = new URL(httpurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(15000);
            connection.setReadTimeout(60000);
            connection.connect();

            if (connection.getResponseCode() == 200) {
                is = connection.getInputStream();
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
                StringBuffer sbf = new StringBuffer();
                String temp = null;
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (null != connection) {
                connection.disconnect();
            }
        }

        return result;
    }

}
