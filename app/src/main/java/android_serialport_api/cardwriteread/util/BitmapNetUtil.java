package android_serialport_api.cardwriteread.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Deprecated
public class BitmapNetUtil {
    public interface BitmapNetData{
        void returndata(Bitmap bitmap);
    }
    private Bitmap bitmap;
    private byte [] bs;

    private static class SingleInstance{
        private static final BitmapNetUtil bu = new BitmapNetUtil();
    }

    public static BitmapNetUtil getInstance(){
        return SingleInstance.bu;
    }

    //网络url转bitmap
    //网络url转bitmap
    public void returnBitMap(final String url, final BitmapNetData bitmapNetData) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL imageurl = new URL(url);
                    HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                    bitmapNetData.returndata(bitmap);
                    is.close();
                } catch (Exception e) {
                    System.out.println("io图片读写错误-url:"+url);
                    e.printStackTrace();
                }
            }
        }).start();
    }

}
