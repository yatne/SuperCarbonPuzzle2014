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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import enums.Controls;
import help.utils.Constants;
import map.Map;
import mapSystem.MapsInfo;
import player.Player;
import textures.TextureHolder;
import view.Alert;
import view.Star;
import view.Text;
import view.buttons.Button;

import java.util.ArrayList;

public class AfterLevelView extends PanelView {

    private Controls control;
    private BitmapFont bigFont;
    private BitmapFont smallFont;
    private Text text;
    private Text movesTakenText;
    private int starsObtained;
    private int starsToObtain;
    private Texture goldenStar;
    private Texture grayStar;
    private ArrayList<Star> starsList;
    private float starPosY;
    private float starSize;
    private Button levelSelectButton;
    private Button retryButton;
    private Button nextLevelButton;
    private boolean drawNextLevelButton;
    private Alert alert;
    private boolean allStarsObtained;
    private int mapNumber;
    private int mapWorld;

    public AfterLevelView(OrthographicCamera camera, BitmapFont buttonFont, final MapsInfo mapsInfo, final Player player) {
        super(camera, buttonFont);

        drawNextLevelButton = true;
        control = Controls.NONE;
        starsList = new ArrayList<>();
        alert = new Alert(camera);


        goldenStar = TextureHolder.goldMenuStar;
        grayStar = TextureHolder.grayMenuStar;

        float width = camera.viewportWidth / 3;
        float height = (((camera.viewportHeight - camera.viewportWidth) / 2) * 3 / 5);
        float posY = height / 2;

        float textPanelHeight = camera.viewportWidth + ((camera.viewportHeight - camera.viewportWidth) / 2);
        float maxBigFontSize = textPanelHeight / 9;


        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);

        bigFont = generator.generateFont((int) maxBigFontSize);
        while (bigFont.getBounds("Level 25 Completed").width > camera.viewportWidth) {
            maxBigFontSize--;
            bigFont = generator.generateFont((int) maxBigFontSize);
        }
        smallFont = generator.generateFont((int) (textPanelHeight) / 12);


        this.buttonFont = generator.generateFont((int) (height * 3 / 4));
        this.buttonFont.setColor(Color.BLACK);
        generator.dispose();

        text = new Text(0, (int) (camera.viewportHeight - textPanelHeight / 35 - textPanelHeight / 25), "");
        movesTakenText = new Text(0, (int) (camera.viewportHeight - (textPanelHeight / 3) - (textPanelHeight / 100) - textPanelHeight / 25), "moves taken:\n30");

        starSize = textPanelHeight / 7;
        starPosY = (camera.viewportHeight - textPanelHeight / 30 - textPanelHeight / 25 - 3 * bigFont.getCapHeight() / 2 - starSize);

        retryButton = new Button(TextureHolder.buttonsTexture, "retry", 0, posY, width, height, this.buttonFont);
        levelSelectButton = new Button(TextureHolder.buttonsTexture, "menu", width, posY, width, height, this.buttonFont);
        nextLevelButton = new Button(TextureHolder.buttonsTexture, "next", 2 * width, posY, width, height, this.buttonFont);


        retryButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                control = Controls.RESET;
                retryButton.setDrawable(retryButton.getTextureRegionDrawable());
            }
        });

        levelSelectButton.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                control = Controls.MENU;
                levelSelectButton.setDrawable(levelSelectButton.getTextureRegionDrawable());

            }
        });
        if (drawNextLevelButton) {
            nextLevelButton.addListener(new ClickListener() {
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    control = Controls.NEXT;
                    nextLevelButton.setDrawable(nextLevelButton.getTextureRegionDrawable());
                }
            });
        }
    }

    public void prepareAfterLevelView(Stage stage, Map map, Player player, MapsInfo mapsInfo, Image background, OrthographicCamera camera) {

        control = Controls.NONE;
        drawNextLevelButton = true;

        starsList.clear();
        starsToObtain = mapsInfo.getStarsToObtain(map.getMapWorld(), map.getMapNumber());
        starsObtained = map.getObtainedStars();
        float theMiddle = camera.viewportWidth / 2;
        float starsSpan = starSize / 10;
        if (starsToObtain == 2) {
            starsList.add(new Star(theMiddle - starSize - (starsSpan / 2), starPosY, (starsObtained >= 1)));
            starsList.add(new Star(theMiddle + (starsSpan / 2), starPosY, (starsObtained >= 2)));
        } else if (starsToObtain == 3) {
            starsList.add(new Star(theMiddle - 3 * starSize / 2 - starsSpan, starPosY, (starsObtained >= 1)));
            starsList.add(new Star(theMiddle - starSize / 2, starPosY, (starsObtained >= 2)));
            starsList.add(new Star(theMiddle + starSize / 2 + starsSpan, starPosY, (starsObtained >= 3)));
        } else if (starsToObtain == 4) {
            starsList.add(new Star(theMiddle - 2 * starSize - 3 * starsSpan / 2, starPosY, (starsObtained >= 1)));
            starsList.add(new Star(theMiddle - starSize - starsSpan / 2, starPosY, (starsObtained >= 2)));
            starsList.add(new Star(theMiddle + starsSpan / 2, starPosY, (starsObtained >= 3)));
            starsList.add(new Star(theMiddle + starSize + 3 * starsSpan / 2, starPosY, (starsObtained >= 4)));
        } else if (starsToObtain == 1) {
            starsList.add(new Star(theMiddle - starSize / 2, starPosY, (starsObtained >= 1)));
        }


        int movesTaken = map.getMovesTaken();
        if (movesTaken > 99) {
            movesTaken = 99;
        }
        int yourBest = player.getBestFromLevel(map.getMapWorld(), map.getMapNumber());
        int nextStar = 0;
        allStarsObtained = true;

        if (map.getMapWorld() == 1 || map.getMapWorld() == 2 || map.getMapWorld() == 5) {
            bigFont.setColor(Color.BLACK);
            smallFont.setColor(Color.BLACK);
        } else if (map.getMapWorld() == 3) {
            bigFont.setColor(Constants.thirdWorldTextColor);
            smallFont.setColor(Constants.thirdWorldTextColor);
        } else if (map.getMapWorld() == 4) {
            bigFont.setColor(Constants.fourthWorldTextColor);
            smallFont.setColor(Constants.fourthWorldTextColor);
        }

        for (Integer goal : map.getGoals()) {
            if (yourBest > goal) {
                allStarsObtained = false;
                if (goal > nextStar) {
                    nextStar = goal;
                }
            }
        }


        movesTakenText.setText("moves t\baken:\n" + movesTaken + "\nyour best:\n" + yourBest);
        if (!allStarsObtained) {

            movesTakenText.setText(movesTakenText.getText() + "\nnext star:\n" + nextStar);
        }

        mapNumber = map.getMapNumber();
        mapWorld = map.getMapWorld();

        text.setText("Level " + mapNumber + " complete");

        if (mapNumber >= mapsInfo.getMapsCountInWorld(mapWorld)) {
            drawNextLevelButton = false;
        }

        levelSelectButton.setButtonWorld(map.getMapWorld());
        nextLevelButton.setButtonWorld(map.getMapWorld());
        retryButton.setButtonWorld(map.getMapWorld());

        stage.addActor(retryButton);
        stage.addActor(levelSelectButton);
        if (drawNextLevelButton)
            stage.addActor(nextLevelButton);

        if (Constants.soundOn) {
            Gdx.input.vibrate(170);

        }

        this.background = background;
    }

    public Controls drawAfterLevel(OrthographicCamera camera, ShaderProgram shader, SpriteBatch batch, Stage stage) {

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

        if (mapWorld == 5) {
            buttonFont.setColor(Constants.fifthWorldButtonColor);
        }

        background.draw(batch, 1);
        bigFont.drawWrapped(batch, text.getText(), text.getPosX(), text.getPosY(), camera.viewportWidth, BitmapFont.HAlignment.CENTER);
        if (allStarsObtained)
            smallFont.drawWrapped(batch, movesTakenText.getText(), movesTakenText.getPosX(), movesTakenText.getPosY() - 2 * smallFont.getCapHeight(), camera.viewportWidth, BitmapFont.HAlignment.CENTER);
        else
            smallFont.drawWrapped(batch, movesTakenText.getText(), movesTakenText.getPosX(), movesTakenText.getPosY(), camera.viewportWidth, BitmapFont.HAlignment.CENTER);
        retryButton.draw(batch, 1, this.buttonFont);
        levelSelectButton.draw(batch, 1, this.buttonFont);
        if (drawNextLevelButton)
            nextLevelButton.draw(batch, 1, this.buttonFont);

        for (Star star : starsList) {
            if (star.isGold()) {
                batch.draw(goldenStar, star.getPosX(), starPosY, starSize, starSize);
            } else {
                batch.draw(grayStar, star.getPosX(), starPosY, starSize, starSize);
            }

        }

        buttonFont.setColor(Color.BLACK);
        batch.end();


        if (alert.isActive()) {
            if (alert.drawAlert(camera) == Controls.MENU) {
                alert.setActive(false);
                Gdx.input.setInputProcessor(stage);
            }
        }
        return control;
    }


}
