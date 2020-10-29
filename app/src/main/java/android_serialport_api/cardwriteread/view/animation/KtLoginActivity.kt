package android_serialport_api.cardwriteread.view.animation

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import android_serialport_api.cardwriteread.Constant
import android_serialport_api.cardwriteread.R
import android_serialport_api.cardwriteread.baiduface.baiduutils.ToastUtils
import android_serialport_api.cardwriteread.kt.NetUtils
import com.yp.baselib.utils.SPUtils
import android_serialport_api.cardwriteread.net.Apis
import android_serialport_api.cardwriteread.net.bean.LoginRequest
import android_serialport_api.cardwriteread.net.bean.LoginResponseV2
import android_serialport_api.cardwriteread.util.MethodUtils
import android_serialport_api.cardwriteread.view.SettingIpActivity
import android_serialport_api.cardwriteread.view.SubbranchActivity
import com.afollestad.materialdialogs.MaterialDialog
import com.google.gson.Gson
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_login.*
import org.xutils.http.RequestParams

/**
 * 登录页
 */
class KtLoginActivity : AppCompatActivity() {
    private lateinit var dialog: MaterialDialog

    // 密码验证状态 -1未验证 0正在验证 1验证成功
    private var mLoginState = -1;
    //百度人脸初始化是否完成 -1未完成 0正在验证 1验证成功
    private val mBaiDuFaceInitState = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        dialog = MethodUtils.getInstanceSingle().openHttpDialog(0, this)

        // 申请权限
        reqPermissions()

        // 取用户名
        etUsername.setText(SPUtils.get(this, "username", "").toString())
        
        // 显示弹窗用于设置IP地址前的校验
        tvSetIP.setOnClickListener {
            val editText = EditText(this)
            AlertDialog.Builder(this).apply {
                setTitle("请输入密码")
                setView(editText)
                setPositiveButton("确认") { dialog: DialogInterface, _: Int ->
                    if (editText.text.isEmpty())
                        return@setPositiveButton
                    if (editText.text.toString() == "123456")
                        startActivity(Intent(this@KtLoginActivity, SettingIpActivity::class.java))
                    dialog.cancel()
                }
                setNegativeButton("取消") { dialog, _ -> dialog.cancel() }
                show()
            }
        }

        // 登录
        btnLogin.setOnClickListener {
            val username = etUsername.text.toString()
            val password = etPassword.text.toString()
            if(mLoginState == 0) return@setOnClickListener
            mLoginState = 0
            if (username.isEmpty() || password.isEmpty()){
                ToastUtils.toast(this, "账号或密码不能为空")
                return@setOnClickListener
            }
            val loginRequest = LoginRequest().apply {
                deviceId = Build.SERIAL
                setUsername(username)
                setPassword(password)
                deviceType = 13
            }
            NetUtils.post<LoginResponseV2>(this, RequestParams(Apis.OffLineIp + Apis.ShangMiInitV2).apply {
                isAsJsonContent = true
                bodyContent = Gson().toJson(loginRequest)
            }) { loginResponse->
                //保存基本信息
                Constant.shopId = loginResponse.data.shopId
                Constant.cashierDeskId = loginResponse.data.cashierDeskId
                Constant.curUsername = loginRequest.username
                Constant.shopName = loginResponse.data.shopName
                
                //保存信息用于下次自动登陆
                SPUtils.put(this, "username", username)
                SPUtils.put(this, "password", password)
                mLoginState = 1
                startActivity(this)
            }
        }
    }

    private fun startActivity(activity: Activity) {
        if (mLoginState == 1 && mBaiDuFaceInitState != 1) {
            ToastUtils.toast("密码验证成功，等待人脸初始化完成")
        }
        if (mLoginState != 1 && mBaiDuFaceInitState == 1) {
            ToastUtils.toast("人脸初始化配置完成,等待登陆")
        }
        if (mLoginState == 1 && mBaiDuFaceInitState == 1) {
            dialog.cancel() //关闭弹窗
            val intent = Intent(activity, SubbranchActivity::class.java)
            activity.startActivity(intent)
            activity.finish()
        }
    }

    /**
     * 动态权限申请
     */
    @SuppressLint("CheckResult")
    private fun reqPermissions() {
        RxPermissions(this).requestEachCombined(CAMERA, READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE, READ_PHONE_STATE, ACCESS_COARSE_LOCATION, NFC,
                BIND_NFC_SERVICE)
                .subscribe { permission ->
                    when {
                        permission.granted -> { }
                        permission.shouldShowRequestPermissionRationale -> { }
                        else -> { }
                    }
                }
    }
}