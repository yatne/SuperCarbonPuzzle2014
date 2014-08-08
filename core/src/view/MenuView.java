package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import enums.Controls;
import help.utils.Constants;
import help.utils.MapsReader;
import org.w3c.dom.NodeList;
import player.Player;

import java.util.ArrayList;

public class MenuView {

    private Stage stage;
    private SpriteBatch batch;
    private Controls control;
    private int selectedLevel;
    private ArrayList<Image> levelButtons;
    private ArrayList<Image> stars;
    private Image background;
    private Image bottomPannel;
    private Image logo;
    private Image button1;
    private Image button2;

    public MenuView() {
        this.stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        this.batch = new SpriteBatch();
        levelButtons = new ArrayList<>();
        stars = new ArrayList<>();
        selectedLevel = 0;
    }

    public void prepareMainMenu(OrthographicCamera camera) {

        selectedLevel = Constants.ValueLevelSelection;
        control = Controls.NONE;


        Gdx.input.setInputProcessor(stage);
        stage.clear();
        batch = new SpriteBatch();
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        background = new Image(new Texture("menus/background.png"));
        background.setSize(camera.viewportWidth, camera.viewportHeight);
        background.setPosition(0, 0);

        bottomPannel = new Image(new Texture("menus/bottom_pannel.png"));
        bottomPannel.setSize(camera.viewportWidth, (camera.viewportHeight - camera.viewportWidth) / 2);
        bottomPannel.setPosition(0, 0);

        logo = new Image(new Texture("menus/logo.png"));
        logo.setSize(camera.viewportWidth, camera.viewportWidth / 5);
        logo.setPosition(0, camera.viewportHeight - (camera.viewportWidth / 5));

        button1 = new Image(new Texture("menus/quit.png"));
        button1.setSize(camera.viewportWidth / 2, camera.viewportWidth / 6);
        button1.setPosition(camera.viewportWidth / 4, camera.viewportHeight - ((camera.viewportWidth / 5) * 4));

        button2 = new Image(new Texture("menus/play.png"));
        button2.setSize(camera.viewportWidth / 2, camera.viewportWidth / 6);
        button2.setPosition(camera.viewportWidth / 4, camera.viewportHeight - ((camera.viewportWidth / 5) * 3));

        stage.addActor(button1);
        stage.addActor(button2);

        button1.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
            }
        });

        button2.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                control = Controls.PLAY;

            }
        });

    }

    public Controls drawMainMenu() {

        batch.begin();
        background.draw(batch, 1);
        bottomPannel.draw(batch, 1);
        logo.draw(batch, 1);
        button1.draw(batch, 1);
        button2.draw(batch, 1);
        batch.end();
        return control;
    }

    public void prepareWorldSelection(OrthographicCamera camera) {

        control = Controls.NONE;
        selectedLevel = Constants.ValueLevelSelection;

        Gdx.input.setInputProcessor(stage);
        stage.clear();
        batch = new SpriteBatch();
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        levelButtons = new ArrayList<>();

        double size = camera.viewportWidth / 6.5;

        for (int i = 0; i < Constants.howManyWorlds; i++) {

            double posX = size / 2 + size * 1.5 * (i % 4);
            double posY = camera.viewportHeight - (2 * size + Math.floor(i / 4) * size * 1.5);
            Image image = (new Image(new Texture("menus/play.png")));
            image.setSize((int) size, (int) size);
            image.setPosition((int) posX, (int) posY);
            levelButtons.add(image);
            stage.addActor(image);

            final int finalI = i;
            image.addListener(new ClickListener() {
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    selectedLevel = finalI + 1;

                }
            });
        }
        background = new Image(new Texture("menus/background.png"));
        background.setPosition(0, 0);
        background.setSize(camera.viewportWidth, camera.viewportHeight);

        button1 = new Image(new Texture("menu.png"));
        button1.setPosition((7 * camera.viewportWidth) / 10, ((camera.viewportHeight - camera.viewportWidth) / 2) / 5);
        button1.setSize((3 * camera.viewportWidth) / 10, (3 * ((camera.viewportHeight - camera.viewportWidth) / 2) / 5));

        stage.addActor(button1);
        button1.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                selectedLevel = Constants.ValueReturnToMainMenu;

            }
        });

    }

    public int drawWorldSelection() {

        batch.begin();
        background.draw(batch, 1);
        for (Image image : levelButtons) {
            image.draw(batch, 1);
        }
        button1.draw(batch, 1);
        batch.end();
        return selectedLevel;

    }

    public void prepareLevelSelection(OrthographicCamera camera, int world, Player player) {

        control = Controls.NONE;
        selectedLevel = Constants.ValueLevelSelection;

        Gdx.input.setInputProcessor(stage);
        stage.clear();
        batch = new SpriteBatch();
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);

        NodeList maps = MapsReader.getMapsList("/resources/maps" + world + ".xml");
        levelButtons = new ArrayList<>();
        stars = new ArrayList<>();


        double levelButtonWidth = camera.viewportWidth / 5;
        double starsSize = levelButtonWidth / 4;
        double levelButtonSpan = (camera.viewportWidth - (4 * levelButtonWidth)) / 5;
        double levelButtonHeight = 30 * levelButtonWidth / 25;

        for (int i = 0; i < maps.getLength(); i++) {

            double posX = levelButtonSpan + (levelButtonWidth + levelButtonSpan) * (i % 4);
            double posY = camera.viewportHeight - (levelButtonHeight + 2 * levelButtonSpan + Math.floor(i / 4) * (levelButtonHeight + levelButtonSpan));
            Image image = (new Image(new Texture("menus/level.png")));
            image.setSize((int) levelButtonWidth, (int) levelButtonHeight);
            image.setPosition((int) posX, (int) posY);
            levelButtons.add(image);

            int levelStars = help.utils.HelpUtils.levelStarsCount(world, i + 1);
            posY = posY + (levelButtonHeight - levelButtonWidth) / 4;

            if (levelStars == 1) {
                posX = posX + (levelButtonWidth / 2) - (starsSize / 2);
            }

            if (levelStars == 2) {
                posX = posX + (levelButtonWidth / 2) - (starsSize);
            }
            if (levelStars == 3) {
                posX = posX + (levelButtonWidth / 2) - 3 * (starsSize / 2);
            }

            if (levelStars == 4) {
                posX = posX + (levelButtonWidth / 2) - (starsSize * 2);
            }
            for (int j = 1; j <= levelStars; j++) {
                Image star;
                if (player.getStarsFromLevel(world, i + 1) >= j)
                    star = new Image(new Texture("menus/star_golden.png"));
                else
                    star = new Image(new Texture("menus/star_gray.png"));
                star.setPosition((int) posX, (int) posY);
                star.setSize((int) starsSize, (int) starsSize);
                stars.add(star);
                posX = posX + starsSize;
            }


            stage.addActor(image);

            final int finalI = i;
            image.addListener(new ClickListener() {
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    selectedLevel = finalI + 1;

                }
            });
        }

        bottomPannel = new Image(new Texture("menus/bottom_pannel.png"));
        bottomPannel.setSize(camera.viewportWidth, (camera.viewportHeight - camera.viewportWidth) / 2);
        bottomPannel.setPosition(0, 0);

        background = new Image(new Texture("menus/background.png"));
        background.setPosition(0, 0);
        background.setSize(camera.viewportWidth, camera.viewportHeight);

        button1 = new Image(new Texture("menu.png"));
        button1.setPosition((7 * camera.viewportWidth) / 10, ((camera.viewportHeight - camera.viewportWidth) / 2) / 5);
        button1.setSize((3 * camera.viewportWidth) / 10, (3 * ((camera.viewportHeight - camera.viewportWidth) / 2) / 5));

        stage.addActor(button1);
        button1.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                selectedLevel = Constants.ValueReturnToMainMenu;

            }
        });

    }

    public int drawMapSelection() {

        batch.begin();
        background.draw(batch, 1);
        for (Image image : levelButtons) {
            image.draw(batch, 1);
        }
        for (Image image : stars) {
            image.draw(batch, 1);
        }
        bottomPannel.draw(batch, 1);
        button1.draw(batch, 1);
        batch.end();
        return selectedLevel;

    }

}
