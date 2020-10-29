package android_serialport_api.cardwriteread.view;

import android.app.Presentation;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import android_serialport_api.cardwriteread.R;


/**
 * 欢迎页副屏幕
 */
public class WelcomeViceScreen extends Presentation {
    public WelcomeViceScreen(Context outerContext, Display display) {
        super(outerContext, display);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_presentation);
    }


}