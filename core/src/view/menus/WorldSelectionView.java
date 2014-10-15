package view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
    private Sprite starImage;
    private TextureRegionDrawable lockedWorld;
    private TextureRegionDrawable lockedClicked;

    public WorldSelectionView(final OrthographicCamera camera, BitmapFont buttonFont, final MapsInfo mapsInfo, Player player) {
        super(camera, buttonFont);

        this.worldButtons = new ArrayList<>();

        alert = new Alert(camera);
        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        worldFont = generator.generateFont((int) (camera.viewportWidth / 9));
        worldFont.setColor(Color.BLACK);
        generator.dispose();

        backButton = new BasicButton(new Texture("menus/buttons.png"), "Back", (camera.viewportWidth) / 10, ((camera.viewportHeight - camera.viewportWidth) / 2) / 5, buttonFont, camera);

        Texture lockedButtons = new Texture("menus/locked.png");
        lockedWorld = new TextureRegionDrawable(new TextureRegion(lockedButtons, 0, 0, lockedButtons.getWidth(), lockedButtons.getHeight() / 2));
        lockedClicked = new TextureRegionDrawable(new TextureRegion(lockedButtons, 0, lockedButtons.getHeight() / 2, lockedButtons.getWidth(), lockedButtons.getHeight() / 2));

        float levelSelectionWidth = camera.viewportWidth;
        float levelSelectionHeight = camera.viewportWidth + ((camera.viewportHeight - camera.viewportWidth) / 2);

        for (int i = 1; i <= Constants.howManyWorlds; i++) {
            final WorldButton worldButton;
            String s = player.getStarsFromWorld(i) + " / " + mapsInfo.getStarsToObtainInWorld(i);
            worldButton = new WorldButton(new Texture("menus/buttons.png"), i, s, levelSelectionWidth, levelSelectionHeight, worldFont);
            worldButton.setButtonWorld(i);
            final int finalI = i;

            worldButton.addListener(new ClickListener() {
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    if (worldButton.isLocked()) {
                        alert.setActive(true);
                        alert.prepareAlert("You need " + mapsInfo.getStarsToUnlockWorld(finalI) + " stars to unlock this world");
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

        int posX = (int) ((camera.viewportWidth * 4 / 5) - (buttonFont.getBounds(Integer.toString(player.getStars())).width / 2));
        int posY = (int) ((((camera.viewportHeight - camera.viewportWidth) / 2) / 5) + buttonFont.getCapHeight() * 3 / 2);

        starsCount = new Text(posX, posY, player.getStars());
        starImage = new Sprite(new Texture("menus/star_golden.png"));

        posX = (int) ((camera.viewportWidth * 4 / 5) - (buttonFont.getBounds("999").width));
        posY = (int) ((((camera.viewportHeight - camera.viewportWidth) / 2) / 5) - buttonFont.getBounds("999").width / 4);

        starImage.setPosition(posX, posY);
        starImage.setSize(2 * buttonFont.getBounds("999").width, 2 * buttonFont.getBounds("999").width);
    }

    public void prepareWorldSelectionView(Stage stage, Player player, MapsInfo mapsInfo, Image background, float cameraViewPortWidth) {

        selectedWorld = 0;
        stage.clear();
        stage.addActor(backButton);
        for (WorldButton worldButton : worldButtons) {
            worldButton.setLocked(false);
            if (mapsInfo.getStarsToUnlockWorld(worldButton.getWorldNumber()) > player.getStars()) {
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

        starsCount.setPosX((int) ((cameraViewPortWidth * 4 / 5) - (buttonFont.getBounds(Integer.toString(player.getStars())).width / 2)));
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
                worldButton.draw(batch, 1, worldFont);
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
