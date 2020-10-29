package android_serialport_api.cardwriteread.presenter;

import android.app.Activity;
import android.text.TextUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import android_serialport_api.cardwriteread.Constant;
import android_serialport_api.cardwriteread.base.BasePresenter;
import android_serialport_api.cardwriteread.contract.SubbranchContract;
import android_serialport_api.cardwriteread.model.DataJsonCallBack;
import android_serialport_api.cardwriteread.model.SubbranchModel;


public class SubbranchPresenter extends BasePresenter<SubbranchContract.View> implements SubbranchContract.Presenter {
    private SubbranchModel subbranchModel;

    public SubbranchPresenter() {
        subbranchModel = new SubbranchModel();
    }

    @Override
    public void query(Activity activity) {
        if (TextUtils.isEmpty(String.valueOf(Constant.shopId))) {
            mView.toast("系统错误", 0);
            return;
        }
        subbranchModel.query(activity, String.valueOf(Constant.shopId), new DataJsonCallBack() {
            @Override
            public void callback(int code, JSONObject jsonObject) throws JSONException {
                System.out.println("js:"+jsonObject);
                JSONArray array = jsonObject.getJSONArray("data");
                List<JSONObject> list = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    list.add(object);
                }
                if(mView != null) {
                    mView.ReshView(list);
                }
            }
        });
    }

}
