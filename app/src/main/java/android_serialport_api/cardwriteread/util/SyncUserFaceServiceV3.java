package android_serialport_api.cardwriteread.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.baidu.idl.main.facesdk.FaceAuth;
import com.baidu.idl.main.facesdk.callback.Callback;
import com.baidu.idl.main.facesdk.model.BDFaceSDKCommon;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android_serialport_api.cardwriteread.Constant;
import android_serialport_api.cardwriteread.MyApplication;
import android_serialport_api.cardwriteread.baiduface.baidudb.DBManager;
import android_serialport_api.cardwriteread.baiduface.baidulistener.SdkInitListener;
import android_serialport_api.cardwriteread.baiduface.baidumanage.FaceSDKManager;
import android_serialport_api.cardwriteread.baiduface.baidumodel.User;
import android_serialport_api.cardwriteread.baiduface.baiduutils.FaceApi;
import android_serialport_api.cardwriteread.baiduface.baiduutils.ToastUtils;
import android_serialport_api.cardwriteread.model.DataJsonCallBackV2;
import android_serialport_api.cardwriteread.net.Apis;
import android_serialport_api.cardwriteread.net.HttpCallBackV2;
import android_serialport_api.cardwriteread.net.NetWorkUtils;
import android_serialport_api.cardwriteread.net.bean.CustomerInfo;
import android_serialport_api.cardwriteread.net.bean.CustomerInfoUpdataV3;
import android_serialport_api.cardwriteread.net.bean.CustomerInfoV3;
import android_serialport_api.cardwriteread.net.bean.GetCustomerListRequestEntity;
import android_serialport_api.cardwriteread.net.bean.GetLicenseEntity;
import android_serialport_api.cardwriteread.net.bean.SyncDataEntity;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * 后台更新V3
 * V3 和 V2整体相似
 */
public class SyncUserFaceServiceV3 {
    private int page = 1;//当前查询用户页码
    private List<CustomerInfoV3> loadlist = new ArrayList<>(); //查询到的所有用户信息集合
    private List<User> users = new ArrayList<>(); //数据库信息
    private String groupId = "default";
    private MaterialDialog materialDialog; //初次加载显示
    private Activity activity;
    private List<CustomerInfoUpdataV3> dataBeans = new ArrayList<>(); //更新数据
    private boolean is = true; //循环是否关闭
    private ExecutorService pool;

    public void setIs(boolean is) {
        this.is = is;
    }

    //空跑
    private void LoadRunTime() throws InterruptedException {
        while (!is) {
            Thread.sleep(1000);
        }
    }

    public SyncUserFaceServiceV3(){
        pool = Executors.newSingleThreadExecutor();
    }

    //启动服务
    public void startService(Activity activity) {
        if (this.activity == null) {
            this.activity = activity;
        }
        /**
         * 初次加载
         * 每次启动都去请求服务器验证上次是否同步完成
         * 如果本地数据小于服务器数据则去向服务器数据同步
         * 如果大于或等于则不进行操作
         */
        queryAllUser();
        pool.submit(() -> uploadFaceDb());
    }

    public void stopService(){
        pool.shutdown();
    }

    private void queryAllUser() {
        users.clear();
        loadlist.clear();
        GetCustomerListRequestEntity entity = new GetCustomerListRequestEntity();
        entity.setShopId(Constant.shopId);
        entity.setPage(page);
        RequestParams params = new RequestParams(Apis.OffLineIp + Apis.GetCustomerListNew);
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(entity));
        syncUserData(params, (code, s) -> {
            try {
                if (code == 0) {
                    JSONArray array = new JSONObject(s).getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        JSONArray faceUrlList = array.getJSONObject(i).getJSONArray("faceUrlList");
                        for (int j = 0; j < faceUrlList.length(); j++) {
                            CustomerInfoV3 customerInfov3 = new CustomerInfoV3();
                            customerInfov3.setCustomerId(array.getJSONObject(i).getString("customerId"));
                            customerInfov3.setCustomerName(array.getJSONObject(i).getString("customerName"));
                            customerInfov3.setFaceId(faceUrlList.getJSONObject(j).getString("faceId"));
                            customerInfov3.setFaceUrl(faceUrlList.getJSONObject(j).getString("faceUrl"));
                            loadlist.add(customerInfov3);
                        }
                    }

                    users = DBManager.getInstance().queryUserByGroupId(groupId);

                    if (users.size() >= loadlist.size()) {
                        return;
                    }

                    Log.d("Test123", loadlist.size()+" "+users.size());

                    //本地数据库为0的时候显示弹窗让用户等待初次数据加载
                    //这样做是因为本地什么数据都没有让用户操作也没意思就让用户等着把 能节约操作性能 又能告诉用户加载人脸数据中
                    //时间计算一个数据大概需要四秒时间入库
                    if (users.size() < loadlist.size()) {
                        String str = String.valueOf(((loadlist.size() - users.size()) * 0.07));
                        if (str.length() > 3) str = str.substring(0, 3);
                        materialDialog = MethodUtils.getInstanceSingle().showDialog(activity, "初次加载人脸数据，大概需要" +
                                str + "分钟，请勿退出...");
                        materialDialog.show();
                    }

                    next();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @SuppressLint("CheckResult")
    private void next() {
        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
            for (int i = 0; i < loadlist.size(); i++) {
                Log.d("Test123", "正在遍历");
                final CustomerInfoV3 customerInfoV3 = loadlist.get(i);
                User user = checkUserIsId(String.valueOf(customerInfoV3.getFaceId()), users);
                if (user == null) {
                    //本地不存在此用户
                    Log.d("TestTime", "开始加脸");
                    long time1 = System.currentTimeMillis();
                    addUserFace(customerInfoV3.getFaceId(), customerInfoV3.getFaceUrl(), customerInfoV3.getCustomerName(),
                            customerInfoV3.getCustomerId());
                    Log.d("TestTime", "共耗时："+(System.currentTimeMillis()-time1)+"ms");
                }
                emitter.onNext(i);
            }
            Log.d("Test123", "遍历完成");

            emitter.onNext(-1);
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer == -1) {
                        if (materialDialog != null) {
                            materialDialog.cancel();
                        }
                        initLicense(MyApplication.getApplication(), () -> Toast.makeText(activity, "初始化人脸数据完成", Toast.LENGTH_SHORT).show());
                    }
                });

    }

    //添加人脸数据
    private void addUserFace(final String id, final String oldUrl, final String name, final String coustomId) {
        String newUrl = oldUrl.replace("http://192.168.0.106", "http://images.yunpengai.com");
        try {
            byte[] feature = new byte[512];

            float ret = FaceApi.getInstance().getFeature(UrlToBitmap(newUrl), feature,
                    BDFaceSDKCommon.FeatureType.BDFACE_FEATURE_TYPE_LIVE_PHOTO);

            System.out.println("ret:" + ret);
            if (ret == 128) {
                String imageName = groupId + "-" + id + ".jpg";
                // 注册到人脸库
                boolean isSuccess = FaceApi.getInstance().registerUserIntoDBmanager(groupId, id, imageName,
                        name + "-" + id + "-" + coustomId + "-" + newUrl, feature);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap UrlToBitmap(String url) throws IOException {
        Bitmap bitmap = null;
        URL imageurl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) imageurl.openConnection();
        conn.setDoInput(true);
        conn.connect();
        InputStream is = conn.getInputStream();
        bitmap = BitmapFactory.decodeStream(is);
        return bitmap;
    }

    //更新人脸数据
    //先删除后添加
    private void uploadUserFace(String id, String url, String name, String customId) {
        DBManager.getInstance().deleteUserName(String.valueOf(id), groupId);
        addUserFace(id, url, name, customId);
    }

    //删除人脸数据
    private void delectUserFace(String id) {
        DBManager.getInstance().deleteUserName(id, groupId);
    }

    //检查用户人脸id是否存在
    private User checkUserIsId(String id, List<User> users) {
        if (users == null) {
            return null;
        }
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUserName().equals(id)) {
                return users.get(i);
            }
        }
        return null;
    }


    //写入完成后开始重新初始化一次百度SDK
    private void initLicense(final Context context, final FinishMethod finishMethod) {
        FaceAuth faceAuth = new FaceAuth();
        String key = Constant.key;
        if (key.indexOf("DangKou") == -1) {
            //序列号授权
            faceAuth.initLicenseOnLine(context, key, new Callback() {
                @Override
                public void onResponse(final int code, final String response) {
                    if (code == 0) {
                        SQMInitModel(context, finishMethod);
                    }
                }
            });
        } else {
            //应用授权
            faceAuth.initLicenseBatchLine(context, key, new Callback() {
                @Override
                public void onResponse(final int code, final String response) {
                    if (code == 0) {
                        SQMInitModel(context, finishMethod);
                    }
                }
            });
        }
    }

    private void SQMInitModel(final Context context, final FinishMethod finishMethod) {
        FaceSDKManager.getInstance().initModel(context, new SdkInitListener() {
            @Override
            public void initStart() {

            }

            @Override
            public void initLicenseSuccess() {

            }

            @Override
            public void initLicenseFail(int errorCode, String msg) {

            }

            @Override
            public void initModelSuccess() {
                if (finishMethod != null) {
                    finishMethod.finish();
                }
            }

            @Override
            public void initModelFail(int errorCode, String msg) {
                ToastUtils.toast(context, errorCode + msg);
            }
        });
    }

    /**
     * 更新数据
     */
    private void uploadFaceDb() {
        GetLicenseEntity entity = new GetLicenseEntity();
        entity.setDeviceId(Build.SERIAL);
        RequestParams params = new RequestParams(Apis.OffLineIp + Apis.SyncData);
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(entity));
        syncUserData(params, (code, s) -> {
            /**
             * 更新
             * {"code":200,"pageCount":0,"pageSize":100,"message":"SUCCESS",
             * "data":[{"msgType":1,"msgContent":"{\"id\":\"24425\",\"name\":\"潘潘\",
             * \"faceUrl\":\"https://cos.yunpengai.com/face/23/1590819408090.jpg\",
             * \"type\":\"1\",\"faceId\":\"3660979974172599059\"}"}]}
             * 删除
             * {"code":200,"pageCount":0,"pageSize":100,"message":"SUCCESS",
             * "data":[{"msgType":1,"msgContent":"{\"id\":\"24425\",\"name\":\"\",
             * \"faceUrl\":\"https://cos.yunpengai.com/face/23/1590540827355.jpg\",
             * \"type\":\"0\",\"faceId\":\"3656306171728418279\"}"},{"msgType":1,
             * "msgContent":"{\"id\":\"24425\",\"name\":\"\",\
             * "faceUrl\":\"https://cos.yunpengai.com/face/23/1590819408090.jpg\",
             * \"type\":\"0\",\"faceId\":\"3660979974172599059\"}"}]}
             * 添加
             * {"code":200,"pageCount":0,"pageSize":100,"message":"SUCCESS","data":[{"msgType":1,"msgContent":"{\"id\":\"24425\",\"name\":\"潘潘\"
             * ,\"faceUrl\":\"https://cos.yunpengai.com/face/23/1590819688396.jpg\"
             * ,\"type\":\"1\",\"faceId\":\"3660984662073863913\"}"}]}
             *
             * {"code":200,"pageCount":0,"pageSize":100,"message":"SUCCESS","data":[{"msgType":2,"msgContent":"https://cos.yunpengai.com/test/dksyj1.1.apk"}]}
             */
            System.out.println("Res:"+s);
            if (new JSONObject(s).getInt("code") == 200) {
                JSONArray array = new JSONObject(s).getJSONArray("data");
                dataBeans.clear();
                for (int i = 0; i < array.length(); i++) {
                    CustomerInfoUpdataV3 customerInfoUpdataV3 = new CustomerInfoUpdataV3();
                    customerInfoUpdataV3.setMsgType(array.getJSONObject(i).getInt("msgType"));
                    if (array.getJSONObject(i).getInt("msgType") == 2) {
                        customerInfoUpdataV3.setMsgContent(array.getJSONObject(i).getString("msgContent"));
                    } else {
                        JSONObject object = new JSONObject(array.getJSONObject(i).getString("msgContent"));
                        customerInfoUpdataV3.setMsgContent(object.toString());
                        customerInfoUpdataV3.setType(object.getInt("type"));
                        customerInfoUpdataV3.setCustomerId(object.getString("id"));
                        customerInfoUpdataV3.setCustomerName(object.getString("name"));
                        customerInfoUpdataV3.setFaceId(object.getString("faceId"));
                        customerInfoUpdataV3.setFaceUrl(object.getString("faceUrl"));
                    }
                    dataBeans.add(customerInfoUpdataV3);
                }
                //调用之前再初始化一次百度sdk
                initLicense(MyApplication.getApplication(), new FinishMethod() {
                    @Override
                    public void finish() {
                        UPnext();
                    }
                });
            } else {
                uploadFaceDb();
            }
        });
    }

    //更新下一步操作
    private void UPnext() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < dataBeans.size(); i++) {
                    LoadRunTime();
                    if (dataBeans.get(i).getMsgType() == 1) {
                        if (dataBeans.get(i).getType() == 1) {
                            //覆盖数据
                            uploadUserFace(new JSONObject(dataBeans.get(i).getMsgContent()).getString("faceId"),
                                    new JSONObject(dataBeans.get(i).getMsgContent()).getString("faceUrl"),
                                    new JSONObject(dataBeans.get(i).getMsgContent()).getString("name"),
                                    new JSONObject(dataBeans.get(i).getMsgContent()).getString("id"));
                        } else if (dataBeans.get(i).getType() == 0) {
                            //删除数据
                            delectUserFace(new JSONObject(dataBeans.get(i).getMsgContent()).getString("faceId"));
                        }
                    } else if (dataBeans.get(i).getMsgType() == 2) {
                        //app update
                        final String content = dataBeans.get(i).getMsgContent();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                System.out.println("开始更新app");
                                String newfilename = downloadApp(content);
                            }
                        }).start();
                    }
                    emitter.onNext(i);
                }
                emitter.onNext(-1);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) {
                        if (integer == -1) {
                            initLicense(MyApplication.getApplication(), new FinishMethod() {
                                @Override
                                public void finish() {
                                    uploadFaceDb();
                                    System.out.println("更新完成");
                                }
                            });
                        }
                    }
                });
    }

    private String downloadApp(String url) {
        String fileName = System.currentTimeMillis() + ".apk";
        File file = new File(FileUtil.appDownloadPath);
        if (!file.exists()) {
            file.mkdir();
        }
        String newFilename = FileUtil.appDownloadPath + "/" + fileName;
        FileDownloader.downloadNet(url, newFilename,activity);
        return newFilename;
    }

    /**
     * 网路请求
     */
    private void syncUserData(RequestParams params, final DataJsonCallBackV2 callBack) {
        NetWorkUtils.postHttpRequest(true, params, new HttpCallBackV2() {
            @Override
            public void onSuccess(String result) throws JSONException {
                Log.d("TestNet", "onSuccess "+params.getUri()+"\n"+params.getBodyContent()+"\n"+result);

                int code = new JSONObject(result).getInt("code");
                callBack.callback(code == 200 ? 0 : 2, result);
            }

            @Override
            public void onCancelled(String msg) throws JSONException {
                Log.d("TestNet", "cancel "+params.getUri()+"\n"+params.getBodyContent()+"\n"+msg);

                callBack.callback(1, msg);
            }

            @Override
            public void onError(String error) throws JSONException {
                Log.d("TestNet", "error "+params.getUri()+"\n"+params.getBodyContent()+"\n"+error);

                callBack.callback(2, error);
            }

            @Override
            public void onFinish() {
                //callBack.callback(3,"");
            }
        });
    }

}
