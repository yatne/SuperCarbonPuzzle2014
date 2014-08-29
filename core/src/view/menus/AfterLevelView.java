package view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import enums.Controls;
import map.Map;
import mapSystem.MapsInfo;
import player.Player;
import view.Alert;
import view.Text;
import view.buttons.Button;

public class AfterLevelView extends PanelView {

    Controls control;
    BitmapFont font;
    String textString;
    Text text;
    Button levelSelectButton;
    Button retryButton;
    Button nextLevelButton;
    boolean drawNextLevelButton;
    Alert alert;
    int mapNumber;
    int mapWorld;

    public AfterLevelView(OrthographicCamera camera, BitmapFont buttonFont, final MapsInfo mapsInfo, final Player player) {
        super(camera, buttonFont);

        drawNextLevelButton = true;
        control = Controls.NONE;

        alert = new Alert(camera);

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

        retryButton = new Button(buttonText, "retry", 0, posY, width, height, buttonFont);
        levelSelectButton = new Button(buttonText, "menu", width, posY, width, height, buttonFont);
        nextLevelButton = new Button(buttonText, "next", 2 * width, posY, width, height, buttonFont);


        retryButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                control = Controls.RESET;
            }
        });

        levelSelectButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                control = Controls.MENU;
            }
        });
        if (drawNextLevelButton) {
            nextLevelButton.addListener(new ClickListener() {
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if (player.getStars() < mapsInfo.getStarsToUnlock(mapWorld, mapNumber + 1)) {
                        alert.setActive(true);
                        alert.prepareAlert("you need " + mapsInfo.getStarsToUnlock(mapWorld, mapNumber + 1) + " stars to unlock next level");
                    } else {
                        control = Controls.NEXT;
                    }
                }
            });
        }
    }

    public void prepareAfterLevelView(Stage stage, Map map, Player player, MapsInfo mapsInfo) {

        control = Controls.NONE;
        drawNextLevelButton = true;
        int starsObtained = player.getStarsFromLevel(map.getMapWorld(), map.getMapNumber());               // TODO: zrobić dobrze!

        mapNumber = map.getMapNumber();
        mapWorld = map.getMapWorld();
        textString = "Congratulations!\n " +
                "\n" +
                "You've made " + map.getMovesTaken() + " moves\n and obtained " + starsObtained + "stars.\n";

        if (starsObtained == map.getGoals().size() + 1) {
            textString = textString + "Thats all you can get for this level";
        } else {
            textString = textString + "You will get next star if you make " + map.getGoals().get(starsObtained - 1) + " or less moves";
        }

        text.setText(textString);

        if (mapNumber >= mapsInfo.getMapsCountInWorld(mapWorld)) {
            drawNextLevelButton = false;
        }


        stage.addActor(retryButton);
        stage.addActor(levelSelectButton);
        if (drawNextLevelButton)
            stage.addActor(nextLevelButton);
    }

    public Controls drawAfterLevel(OrthographicCamera camera, ShaderProgram shader, SpriteBatch batch, Stage stage) {

        if (alert.isActive()) {
            shader.begin();
            shader.setUniformf("grayscale", 0f);
            shader.end();
        } else {
            shader.begin();
            shader.setUniformf("grayscale", 1f);
            shader.end();
        }

        batch.begin();
        background.draw(batch, 1);
        font.drawWrapped(batch, text.getText(), text.getPosX(), text.getPosY(), camera.viewportWidth, BitmapFont.HAlignment.CENTER);
        retryButton.draw(batch, 1, buttonFont);
        levelSelectButton.draw(batch, 1, buttonFont);
        if (drawNextLevelButton)
            nextLevelButton.draw(batch, 1, buttonFont);
        batch.end();

        if (alert.isActive()) {
            if (alert.drawAlert(camera) == Controls.MENU) {
                alert.setActive(false);
                Gdx.input.setInputProcessor(stage);
            }
        }
        return control;
    }


}
