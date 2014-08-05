package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import controllers.KeyboardController;
import enums.Controls;
import enums.GameStates;
import help.utils.Constants;
import map.Map;
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

    @Override
    public void create() {

        keyboardController = new KeyboardController();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        map = new Map(1);
        gameState = INTRO;
        menuView = new MenuView();
        mapView = new MapView();

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
                    menuView.prepareLevelSelection(camera);
                    gameState = LEVEL_SELECT;
                }
                break;
            }

            case LEVEL_SELECT: {

                if (menuView.drawMapSelection(camera) > Constants.ValueLevelSelection) {
                    selectedLevel = menuView.drawMapSelection(camera);
                    gameState = PRE_LEVEL;
                } else if (menuView.drawMapSelection(camera) == Constants.ValueReturnToMainMenu) {
                    menuView.prepareMainMenu(camera);
                    gameState = MENU;
                }
                break;
            }

            case PRE_LEVEL: {
                map = new Map(selectedLevel);
                mapView.prepareMapUI(camera);
                mapView.prepareMap(camera, map);
                gameState = LEVEL;
                break;
            }

            case LEVEL: {
                if (map.checkForFinish()) {
                    selectedLevel++;
                    map = new Map(selectedLevel);
                    mapView.prepareMap(camera, map);
                }

                Controls control = mapView.getControl();

                if (control == Controls.NONE) {
                    control = keyboardController.checkForControl();
                }
                if (control == Controls.NONE) {
                    mapView.drawMap(map, camera);
                } else {
                    if (control == Controls.RESET) {
                        map = new Map(selectedLevel);
                        mapView.prepareMap(camera, map);
                    } else if (control == Controls.NEXT) {
                        selectedLevel++;
                        map = new Map(selectedLevel);
                        mapView.prepareMap(camera, map);
                    } else if (control == Controls.PREVIOUS && selectedLevel != 1) {
                        selectedLevel--;
                        map = new Map(selectedLevel);
                        mapView.prepareMap(camera, map);
                    } else if (control == Controls.MENU) {
                        menuView.prepareMainMenu(camera);
                        gameState = MENU;
                    } else {
                        map.makeMove(control);
                        gameState = LEVEL_ANIMATION;
                    }
                }
                break;
            }

            case LEVEL_ANIMATION: {
                if (mapView.drawAnimation(map, camera)) {
                    gameState = LEVEL;
                }
                break;
            }
            case WORLD_SELECT:
                break;
            case AFTER_LEVEL:
                break;
        }

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


        switch (gameState) {
            case MENU: {
                menuView.prepareMainMenu(camera);
                break;
            }
            case LEVEL_SELECT: {
                menuView.prepareLevelSelection(camera);
                break;
            }
            case LEVEL: {
                mapView.prepareMap(camera, map);
                mapView.prepareMapUI(camera);
                break;
            }
        }
    }
}
