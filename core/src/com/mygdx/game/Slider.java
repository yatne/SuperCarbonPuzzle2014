package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import enums.Controls;
import enums.GameStates;
import help.utils.KeyboardController;
import map.Map;
import menus.MenuView;
import view.MapView;

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
        mapView = new MapView(map, camera);
        gameState = INTRO;
        menuView = new MenuView();


    }

    @Override
    public void render() {

        switch (gameState) {

            case INTRO: {
                System.out.println("intro");
                selectedLevel = 1;
                gameState = MENU;
                menuView.prepareMainMenu(camera);
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

                if (menuView.drawMapSelection(camera) > 0) {
                    selectedLevel = menuView.drawMapSelection(camera);
                    gameState = PRE_LEVEL;
                } else if (menuView.drawMapSelection(camera) == -1) {
                    menuView.prepareMainMenu(camera);
                    gameState = MENU;
                }
                break;
            }

            case PRE_LEVEL: {
                map = new Map(selectedLevel);
                mapView = new MapView(map, camera);
                gameState = LEVEL;


                break;
            }

            case LEVEL: {
                if (map.checkForFinish()) {
                    selectedLevel++;
                    map = new Map(selectedLevel);
                    mapView = new MapView(map, camera);
                }
                Controls control = mapView.getControl();

                if (control == Controls.NONE) {

                    control = keyboardController.checkForControl();
                    if (control == Controls.NONE) {
                        mapView.drawMap(map, camera);
                    } else {
                        if (control == Controls.RESET) {
                            map = new Map(selectedLevel);
                            mapView = new MapView(map, camera);
                        } else if (control == Controls.NEXT) {
                            selectedLevel++;
                            map = new Map(selectedLevel);
                            mapView = new MapView(map, camera);
                        } else if (control == Controls.PREVIOUS && selectedLevel != 1) {
                            selectedLevel--;
                            map = new Map(selectedLevel);
                            mapView = new MapView(map, camera);
                        } else {
                            map.makeMove(control);
                            gameState = LEVEL_ANIMATION;
                        }
                    }
                } else if (control == Controls.RESET) {
                    map = new Map(selectedLevel);
                    mapView = new MapView(map, camera);
                } else if (control == Controls.MENU) {
                    menuView.prepareMainMenu(camera);
                    gameState = MENU;
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
        mapView.createSpritesList(map, camera);
        mapView.resizeButtons(camera);
        mapView.createFonts(camera);

        switch (gameState) {
            case MENU: {
                menuView.prepareMainMenu(camera);
                break;
            }
            case LEVEL_SELECT: {
                menuView.prepareLevelSelection(camera);
                break;
            }
        }
    }
}
