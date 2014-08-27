package view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import enums.Controls;
import help.utils.MapsReader;
import org.w3c.dom.NodeList;
import player.Player;
import view.Alert;
import view.Text;
import view.buttons.BasicButton;
import view.buttons.LevelButton;

import java.util.ArrayList;
import java.util.HashMap;

public class LevelSelectionView extends PanelView {

    private BitmapFont levelFont;
    private ArrayList<LevelButton> levelButtons;
    private ArrayList<LevelButton> lockedLevelButtons;
    private HashMap<Integer, Texture> worldButtonTexture;
    private BasicButton backButton;
    private int selectedWorld;
    private int selectedLevel;
    private Alert alert;
    private Text starsCount;
    private TextureRegion region;
    private TextureRegionDrawable regionDrawable;

    public LevelSelectionView(final int selectedWorld, final OrthographicCamera camera, Player player, ShaderProgram shader, BitmapFont buttonFont) {
        super(camera, buttonFont);

        selectedLevel = 0;

        alert = new Alert();
        Texture level = new Texture("menus/levelbuttons.png");

        region = new TextureRegion(level, 139, 0, 139, 190);
        regionDrawable = new TextureRegionDrawable(region);

        Texture levelLocked = new Texture("menus/level_locked.png");

        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        levelFont = generator.generateFont((int) (camera.viewportWidth / 6.9));
        levelFont.setColor(Color.BLACK);
        generator.dispose();

        NodeList maps = MapsReader.getMapsList("/resources/maps" + selectedWorld + ".xml");
        levelButtons = new ArrayList<>();

        Texture goldenStar = new Texture("menus/star_golden.png");
        Texture grayStar = new Texture("menus/star_gray.png");

        for (int i = 0; i < maps.getLength(); i++) {

            LevelButton levelButton;

            boolean locked = false;
            if (help.utils.MapsReader.starsToUnlock(selectedWorld, i + 1) <= player.getStars()) {
                levelButton = new LevelButton(region, selectedWorld, i, player, camera, levelFont, Color.BLACK, goldenStar, grayStar);

            } else {
                levelButton = new LevelButton(levelLocked, selectedWorld, i, player, camera, levelFont, Color.WHITE, goldenStar, grayStar);
                locked = true;
            }

            levelButtons.add(levelButton);

            final int finalI = i + 1;

            if (!locked) {
                levelButton.addListener(new ClickListener() {
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        selectedLevel = finalI;
                    }
                });
            } else {
                levelButton.addListener(new ClickListener() {
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        //          alert = new Alert(camera, finalI, selectedWorld, stage, false);
                        return true;
                    }
                });
            }

        }

        backButton = new BasicButton(new Texture("menus/button.png"), "Back", (camera.viewportWidth) / 10, ((camera.viewportHeight - camera.viewportWidth) / 2) / 5, buttonFont, camera);
        backButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                selectedLevel = -1;
            }
        });

        starsCount = new Text((int) (camera.viewportWidth * 4 / 5), (int) ((((camera.viewportHeight - camera.viewportWidth) / 2) / 5) + buttonFont.getCapHeight() * 3 / 2), player.getStars());

    }

    public void prepareLevelSelection(int selectedWorld, Stage stage) {

        this.selectedWorld = selectedWorld;
        this.selectedLevel = 0;


    }

    public int drawLevelSelection(OrthographicCamera camera, ShaderProgram shader, SpriteBatch batch) {

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
        for (LevelButton levelButton : levelButtons) {
            levelButton.draw(batch, 1, levelFont);
        }
        backButton.draw(batch, 1, buttonFont);
        buttonFont.draw(batch, starsCount.getStringNumber(), starsCount.getPosX(), starsCount.getPosY());
        batch.end();

        if (alert.isActive()) {

            if (alert.drawAlert(camera) == Controls.MENU) {
                alert.setActive(false);

            }
        }


        return selectedLevel;
    }
}
