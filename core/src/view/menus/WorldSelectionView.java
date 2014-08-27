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
import player.Player;
import view.Alert;
import view.buttons.BasicButton;
import view.buttons.WorldButton;

import java.util.ArrayList;

public class WorldSelectionView extends PanelView {

    private BitmapFont worldFont;
    private ArrayList<WorldButton> worldButtons;
    private BasicButton backButton;
    private int selectedWorld;
    private Alert alert;

    public WorldSelectionView(final OrthographicCamera camera, Player player, BitmapFont buttonFont) {
        super(camera, buttonFont);

        this.worldButtons = new ArrayList<>();
        alert = new Alert();

        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        worldFont = generator.generateFont((int) (camera.viewportWidth / 9));
        worldFont.setColor(Color.BLACK);
        generator.dispose();

        backButton = new BasicButton(new Texture("menus/button.png"), "Back", (camera.viewportWidth) / 10, ((camera.viewportHeight - camera.viewportWidth) / 2) / 5, buttonFont, camera);

        for (int i = 1; i <= 2; i++) {
            final WorldButton worldButton;
            int starsToUnlock = help.utils.MapsReader.starsToUnlock(i, 1);
            boolean locked = false;
            if (starsToUnlock <= player.getStars())
                worldButton = new WorldButton(new Texture("menus/button.png"), i, help.utils.MapsReader.getWorldName(i), camera, worldFont);
            else {
                worldButton = new WorldButton(new Texture("menus/button.png"), i, "LOCKED: need " + starsToUnlock + " stars", camera, worldFont);
                locked = true;
            }

            final int finalI = i;
            if (!locked) {
                worldButton.addListener(new ClickListener() {
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchUp(event, x, y, pointer, button);
                        selectedWorld = finalI;
                    }
                });
            } else {
                worldButton.addListener(new ClickListener() {
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        super.touchUp(event, x, y, pointer, button);
                        alert = new Alert(camera, 1, finalI, true);                    // @TODO:     pobawic sie z alertem!

                    }
                });
            }

            worldButtons.add(worldButton);
        }

        backButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                selectedWorld = -1;
            }
        });


    }

    public void prepareWorldSelectionView(Stage stage) {

        selectedWorld = 0;
        stage.clear();
        stage.addActor(backButton);
        for (WorldButton worldButton : worldButtons) {
            stage.addActor(worldButton);
        }

    }

    public int drawWorldSelection(SpriteBatch batch, OrthographicCamera camera, ShaderProgram shader) {


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
            worldButton.draw(batch, 1, worldFont);
        }
        backButton.draw(batch, 1, worldFont);
        batch.end();

        if (alert.isActive()) {
            if (alert.drawAlert(camera) == Controls.MENU) {
                alert.setActive(false);
            }
        }
        return selectedWorld;
    }


}
