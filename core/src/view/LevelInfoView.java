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
import map.Map;
import player.Player;

import java.util.ArrayList;

public class LevelInfoView {

    Stage stage;
    SpriteBatch batch;
    Image background;
    BitmapFont font;
    Text text;
    Image goldenStar;
    Image grayStar;
    ArrayList<Integer> goals;
    Image button;
    Text ButtonText;
    Controls control;

    public LevelInfoView() {
    }

    public void preparePreLevel(OrthographicCamera camera, Map map) {

        control = Controls.NONE;

        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        font = generator.generateFont((int) ((camera.viewportHeight - camera.viewportWidth) / 4));
        font.setColor(Color.BLACK);
        generator.dispose();

        String textString = new String();
        if (map.getGoals().size() == 0) {
            textString = "you only need to beat this level.";
        } else if (map.getGoals().size() == 1) {
            textString = "goals for this level: \n" +
                    "   1 star: anything\n" +
                    "   2 stars: " + map.getGoals().get(0) + " moves";
        } else if (map.getGoals().size() == 2) {
            textString = "goals for this level: \n" +
                    "   1 star: anything\n" +
                    "   2 stars: " + map.getGoals().get(0) + " moves\n" +
                    "   3 stars: " + map.getGoals().get(1) + " moves";
        } else if (map.getGoals().size() == 3) {
            textString = "goals for this level: \n" +
                    "   1 star: anything\n" +
                    "   2 stars: " + map.getGoals().get(0) + "moves \n" +
                    "   3 stars: " + map.getGoals().get(1) + "moves \n" +
                    "   4 stars: " + map.getGoals().get(2) + "moves";
        }


        text = new Text(0, (int) (camera.viewportHeight - font.getCapHeight()), textString);


        background = new Image(new Texture("menus/background.png"));
        background.setPosition(0, 0);
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        stage.addActor(background);
        background.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                control = Controls.NEXT;
            }
        });
    }

    public void preparePostLevel(OrthographicCamera camera, Map map, Player player) {

        control = Controls.NONE;

        batch = new SpriteBatch();
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        font = generator.generateFont((int) ((camera.viewportHeight - camera.viewportWidth) / 4));
        font.setColor(Color.BLACK);
        generator.dispose();

        int starsObtained = player.getStarsFromLevel(map.getMapWorld(), map.getMapNumber());               // to trzeba zmienic

        String textString = "Congratulations!\n " +
                "\n" +
                "You've made " + map.getMovesTaken() + " moves\n and obtained " + starsObtained + "stars.\n";

        if (starsObtained == map.getGoals().size() + 1) {
            textString = textString + "Thats all you can get for this level";
        } else {
            textString = textString + "You will get next star if you make " + map.getGoals().get(starsObtained-1) + " or less moves";
        }


        text = new Text(0, (int) (camera.viewportHeight - font.getCapHeight()), textString);


        background = new Image(new Texture("menus/background.png"));
        background.setPosition(0, 0);
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        stage.addActor(background);
        background.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                control = Controls.NEXT;
            }
        });
    }

    public Controls drawPreLevel(OrthographicCamera camera) {

        batch.begin();
        background.draw(batch, 1);

        font.drawWrapped(batch, text.getText(), text.getPosX(), text.getPosY(), camera.viewportWidth, BitmapFont.HAlignment.CENTER);
        batch.end();

        return control;
    }

}
