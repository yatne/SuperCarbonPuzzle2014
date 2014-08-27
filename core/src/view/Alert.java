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
    private BitmapFont font;
    private Image image;
    private Controls control;
    private Text text;
    private Button button;
    private boolean active;

    public Alert() {
        active = false;
    }

    public Alert(OrthographicCamera camera, int mapNumber, int mapWorld, boolean worldAlert) {

        control = Controls.NONE;
        altBatch = new SpriteBatch();
        active = true;

        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        font = generator.generateFont((int) (camera.viewportHeight / 11));
        font.setColor(Color.BLACK);
        generator.dispose();


        String ending;
        if (worldAlert) {
            ending = " stars to unlock this world.";
        } else {
            ending = "stars to unlock this level.";
        }

        image = new Image(new Texture("menus/alert.png"));
        image.setSize(camera.viewportWidth * 4 / 5, 3 * camera.viewportHeight / 5);
        image.setPosition(camera.viewportWidth / 10, camera.viewportHeight / 5);

        String textString = "you need " + help.utils.MapsReader.starsToUnlock(mapWorld, mapNumber) + ending;

        text = new Text((int) (2 * camera.viewportWidth / 15)
                , (int) ((4 * camera.viewportHeight / 5) - (2 * font.getCapHeight() / 3))
                , textString);

        button = new Button(new Texture("menus/button.png"), "OK",
                camera.viewportWidth / 2 - (camera.viewportWidth / 8), camera.viewportHeight / 5 + (font.getCapHeight() / 2),
                camera.viewportWidth / 4, (float) (font.getCapHeight() * 2), font);
        Stage stage = new Stage();             //     to tutaj nie bedzie działalo!!
        stage.addActor(button);

        button.addListener(

                new ClickListener() {

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        control = Controls.MENU;
                    }
                });
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
