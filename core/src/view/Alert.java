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

public class Alert {

    private BitmapFont font;
    private Image image;

    private Image button;
    private Text text;
    private SpriteBatch batch;
    private Text buttonText;
    private Controls control;

    public Alert(OrthographicCamera camera, int mapNumber, int mapWorld) {

        control = Controls.NONE;

        Stage stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        batch = new SpriteBatch();
        createFonts(camera);
        image = new Image(new Texture("menus/alert.png"));
        image.setSize(camera.viewportWidth * 4 / 5, 3 * camera.viewportHeight / 5);
        image.setPosition(camera.viewportWidth / 10, camera.viewportHeight / 5);

        String textString = "you need "+ help.utils.MapsReader.starsToUnlock(mapWorld,mapNumber)+" stars to unlock this level.";

        text = new Text((int) (2 * camera.viewportWidth / 15)
                , (int) ((4 * camera.viewportHeight / 5) - (2 * font.getCapHeight() / 3))
                , textString);

        button = new Image(new Texture("menus/button.png"));
        button.setSize(camera.viewportWidth / 4, (float) (font.getCapHeight() * 2));
        button.setPosition(camera.viewportWidth / 2 - (camera.viewportWidth / 8), camera.viewportHeight / 5 + (font.getCapHeight() / 2));

        stage.addActor(button);

        button.addListener(

                new ClickListener() {

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        control = Controls.MENU;

                    }
                });


        int okPosX = (int) ( camera.viewportWidth / 2 - font.getBounds("OK").width / 2);
        int okPosY = (int) (camera.viewportHeight / 5 + 2*(font.getCapHeight()))  ;

        buttonText = new Text(okPosX,okPosY,"OK");

        Image fog = new Image(new Texture("menus/transparent.png"));
        fog.setPosition(0,0);
        fog.setSize(camera.viewportWidth, camera.viewportHeight);
        batch.begin();
        fog.draw(batch,1);
        fog.draw(batch,1);
        fog.draw(batch,1);
        fog.draw(batch,1);
        batch.end();
    }

    public Controls drawAlert(OrthographicCamera camera) {

        batch.begin();
        image.draw(batch, 1);
        button.draw(batch, 1);
        font.drawWrapped(batch, text.getText(), text.getPosX(), text.getPosY(), (2 * camera.viewportWidth / 3), BitmapFont.HAlignment.CENTER);
        font.draw(batch, "OK", buttonText.getPosX(), buttonText.getPosY());
        batch.end();

        return control;
    }

    public void createFonts(OrthographicCamera camera) {
        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        font = generator.generateFont((int) (camera.viewportHeight / 11));
        font.setColor(Color.BLACK);
        generator.dispose();
    }

}
