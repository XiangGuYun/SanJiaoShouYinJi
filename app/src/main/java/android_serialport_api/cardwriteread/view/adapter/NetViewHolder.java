package android_serialport_api.cardwriteread.view.adapter;

import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zhpan.bannerview.holder.ViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import android_serialport_api.cardwriteread.R;

public class NetViewHolder implements ViewHolder<String> {

    @Override
    public int getLayoutId() {
        return R.layout.item_net;
    }

    @Override
    public void onBind(View itemView, String str, int position, int size) {
        ImageView imageView = itemView.findViewById(R.id.banner_image);
        try {
            Glide.with(imageView.getContext()).asBitmap().load(str).into(imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}