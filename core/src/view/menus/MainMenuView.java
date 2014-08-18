package view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import enums.Controls;
import view.buttons.BasicButton;

public class MainMenuView extends PanelView {

    private BasicButton playButton;
    private BasicButton quitButton;
    private Image logo;
    private Controls control;

    public MainMenuView(OrthographicCamera camera) {
        super(camera);

        control = Controls.NONE;

        logo = new Image(new Texture("menus/logo.png"));
        logo.setSize(camera.viewportWidth, camera.viewportWidth / 5);
        logo.setPosition(0, camera.viewportHeight - (camera.viewportWidth / 5));

        playButton = new BasicButton(new Texture("menus/button.png"), "Play", camera.viewportWidth / 4, camera.viewportHeight - ((camera.viewportWidth / 5) * 3), buttonFont, camera);
        quitButton = new BasicButton(new Texture("menus/button.png"), "Quit", camera.viewportWidth / 4, camera.viewportHeight - ((camera.viewportWidth / 5) * 4), buttonFont, camera);

        stage.addActor(playButton);
        stage.addActor(quitButton);

        playButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                control = Controls.PLAY;

            }
        });
        quitButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
        });

    }

    @Override
    public Controls drawMainMenu() {
        batch.begin();

        background.draw(batch, 1);
        logo.draw(batch, 1);
        playButton.draw(batch, 1, buttonFont);
        quitButton.draw(batch, 1, buttonFont);

        batch.end();

        return control;

    }
}
