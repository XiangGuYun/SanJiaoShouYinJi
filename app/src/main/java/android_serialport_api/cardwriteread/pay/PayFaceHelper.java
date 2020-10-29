package android_serialport_api.cardwriteread.pay;

import android.app.Activity;

import android_serialport_api.cardwriteread.baiduface.baiducallBack.FaceDetectCallBack;
import android_serialport_api.cardwriteread.baiduface.baiducamera.AutoTexturePreviewView;
import android_serialport_api.cardwriteread.baiduface.baiducamera.CameraPreviewManager;
import android_serialport_api.cardwriteread.baiduface.baidumanage.FaceSDKManager;
import android_serialport_api.cardwriteread.baiduface.baidumodel.LivenessModel;
import android_serialport_api.cardwriteread.baiduface.baidumodel.SingleBaseConfig;

public class PayFaceHelper {

    private static PayFaceHelper helper;
    private OnGetLivenessModelListener getLivenessModelListener;
    private ConditionFacePay conditionFacePay;

    private PayFaceHelper(){

    }

    public static PayFaceHelper getInstance(){
        if(helper == null){
            helper = new PayFaceHelper();
        }
        return helper;
    }

    public void initCamera(Activity activity, AutoTexturePreviewView previewView,
                           OnGetLivenessModelListener listener, ConditionFacePay listener1) {
        this.getLivenessModelListener = listener;
        this.conditionFacePay = listener1;
        CameraPreviewManager.getInstance().setCameraFacing(CameraPreviewManager.CAMERA_USB);
        CameraPreviewManager.getInstance().setDisplayOrientation(180);
        CameraPreviewManager.getInstance().startPreview(activity, previewView,
                SingleBaseConfig.getBaseConfig().getRgbAndNirWidth(), SingleBaseConfig.getBaseConfig().getRgbAndNirHeight(),
                (data, camera, width, height) -> {
                    // 摄像头预览数据进行人脸检测
                    // 调节资源冲突
                    // 只有当为支付状态并且开启人脸识别支付后才能开始识别支付
                    // isFacePay为是否开启人脸识别，由用户手动按钮开启/关
                    if (conditionFacePay.condition()) {
                        FaceSDKManager.getInstance().onDetectCheck(data, null, null,
                                height, width, 2, new FaceDetectCallBack() {
                                    @Override
                                    public void onFaceDetectCallback(LivenessModel livenessModel) {
                                        getLivenessModelListener.onGet(livenessModel);
                                    }

                                    @Override
                                    public void onTip(int code, String msg) {

                                    }

                                    @Override
                                    public void onFaceDetectDarwCallback(LivenessModel livenessModel) {

                                    }
                                });
                    }

                    //判断卡帧
                    //一旦人脸识别进入了支付状态则 isPresentationPaus 为会改为true，意味这卡帧，也就是停止对副屏幕的更新
                    //直到人脸识别付款完毕 isPresentationPaus 状态重新回到false
                    //只有人脸识别支付方法中才能改变 isPresentationPaus 的状态为true，另外俩种支付不会改变
//                        if (isPresentationPaus == false){
//                            Bitmap tempBmp = new NV21ToBitmap(mView.GetActivity()).nv21ToBitmap(data,width, height);
//                            saveBitmap = CameraUtil.getInstance().setTakePicktrueOrientation(mCameraId, tempBmp);
//                            if (saveBitmap != null) {
//                                //传到副屏进行显示
//                                mPresentation.uploadImage(saveBitmap);
//                            }
//                        }
                });
    }



    public interface OnGetLivenessModelListener{
        void onGet(LivenessModel livenessModel);
    }

    public interface ConditionFacePay{
        boolean condition();
    }


}
