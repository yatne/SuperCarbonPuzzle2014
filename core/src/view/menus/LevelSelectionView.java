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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import enums.Controls;
import mapSystem.MapsInfo;
import player.Player;
import view.Alert;
import view.Text;
import view.buttons.BasicButton;
import view.buttons.LevelButton;

import java.util.ArrayList;

public class LevelSelectionView extends PanelView {

    private BitmapFont levelFont;
    private ArrayList<LevelButton> levelButtons;
    private BasicButton backButton;
    private int selectedLevel;
    private int levelsInWorld;
    private Alert alert;
    private Text starsCount;
    private TextureRegion region;
    private TextureRegionDrawable regionDrawable;
    private TextureRegion touchedRegion;
    private TextureRegionDrawable touchRegionDrawable;
    private TextureRegionDrawable lockedLevel;
    private int world;

    public LevelSelectionView(final OrthographicCamera camera, Player player, BitmapFont buttonFont, final MapsInfo mapsInfo) {
        super(camera, buttonFont);

        selectedLevel = 0;

        alert = new Alert(camera);

        TextureRegion lockedLevelTexture = new TextureRegion(new Texture("menus/level_locked.png"), 0, 0, 139, 190);
        lockedLevel = new TextureRegionDrawable(lockedLevelTexture);
        Texture level = new Texture("menus/levelbuttons.png");
        region = new TextureRegion(level, 139, 0, 139, 190);
        regionDrawable = new TextureRegionDrawable(region);

        touchedRegion = new TextureRegion(level, 139, 190, 139, 190);
        touchRegionDrawable = new TextureRegionDrawable(region);

        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        levelFont = generator.generateFont((int) (camera.viewportWidth / 6.9));
        levelFont.setColor(Color.BLACK);
        generator.dispose();


        levelButtons = new ArrayList<>();

        Texture goldenStar = new Texture("menus/star_golden.png");
        Texture grayStar = new Texture("menus/star_gray.png");

        for (int i = 0; i < 16; i++) {

            final LevelButton levelButton = new LevelButton(region, i, camera, levelFont, Color.BLACK, goldenStar, grayStar, i + 1);

            levelButton.setLocked(false);
            final int finalI = i + 1;

            levelButton.addListener(new ClickListener() {
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if (finalI <= levelsInWorld) {
                        if (!levelButton.isLocked()) {
                            selectedLevel = finalI;
                        } else {
                            alert.setActive(true);
                            alert.prepareAlert("you need " + mapsInfo.getStarsToUnlock(world, finalI) + " stars to unlock this level");
                        }
                    }
                }
            });

            levelButton.addListener(new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchDown(event, x, y, pointer, button);
                    if (!levelButton.isLocked()) {
                        levelButton.setDrawable(touchRegionDrawable);
                    }
                    return true;
                }
            });

            levelButtons.add(levelButton);
        }

        backButton = new BasicButton(new Texture("menus/buttons.png"), "Back", (camera.viewportWidth) / 10, ((camera.viewportHeight - camera.viewportWidth) / 2) / 5, buttonFont, camera);
        backButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                selectedLevel = -1;
                backButton.setDrawable(backButton.getTextureRegionDrawable());
            }
        });

        starsCount = new Text((int) (camera.viewportWidth * 4 / 5), (int) ((((camera.viewportHeight - camera.viewportWidth) / 2) / 5) + buttonFont.getCapHeight() * 3 / 2), player.getStars());

    }

    public void prepareLevelSelection(int selectedWorld, Stage stage, Player player, MapsInfo mapsInfo, Image background) {

        this.selectedLevel = 0;

        region.setRegion((selectedWorld - 1) * 139, 0, 139, 190);
        regionDrawable.setRegion(region);

        world = selectedWorld;

        touchedRegion.setRegion((selectedWorld - 1) * 139, 190, 139, 190);
        touchRegionDrawable.setRegion(touchedRegion);

        stage.clear();
        stage.addActor(backButton);
        for (LevelButton levelButton : levelButtons) {

            levelButton.setDrawable(regionDrawable);
            levelButton.setLocked(false);

            levelButton.setStars(mapsInfo.getStarsToObtain(selectedWorld, levelButton.getLevel()), player.getStarsFromLevel(selectedWorld, levelButton.getLevel()));
            if (mapsInfo.getStarsToUnlock(selectedWorld, levelButton.getLevel()) > player.getStars()) {
                levelButton.setLocked(true);
            }
            stage.addActor(levelButton);
        }
        starsCount.setText(Integer.toString(player.getStars()));
        levelsInWorld = mapsInfo.getMapsCountInWorld(selectedWorld);
        backButton.setButtonWorld(selectedWorld);
        this.background = background;

    }

    public int drawLevelSelection(OrthographicCamera camera, ShaderProgram shader, SpriteBatch batch, Stage stage) {

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

        for (int i = 0; i < levelsInWorld; i++) {
            if (levelButtons.get(i).isLocked()) {
                levelButtons.get(i).drawLocked(batch, 1, levelFont, lockedLevel);
            } else {
                levelButtons.get(i).draw(batch, 1, levelFont);
            }
        }

        backButton.draw(batch, 1, buttonFont);
        buttonFont.draw(batch, starsCount.getText(), starsCount.getPosX(), starsCount.getPosY());
        batch.end();

        if (alert.isActive()) {

            if (alert.drawAlert(camera) == Controls.MENU) {
                alert.setActive(false);
                Gdx.input.setInputProcessor(stage);
            }
        }
        return selectedLevel;
    }
}
