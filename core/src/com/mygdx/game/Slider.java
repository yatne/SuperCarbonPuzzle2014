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
import player.Player;
import view.MapView;
import view.menus.LevelSelectionView;
import view.menus.MainMenuView;
import view.menus.PanelView;
import view.menus.WorldSelectionView;

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
    private Map map;
    private MapView mapView;
    private KeyboardController keyboardController;
    private GameStates gameState;
    private int selectedLevel;
    private int selectedWorld;
    private Player player;
    private PanelView panelView;
    private WorldSelectionView worldSelectionView;
    private BitmapFont buttonFont;
    private MainMenuView mainMenuView;
    private LevelSelectionView levelSelectionView;
    private SpriteBatch mainBatch;
    private Stage mainStage;

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


        keyboardController = new KeyboardController();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new Map(1, 1);
        gameState = INTRO;

        mapView = new MapView();
        Constants.spritesMovingSpeed = (int) (Constants.spritesSpeedFactor * camera.viewportWidth);
        player = new Player();

        selectedWorld = 1;

        mainStage = new Stage();
        panelView = new PanelView(camera, buttonFont);

        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        FreeTypeFontGenerator.FreeTypeFontParameter freeTypeFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        freeTypeFontParameter.size = (int) (camera.viewportWidth / 8);
        buttonFont = generator.generateFont((int) (camera.viewportWidth / 8));
        buttonFont.setColor(0, 0, 0, 1);
        generator.dispose();

        mainMenuView = new MainMenuView(camera, buttonFont);
        worldSelectionView = new WorldSelectionView(camera, player, buttonFont);
        levelSelectionView = new LevelSelectionView(selectedWorld, camera, player, shader, buttonFont);
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
                    worldSelectionView.prepareWorldSelectionView(mainStage);
                    gameState = WORLD_SELECT;
                }
                break;
            }

            case WORLD_SELECT: {
                int nowSelectedWorld = worldSelectionView.drawWorldSelection(mainBatch, camera, shader);
                if (nowSelectedWorld > 0) {

                    selectedWorld = nowSelectedWorld;
                    levelSelectionView.prepareLevelSelection(selectedWorld, mainStage);
                    levelSelectionView = new LevelSelectionView(selectedWorld, camera, player, shader, buttonFont);
                    gameState = LEVEL_SELECT;

                } else if (nowSelectedWorld == -1) {
                    mainMenuView.prepareMainMenu(mainStage);
                    gameState = MENU;
                }
                break;
            }


            case LEVEL_SELECT: {

                if (levelSelectionView.drawLevelSelection(camera,shader,mainBatch) > 0) {

                    //   map = new Map(selectedWorld, selectedLevel);
                    // panelView.closePanel();
                    //   panelView = new PreLevelView(camera, shader, map, player, buttonFont);
                    //    gameState = PRE_LEVEL;
                } else if (levelSelectionView.drawLevelSelection(camera,shader,mainBatch) == -1) {
                    //      panelView.closePanel();
                    //       panelView = new WorldSelectionView(camera, player, shader, buttonFont);
                    //      gameState = WORLD_SELECT;
                }

                break;
            }
                        /*
            case PRE_LEVEL: {

                if (panelView.drawPreLevel(camera) == Controls.PLAY) {
                    mapView.prepareMapUI(camera, map);
                    mapView.prepareMap(camera, map);
                    gameState = LEVEL;
                } else if (panelView.drawPreLevel(camera) == Controls.MENU) {
                    panelView.closePanel();
                    panelView = new LevelSelectionView(selectedWorld, camera, player, shader, buttonFont);
                    gameState = LEVEL_SELECT;
                }

                break;
            }

            case LEVEL: {

                if (map.checkForFinish()) {

                    player.update(map);
                    panelView.closePanel();
                    panelView = new AfterLevelView(camera, shader, map, player, buttonFont);
                    gameState = AFTER_LEVEL;
                    player.savePlayer();
                }

                Controls control = mapView.getControl();

                if (control == Controls.NONE) {
                    control = keyboardController.checkForControl();
                }
                if (control == Controls.NONE) {
                    mapView.drawMap(map, player, camera);
                } else {
                    if (control == Controls.RESET) {
                        map = new Map(selectedWorld, selectedLevel);
                        mapView.prepareMap(camera, map);
                    } else if (control == Controls.NEXT) {
                        selectedLevel++;
                        map = new Map(selectedWorld, selectedLevel);
                        mapView.prepareMap(camera, map);
                    } else if (control == Controls.PREVIOUS && selectedLevel != 1) {
                        selectedLevel--;
                        map = new Map(selectedWorld, selectedLevel);
                        mapView.prepareMap(camera, map);
                    } else if (control == Controls.MENU) {
                        panelView.closePanel();
                        panelView = new LevelSelectionView(selectedWorld, camera, player, shader, buttonFont);
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
                if (mapView.drawAnimation(map, player, camera)) {
                    mapView.afterAnimation(camera);
                    gameState = LEVEL;
                }
                break;
            }

            case AFTER_LEVEL: {

                if (panelView.drawAfterLevel(camera) == Controls.NEXT) {

                    selectedLevel++;

                    map = new Map(selectedWorld, selectedLevel);
                    mapView.prepareMapUI(camera, map);
                    mapView.prepareMap(camera, map);

                    panelView.closePanel();
                    panelView = new PreLevelView(camera, shader, map, player, buttonFont);
                    gameState = PRE_LEVEL;

                } else if (panelView.drawAfterLevel(camera) == Controls.RESET) {
                    map = new Map(selectedWorld, selectedLevel);
                    mapView.prepareMapUI(camera, map);
                    mapView.prepareMap(camera, map);

                    panelView.closePanel();
                    panelView = new PreLevelView(camera, shader, map, player, buttonFont);

                    gameState = LEVEL;
                } else if (panelView.drawAfterLevel(camera) == Controls.MENU) {

                    panelView.closePanel();
                    panelView = new LevelSelectionView(selectedWorld, camera, player, shader, buttonFont);
                    gameState = LEVEL_SELECT;
                }
                break;    */
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

                panelView = new LevelSelectionView(selectedWorld, camera, player, shader, buttonFont);
                break;
            }
            case LEVEL: {
                mapView.prepareMap(camera, map);
                mapView.prepareMapUI(camera, map);
                break;
            }
            case ALERT: {

                panelView = new LevelSelectionView(selectedWorld, camera, player, shader, buttonFont);
                gameState = LEVEL_SELECT;
                break;
            }
        }
    }

    @Override
    public void pause() {

    }

}
