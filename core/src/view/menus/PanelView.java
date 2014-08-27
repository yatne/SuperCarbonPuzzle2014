package view.menus;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import enums.Controls;

public class PanelView {

    protected Image background;
    protected BitmapFont buttonFont;

    public PanelView(OrthographicCamera camera, BitmapFont buttonFont) {
        this.buttonFont = buttonFont;
        preparePanel(camera);
    }

    public void preparePanel(OrthographicCamera camera) {



        background = new Image(new Texture("menus/background.png"));
        background.setPosition(0, 0);
        background.setSize(camera.viewportWidth, camera.viewportHeight);


    }


    public Controls drawAfterLevel(OrthographicCamera camera) {
        return Controls.NONE;
    }

    public Controls drawPreLevel(OrthographicCamera camera) {
        return Controls.NONE;
    }

}