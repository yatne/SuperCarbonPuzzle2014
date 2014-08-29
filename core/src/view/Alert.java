package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import enums.Controls;
import view.buttons.Button;

public class Alert {

    private SpriteBatch altBatch;
    private Stage alertStage;
    private BitmapFont font;
    private Image image;
    private Controls control;
    private Text text;
    private Button button;
    private boolean active;

    public Alert() {
        active = false;
    }

    public Alert(OrthographicCamera camera) {

        alertStage = new Stage();

        control = Controls.NONE;
        altBatch = new SpriteBatch();
        active = false;

        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        font = generator.generateFont((int) (camera.viewportHeight / 11));
        font.setColor(Color.BLACK);
        generator.dispose();

        image = new Image(new Texture("menus/alert.png"));
        image.setSize(camera.viewportWidth * 4 / 5, 3 * camera.viewportHeight / 5);
        image.setPosition(camera.viewportWidth / 10, camera.viewportHeight / 5);

        text = new Text((int) (2 * camera.viewportWidth / 15), (int) ((4 * camera.viewportHeight / 5) - (2 * font.getCapHeight() / 3))
                , "");

        button = new Button(new Texture("menus/button.png"), "OK",
                camera.viewportWidth / 2 - (camera.viewportWidth / 8), camera.viewportHeight / 5 + (font.getCapHeight() / 2),
                camera.viewportWidth / 4, (float) (font.getCapHeight() * 2), font);

        button.addListener(

                new ClickListener() {
                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchUp(event,x,y,pointer,button);
                        control = Controls.MENU;
                    }
                });
    }

    public void prepareAlert(String alertString) {

        control=Controls.NONE;
        text.setText(alertString);
        Gdx.input.setInputProcessor(alertStage);
        alertStage.clear();
        alertStage.addActor(button);

    }

    public Controls drawAlert(OrthographicCamera camera) {

        altBatch.begin();
        image.draw(altBatch, 1);
        button.draw(altBatch, 1, font);
        font.drawWrapped(altBatch, text.getText(), text.getPosX(), text.getPosY(), (2 * camera.viewportWidth / 3), BitmapFont.HAlignment.CENTER);
        altBatch.end();

        return control;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
