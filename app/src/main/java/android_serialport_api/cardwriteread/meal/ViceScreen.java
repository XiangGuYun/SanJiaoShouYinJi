package android_serialport_api.cardwriteread.meal;

import android.app.Activity;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.view.Display;

import android_serialport_api.cardwriteread.view.MainViceScreen;

public class ViceScreen {

    public static MainViceScreen get(Activity ctx){
        DisplayManager mDisplayManager = (DisplayManager) ctx.getSystemService(Context.DISPLAY_SERVICE);
        Display[] mDisplay = mDisplayManager.getDisplays();
        MainViceScreen mPresentation = new MainViceScreen(ctx, mDisplay[1]);
        return mPresentation;
    }

}
