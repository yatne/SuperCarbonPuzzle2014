package com.mygdx.game.android;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mygdx.game.IActivityRequestHandler;
import com.mygdx.game.Slider;

import static com.google.android.gms.ads.AdSize.SMART_BANNER;


public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler {
    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    protected Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_ADS: {
                    adView.setVisibility(View.VISIBLE);
                    break;
                }
                case HIDE_ADS: {
                    adView.setVisibility(View.GONE);
                    break;
                }
            }
        }
    };
    private AdView adView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RelativeLayout layout = new RelativeLayout(this);

        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        StrictMode.enableDefaults();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        View gameView = initializeForView(new Slider(this), config);

        adView = new AdView(this); // Put in your secret key here
        adView.setAdSize(SMART_BANNER);
        adView.setAdUnitId("to_dostane_jak_zaplace");

        AdRequest adRequest = new AdRequest.Builder()
                .addKeyword("game")
                .build();

        adView.loadAd(adRequest);

        layout.addView(gameView);


        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        layout.addView(adView, adParams);


        setContentView(layout);
    }

    @Override
    public void showAds(boolean show) {
        handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
    }
}
