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
import help.utils.Constants;
import mapSystem.MapsInfo;
import player.Player;
import sound.ClickSound;
import sound.SlideSound;
import textures.TextureHolder;
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
    private Image starImage;
    private TextureRegion region;
    private TextureRegionDrawable regionDrawable;
    private TextureRegion touchedRegion;
    private TextureRegionDrawable touchRegionDrawable;
    private TextureRegionDrawable lockedLevel;
    private TextureRegionDrawable lockedTouched;
    private int world;


    public LevelSelectionView(final OrthographicCamera camera, Player player, BitmapFont buttonFont, final MapsInfo mapsInfo) {
        super(camera, buttonFont);

        selectedLevel = 0;

        alert = new Alert(camera);

        TextureRegion lockedLevelTexture = new TextureRegion(TextureHolder.lockedLevelTexture, 0, 0, TextureHolder.lockedLevelTexture.getWidth(), TextureHolder.lockedLevelTexture.getHeight() / 2);
        TextureRegion lockedTouchedLevelTexture = new TextureRegion(TextureHolder.lockedLevelTexture, 0, TextureHolder.lockedLevelTexture.getHeight() / 2, TextureHolder.lockedLevelTexture.getWidth(), TextureHolder.lockedLevelTexture.getHeight() / 2);
        lockedLevel = new TextureRegionDrawable(lockedLevelTexture);
        lockedTouched = new TextureRegionDrawable(lockedTouchedLevelTexture);
        region = new TextureRegion(TextureHolder.levelButtonsTexture, 0, 0, 139, 190);
        regionDrawable = new TextureRegionDrawable(region);

        touchedRegion = new TextureRegion(TextureHolder.levelButtonsTexture, 139, 190, 139, 190);
        touchRegionDrawable = new TextureRegionDrawable(region);

        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        levelFont = generator.generateFont((int) (camera.viewportWidth / 6.9));
        levelFont.setColor(Color.BLACK);
        generator.dispose();


        levelButtons = new ArrayList<>();


        float levelSelectionWidth = camera.viewportWidth;
        float levelSelectionHeight = camera.viewportWidth + ((camera.viewportHeight - camera.viewportWidth) / 2);

        for (int i = 0; i < 25; i++) {

            final LevelButton levelButton = new LevelButton(region, i, levelSelectionWidth, levelSelectionHeight, levelFont, Color.BLACK, TextureHolder.goldenStar, TextureHolder.grayStar, i + 1);

            levelButton.setLocked(false);
            final int finalI = i + 1;

            levelButton.addListener(new ClickListener() {
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if (finalI <= levelsInWorld) {
                        if (!levelButton.isLocked() || Constants.cheatMode) {
                            selectedLevel = finalI;
                        } else {
                            alert.setActive(true);
                            alert.prepareAlert("you need " + mapsInfo.getStarsToUnlock(world, finalI) + " stars to unlock this level");
                            levelButton.setDrawable(lockedLevel);
                        }
                    }
                }
            });

            levelButton.addListener(new ClickListener() {
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchDown(event, x, y, pointer, button);
                    if (!levelButton.isLocked() || Constants.cheatMode) {
                        levelButton.setDrawable(touchRegionDrawable);
                        if (Constants.soundOn)
                            ClickSound.clickSound.play();
                    } else {
                        levelButton.setDrawable(lockedTouched);
                        if (Constants.soundOn)
                            ClickSound.clickSound.play();
                    }
                    return true;
                }
            });

            levelButtons.add(levelButton);
        }


        backButton = new BasicButton(TextureHolder.buttonsTexture, "Back", (camera.viewportWidth) / 10, ((camera.viewportHeight - camera.viewportWidth) / 2) / 5, buttonFont, camera);
        backButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                selectedLevel = -1;
                backButton.setDrawable(backButton.getTextureRegionDrawable());
            }
        });

        int posX = (int) ((camera.viewportWidth * 4 / 5) - (buttonFont.getBounds(Integer.toString(player.getStars())).width / 2));
        int posY = (int) ((((camera.viewportHeight - camera.viewportWidth) / 2) / 5) + buttonFont.getCapHeight() * 3 / 2);

        starsCount = new Text(posX, posY, player.getStars());
        starImage = new Image(new Texture("menus/score_star.png"));

        starImage.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (Constants.soundOn) {
                    if (Constants.cheatActivation < 9) {

                        ClickSound.clickSound.play(0.1f);
                    } else {
                        SlideSound.slideSound.play(0.1f);

                    }
                }
                if (Constants.cheatActivation >= 5) {

                    Constants.cheatActivation++;
                    if (Constants.cheatActivation == 10) {
                        Constants.cheatActivation = 0;
                        Constants.cheatMode = true;
                        for (LevelButton levelButton : levelButtons) {
                            levelButton.setDrawable(regionDrawable);
                            levelButton.setLocked(false);
                        }
                    }
                } else {
                    Constants.cheatActivation = 0;
                }
            }
        });

        posX = (int) ((camera.viewportWidth * 4 / 5) - (buttonFont.getBounds("999").width));
        posY = (int) ((((camera.viewportHeight - camera.viewportWidth) / 2) / 5) - buttonFont.getBounds("999").width / 4);

        starImage.setPosition(posX, posY);
        starImage.setSize(2 * buttonFont.getBounds("999").width, 2 * buttonFont.getBounds("999").width);
    }

    public void prepareLevelSelection(int selectedWorld, Stage stage, Player player, MapsInfo mapsInfo, Image background, float cameraViewPortWidth) {

        this.selectedLevel = 0;

        region.setRegion((selectedWorld - 1) * (TextureHolder.levelButtonsTexture.getWidth() / Constants.howManyWorlds), 0, (TextureHolder.levelButtonsTexture.getWidth() / Constants.howManyWorlds), TextureHolder.levelButtonsTexture.getHeight() / 2);
        regionDrawable.setRegion(region);

        world = selectedWorld;

        touchedRegion.setRegion((selectedWorld - 1) * (TextureHolder.levelButtonsTexture.getWidth() / Constants.howManyWorlds), TextureHolder.levelButtonsTexture.getHeight() / 2, (TextureHolder.levelButtonsTexture.getWidth() / Constants.howManyWorlds), TextureHolder.levelButtonsTexture.getHeight() / 2);
        touchRegionDrawable.setRegion(touchedRegion);

        stage.clear();
        stage.addActor(backButton);
        stage.addActor(starImage);
        for (LevelButton levelButton : levelButtons) {

            levelButton.setDrawable(regionDrawable);
            levelButton.setLocked(false);

            levelButton.setStars(mapsInfo.getStarsToObtain(selectedWorld, levelButton.getLevel()), player.getStarsFromLevel(selectedWorld, levelButton.getLevel()));
            if (mapsInfo.getStarsToUnlock(selectedWorld, levelButton.getLevel()) > player.getStars() && !Constants.cheatMode) {
                levelButton.setDrawable(lockedLevel);
                levelButton.setLocked(true);
            }
            stage.addActor(levelButton);
        }

        if (world == 5) {
            buttonFont.setColor(Constants.fifthWorldButtonColor);
            levelFont.setColor(Constants.fifthWorldButtonColor);
        } else {
            buttonFont.setColor(Color.BLACK);
            levelFont.setColor(Color.BLACK);
        }

        starsCount.setPosX((int) ((cameraViewPortWidth * 4 / 5) - (buttonFont.getBounds(Integer.toString(player.getStars())).width / 2)));
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
            if (levelButtons.get(i).isLocked() && !Constants.cheatMode) {
                levelButtons.get(i).draw(batch, 1, levelFont);
            } else {
                if (world == 5) {
                    levelFont.setColor(Constants.fifthWorldButtonColor);
                }
                levelButtons.get(i).draw(batch, 1, levelFont);
            }
        }
        levelFont.setColor(Color.BLACK);

        if (world == 5) {
            buttonFont.setColor(Constants.fifthWorldButtonColor);
        }
        backButton.draw(batch, 1, buttonFont);
        starImage.draw(batch, 1);
        buttonFont.setColor(Color.BLACK);
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
