package mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.ActionResolver;
import com.mygdx.game.Slider;
import help.utils.Constants;

public class DesktopLauncher implements ActionResolver {
    private static DesktopLauncher application;

    public static void main(String[] arg) {
        if (application == null) {
            application = new DesktopLauncher();
        }
        Constants.version="desktop";
        Constants.adHeight = 0;
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Super Carbon Puzzle 2014";
        config.useGL30 = false;
        config.width = 320;
        config.height = 480;
        new LwjglApplication(new Slider(application), config);
    }

    @Override
    public void showInterstitialAd() {
        System.out.println("Show Ad!");
    }

    @Override
    public void loadInterstitialAd() {
        System.out.println("Load Ad!");
    }
}
