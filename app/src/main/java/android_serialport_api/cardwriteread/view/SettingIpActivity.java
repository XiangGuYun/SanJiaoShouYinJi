package android_serialport_api.cardwriteread.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android_serialport_api.cardwriteread.R;
import android_serialport_api.cardwriteread.net.Apis;
import android_serialport_api.cardwriteread.util.SharedUtil;

public class SettingIpActivity extends AppCompatActivity {

    private EditText settingipInput;
    private Button settingipBcsz;
    private Button settingipHysz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_ip);
        initView();
    }

    private void initView() {
        settingipInput = (EditText) findViewById(R.id.settingip_input);
        settingipBcsz = (Button) findViewById(R.id.settingip_bcsz);
        settingipHysz = (Button) findViewById(R.id.settingip_hysz);
        settingipInput.setText(SharedUtil.getInstatnce().getShared(this).getString("ip", ""));
        settingipBcsz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(settingipInput.getText())) {
                    Toast.makeText(SettingIpActivity.this, "ip地址不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                SharedUtil.getInstatnce().getShared(SettingIpActivity.this).edit().putString("ip", settingipInput.getText().toString()).commit();
                Apis.OffLineIp = SharedUtil.getInstatnce().getShared(SettingIpActivity.this).getString("ip","");
                Toast.makeText(SettingIpActivity.this, "设置成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        settingipHysz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedUtil.getInstatnce().getShared(SettingIpActivity.this).edit().putString("ip","https://binguoai.com").commit();
                Apis.OffLineIp = SharedUtil.getInstatnce().getShared(SettingIpActivity.this).getString("ip","");
                Toast.makeText(SettingIpActivity.this, "还原设置成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
