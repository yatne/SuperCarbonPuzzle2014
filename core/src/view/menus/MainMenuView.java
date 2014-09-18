package view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import enums.Controls;
import view.buttons.BasicButton;

public class MainMenuView extends PanelView {

    private BasicButton playButton;
    private BasicButton quitButton;
    private Image logo;
    private Controls control;

    public MainMenuView(OrthographicCamera camera, BitmapFont buttonFont) {
        super(camera, buttonFont);

        this.logo = new Image(new Texture("menus/logo.png"));
        logo.setSize(camera.viewportWidth *4/5, camera.viewportWidth *2/5);
        logo.setPosition(camera.viewportWidth *1/10, camera.viewportHeight - (camera.viewportWidth *3/5));

        playButton = new BasicButton(new Texture("menus/button.png"), "Play", camera.viewportWidth / 4, camera.viewportHeight - ((camera.viewportWidth / 5) * 5), buttonFont, camera);
        quitButton = new BasicButton(new Texture("menus/button.png"), "Quit", camera.viewportWidth / 4, camera.viewportHeight - ((camera.viewportWidth / 5) * 6), buttonFont, camera);


        playButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                control = Controls.PLAY;
                playButton.setDrawable(playButton.getTextureRegionDrawable());

            }
        });

        quitButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Gdx.app.exit();

            }
        });


    }

    public void prepareMainMenu(Stage stage) {

        stage.clear();
        control = Controls.NONE;
        stage.addActor(playButton);
        stage.addActor(quitButton);


    }

    public Controls drawMainMenu(SpriteBatch batch) {

        batch.begin();
        background.draw(batch, 1);
        logo.draw(batch, 1);
        playButton.draw(batch, 1, buttonFont);
        quitButton.draw(batch, 1, buttonFont);

        batch.end();
        return this.control;

    }

    public Controls getControl() {
        return control;
    }
}
