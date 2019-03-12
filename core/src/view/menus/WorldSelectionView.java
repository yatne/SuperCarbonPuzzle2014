package view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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
import view.buttons.WorldButton;

import java.util.ArrayList;

public class WorldSelectionView extends PanelView {

    private BitmapFont worldFont;
    private ArrayList<WorldButton> worldButtons;
    private BasicButton backButton;
    private int selectedWorld;
    private Alert alert;
    private Text starsCount;
    private Image starImage;
    private TextureRegionDrawable lockedWorld;
    private TextureRegionDrawable lockedClicked;

    public WorldSelectionView(final OrthographicCamera camera, BitmapFont buttonFont, final MapsInfo mapsInfo, Player player) {
        super(camera, buttonFont);

        this.worldButtons = new ArrayList<>();

        alert = new Alert(camera);
        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.size = (int) (camera.viewportWidth / 9);
        worldFont = generator.generateFont(freeTypeFontParameter);
        worldFont.setColor(Color.BLACK);
        generator.dispose();

        backButton = new BasicButton(TextureHolder.buttonsTexture, "Back", (camera.viewportWidth) / 10, ((camera.viewportHeight - camera.viewportWidth) / 2) / 5, buttonFont, camera);

        Texture lockedButtons = new Texture("menus/locked.png");
        lockedWorld = new TextureRegionDrawable(new TextureRegion(lockedButtons, 0, 0, lockedButtons.getWidth(), lockedButtons.getHeight() / 2));
        lockedClicked = new TextureRegionDrawable(new TextureRegion(lockedButtons, 0, lockedButtons.getHeight() / 2, lockedButtons.getWidth(), lockedButtons.getHeight() / 2));

        float levelSelectionWidth = camera.viewportWidth;
        float levelSelectionHeight = camera.viewportWidth + ((camera.viewportHeight - camera.viewportWidth) / 2);

        for (int i = 1; i <= Constants.howManyWorlds; i++) {
            final WorldButton worldButton;
            String s = player.getStarsFromWorld(i) + " / " + mapsInfo.getStarsToObtainInWorld(i);
            worldButton = new WorldButton(TextureHolder.buttonsTexture, i, s, levelSelectionWidth, levelSelectionHeight, worldFont);
            worldButton.setButtonWorld(i);
            final int finalI = i;

            worldButton.addListener(new ClickListener() {
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if (worldButton.isLocked() && !Constants.cheatMode) {
                        alert.setActive(true);
                        alert.prepareAlert("You need to beat 10 levels in the previous world.");
                    } else
                        selectedWorld = finalI;
                    worldButton.setDrawable(worldButton.getTextureRegionDrawable());
                }
            });

            worldButtons.add(worldButton);
        }

        backButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                backButton.setDrawable(backButton.getTextureRegionDrawable());
                selectedWorld = -1;
            }
        });
        backButton.setButtonWorld(1);
        GlyphLayout glyphLayout = new GlyphLayout(buttonFont, Integer.toString(player.getStars()));
        int posX = (int) ((camera.viewportWidth * 4 / 5) - (glyphLayout.width / 2));
        int posY = (int) ((((camera.viewportHeight - camera.viewportWidth) / 2) / 5) + buttonFont.getCapHeight() * 3 / 2);
        starsCount = new Text(posX, posY, player.getStars());
        starImage = new Image(new Texture("menus/score_star.png"));

        starImage.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                if (Constants.cheatActivation < 5) {
                    if (Constants.soundOn) {
                        if (Constants.cheatActivation < 4) {
                            ClickSound.clickSound.play(0.1f);
                        } else {
                            SlideSound.slideSound.play(0.1f);

                        }
                    }
                    Constants.cheatActivation++;
                } else {
                    Constants.cheatActivation = 0;
                }
            }
        });

        glyphLayout.setText(buttonFont, "999");
        posX = (int) ((camera.viewportWidth * 4 / 5) - (glyphLayout.width));
        posY = (int) ((((camera.viewportHeight - camera.viewportWidth) / 2) / 5) - glyphLayout.width / 4);

        starImage.setPosition(posX, posY);
        starImage.setSize(2 * glyphLayout.width, 2 * glyphLayout.width);
    }

    public void prepareWorldSelectionView(Stage stage, Player player, MapsInfo mapsInfo, Image background, float cameraViewPortWidth) {

        selectedWorld = 0;
        stage.clear();
        stage.addActor(backButton);
        stage.addActor(starImage);
        for (WorldButton worldButton : worldButtons) {
            worldButton.setLocked(false);
            if (worldButton.getWorldNumber() > 1 && player.getLevelsDoneInWorld(worldButton.getWorldNumber() - 1) < 10 && !Constants.cheatMode) {

                lockedWorld = new TextureRegionDrawable(new TextureRegion(TextureHolder.lockedButton, 0, 0, TextureHolder.lockedButton.getWidth(), TextureHolder.lockedButton.getHeight() / 2));
                lockedClicked = new TextureRegionDrawable(new TextureRegion(TextureHolder.lockedButton, 0, TextureHolder.lockedButton.getHeight() / 2, TextureHolder.lockedButton.getWidth(), TextureHolder.lockedButton.getHeight() / 2));
                worldButton.setLocked(true);
                worldButton.setTextureRegionDrawable(lockedWorld);
                worldButton.setDrawable(lockedWorld);
                worldButton.setTextureRegionDrawablePressed(lockedClicked);
            } else {
                worldButton.setButtonWorld(worldButton.getWorldNumber());
            }
            stage.addActor(worldButton);
        }

        for (int i = 1; i <= Constants.howManyWorlds; i++) {
            String s = player.getStarsFromWorld(i) + " / " + mapsInfo.getStarsToObtainInWorld(i);
            worldButtons.get(i - 1).setText(s);
        }
        GlyphLayout glyphLayout = new GlyphLayout(buttonFont, Integer.toString(player.getStars()));

        starsCount.setPosX((int) ((cameraViewPortWidth * 4 / 5) - glyphLayout.width / 2));
        starsCount.setText(Integer.toString(player.getStars()));
        for (WorldButton wb : worldButtons) {
            String s = player.getStarsFromWorld(wb.getWorldNumber()) + " / " + mapsInfo.getStarsToObtainInWorld(wb.getWorldNumber());
            wb.adjustStar(buttonFont, s, cameraViewPortWidth);
        }

        this.background = background;
    }

    public int drawWorldSelection(SpriteBatch batch, OrthographicCamera camera, ShaderProgram shader, Stage stage) {


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
        for (WorldButton worldButton : worldButtons) {
            if (worldButton.isLocked()) {
                worldButton.drawWithAltText(batch, 1, worldFont, "LOCKED!", camera.viewportWidth);
            } else {
                if (worldButton.getWorldNumber() == 5) {
                    worldFont.setColor(Constants.fifthWorldButtonColor);
                    worldButton.draw(batch, 1, worldFont);
                    worldFont.setColor(Color.BLACK);
                } else {
                    worldButton.draw(batch, 1, worldFont);
                }
            }
        }
        backButton.draw(batch, 1, buttonFont);
        starImage.draw(batch, 1);
        buttonFont.draw(batch, starsCount.getText(), starsCount.getPosX(), starsCount.getPosY());
        batch.end();

        if (alert.isActive()) {
            if (alert.drawAlert(camera) == Controls.MENU) {
                alert.setActive(false);
                Gdx.input.setInputProcessor(stage);
            }

        }

        return selectedWorld;
    }

}
