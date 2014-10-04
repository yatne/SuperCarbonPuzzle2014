package view.menus;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class PanelView {

    protected Image background;
    protected BitmapFont buttonFont;

    public PanelView(OrthographicCamera camera, BitmapFont buttonFont) {
        this.buttonFont = buttonFont;
        preparePanel(camera);
    }

    public void preparePanel(OrthographicCamera camera) {


    }


}