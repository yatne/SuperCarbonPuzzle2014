package view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import enums.Controls;

public class PanelView {

    protected Stage stage;
    protected SpriteBatch batch;
    protected Image background;
    protected BitmapFont buttonFont;

    public PanelView(OrthographicCamera camera) {
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        this.batch = new SpriteBatch();

        background = new Image(new Texture("menus/background.png"));
        background.setPosition(0, 0);
        background.setSize(camera.viewportWidth, camera.viewportHeight);

        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        buttonFont = generator.generateFont((int) (camera.viewportWidth / 8));
        buttonFont.setColor(0, 0, 0, 1);
        generator.dispose();
                                                                 }

    public Controls drawMainMenu() {
        return Controls.NONE;
    }

    public int drawLevelSelection() {
        return 0;
    }
}
