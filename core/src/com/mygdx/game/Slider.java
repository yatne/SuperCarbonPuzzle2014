package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import controllers.KeyboardController;
import enums.Controls;
import enums.GameStates;
import help.utils.Constants;
import map.Map;
import player.Player;
import view.Alert;
import view.LevelInfoView;
import view.MapView;
import view.MenuView;
import view.menus.LevelSelectionView;
import view.menus.MainMenuView;
import view.menus.PanelView;

import static enums.GameStates.*;

public class Slider extends ApplicationAdapter {

    private OrthographicCamera camera;
    private Map map;
    private MapView mapView;
    private KeyboardController keyboardController;
    private GameStates gameState;
    private int selectedLevel;
    private int selectedWorld;
    private MenuView menuView;
    private Player player;
    private Alert alert;
    private boolean levelInfoPrepared;
    private LevelInfoView levelInfoView;
    private PanelView panelView;

    @Override
    public void create() {

        resetStates();

        keyboardController = new KeyboardController();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new Map(1, 1);
        gameState = INTRO;
        menuView = new MenuView();
        mapView = new MapView();
        levelInfoView = new LevelInfoView();
        Constants.spritesMovingSpeed = (int) (Constants.spritesSpeedFactor * camera.viewportWidth);
        player = new Player();

        selectedWorld = 1;

        panelView = new PanelView(camera);

    }

    @Override
    public void render() {

        switch (gameState) {

            case INTRO: {

                resetStates();

                //menuView.prepareMainMenu(camera);
                panelView = new MainMenuView(camera);
                gameState = MENU;
                break;
            }

            case MENU: {
                if (panelView.drawMainMenu() == Controls.PLAY) {
                    //menuView.prepareLevelSelection(camera, 1, player);
                    selectedWorld=1;
                    panelView = new LevelSelectionView(selectedWorld,camera,player);
                    gameState = LEVEL_SELECT;
                }
                break;
            }

            case WORLD_SELECT: {
                if (menuView.drawWorldSelection() > Constants.ValueLevelSelection) {

                    selectedWorld = menuView.drawWorldSelection();
                    menuView.prepareLevelSelection(camera, selectedWorld, player);
                    gameState = LEVEL_SELECT;
                } else if (menuView.drawWorldSelection() == Constants.ValueReturnToMainMenu) {
                   // menuView.prepareMainMenu(camera);
                    gameState = MENU;
                }
                break;
            }


            case LEVEL_SELECT: {

                if(panelView.drawLevelSelection()>0){
                    selectedLevel = panelView.drawLevelSelection();
                    if (help.utils.MapsReader.starsToUnlock(selectedWorld, selectedLevel) > player.getStars()) {
                        alert = new Alert(camera, selectedLevel, selectedWorld);
                        gameState = ALERT;
                    } else {
                        map = new Map(selectedWorld, selectedLevel);

                        gameState = PRE_LEVEL;
                    }
                }else if (panelView.drawLevelSelection()==-1){
                    panelView = new MainMenuView(camera);
                    gameState = MENU;
                }

                break;
            }

            case PRE_LEVEL: {

                if (levelInfoPrepared) {

                    if (levelInfoView.drawPreLevel(camera) == Controls.NEXT) {

                        mapView.prepareMapUI(camera, map);
                        mapView.prepareMap(camera, map);

                        gameState = LEVEL;
                    }
                } else {
                    levelInfoView.preparePreLevel(camera, map);
                    levelInfoPrepared = true;
                }
                break;
            }

            case LEVEL: {
                resetStates();
                if (map.checkForFinish()) {

                    player.update(map);
                    selectedLevel++;
                    levelInfoView.preparePostLevel(camera, map, player);
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
                      //  menuView.prepareMainMenu(camera);
                        gameState = MENU;
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

                if (levelInfoView.drawPreLevel(camera) == Controls.NEXT) {


                    map = new Map(selectedWorld, selectedLevel);
                    mapView.prepareMapUI(camera, map);
                    mapView.prepareMap(camera, map);

                    gameState = LEVEL;
                }
                break;
            }

            case ALERT: {

                if (alert.drawAlert(camera) == Controls.MENU) {
                    menuView.prepareLevelSelection(camera, selectedWorld, player);
                    gameState = LEVEL_SELECT;
                    selectedLevel = 0;
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
                menuView.prepareLevelSelection(camera, selectedWorld, player);
                break;
            }
            case LEVEL: {
                mapView.prepareMap(camera, map);
                mapView.prepareMapUI(camera, map);
                break;
            }
            case ALERT: {
                menuView.prepareLevelSelection(camera, selectedWorld, player);
                menuView.drawMapSelection();
                Alert alert = new Alert(camera, selectedLevel, selectedWorld);
                alert.drawAlert(camera);
                break;
            }
        }
    }

    @Override
    public void pause() {
    }

    private void resetStates() {
        levelInfoPrepared = false;
    }
}
