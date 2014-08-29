package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import controllers.KeyboardController;
import enums.Controls;
import enums.GameStates;
import help.utils.Constants;
import map.Map;
import mapSystem.MapsInfo;
import player.Player;
import view.MapView;
import view.menus.*;

import static enums.GameStates.*;

public class Slider extends ApplicationAdapter {

    final String VERT =
            "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
                    "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" +
                    "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +

                    "uniform mat4 u_projTrans;\n" +
                    " \n" +
                    "varying vec4 vColor;\n" +
                    "varying vec2 vTexCoord;\n" +

                    "void main() {\n" +
                    "       vColor = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" +
                    "       vTexCoord = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +
                    "       gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
                    "}";
    final String FRAG =
            //GL ES specific stuff
            "#ifdef GL_ES\n" //
                    + "#define LOWP lowp\n" //
                    + "precision mediump float;\n" //
                    + "#else\n" //
                    + "#define LOWP \n" //
                    + "#endif\n" + //
                    "varying LOWP vec4 vColor;\n" +
                    "varying vec2 vTexCoord;\n" +
                    "uniform sampler2D u_texture;\n" +
                    "uniform float grayscale;\n" +
                    "void main() {\n" +
                    "       vec4 texColor = texture2D(u_texture, vTexCoord);\n" +
                    "       \n" +
                    "       float gray = dot(texColor.rgb, vec3(0.299, 0.587, 0.114));\n" +
                    "       texColor.rgb = mix(vec3(gray), texColor.rgb, grayscale);\n" +
                    "       \n" +
                    "       gl_FragColor = texColor * vColor;\n" +
                    "}";
    private ShaderProgram shader;
    private OrthographicCamera camera;
    private Stage mainStage;
    private SpriteBatch mainBatch;
    private Player player;
    private MapsInfo mapsInfo;
    private int selectedLevel;
    private int selectedWorld;
    private GameStates gameState;
    private Map map;
    private MapView mapView;
    private KeyboardController keyboardController;
    private WorldSelectionView worldSelectionView;
    private MainMenuView mainMenuView;
    private LevelSelectionView levelSelectionView;
    private PreLevelView preLevelView;
    private AfterLevelView afterLevelView;
    private BitmapFont buttonFont;

    @Override
    public void create() {

        ShaderProgram.pedantic = false;
        shader = new ShaderProgram(VERT, FRAG);
        if (!shader.isCompiled()) {
            System.err.println(shader.getLog());
            System.exit(0);
        }
        if (shader.getLog().length() != 0)
            System.out.println(shader.getLog());


        mainBatch = new SpriteBatch();
        mainBatch.setShader(shader);

        shader.begin();
        shader.setUniformf("grayscale", 1f);
        shader.end();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        keyboardController = new KeyboardController();

        map = new Map(1, 1);
        gameState = INTRO;

        mapView = new MapView(camera);
        Constants.spritesMovingSpeed = (int) (Constants.spritesSpeedFactor * camera.viewportWidth);

        player = new Player();
        mapsInfo = new MapsInfo();

        selectedWorld = 1;

        mainStage = new Stage();


        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.size = (int) (camera.viewportWidth / 8);
        buttonFont = generator.generateFont((int) (camera.viewportWidth / 8));
        buttonFont.setColor(0, 0, 0, 1);
        generator.dispose();

        mainMenuView = new MainMenuView(camera, buttonFont);
        worldSelectionView = new WorldSelectionView(camera, buttonFont, mapsInfo);
        levelSelectionView = new LevelSelectionView(camera, player, buttonFont, mapsInfo);
        preLevelView = new PreLevelView(camera, buttonFont);
        afterLevelView = new AfterLevelView(camera, buttonFont, mapsInfo, player);
        Gdx.input.setInputProcessor(mainStage);
    }

    @Override
    public void render() {

        switch (gameState) {

            case INTRO: {
                gameState = MENU;
                mainMenuView.prepareMainMenu(mainStage);
                break;
            }

            case MENU: {
                Controls control = mainMenuView.drawMainMenu(mainBatch);
                if (control == Controls.PLAY) {
                    worldSelectionView.prepareWorldSelectionView(mainStage, player, mapsInfo);
                    gameState = WORLD_SELECT;
                }
                break;
            }

            case WORLD_SELECT: {
                int nowSelectedWorld = worldSelectionView.drawWorldSelection(mainBatch, camera, shader, mainStage);
                if (nowSelectedWorld > 0) {

                    selectedWorld = nowSelectedWorld;
                    levelSelectionView.prepareLevelSelection(selectedWorld, mainStage, player, mapsInfo);
                    gameState = LEVEL_SELECT;

                } else if (nowSelectedWorld == -1) {
                    mainMenuView.prepareMainMenu(mainStage);
                    gameState = MENU;
                }
                break;
            }


            case LEVEL_SELECT: {
                int nowSelectedLevel = levelSelectionView.drawLevelSelection(camera, shader, mainBatch, mainStage);
                if (nowSelectedLevel > 0) {
                    selectedLevel = nowSelectedLevel;
                    map.loadMap(selectedWorld, selectedLevel);
                    preLevelView.preparePreLevel(mainStage, map, player);
                    gameState = PRE_LEVEL;
                } else if (nowSelectedLevel == -1) {
                    worldSelectionView.prepareWorldSelectionView(mainStage, player, mapsInfo);
                    gameState = WORLD_SELECT;
                }

                break;
            }

            case PRE_LEVEL: {

                Controls control = preLevelView.drawPreLevel(camera, mainBatch);
                if (control == Controls.PLAY) {
                    mapView.prepareMapUI(camera, map, mainStage);
                    mapView.prepareMap(camera, map);
                    gameState = LEVEL;
                } else if (control == Controls.MENU) {
                    levelSelectionView.prepareLevelSelection(selectedWorld, mainStage, player, mapsInfo);
                    gameState = LEVEL_SELECT;
                }

                break;
            }

            case LEVEL: {

                if (map.checkForFinish()) {

                    player.update(map);
                    Gdx.input.setInputProcessor(mainStage);
                    afterLevelView.prepareAfterLevelView(mainStage, map, player, mapsInfo);
                    gameState = AFTER_LEVEL;
                    player.savePlayer();
                }

                Controls control = mapView.getControl();

                if (control == Controls.NONE) {
                    control = keyboardController.checkForControl();
                }
                if (control == Controls.NONE) {
                    mapView.drawMap(map, camera, mainBatch);
                } else {
                    if (control == Controls.RESET) {
                        map.loadMap(selectedWorld, selectedLevel);
                        mapView.prepareMap(camera, map);
                    } else if (control == Controls.NEXT) {
                        selectedLevel++;
                        map.loadMap(selectedWorld, selectedLevel);
                        mapView.prepareMap(camera, map);
                    } else if (control == Controls.PREVIOUS && selectedLevel != 1) {
                        selectedLevel--;
                        map.loadMap(selectedWorld, selectedLevel);
                        mapView.prepareMap(camera, map);
                    } else if (control == Controls.MENU) {
                        Gdx.input.setInputProcessor(mainStage);
                        levelSelectionView.prepareLevelSelection(selectedWorld, mainStage, player, mapsInfo);
                        gameState = LEVEL_SELECT;
                    } else {
                        map.makeMove(control);
                        mapView.checkForPortalMoves(map);
                        mapView.prepareAnimation();
                        gameState = LEVEL_ANIMATION;
                    }
                }
                break;
            }

            case LEVEL_ANIMATION: {
                if (mapView.drawAnimation(map, camera, mainBatch)) {
                    mapView.afterAnimation(camera);
                    gameState = LEVEL;
                }
                break;
            }

            case AFTER_LEVEL: {

                Controls controls = afterLevelView.drawAfterLevel(camera, shader, mainBatch, mainStage);
                if (controls == Controls.NEXT) {
                    selectedLevel++;
                    map.loadMap(selectedWorld, selectedLevel);
                    preLevelView.preparePreLevel(mainStage, map, player);
                    gameState = PRE_LEVEL;

                } else if (controls == Controls.RESET) {
                    map.loadMap(selectedWorld, selectedLevel);
                    mapView.prepareMapUI(camera, map, mainStage);
                    mapView.prepareMap(camera, map);
                    gameState = LEVEL;
                } else if (controls == Controls.MENU) {
                    levelSelectionView.prepareLevelSelection(selectedWorld, mainStage, player, mapsInfo);
                    gameState = LEVEL_SELECT;

                }
                break;
            }

        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Constants.spritesMovingSpeed = (int) (Constants.spritesSpeedFactor * camera.viewportWidth);


        switch (gameState) {
            case MENU: {
                //    menuView.prepareMainMenu(camera);
                break;
            }
            case LEVEL_SELECT: {


                break;
            }
            case LEVEL: {
                mapView.prepareMap(camera, map);
                mapView.prepareMapUI(camera, map, mainStage);
                break;
            }
            case ALERT: {


                gameState = LEVEL_SELECT;
                break;
            }
        }
    }

    @Override
    public void pause() {

    }

}
