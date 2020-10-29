package com.baidu.idl.main.facesdk.utils;

import android.util.Log;

import com.baidu.vis.unified.license.HttpStatus;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;

public class MyHttpUtils {
    public MyHttpUtils() {
    }

    public static HttpStatus requestPost(String urlStr, String paramStr, String contentType, String TAG) {
        HttpsURLConnection conn = null;
        InputStream is = null;
        String resultData = null;
        HttpStatus httpStatus = null;
        OutputStream outputStream = null;
        InputStream inputStream = null;
        ByteArrayOutputStream baos = null;

        try {
            URL url = new URL(urlStr);
            conn = (HttpsURLConnection)url.openConnection();
            System.setProperty("sun.net.client.defaultConnectTimeout", "8000");
            System.setProperty("sun.net.client.defaultReadTimeout", "8000");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setRequestMethod("POST");
            conn.setUseCaches(false);
            conn.setInstanceFollowRedirects(true);
            conn.setSSLSocketFactory(HttpsHelp.createSSLSocketFactory());
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            conn.setRequestProperty("Content-Type", contentType);
            conn.connect();
            outputStream = conn.getOutputStream();
            outputStream.write(paramStr.getBytes());
            outputStream.flush();
            outputStream.close();
            int responseCode = conn.getResponseCode();
            Log.e(TAG, "request code " + responseCode);
            if (200 == responseCode) {
                inputStream = conn.getInputStream();
                byte[] buffer = new byte[1024];
                baos = new ByteArrayOutputStream();
                boolean var14 = true;

                int len;
                while((len = inputStream.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }

                byte[] b = baos.toByteArray();
                resultData = new String(b, "utf-8");
                baos.flush();
                Log.e(TAG, "request data " + resultData);
            }

            httpStatus = new HttpStatus(responseCode, resultData);
        } catch (MalformedURLException var34) {
            Log.e(TAG, "MalformedURLException " + var34.getMessage());
            var34.printStackTrace();
        } catch (IOException var35) {
            Log.e(TAG, "IOException " + var35.getMessage());
            var35.printStackTrace();
        } catch (Exception var36) {
            Log.e(TAG, "Exception " + var36.getMessage());
            var36.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    ((InputStream)is).close();
                } catch (IOException var33) {
                    var33.printStackTrace();
                }
            }

            try {
                if (outputStream != null) {
                    outputStream.close();
                }

                if (baos != null) {
                    baos.close();
                }

                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException var32) {
                var32.printStackTrace();
            }

            if (conn != null) {
                conn.disconnect();
            }

        }

        return httpStatus;
    }
}