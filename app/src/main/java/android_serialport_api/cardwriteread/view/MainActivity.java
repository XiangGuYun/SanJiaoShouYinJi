package android_serialport_api.cardwriteread.view;


import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.tu.loadingdialog.LoadingDailog;
import com.google.gson.JsonParser;
import com.kproduce.roundcorners.RoundFrameLayout;
import com.yp.baselib.base.BaseFragment;
import com.yp.baselib.helper.SoundHelper;
import com.yp.baselib.utils.FileUtils;
import com.yp.baselib.utils.SPUtils;
import com.yp.baselib.utils.fragment.old.FragmentUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import android_serialport_api.cardwriteread.Constant;
import android_serialport_api.cardwriteread.R;
import android_serialport_api.cardwriteread.baiduface.baiducamera.AutoTexturePreviewView;
import android_serialport_api.cardwriteread.baiduface.baiducamera.CameraPreviewManager;
import android_serialport_api.cardwriteread.baiduface.baiduutils.ToastUtils;
import android_serialport_api.cardwriteread.contract.MainContract;
import android_serialport_api.cardwriteread.jiedan.JieDanFragment;
import android_serialport_api.cardwriteread.jiedan.MainActHelper;
import android_serialport_api.cardwriteread.jiedan.print.PrinterHelper;
import android_serialport_api.cardwriteread.meal.AllCaiModel;
import android_serialport_api.cardwriteread.meal.AllCaiTypeModel;
import android_serialport_api.cardwriteread.meal.Dlg;
import android_serialport_api.cardwriteread.meal.Jc_Utils;
import android_serialport_api.cardwriteread.meal.LeftListAdapter;
import android_serialport_api.cardwriteread.meal.MoneyUtils;
import android_serialport_api.cardwriteread.meal.MyListAdapter;
import android_serialport_api.cardwriteread.meal.RV;
import android_serialport_api.cardwriteread.meal.RightGridAdapter;
import android_serialport_api.cardwriteread.meal.TimerLayer;
import android_serialport_api.cardwriteread.net.Req;
import android_serialport_api.cardwriteread.pay.PayCardHelper;
import android_serialport_api.cardwriteread.presenter.MainPresenter;
import android_serialport_api.cardwriteread.util.MethodUtils;
import android_serialport_api.cardwriteread.util.SharedUtil;
import android_serialport_api.cardwriteread.util.SoundUtils;
import android_serialport_api.cardwriteread.view.adapter.CalculatorNumberAdapter;
import android_serialport_api.cardwriteread.view.adapter.OrderDataAdapter;
import android_serialport_api.cardwriteread.view.adapter.PromptAdapter;
import android_serialport_api.cardwriteread.weight.WeightFragment;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static android_serialport_api.cardwriteread.Constant.FACE_SETTING;
import static android_serialport_api.cardwriteread.Constant.FACE_SIMILAR;
import static android_serialport_api.cardwriteread.Constant.FIXED_SETTING;
import static android_serialport_api.cardwriteread.Constant.PWD_ENTER_FACE_MANAGER;


/**
 * @author 86139
 */
public class MainActivity extends me.yokeyword.fragmentation.SupportActivity implements MainContract.View, View.OnClickListener {

    public static final String NUMBER_MODIFIER_FACE_RECO = "6666.66";

    public MainPresenter presenter;

    private LinearLayout llSystemExit;
    private RecyclerView rvOrder;
    private TextView tvShopName;
    private TextView tvOrderPlaceholder;
    private Button btnOrderStatistical;
    private TextView tvBusinessCode;
    private Button btnSettFixedCashier;
    private Button btnSwitchFaceRecognition;
    private RecyclerView rvPromptPrice;
    private EditText etPriceInput;
    private RecyclerView rvKeyboardNumber;
    private Button btnCheckOut;
    private Button btnFaceFaceManager;
    /**
     * 支付弹窗
     */
    private RoundFrameLayout flPayDialogLayout;
    private RoundFrameLayout flPayDialogLayout1;
    private TextView tvPayDialogMoney;
    private TextView tvPayDialogMoney1;
    private Button btnClosePayDialog;
    private Button btnClosePayDialog1;
    private TextView tvPayDialogDetails;
    private TextView tvPayDialogDetails1;

    /**
     * 测试用
     */
    private AutoTexturePreviewView textureFaceImg;
    private AlertDialog dialogSettings;
    public static boolean isMeal = false;
    public JieDanFragment fragJieDan;
    public WeightFragment fragWeight;
    public boolean isJieDan = true;
    private Badge badge1;
    private Badge badge2;
    private LeftListAdapter leftListAdapter;

    public void showShouYin() {
        findViewById(R.id.fl_meal).setVisibility(View.GONE);
    }

    public void showDianCan() {
        findViewById(R.id.fl_meal).setVisibility(View.VISIBLE);
    }

    public FragmentUtils<BaseFragment> fu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        String config = FileUtils.INSTANCE.readAssetsText(this, "Config.json");
        isJieDan = new JsonParser().parse(config).getAsJsonObject().get("isJieDan").getAsBoolean();
        tv(R.id.btnJieDan).setText(isJieDan ? "接单模式" : "称重收银模式");
        tv(R.id.tv_jiedan).setText(isJieDan ? "接单模式" : "称重收银模式");
        if(!isJieDan) tv(R.id.tv_jiedan).setTextSize(30);
        try {
            if (isJieDan) {
                fragJieDan = new JieDanFragment();
                fu = new FragmentUtils(this, fragJieDan, R.id.flContainer);
                fu.hide(fragJieDan);
            } else {
                fragWeight = new WeightFragment();
                fu = new FragmentUtils(this, fragWeight, R.id.flContainer);
                fu.hide(fragWeight);
            }


            initPresenter(); //initPresenter方法一定要在所有方法之前进行初始化

            PrinterHelper.Companion.getInstance().connect(this);

            findViewById(R.id.viewJieDan).setOnClickListener(v -> {
                MainActHelper.Companion.getInstance(this).showFragment(R.id.flContainer);
            });


            PayCardHelper.getInstance().initCardReader((String cardNumber) -> {
                Log.d("CardReader", cardNumber);
                presenter.doCardNumber(cardNumber);
            });

            initView();
            presenter.initViceScreen();
            presenter.initCamera(this, textureFaceImg);
            presenter.enableBackgroundUpdate(true);
            //初始化人脸识别是否开启 默认开启
            if (!SharedUtil.getInstatnce().getShared(this).getBoolean(Constant.branchId + FACE_SETTING, true)) {
                presenter.setisFacePay(false);
                btnSwitchFaceRecognition.setText("开启人脸识别");
            }
            //初始化固定收银是否开启 默认不开启
            if (SharedUtil.getInstatnce().getShared(this).getBoolean(Constant.branchId + FIXED_SETTING, false)) {
                presenter.uploadFixedPayState();
                btnSettFixedCashier.setText("取消固定收银");
            }
            //默认不是支付状态关闭人脸识别显示流
            //把textureView隐藏掉默认不会去显示
            presenter.getmPresentation().getTexturePreview().setVisibility(View.GONE);

            initMealView();

            findViewById(R.id.viewMeal).setOnClickListener(v -> {
                findViewById(R.id.fl_meal).setVisibility(View.VISIBLE);
//                startActivity(new Intent(getActivity(), MealActivity.class));
//                finish();
            });
        } catch (Exception e) {

        }

    }

    /**
     * 菜品列表
     */
    private List<AllCaiModel.DataBean.ResultsBean> listDishes = new ArrayList<>();

    /**
     * 菜品类别列表
     */
    private List<AllCaiTypeModel.DataBean.ResultsBean> listDishClass = new ArrayList<>();

    /**
     * 已点餐品列表
     */
    private List<AllCaiModel.DataBean.ResultsBean> listDishOrder = new ArrayList<>();

    /**
     * 加载中对话框
     */
    private LoadingDailog dialogLoading = null;

    /**
     * 订单统计对话框
     */
    private Dlg.OrderStatDialog dialogOrderStat = null;

    /**
     * 待支付价格
     */
    private double priceForPay = 0.0;

    /**
     * 当前选中的菜品类别列表位置
     */
    private static int selectedClassIndex = 0;

    /**
     * 用于计时
     */
    private Timer timer = null;

    private int badgeNumber1 = 0;

    @Subscribe
    public void handle(Message msg) {

        switch (msg.what) {
            case 0x123:
                if (!MainActHelper.Companion.getInstance(null).isShow()) {
                    badgeNumber1++;
                    badge1.setBadgeNumber(badgeNumber1);
                    badge2.setBadgeNumber(badgeNumber1);
                }
                break;
            case 0x124:
                badgeNumber1 = 0;
                badge1.setBadgeNumber(badgeNumber1);
                badge2.setBadgeNumber(badgeNumber1);
            case 0x1111:
                clearMealOrder();
                break;
            default:
        }

    }

    private void initMealView() {
        dialogLoading = Dlg.getLoadingDialog(this);
        dialogOrderStat = Dlg.getOrderStatDialog(this);
        initTimer();
        initDishClassListView();
        initDishesGridView();
        initOrderDishListView();
        tv(R.id.tv_qie).setOnClickListener(v -> findViewById(R.id.fl_meal).setVisibility(View.GONE));
        tv(R.id.tv_jiedan).setOnClickListener(v -> {
            MainActHelper.Companion.getInstance(this).showFragment(R.id.flContainer);
        });
        tv(R.id.tv_jiesuan).setOnClickListener(v -> {
            SoundUtils.play(2);
            pay(true);
            isMeal = true;
        });
        tv(R.id.tv_baobiao).setOnClickListener(v -> {
            dialogLoading.show();
            Req.reqOrderCount(this, list -> {
                dialogOrderStat.updateAndShow(list);
                dialogLoading.dismiss();
            });
        });
        tv(R.id.tv_refresh).setOnClickListener(v -> Req.reqDishesTypes(true, list -> bindDishClassData(list), list -> bindAllCaiData(list)));
        RV.doXNumberList(findViewById(R.id.rvX));
        Req.reqDishesTypes(false,
                list -> {
                    bindDishClassData(list);
                },
                list -> {
                    bindAllCaiData(list);
                });
    }

    private void bindAllCaiData(List<AllCaiModel.DataBean.ResultsBean> list) {
        listDishes.clear();
        listDishes.addAll(list);
        RV.update((GridView) findViewById(R.id.gridView));
        for (int i = 0; i < listDishClass.size(); i++) {
            List<AllCaiModel.DataBean.ResultsBean> mList = new ArrayList<>();
            for (int j = 0; j < listDishes.size(); j++) {
                if (listDishClass.get(i).getId() == listDishes.get(j).getMinorCategoryId()) {
                    mList.add(listDishes.get(j));
                }
            }
            listDishClass.get(i).setmList(mList);
        }
        listDishes.clear();
        listDishes.addAll(listDishClass.get(selectedClassIndex).getmList());
        RV.update((GridView) findViewById(R.id.gridView));
    }

    private void bindDishClassData(List<AllCaiTypeModel.DataBean.ResultsBean> list) {
        listDishClass.clear();
        listDishClass.addAll(list);
        RV.update((ListView) findViewById(R.id.listview_right));
    }

    private void initOrderDishListView() {
        leftListAdapter = new LeftListAdapter(this, listDishOrder,
                position -> delSelectedOrderDish(position));
        ((ListView) findViewById(R.id.listview_left)).setAdapter(leftListAdapter);
    }

    private void delSelectedOrderDish(int position) {
        if (listDishOrder.get(position).getCount() == 1) {
            listDishOrder.remove(position);
            RV.update((ListView) findViewById(R.id.listview_left));
            calculateMoney();
            return;
        }
        listDishOrder.get(position).setCount(listDishOrder.get(position).getCount());
        RV.update((ListView) findViewById(R.id.listview_left));
        calculateMoney();
    }

    private void initDishesGridView() {
        RightGridAdapter rightGridAdapter = new RightGridAdapter(this, listDishes,
                position -> addDishToOrderList(position));
        ((GridView) findViewById(R.id.gridView)).setAdapter(rightGridAdapter);
    }

    private void addDishToOrderList(int position) {
        if (isSame(position)) {
            for (int i = 0; i < listDishOrder.size(); i++) {
                if (listDishOrder.get(i).getId() == listDishes.get(position).getId()) {
                    listDishOrder.get(i).setCount(listDishOrder.get(i).getCount() + 1);
                }
            }
        } else {
            listDishOrder.add(listDishes.get(position));
        }
        RV.update((ListView) findViewById(R.id.listview_left));
        calculateMoney();
    }

    private TextView tv(int id) {
        return findViewById(id);
    }

    public static int selectedIndex = 0;

    private void calculateMoney() {
        int count = 0;
        int totalMoney = 0;
        if (listDishOrder.size() == 0) {
            tv(R.id.tv_total).setText("总计¥" + MoneyUtils.changeF2Y(totalMoney * (selectedIndex + 1)));
            tv(R.id.tv_count1).setText("数量" + count * (selectedIndex + 1));
            priceForPay = Double.parseDouble(MoneyUtils.changeF2Y(totalMoney));
            return;
        }
        for (int i = 0; i < listDishOrder.size(); i++) {
            count += listDishOrder.get(i).getCount();
            totalMoney += listDishOrder.get(i).getCount() * listDishOrder.get(i).getPrice();
        }
        tv(R.id.tv_total).setText("总计¥" + MoneyUtils.changeF2Y(totalMoney * (selectedIndex + 1)));
        priceForPay = Double.parseDouble(MoneyUtils.changeF2Y(totalMoney));
        tv(R.id.tv_count1).setText("数量" + count * (selectedIndex + 1));
    }

    public void clearMealOrder(){
        listDishOrder.clear();
        leftListAdapter.notifyDataSetChanged();
        tv(R.id.tv_total).setText("总计¥0");
        tv(R.id.tv_count1).setText("数量");
    }

    /**
     * 判断菜品是否已经位于已点餐品列表中
     */
    private boolean isSame(int position) {
        if (listDishOrder.size() == 0) return false;
        for (int i = 0; i < listDishOrder.size(); i++) {
            if (listDishOrder.get(i).getId() == listDishes.get(position).getId()) {
                return true;
            }
        }
        return false;
    }

    private void initDishClassListView() {
        MyListAdapter adapter = new MyListAdapter(this, listDishClass);
        ListView listview_right = findViewById(R.id.listview_right);
        listview_right.setAdapter(adapter);
        listview_right.setOnItemClickListener((parent, view, position, id) -> {
            selectedClassIndex = position;
            adapter.setCurPositon(position);
            adapter.notifyDataSetChanged();
            listview_right.smoothScrollToPositionFromTop(position, (parent.getHeight() - view.getHeight()) / 2);
            if (listDishClass.size() == 0) {
                return;
            }
            List<AllCaiModel.DataBean.ResultsBean> childListBeanList = listDishClass.get(position).getmList();
            if (childListBeanList == null) {
                childListBeanList = new ArrayList<>();
            }
            listDishes.clear();
            listDishes.addAll(childListBeanList);
            RV.update((GridView) findViewById(R.id.gridView));
        });
    }

    private void initTimer() {
        timer = TimerLayer.getCommonTimer(this, () -> {
            ((TextView) findViewById(R.id.tv_title)).setText(Constant.shopName);
            ((TextView) findViewById(R.id.tv_date)).setText(Jc_Utils.timeStamp2Date(System.currentTimeMillis()));
            ((TextView) findViewById(R.id.tv_version)).setText("v" + Jc_Utils.getAppVersionName(this));
        });
    }

    private void initView() {
        //退出
        llSystemExit = findViewById(R.id.system_out);
        //商铺名称
        tvShopName = findViewById(R.id.shopname);
        //订单列表
        rvOrder = findViewById(R.id.orderdata);
        //订单占位
        tvOrderPlaceholder = findViewById(R.id.order_placeholder);
        //订单统计
        btnOrderStatistical = findViewById(R.id.order_statistical);
        //tvBusinessCode(商户)信息，位于退出按钮的右侧
        tvBusinessCode = findViewById(R.id.vip);
        //设置固定收银
        btnSettFixedCashier = findViewById(R.id.setting_cashier);
        //关闭开启人脸识别
        btnSwitchFaceRecognition = findViewById(R.id.face_recognition);
        //计算器输入提示
        rvPromptPrice = findViewById(R.id.prompt);
        //计算器显示框
        etPriceInput = findViewById(R.id.calculator_input);
        //键盘数字
        rvKeyboardNumber = findViewById(R.id.calculator_number);
        //结账
        btnCheckOut = findViewById(R.id.calculator_res);
        //支付弹窗布局
        flPayDialogLayout = findViewById(R.id.paydialog_layout);
        flPayDialogLayout1 = findViewById(R.id.paydialog_layout1);
        tvPayDialogMoney = findViewById(R.id.paydialog_money);
        tvPayDialogMoney1 = findViewById(R.id.paydialog_money1);
        tvPayDialogDetails = findViewById(R.id.paydialog_details);
        tvPayDialogDetails1 = findViewById(R.id.paydialog_details1);
        btnClosePayDialog = findViewById(R.id.paydialog_cancel);
        btnClosePayDialog1 = findViewById(R.id.paydialog_cancel1);
        //人脸管理
        btnFaceFaceManager = findViewById(R.id.face_facemanage);
        //测试
        textureFaceImg = findViewById(R.id.draw_detect_face_view);


        llSystemExit.setOnClickListener(this);
        btnSettFixedCashier.setOnClickListener(this);
        btnCheckOut.setOnClickListener(this);
        btnClosePayDialog.setOnClickListener(this);
        btnClosePayDialog1.setOnClickListener(this);
        btnFaceFaceManager.setOnClickListener(this);
        btnOrderStatistical.setOnClickListener(this);
        btnSwitchFaceRecognition.setOnClickListener(this);

        //初始话基本数据
        initData();

        //初始化键盘
        initKeyboard();

        //开启输入监听
        etPriceInput.addTextChangedListener(textWatcher);

        //开启二维码扫描
        presenter.initSearchQrCode();

        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_settings, null);

        dialogView.findViewById(R.id.rbQianZhi).setOnClickListener(v -> {
            SPUtils.INSTANCE.put(this, "CameraFace", CameraPreviewManager.CAMERA_FACING_FRONT, "default");
        });

        dialogView.findViewById(R.id.rbHouZhi).setOnClickListener(v -> {
            ToastUtils.toast("后置");
            SPUtils.INSTANCE.put(this, "CameraFace", CameraPreviewManager.CAMERA_FACING_BACK, "default");
        });

        if (Integer.parseInt(SPUtils.INSTANCE.get(this, "CameraFace", CameraPreviewManager.CAMERA_FACING_FRONT, "default").toString()) == CameraPreviewManager.CAMERA_FACING_FRONT) {
            ((RadioButton) dialogView.findViewById(R.id.rbQianZhi)).setChecked(true);
        } else {
            ((RadioButton) dialogView.findViewById(R.id.rbHouZhi)).setChecked(true);
        }

        dialogView.findViewById(R.id.rbPre0).setOnClickListener(v -> {
            SPUtils.INSTANCE.put(this, "displayOrient", 0, "default");
        });
        dialogView.findViewById(R.id.rbPre90).setOnClickListener(v -> {
            SPUtils.INSTANCE.put(this, "displayOrient", 90, "default");
        });

        dialogView.findViewById(R.id.rbPre180).setOnClickListener(v -> {
            SPUtils.INSTANCE.put(this, "displayOrient", 180, "default");
        });

        dialogView.findViewById(R.id.rbPre270).setOnClickListener(v -> {
            SPUtils.INSTANCE.put(this, "displayOrient", 270, "default");
        });

        dialogView.findViewById(R.id.rbShowCardYes).setOnClickListener(v -> {
            SPUtils.INSTANCE.put(this, "debug", 1, "default");
        });

        dialogView.findViewById(R.id.rbShowCardNo).setOnClickListener(v -> {
            SPUtils.INSTANCE.put(this, "debug", 0, "default");
        });

        switch (SPUtils.INSTANCE.getInt(this, "displayOrient", 0, "default")) {
            case 0:
                ((RadioButton) dialogView.findViewById(R.id.rbPre0)).setChecked(true);
                break;
            case 90:
                ((RadioButton) dialogView.findViewById(R.id.rbPre90)).setChecked(true);
                break;
            case 180:
                ((RadioButton) dialogView.findViewById(R.id.rbPre180)).setChecked(true);
                break;
            case 270:
                ((RadioButton) dialogView.findViewById(R.id.rbPre270)).setChecked(true);
                break;
            default:
        }

        dialogView.findViewById(R.id.rbDetect0).setOnClickListener(v ->
                SPUtils.INSTANCE.put(this, "detectDir", 0, "default"));

        dialogView.findViewById(R.id.rbDetect90).setOnClickListener(v -> {
            SPUtils.INSTANCE.put(this, "detectDir", 90, "default");
        });

        dialogView.findViewById(R.id.rbDetect180).setOnClickListener(v -> {
            SPUtils.INSTANCE.put(this, "detectDir", 180, "default");
        });

        dialogView.findViewById(R.id.rbDetect270).setOnClickListener(v -> {
            SPUtils.INSTANCE.put(this, "detectDir", 270, "default");
        });

        switch (SPUtils.INSTANCE.getInt(this, "detectDir", 0, "default")) {
            case 0:
                ((RadioButton) dialogView.findViewById(R.id.rbDetect0)).setChecked(true);
                break;
            case 90:
                ((RadioButton) dialogView.findViewById(R.id.rbDetect90)).setChecked(true);
                break;
            case 180:
                ((RadioButton) dialogView.findViewById(R.id.rbDetect180)).setChecked(true);
                break;
            case 270:
                ((RadioButton) dialogView.findViewById(R.id.rbDetect270)).setChecked(true);
                break;
            default:
        }

        ((EditText) dialogView.findViewById(R.id.etCardLength)).setText(SPUtils.INSTANCE.getInt(this, "cardLength", 9, "default") + "");

        ((EditText) dialogView.findViewById(R.id.etSimilar)).setText(SPUtils.INSTANCE.getInt(this, FACE_SIMILAR, 94, "default") + "");

        dialogView.findViewById(R.id.tvClose).setOnClickListener(v -> {
            SPUtils.INSTANCE.put(this, FACE_SIMILAR, Integer.parseInt(((EditText) dialogView.findViewById(R.id.etSimilar)).getText().toString()), "default");
            SPUtils.INSTANCE.put(this, "cardLength", Integer.parseInt(((EditText) dialogView.findViewById(R.id.etCardLength)).getText().toString()), "default");

            dialogSettings.dismiss();
        });

        dialogView.findViewById(R.id.rbMirrorYes).setOnClickListener(v -> {
            SPUtils.INSTANCE.put(this, "mirror", 1, "default");
        });

        dialogView.findViewById(R.id.rbMirrorNo).setOnClickListener(v -> {
            SPUtils.INSTANCE.put(this, "mirror", 0, "default");
        });

        if (SPUtils.INSTANCE.getInt(this, "mirror", 0, "default") == 1) {
            ((RadioButton) dialogView.findViewById(R.id.rbMirrorYes)).setChecked(true);
        } else {
            ((RadioButton) dialogView.findViewById(R.id.rbMirrorNo)).setChecked(true);
        }


        dialogSettings = new AlertDialog.Builder(this).setView(dialogView).create();

        badge1 = new QBadgeView(this).bindTarget(findViewById(R.id.viewJieDan)).setBadgeTextSize(30f, true)
                .setGravityOffset(0f, 0f, true).setShowShadow(false).setBadgePadding(4f, true)
                .setBadgeBackgroundColor(Color.parseColor("#E64545"));

        badge2 = new QBadgeView(this).bindTarget(findViewById(R.id.tv_jiedan)).setBadgeTextSize(30f, true)
                .setGravityOffset(0f, 0f, true).setShowShadow(false).setBadgePadding(4f, true)
                .setBadgeBackgroundColor(Color.parseColor("#E64545"));

    }

    /**
     * 初始化数据
     */
    private void initData() {
        try {
            tvShopName.setText(Constant.shopName);
            tvBusinessCode.setText(String.format("商户:%s", Constant.curUsername));
            initOrderData();
        } catch (Exception e) {
            toast("initData error", 0);
            e.printStackTrace();
        }
    }

    /**
     * 初始化订单数据
     */
    @Override
    public void initOrderData() {
        OrderDataAdapter orderDataAdapter = new OrderDataAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvOrder.setLayoutManager(linearLayoutManager);
        rvOrder.setAdapter(orderDataAdapter);
    }

    /**
     * 监听editText输入，退出MainActivity时记得销毁监听。
     */
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            try {
                updatePromptListAbovePriceInput();
                presenter.setMoney(Float.parseFloat(etPriceInput.getText().toString()));
                //更新副屏幕
                presenter.updateViceScreenDisplay();
            } catch (Exception e1) {
                presenter.setMoney(0);
                presenter.updateViceScreenDisplay();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /**
     * 更新价格输入框上方的提示价格列表
     */
    private void updatePromptListAbovePriceInput() {
        try {
            PromptAdapter promptAdapter = new PromptAdapter(etPriceInput, v -> {
                // 当点击某个提示价格列表项时，更新价格输入框文字
                etPriceInput.setText(String.valueOf(v));
                // 并触发支付操作
                pay(false);
            });
            LinearLayoutManager manager = new LinearLayoutManager(MainActivity.this);
            manager.setOrientation(LinearLayoutManager.HORIZONTAL);
            rvPromptPrice.setLayoutManager(manager);
            rvPromptPrice.setAdapter(promptAdapter);
        } catch (Exception e) {
            toast("updatePromptListAbovePriceInput error", 0);
            e.printStackTrace();
        }
    }

    private void initPresenter() {
        presenter = new MainPresenter();
        presenter.attachView(this);
    }

    /**
     * 初始化键盘
     */
    private void initKeyboard() {
        try {
            CalculatorNumberAdapter rvKeyboardNumberAdapter = new CalculatorNumberAdapter((int) (getWindowManager().getDefaultDisplay().getHeight() -
                    MethodUtils.getInstanceSingle().getDimens(this, R.dimen.dp_220)), etPriceInput);
            GridLayoutManager manager = new GridLayoutManager(this, 3);
            rvKeyboardNumber.setLayoutManager(manager);
            rvKeyboardNumber.setAdapter(rvKeyboardNumberAdapter);
        } catch (Exception e) {
            toast("initKeyboard error", 0);
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("dasdasdad", "onRestart");
        presenter.initCamera(this, textureFaceImg);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("dasdasdad", "onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("dasdasdad", "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("dasdasdad", "onStop");
        CameraPreviewManager.getInstance().stopPreview();
    }

    @Override
    protected void onDestroy() {
        Log.d("dasdasdad", "onDestroy");
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (presenter != null) {
            presenter.detachView();
        }
        etPriceInput.removeTextChangedListener(textWatcher);
        timer.cancel();
        PrinterHelper.Companion.getInstance().disconnect(this);
        SoundHelper.Companion.getInstance().release();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        try {
            if (presenter.scanGunKeyEventHolder.isInputFromReader(this, event)) {
                if (presenter.scanGunKeyEventHolder != null) {
                    //控制隐藏显示，在拦截软键盘输入事件的时候，放置button误触退出该界面
                    presenter.scanGunKeyEventHolder.resolveKeyEvent(event);
                }
            }
        } catch (Exception e) {
            toast("dispatchKeyEvent error", 0);
            e.printStackTrace();
        }
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void toast(final String s, int type) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show());
    }

    @Override
    public void pay(boolean isMeal) {
        try {
            if (!isMeal) {
                // 如果输入的价格是6666.60，则进入人脸管理界面
                if (PWD_ENTER_FACE_MANAGER.equals(etPriceInput.getText().toString())) {
                    startActivity(new Intent(MainActivity.this, FaceManageActivity.class));
                    return;
                }
                if ("6666.66".equals(etPriceInput.getText().toString())) {
                    dialogSettings.show();
                    return;
                }
                // 如果输入的价格是6666.66，则打开阈值设置对话框
                if (NUMBER_MODIFIER_FACE_RECO.equals(etPriceInput.getText().toString())) {
                    EditText editText = new EditText(this);
                    editText.setHint("请输入相似度阈值（当前阈值是" + SPUtils.INSTANCE.get(this, Constant.FACE_SIMILAR, 94, "default").toString() + "）");
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    new AlertDialog.Builder(this).setTitle("设置人脸相似度阈值").setView(
                            editText
                    ).setPositiveButton("确定", (dialog, which) -> {
                        if (Integer.parseInt(editText.getText().toString()) <= 100) {
                            ToastUtils.toast("设置成功");
                            SPUtils.INSTANCE.put(this, FACE_SIMILAR, Integer.parseInt(editText.getText().toString()), "default");
                            dialog.dismiss();
                        } else {
                            ToastUtils.toast("设置失败，相似度不能超出100");
                        }
                    }).setNegativeButton("取消", (dialog, which) -> {
                        dialog.dismiss();
                    }).show();
                    return;
                }
                if (TextUtils.isEmpty(etPriceInput.getText()) || Double.parseDouble(etPriceInput.getText().toString()) <= 0) {
                    // music：请输入金额
                    SoundUtils.play(1);
                    toast("金额不能为空", 0);
                    return;
                }
            } else {
                // 结算
                int payMoney = 0;
                if (listDishOrder.size() == 0) {
                    payMoney = 0;
                } else {
                    int i = 0;
                    while (i < listDishOrder.size()) {
                        payMoney += listDishOrder.get(i).getCount() * listDishOrder.get(i).getPrice();
                        i++;
                    }
                }
                if (payMoney == 0) {
                    ToastUtils.toast("请添加菜");
                    return;
                }
            }
            presenter.getmPresentation().getTexturePreview().setVisibility(View.VISIBLE);
            if (!isMeal) {
                presenter.setMoney(Float.parseFloat(etPriceInput.getText().toString()));
                tvPayDialogMoney.setText(etPriceInput.getText().toString());
            } else {
                presenter.setMoney((float) priceForPay);
                tvPayDialogMoney1.setText(String.valueOf(priceForPay));
                presenter.updateViceScreenDisplay();
            }
            uploadPayDialogText("待支付");
            // 打开支付对话框
            openPayDialog(isMeal);
            presenter.uploadPayState(true); //更新支付状态
            presenter.enableBackgroundUpdate(false); //停止后台更新
        } catch (Exception e) {
            toast("pay error", 0);
            e.printStackTrace();
        }
    }

    @Override
    public MainActivity getActivity() {
        return MainActivity.this;
    }

    /**
     * 开启支付弹窗
     */
    @Override
    public void openPayDialog(boolean isMeal) {
        if (isMeal) {
            flPayDialogLayout1.setVisibility(View.VISIBLE);
        } else {
            flPayDialogLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 关闭支付弹窗
     */
    @Override
    public void closePayDialog(boolean isMeal) {
//        if(!presenter.isPayState() && !btnSettFixedCashier.getText().toString().equals("取消固定收银")){
        presenter.getmPresentation().getTexturePreview().setVisibility(View.GONE);
        flPayDialogLayout.setVisibility(View.GONE);
        flPayDialogLayout1.setVisibility(View.GONE);
        //清除输入框
        etPriceInput.setText("");
        presenter.getmPresentation().uploadDetails("");
        uploadPayDialogText("待输入");
        presenter.uploadPayState(false);
        //开启后台更新
        presenter.enableBackgroundUpdate(true);
//        }
    }

    /**
     * 更新弹窗内容
     *
     * @param s
     */
    @Override
    public void uploadPayDialogText(final String s) {
        runOnUiThread(() -> {
            if (!isMeal) {
                tvPayDialogDetails.setText(s);
            } else {
                tvPayDialogDetails1.setText(s);
            }
        });
    }

    /**
     * 打开统计订单弹窗
     *
     * @param list
     */
    @Override
    public void showStatisticalOrderDataDialog(List<com.alibaba.fastjson.JSONObject> list) {
        OrderDataDialog dataDialog = new OrderDataDialog(this, list);
        dataDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.system_out:
                //提出
                cancel();
                break;
            case R.id.calculator_res:
                //开启支付状态
                SoundUtils.play(2);
                pay(false);
                isMeal = false;
                break;
            case R.id.paydialog_cancel:
                //弹窗取消按钮
                closePayDialog(false);
                break;
            case R.id.paydialog_cancel1:
                //弹窗取消按钮
                closePayDialog(true);
                break;
            case R.id.setting_cashier:
                //设置/取消固定收银
                SoundUtils.play("设置固定收银".equals(btnSettFixedCashier.getText().toString()) ? 9 : 8);
                btnSettFixedCashier.setText("设置固定收银".equals(btnSettFixedCashier.getText().toString()) ? "取消固定收银" : "设置固定收银");
                presenter.uploadFixedPayState();
                break;
            case R.id.face_facemanage:
                startActivity(new Intent(MainActivity.this, FaceManageActivity.class));
                break;
            case R.id.order_statistical:
                //订单统计
                presenter.statisticalOrderData();
                break;
            case R.id.face_recognition:
                //关闭/开启人脸识别支付
                final String CLOSE_FACE = "关闭人脸识别";
                if (CLOSE_FACE.equals(btnSwitchFaceRecognition.getText().toString())) {
                    SoundUtils.play(10);
                    btnSwitchFaceRecognition.setText("开启人脸识别");
                    presenter.setisFacePay(false);
                } else {
                    SoundUtils.play(11);
                    btnSwitchFaceRecognition.setText("关闭人脸识别");
                    presenter.setisFacePay(true);
                }
                break;
            default:
        }
    }

    @Override
    public void onBackPressedSupport() {

        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("确定要退出应用吗？")
                .setNegativeButton("否", (dialog, which) -> {
                    dialog.dismiss();
                }).setPositiveButton("是", (dialog, which) -> {
            super.onBackPressedSupport();
        }).create().show();

    }

    /**
     * 退出系统到登陆界面
     */
    private void cancel() {
        presenter.enableBackgroundUpdate(false);
        SharedUtil.getInstatnce().getShared(this).edit().clear().apply();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

}
