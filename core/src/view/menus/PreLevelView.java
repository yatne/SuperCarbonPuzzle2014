package view.menus;

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
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import enums.Controls;
import map.Map;
import player.Player;
import view.Text;
import view.buttons.Button;

public class PreLevelView extends PanelView {

    private Controls control;
    private BitmapFont font;
    private Text text;
    private String textString;
    private Button backButton;
    private Button playButton;

    public PreLevelView(OrthographicCamera camera, BitmapFont buttonFont) {
        super(camera, buttonFont);

        control = Controls.NONE;

        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        font = generator.generateFont((int) ((camera.viewportHeight - camera.viewportWidth) / 4));
        font.setColor(Color.BLACK);
        generator.dispose();

        text = new Text(0, (int) (camera.viewportHeight - font.getCapHeight()), "");

        Texture buttonText = new Texture("menus/button.png");

        float width = camera.viewportWidth / 3;
        float height = (((camera.viewportHeight - camera.viewportWidth) / 2) / 2);
        float posY = height / 2;

        backButton = new Button(buttonText, "back", 0, posY, width, height, buttonFont);
        playButton = new Button(buttonText, "play", 2 * width, posY, width, height, buttonFont);


        backButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event,x,y,pointer,button);
                control = Controls.MENU;
            }
        });
        playButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event,x,y,pointer,button);
                control = Controls.PLAY;
            }
        });

    }

    public void preparePreLevel(Stage stage, Map map, Player player) {

        control=Controls.NONE;

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

        text.setText(textString);

        stage.clear();
        stage.addActor(backButton);
        stage.addActor(playButton);

    }

    public Controls drawPreLevel(OrthographicCamera camera, SpriteBatch batch) {
        batch.begin();
        background.draw(batch, 1);
        font.drawWrapped(batch, text.getText(), text.getPosX(), text.getPosY(), camera.viewportWidth, BitmapFont.HAlignment.CENTER);
        backButton.draw(batch, 1, buttonFont);
        playButton.draw(batch, 1, buttonFont);
        batch.end();
        return control;
    }
}
