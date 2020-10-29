package android_serialport_api.cardwriteread.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import android_serialport_api.cardwriteread.R;
import android_serialport_api.cardwriteread.baiduface.baiduactivity.FaceRGBCloseDebugSearchActivity;
import android_serialport_api.cardwriteread.baiduface.baiduactivity.FaceRegisterActivity;
import android_serialport_api.cardwriteread.baiduface.baiduactivity.FaceUserGroupListActivity;
import android_serialport_api.cardwriteread.baiduface.baidumanage.FaceSDKManager;

/**
 * 百度ai人脸管理
 * 非管理员勿用
 */
public class FaceManageActivity extends AppCompatActivity {

    private Button b1;
    private Button b2;
    private Button b3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_manage);
        initView();
    }

    private void initView() {
        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FaceSDKManager.getInstance().initStatus == FaceSDKManager.SDK_UNACTIVATION) {
                    Toast.makeText(FaceManageActivity.this, "SDK还未激活初始化，请先激活初始化", Toast.LENGTH_LONG).show();
                    return;
                } else if (FaceSDKManager.getInstance().initStatus == FaceSDKManager.SDK_INIT_FAIL) {
                    Toast.makeText(FaceManageActivity.this, "SDK初始化失败，请重新激活初始化", Toast.LENGTH_LONG).show();
                    return;
                } else if (FaceSDKManager.getInstance().initStatus == FaceSDKManager.SDK_INIT_SUCCESS) {
                    Toast.makeText(FaceManageActivity.this, "SDK正在加载模型，请稍后再试", Toast.LENGTH_LONG).show();
                    return;
                } else if (FaceSDKManager.getInstance().initStatus == FaceSDKManager.SDK_MODEL_LOAD_SUCCESS) {
                    startActivity(new Intent(FaceManageActivity.this, FaceRGBCloseDebugSearchActivity.class));
                }
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FaceManageActivity.this, FaceRegisterActivity.class));
            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FaceManageActivity.this, FaceUserGroupListActivity.class));
            }
        });
    }
}
