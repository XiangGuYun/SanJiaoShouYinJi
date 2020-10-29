package android_serialport_api.cardwriteread.jiedan.api;

import android.content.Context;
import android.util.Log;

import retrofit2.Call;
import retrofit2.Response;


/**
 */

public abstract class MyCallback<T> implements retrofit2.Callback<T> {
    private static final String TAG = "MyCallback";
    public abstract void onSuccess(T t);

    public MyCallback() {

    }

    public MyCallback(Context mContext, String progressTitle) {
    }

    public MyCallback(Context mContext) {
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response == null || response.body() == null) {
            onFailure(call, new Throwable());
            return;
        }
        onSuccess(response.body());
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.d(TAG, "onFailure: ");
    }
}
