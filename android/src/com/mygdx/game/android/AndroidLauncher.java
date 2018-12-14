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
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.mygdx.game.ActionResolver;
import com.mygdx.game.IActivityRequestHandler;
import com.mygdx.game.Slider;
import help.utils.Constants;

import static com.google.android.gms.ads.AdSize.SMART_BANNER;


public class AndroidLauncher extends AndroidApplication implements IActivityRequestHandler, ActionResolver {
    private final int SHOW_ADS = 1;
    private final int HIDE_ADS = 0;
    private final int SHOW_INTER = 2;
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
                case SHOW_INTER: {
                    showInterstitialAd();
                    break;
                }
            }
        }
    };
    private InterstitialAd interstitial;
    private AdView adView;

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


        // Create the interstitial.
        interstitial = new InterstitialAd(this);
        interstitial.setAdUnitId("ca-app-pub-5922776279277926/9272619292");

        // Create ad request.
        AdRequest adRequest = new AdRequest.Builder().build();
        interstitial.loadAd(adRequest);

        interstitial.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
               // Toast.makeText(getApplicationContext(), "Finished Loading Interstitial", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdClosed() {
               // Toast.makeText(getApplicationContext(), "Closed Interstitial", Toast.LENGTH_SHORT).show();
            }
        });


        //--------------------------------

        adView = new AdView(this); // Put in your secret key here
        adView.setAdSize(SMART_BANNER);
        adView.setAdUnitId("ca-app-pub-5922776279277926/4680432899");
        AdRequest banerAdRequest = new AdRequest.Builder()
                .addKeyword("game")
                .build();
        adView.loadAd(banerAdRequest);
        layout.addView(gameView);


        RelativeLayout.LayoutParams adParams =
                new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        layout.addView(adView, adParams);

        Constants.adHeight = layout.getHeight();
        setContentView(layout);

        try {
            Constants.version = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {

        }
        Constants.adHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics());

    }

    @Override
    public void showAds(boolean show) {
        handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
    }

    @Override
    public void showIntAd() {
        try {
            runOnUiThread(new Runnable() {
                public void run() {

                    if (interstitial.isLoaded()) {
                        interstitial.show();
                        AdRequest interstitialRequest = new AdRequest.Builder().build();

                    } else {
                        AdRequest interstitialRequest = new AdRequest.Builder().build();
                        interstitial.loadAd(interstitialRequest);

                    }
                }
            });
        } catch (Exception e) {
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void showInterstitialAd() {
        if (interstitial.isLoaded()) {
            interstitial.show();
        }
    }
}
