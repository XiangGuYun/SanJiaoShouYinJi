package android_serialport_api.cardwriteread.kt

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import com.tbruyelle.rxpermissions2.RxPermissions

object LoginUtils{
    @SuppressLint("CheckResult")
    fun reqPermissions(activity: Activity, onGranted:()->Unit) {
        RxPermissions(activity).requestEachCombined(Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.NFC,
                Manifest.permission.BIND_NFC_SERVICE)
                .subscribe { permission ->
                    when {
                        permission.granted -> {
                            onGranted.invoke()
                        }
                        permission.shouldShowRequestPermissionRationale -> {
                        }
                        else -> {
                        }
                    }
                }
    }
}