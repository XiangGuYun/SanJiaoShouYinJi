package android_serialport_api.cardwriteread.view;

import android.app.Activity;
import android.app.Presentation;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.zhpan.bannerview.BannerViewPager;
import com.zhpan.bannerview.constants.IndicatorGravity;
import com.zhpan.bannerview.constants.IndicatorSlideMode;
import com.zhpan.bannerview.holder.HolderCreator;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android_serialport_api.cardwriteread.Constant;
import android_serialport_api.cardwriteread.R;
import android_serialport_api.cardwriteread.baiduface.baiducamera.AutoTexturePreviewView;
import com.yp.baselib.utils.SPUtils;
import android_serialport_api.cardwriteread.meal.BusUtils;
import android_serialport_api.cardwriteread.net.Apis;
import android_serialport_api.cardwriteread.net.HttpCallBackV2;
import android_serialport_api.cardwriteread.net.NetWorkUtils;
import android_serialport_api.cardwriteread.net.bean.ShopConfigRequest;
import android_serialport_api.cardwriteread.util.NamedThreadFactory;
import android_serialport_api.cardwriteread.view.adapter.NetViewHolder;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.zhpan.bannerview.constants.IndicatorStyle.ROUND_RECT;
import static com.zhpan.bannerview.constants.PageStyle.MULTI_PAGE_SCALE;


/**
 * 副屏幕
 *
 * @author 86139
 */
public class MainViceScreen extends Presentation {
    private Activity activity;
    public TextView tv_display;
    private TextView tv_title;
    private ImageView img;
    private TextView tvTitle;
    private AutoTexturePreviewView texturepreview;
    private BannerViewPager adBanner;
    private final int sumTime = 300;
    /**
     * 倒计时10秒 无操作直接开启广告
     */
    private int downTime = sumTime;
    private TextView adDetails;
    private ExecutorService threadPool;
    private View viewTest;
    private static boolean TEST;

    public MainViceScreen(Activity activity, Display display) {
        super(activity, display);
        this.activity = activity;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handle(Message message) {
        switch (message.what) {
            case 0x7788:
                findViewById(R.id.ad_details1).setVisibility(GONE);
                break;
            case 0x7789:
                findViewById(R.id.ad_details1).setVisibility(VISIBLE);
                break;
            case 0x7790:
                ((TextView) findViewById(R.id.tv1)).setText(message.obj.toString());
                break;
            case 0x7791:
                ((TextView) findViewById(R.id.tv2)).setText(message.obj.toString());
                break;
            case 0x7792:
                ((TextView) findViewById(R.id.tv0)).setText(message.obj.toString().split("-")[0]);
                ((TextView) findViewById(R.id.tv0_1)).setText(message.obj.toString().split("-")[1]);
                break;
            case 0x7793:
                texturepreview.setVisibility(VISIBLE);
                break;
            case 0x7794:
                texturepreview.setVisibility(View.INVISIBLE);
                break;
            default:
        }
    }

    public static void showAdPicture() {
        BusUtils.sendMsg(0x7789);
        Log.d("dasdasd", "+++++++++++++++++");
    }

    public static void hideAdPicture() {
        BusUtils.sendMsg(0x7788);
        Log.d("dasdasd", "-----------------");

    }

    public static void showTV0(String text1, String text2) {
        if(TEST){
            BusUtils.sendMsgWithObj(0x7792, text1 + "-" + text2);
        }
    }

    public static void showTV1(String text) {
        if(TEST){
            BusUtils.sendMsgWithObj(0x7790, text);
        }
    }

    public static void showTV2(String text) {
        if(TEST){
            BusUtils.sendMsgWithObj(0x7791, text);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_display_ad);
        EventBus.getDefault().register(this);
        initView();
        queryBannerList();
        threadPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(), new NamedThreadFactory("测试"));
        setOnDismissListener(dialog -> threadPool.shutdown());

        viewTest = findViewById(R.id.llTest);

//        findViewById(R.id.tv_title).setOnClickListener(v -> {
//            if(viewTest.getVisibility() == VISIBLE){
//                viewTest.setVisibility(GONE);
//                TEST = false;
//
//            } else {
//                viewTest.setVisibility(VISIBLE);
//                TEST = true;
//
//            }
//        });

    }

    private void startRun() {
        threadPool.submit(() -> {
            try {
                while (!threadPool.isShutdown()) {
                    Thread.sleep(1000);
                    downTime--;
                    System.out.println("downTime is " + downTime);
                    if (downTime <= 0) {
                        //开启广告
                        setBanner(VISIBLE);
                    } else {
                        setBanner(GONE);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
    }

    private void initView() {
        tv_display = findViewById(R.id.tv_display);
        tv_title = findViewById(R.id.tv_title);
        img = findViewById(R.id.img);
        texturepreview = findViewById(R.id.texturepreview);
        // 解决镜像问题
        if (SPUtils.INSTANCE.getInt(activity, "mirror", 1, "default") == 0) {
            texturepreview.setScaleX(-1);
        }

        adBanner = findViewById(R.id.ad_banner);
        adDetails = findViewById(R.id.ad_details);
    }

    public AutoTexturePreviewView getTexturePreview() {
        return texturepreview;
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 更新支付相关的文本
     *
     * @param money
     * @param color
     */
    public void updateTextForPay(final String money, final int color) {
        downTime = sumTime;
        tv_display.post(() -> {
            tv_display.setText(money);
            tv_display.setTextColor(color);
        });
    }

    //更新文字
    public void uploadDetails(final String details) {
        downTime = sumTime;
        adDetails.post(() -> {
                    adDetails.setText(details);
                }
        );
    }

    public void uploadDetails1(final String details) {
        threadPool.submit(() -> {
            activity.runOnUiThread(() -> adDetails.setText(details));
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            activity.runOnUiThread(() ->
                    adDetails.setText("")
            );
        });
    }

    public String getDetailsText() {
        return adDetails.getText().toString();
    }

    private void setBanner(final int v) {
        adBanner.post(() -> adBanner.setVisibility(v));
    }

    private void queryBannerList() {
        final List<String> bannerList = new ArrayList<>();
        final ShopConfigRequest shopConfigRequest = new ShopConfigRequest();
        shopConfigRequest.setBranchId(Constant.branchId);
        shopConfigRequest.setDeviceId(Build.SERIAL);
        shopConfigRequest.setShopId(Constant.shopId);
        RequestParams params = new RequestParams(Apis.OffLineIp + Apis.GetShopConfig);
        params.setAsJsonContent(true);
        params.setBodyContent(new Gson().toJson(shopConfigRequest));
        NetWorkUtils.postHttpRequest(activity, false, params, new HttpCallBackV2() {
            @Override
            public void onSuccess(String result) throws JSONException {
                JSONObject jsonObject = new JSONObject(result).getJSONObject("data");
                JSONArray array = jsonObject.getJSONArray("advertImgList");
                for (int i = 0; i < array.length(); i++) {
                    bannerList.add(array.getString(i));
                }
                adBanner.showIndicator(true)
                        .setInterval(3000)
                        .setCanLoop(false)
                        .setAutoPlay(true)
                        .setIndicatorSlideMode(IndicatorSlideMode.WORM)
                        .setRoundCorner(2)
                        .setIndicatorStyle(ROUND_RECT)
                        .setIndicatorColor(Color.parseColor("#8C6C6D72"), Color.parseColor("#F9BD00"))
                        .setIndicatorGravity(IndicatorGravity.CENTER)
                        .setScrollDuration(1000)
                        .setPageStyle(MULTI_PAGE_SCALE)
                        .setHolderCreator(new HolderCreator<NetViewHolder>() {
                            @Override
                            public NetViewHolder createViewHolder() {
                                return new NetViewHolder();
                            }
                        })
                        .create(bannerList);
                startRun();
            }

            @Override
            public void onCancelled(String msg) {

            }

            @Override
            public void onError(String error) {

            }

            @Override
            public void onFinish() {

            }
        });
    }

}