package android_serialport_api.cardwriteread.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kproduce.roundcorners.RoundTextView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import android_serialport_api.cardwriteread.R;
import android_serialport_api.cardwriteread.contract.LoginContract;
import android_serialport_api.cardwriteread.presenter.LoginPresenter;
import android_serialport_api.cardwriteread.util.SharedUtil;

/**
 * 登录页
 *
 * @author 86139
 */
public class LoginActivity extends AppCompatActivity implements LoginContract.View, View.OnClickListener {

    private LoginPresenter presenter;
    private RoundTextView tvSetIp;
    private EditText etUsername;
    private EditText etPassword;
    public WelcomeViceScreen screenWelcome = null;

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        checkBluetoothPermission();
        new RxPermissions(this).requestEachCombined(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.NFC,
                Manifest.permission.BIND_NFC_SERVICE)
                .subscribe(permission -> {
                            presenter.getBaiDuSqm(this);
                        }
                );
        initPresenter();
        initView();
        initViceScreen();
        //检测是否需要自动登陆
        presenter.autoLogin(this);

//        findViewById(R.id.ivLogo).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(LoginActivity.this, JieDanLoginActivity.class));
//            }
//        });

    }


    /**
     * 初始化副屏
     */
    private void initViceScreen() {
        // 获取显示管理器
        DisplayManager mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        // 获取设备的显示屏数量
        assert mDisplayManager != null;
        Display[] displayArr = mDisplayManager.getDisplays();
        // 如果设备的显示屏只有一个，则return
        if (displayArr.length < 2) {
            return;
        }
        try {
            // 将显示屏数组的第二个元素传入构造函数，创建一个欢迎页副屏
            screenWelcome = new WelcomeViceScreen(this, displayArr[1]);
            // 显示副屏
            screenWelcome.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化Presenter
     */
    private void initPresenter() {
        presenter = new LoginPresenter();
        presenter.attachView(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 当副屏依赖的Activity销毁时，一定要进行dismiss，否则会造成内存泄漏
        if (screenWelcome != null) {
            screenWelcome.dismiss();
        }
        if (presenter != null) {
            presenter.detachView();
        }
    }

    private void initView() {
        tvSetIp = findViewById(R.id.tvSetIP);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        String username = SharedUtil.getInstatnce().getShared(LoginActivity.this).getString("username", "");
        if (!"".equals(username)) {
            etUsername.setText(username);
        }
        tvSetIp.setOnClickListener(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSetIP:
                // 设置ip地址
                // 运维使用点击输入密码进去调试
                showDialogSetting();
                break;
            case R.id.btnLogin:
                // 登录
                presenter.login(this, etUsername.getText().toString(), etPassword.getText().toString());
                break;
            default:
        }
    }

    private final String PWD_IP_SET = "123456";

    /**
     * 显示弹窗用于设置IP地址前的校验
     */
    private void showDialogSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("请输入密码");
        final EditText editText = new EditText(LoginActivity.this);
        builder.setView(editText);
        builder.setPositiveButton("确认", (dialog, which) -> {
            if (TextUtils.isEmpty(editText.getText())) {
                return;
            }
            if (PWD_IP_SET.equals(editText.getText().toString())) {
                Intent intent = new Intent(LoginActivity.this, SettingIpActivity.class);
                startActivity(intent);
            }
            dialog.cancel();
        });
        builder.setNegativeButton("取消", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void toast(final String s, int type) {
        runOnUiThread(() -> Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show());
    }

    // 权限申请 ---------------------------------------------------------

//    /**
//     * 读取手机状态权限
//     * 使用相机权限
//     * 存储权限
//     */
//    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_PHONE_STATE,
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.CAMERA,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//            , Manifest.permission.ACCESS_COARSE_LOCATION,
//            Manifest.permission.NFC, Manifest.permission.NFC_TRANSACTION_EVENT, Manifest.permission.BIND_NFC_SERVICE};
//
//    /**
//     * 校验权限
//     */
//    private void checkBluetoothPermission() {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            final int requestPermissionCode = 20002;
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, requestPermissionCode);
//            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, requestPermissionCode);
//            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, requestPermissionCode);
//            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, requestPermissionCode);
//            } else if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, requestPermissionCode);
//            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, requestPermissionCode);
//            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.NFC_TRANSACTION_EVENT) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, requestPermissionCode);
//            } else if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BIND_NFC_SERVICE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, requestPermissionCode);
//            } else {
//                System.out.println("无需授权");
//            }
//        } else {//小于23版本直接使用
//            System.out.println("checkBluetoothPermission: 小于23版本直接使用");
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        presenter.getBaiDuSqm(this);
//    }
}
