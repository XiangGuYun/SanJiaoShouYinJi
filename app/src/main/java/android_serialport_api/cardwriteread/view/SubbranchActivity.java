package android_serialport_api.cardwriteread.view;

import android.content.Context;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import android_serialport_api.cardwriteread.Constant;
import android_serialport_api.cardwriteread.R;
import android_serialport_api.cardwriteread.contract.SubbranchContract;
import android_serialport_api.cardwriteread.presenter.SubbranchPresenter;
import android_serialport_api.cardwriteread.util.SharedUtil;
import android_serialport_api.cardwriteread.view.adapter.SubbranchAdapter;


//FaceRGBRegisterActivity
public class SubbranchActivity extends AppCompatActivity implements SubbranchContract.View {
    private SubbranchPresenter subbranchPresenter;
    private RecyclerView subbranchGroup;
    WelcomeViceScreen welcomePresentation = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subbranch);
        initPresenter();
        initView();
        subbranchPresenter.query(this);
        initPresentation();
        AutoSelect();
    }

    private void initPresentation(){
        DisplayManager mDisplayManager = (DisplayManager) getSystemService(Context.DISPLAY_SERVICE);
        Display[]mDisplay = mDisplayManager.getDisplays();
        if (mDisplay.length < 2) {
            return;
        }
        if (welcomePresentation == null) {
            welcomePresentation = new WelcomeViceScreen(this, mDisplay[1]);
            if(!isFinishing())
                welcomePresentation.show();
        }
    }

    private void AutoSelect(){
        String loginbranchId = SharedUtil.getInstatnce().getShared(SubbranchActivity.this).getString("loginbranchId","");
        String loginbranchName = SharedUtil.getInstatnce().getShared(SubbranchActivity.this).getString("loginbranchName","");
        if (!loginbranchId.equals("")){
            Constant.branchId = Integer.valueOf(loginbranchId);
            Constant.shopName =  loginbranchName;
            Intent intent = new Intent(SubbranchActivity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    private void initPresenter(){
        subbranchPresenter = new SubbranchPresenter();
        subbranchPresenter.attachView(this);
    }

    private void initView() {
        subbranchGroup = (RecyclerView) findViewById(R.id.subbranch_group);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        welcomePresentation.dismiss();
        subbranchPresenter.detachView();
    }

    @Override
    public void toast(String s, int type) {
        Toast.makeText(SubbranchActivity.this,s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void ReshView(List<JSONObject> list) {
        if (list.size() == 0){
            return;
        }
        final SubbranchAdapter adapter = new SubbranchAdapter(list, new SubbranchAdapter.ItemClick() {
            @Override
            public void Click(JSONObject object) throws JSONException {
                Constant.branchId = Integer.valueOf(object.getString("branchId"));
                Constant.shopName =  object.getString("branchName");
                //记录上次选择的分店id
                SharedUtil.getInstatnce().getShared(SubbranchActivity.this).edit()
                        .putString("loginbranchId", String.valueOf(Constant.branchId))
                        .putString("loginbranchName", String.valueOf(Constant.shopName)).commit();
                Intent intent = new Intent(SubbranchActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        },this);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                subbranchGroup.setLayoutManager(linearLayoutManager);
                subbranchGroup.setAdapter(adapter);
            }
        });
    }
}
