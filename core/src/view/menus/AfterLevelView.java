package view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import enums.Controls;
import map.Map;
import player.Player;
import view.Text;
import view.buttons.Button;

public class AfterLevelView extends PanelView {

    Controls control;
    BitmapFont font;
    Text text;
    Button levelSelectButton;
    Button retryButton;
    Button nextLevelButton;
    boolean drawNextLevelButton;

    public AfterLevelView(OrthographicCamera camera, ShaderProgram shader, Map map, Player player, BitmapFont buttonFont) {
        super(camera, buttonFont);

        drawNextLevelButton = true;
        control = Controls.NONE;

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
            textString = textString + "You will get next star if you make " + map.getGoals().get(starsObtained - 1) + " or less moves";
        }

        if (map.getMapNumber() == 16) {
            drawNextLevelButton = false;
        }
        if (map.getMapNumber() < 16 && (player.getStars() < help.utils.MapsReader.starsToUnlock(map.getMapWorld(), map.getMapNumber() + 1))) {
            drawNextLevelButton = false;
        }

        text = new Text(0, (int) (camera.viewportHeight - font.getCapHeight()), textString);

        Texture buttonText = new Texture("menus/button.png");

        float width = camera.viewportWidth / 3;
        float height = (((camera.viewportHeight - camera.viewportWidth) / 2) / 2);
        float posY = height / 2;

        retryButton = new Button(buttonText, "retry", 0, posY, width, height, buttonFont);
        levelSelectButton = new Button(buttonText, "menu", width, posY, width, height, buttonFont);
        nextLevelButton = new Button(buttonText, "next", 2 * width, posY, width, height, buttonFont);
        // stage.addActor(retryButton);
        // stage.addActor(levelSelectButton);
        // stage.addActor(nextLevelButton);

        retryButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                control = Controls.RESET;
            }
        });

        levelSelectButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                control = Controls.MENU;
            }
        });
        if (drawNextLevelButton) {
            nextLevelButton.addListener(new ClickListener() {
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    control = Controls.NEXT;
                }
            });
        }
    }

  /*  @Override
    public Controls drawAfterLevel(OrthographicCamera camera) {
        batch.begin();
        background.draw(batch, 1);
        font.drawWrapped(batch, text.getText(), text.getPosX(), text.getPosY(), camera.viewportWidth, BitmapFont.HAlignment.CENTER);
        retryButton.draw(batch, 1, buttonFont);
        levelSelectButton.draw(batch, 1, buttonFont);
        if (drawNextLevelButton)
            nextLevelButton.draw(batch, 1, buttonFont);
        batch.end();
        return control;

    } */


}
