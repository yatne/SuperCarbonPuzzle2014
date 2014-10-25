package view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import enums.Controls;
import help.utils.Constants;
import map.Map;
import player.Player;
import textures.TextureHolder;
import view.Text;
import view.buttons.Button;

public class PreLevelView extends PanelView {

    private Controls control;
    private BitmapFont font;
    private BitmapFont bigFont;
    private BitmapFont smallFont;
    private Text text;
    private String textString;
    private Button backButton;
    private Button playButton;
    private int world;

    public PreLevelView(OrthographicCamera camera, BitmapFont buttonFont) {
        super(camera, buttonFont);

        control = Controls.NONE;


        float width = camera.viewportWidth / 3;
        float height = (((camera.viewportHeight - camera.viewportWidth) / 2) * 3 / 5);
        float posY = height / 2;


        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        font = generator.generateFont((int) ((camera.viewportHeight - camera.viewportWidth) / 4));
        font.setColor(Color.BLACK);
        this.buttonFont = generator.generateFont((int) (height * 3 / 4));
        this.buttonFont.setColor(Color.BLACK);

        float textPanelHeight = camera.viewportWidth + ((camera.viewportHeight - camera.viewportWidth) / 2);
        float maxBigFontSize = textPanelHeight / 9;
        bigFont = generator.generateFont((int) maxBigFontSize);
        while (bigFont.getBounds("Level 25 Completed").width > camera.viewportWidth) {
            maxBigFontSize--;
            bigFont = generator.generateFont((int) maxBigFontSize);
        }
        smallFont = generator.generateFont((int) (textPanelHeight) / 12);
        generator.dispose();

        text = new Text(0, (int) (camera.viewportHeight - font.getCapHeight()), "");

        backButton = new Button(TextureHolder.buttonsTexture, "back", 0, posY, width, height, this.buttonFont);
        playButton = new Button(TextureHolder.buttonsTexture, "play", 2 * width, posY, width, height, this.buttonFont);


        backButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                control = Controls.MENU;
                backButton.setDrawable(backButton.getTextureRegionDrawable());
            }
        });
        playButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                control = Controls.PLAY;
                playButton.setDrawable(playButton.getTextureRegionDrawable());

            }
        });

    }

    public void preparePreLevel(Stage stage, Map map, Player player, Image background) {

        control = Controls.NONE;
        world = map.getMapWorld();

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


        if (map.getMapWorld() == 1 || map.getMapWorld() == 2 || map.getMapWorld() == 5) {
            font.setColor(Color.BLACK);
            bigFont.setColor(Color.BLACK);
            smallFont.setColor(Color.BLACK);

        } else if (map.getMapWorld() == 3) {
            font.setColor(Constants.thirdWorldTextColor);
            bigFont.setColor(Constants.thirdWorldTextColor);
            smallFont.setColor(Constants.thirdWorldTextColor);
        } else if (map.getMapWorld() == 4) {
            font.setColor(Constants.fourthWorldTextColor);
            bigFont.setColor(Constants.fourthWorldTextColor);
            smallFont.setColor(Constants.fourthWorldTextColor);
        }

        text.setText(textString);

        stage.clear();
        stage.addActor(backButton);
        stage.addActor(playButton);
        this.background = background;

        playButton.setButtonWorld(map.getMapWorld());
        backButton.setButtonWorld(map.getMapWorld());


    }

    public Controls drawPreLevel(OrthographicCamera camera, SpriteBatch batch) {
        batch.begin();
        background.draw(batch, 1);
        font.drawWrapped(batch, text.getText(), text.getPosX(), text.getPosY(), camera.viewportWidth, BitmapFont.HAlignment.CENTER);
        if (world == 5) {
            buttonFont.setColor(Constants.fifthWorldButtonColor);
        }
        backButton.draw(batch, 1, buttonFont);
        playButton.draw(batch, 1, buttonFont);
        buttonFont.setColor(Color.BLACK);
        batch.end();
        return control;
    }
}
