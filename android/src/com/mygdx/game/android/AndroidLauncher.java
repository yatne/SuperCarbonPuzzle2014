package com.mygdx.game.android;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.ActionResolver;
import com.mygdx.game.IActivityRequestHandler;
import com.mygdx.game.Slider;
import help.utils.Constants;


public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler, ActionResolver {
    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    private final int SHOW_INTER = 2;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_ADS: {
                    break;
                }
                case HIDE_ADS: {
                    break;
                }
                case SHOW_INTER: {
                    break;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        AppRater.app_launched(this);
        RelativeLayout layout = new RelativeLayout(this);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        StrictMode.enableDefaults();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        View gameView = initializeForView(new Slider(this), config);

        //--------------------------------
        layout.addView(gameView);

        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        setContentView(layout);

        try {
            Constants.version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        Constants.adHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());
    }

    @Override
    public void showInterstitialAd() {

    }

    @Override
    public void showAds(boolean show) {

    }

    @Override
    public void showIntAd() {

    }
}
