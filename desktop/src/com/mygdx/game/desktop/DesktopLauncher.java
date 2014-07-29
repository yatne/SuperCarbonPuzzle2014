package com.mygdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.game.Slider;

public class DesktopLauncher {
	public static void main (String[] arg) {

		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
            config.title = "Super Carbon Puzzle 2014";
            config.useGL30 = false;
            config.width = 320;
            config.height = 480;
		new LwjglApplication(new Slider(), config);
	}
}
