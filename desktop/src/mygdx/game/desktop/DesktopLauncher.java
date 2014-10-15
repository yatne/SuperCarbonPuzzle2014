package mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.IActivityRequestHandler;
import com.mygdx.game.Slider;
import help.utils.Constants;

public class DesktopLauncher implements IActivityRequestHandler {
    private static DesktopLauncher application;

    public static void main(String[] arg) {
        if (application == null) {
            application = new DesktopLauncher();
        }
        Constants.adHeight = 0;
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Super Carbon Puzzle 2014";
        config.useGL30 = false;
        config.width = 320;
        config.height = 480;
        new LwjglApplication(new Slider(application), config);
    }

    @Override
    public void showAds(boolean show) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
