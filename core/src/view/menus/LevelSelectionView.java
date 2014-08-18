package view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import help.utils.MapsReader;
import org.w3c.dom.NodeList;
import player.Player;
import view.buttons.BasicButton;
import view.buttons.LevelButton;

import java.util.ArrayList;

public class LevelSelectionView extends PanelView {

    private BitmapFont levelFont;
    private ArrayList<LevelButton> levelButtons;
    private BasicButton backButton;
    private int selectedLevel;

    public LevelSelectionView(int selectedWorld, OrthographicCamera camera, Player player) {
        super(camera);

        selectedLevel = 0;

        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        levelFont = generator.generateFont((int) (camera.viewportWidth / 6.9));
        levelFont.setColor(Color.BLACK);
        generator.dispose();

        NodeList maps = MapsReader.getMapsList("/resources/maps" + selectedWorld + ".xml");
        levelButtons = new ArrayList<>();
        for (int i = 0; i < maps.getLength(); i++) {

            Texture texture;
            LevelButton levelButton;
            if (help.utils.MapsReader.starsToUnlock(selectedWorld, i + 1) <= player.getStars()) {
                texture = new Texture("menus/level.png");
                levelButton = new LevelButton(texture, selectedWorld, i, player, camera, levelFont, Color.BLACK);
            } else {
                texture = new Texture("menus/level_locked.png");
                levelButton = new LevelButton(texture, selectedWorld, i, player, camera, levelFont, Color.WHITE);
            }


            stage.addActor(levelButton);
            levelButtons.add(levelButton);

            final int finalI = i + 1;
            levelButton.addListener(new ClickListener() {
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    selectedLevel = finalI;
                }
            });
        }

        backButton = new BasicButton(new Texture("menus/button.png"), "Back", (camera.viewportWidth) / 10, ((camera.viewportHeight - camera.viewportWidth) / 2) / 5, buttonFont, camera);
        stage.addActor(backButton);
        backButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                selectedLevel = -1;
            }
        });

    }

    @Override
    public int drawLevelSelection() {

        batch.begin();
        background.draw(batch, 1);
        for (LevelButton levelButton : levelButtons) {
            levelButton.draw(batch, 1, levelFont);
        }
        backButton.draw(batch, 1, buttonFont);

        batch.end();
        return selectedLevel;
    }
}
