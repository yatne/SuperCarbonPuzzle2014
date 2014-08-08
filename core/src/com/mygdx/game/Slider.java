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
import view.MapView;
import view.MenuView;

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

    @Override
    public void create() {

        keyboardController = new KeyboardController();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new Map(1, 1);
        gameState = INTRO;
        menuView = new MenuView();
        mapView = new MapView();
        Constants.spritesMovingSpeed = (int) (Constants.spritesSpeedFactor * camera.viewportWidth);
        player = new Player();

        selectedWorld=1;

    }

    @Override
    public void render() {

        switch (gameState) {

            case INTRO: {
                menuView.prepareMainMenu(camera);
                gameState = MENU;
                break;
            }

            case MENU: {
                if (menuView.drawMainMenu() == Controls.PLAY) {
                    menuView.prepareLevelSelection(camera,1,player);
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
                    menuView.prepareMainMenu(camera);
                    gameState = MENU;
                }
                break;
            }


            case LEVEL_SELECT: {

                if (menuView.drawMapSelection() > Constants.ValueLevelSelection) {
                    selectedLevel = menuView.drawMapSelection();
                    gameState = PRE_LEVEL;
                } else if (menuView.drawMapSelection() == Constants.ValueReturnToMainMenu) {
                    menuView.prepareWorldSelection(camera);
                    gameState = WORLD_SELECT;
                }
                break;
            }

            case PRE_LEVEL: {
                map = new Map(selectedWorld, selectedLevel);
                mapView.prepareMapUI(camera);
                mapView.prepareMap(camera, map);
                gameState = LEVEL;
                break;
            }

            case LEVEL: {
                if (map.checkForFinish()) {

                    player.update(map);
                    selectedLevel++;
                    map = new Map(selectedWorld, selectedLevel);
                    mapView.prepareMap(camera, map);

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
                        menuView.prepareMainMenu(camera);
                        gameState = MENU;
                    } else {
                        map.makeMove(control);
                        mapView.checkForPortalMoves(map);
                        gameState = LEVEL_ANIMATION;
                    }
                }
                break;
            }

            case LEVEL_ANIMATION: {
                if (mapView.drawAnimation(map, player, camera)) {
                    gameState = LEVEL;
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
                menuView.prepareMainMenu(camera);
                break;
            }
            case LEVEL_SELECT: {
                menuView.prepareLevelSelection(camera, selectedWorld, player);
                break;
            }
            case LEVEL: {
                mapView.prepareMap(camera, map);
                mapView.prepareMapUI(camera);
                break;
            }
        }
    }

    @Override
    public void pause() {
    }
}
