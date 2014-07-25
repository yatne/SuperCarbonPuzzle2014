package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import enums.Controls;
import enums.GameStates;
import help.utils.KeyboardController;
import map.Map;
import view.MapView;

import static enums.GameStates.*;

public class Slider extends ApplicationAdapter {


    private OrthographicCamera camera;
    private Texture empty;
    private Map map;
    private MapView mapView;
    private KeyboardController keyboardController;
    private GameStates gameState;
    private int selectedLevel;
    private int selectedWorld;

    @Override
    public void create() {
        keyboardController = new KeyboardController();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        empty = new Texture("empty.png");
        map = new Map(1);
        mapView = new MapView(empty, map, camera);
        gameState = INTRO;
    }

    @Override
    public void render() {

        switch (gameState) {

            case INTRO: {
                System.out.println("intro");
                selectedLevel = 1;
                gameState = PRE_LEVEL;
                break;
            }

            case PRE_LEVEL: {
                map = new Map(selectedLevel);
                gameState = LEVEL;
            }

            case LEVEL: {
                if (map.checkForFinish()) {
                    selectedLevel++;
                    map = new Map(selectedLevel);
                    mapView = new MapView(empty, map, camera);
                }

                Controls control = keyboardController.checkForControl();
                if (control == Controls.NONE) {
                    mapView.drawMap(empty, map, camera);
                } else {
                    if (control == Controls.RESET) {
                        map = new Map(selectedLevel);
                        mapView = new MapView(empty, map, camera);
                    } else if (control == Controls.NEXT) {
                        selectedLevel++;
                        map = new Map(selectedLevel);
                        mapView = new MapView(empty, map, camera);
                    } else if (control == Controls.PREVIOUS && selectedLevel != 1) {
                        selectedLevel--;
                        map = new Map(selectedLevel);
                        mapView = new MapView(empty, map, camera);
                    } else {
                        map.makeMove(control);
                        gameState = LEVEL_ANIMATION;
                    }
                }
                break;
            }

            case LEVEL_ANIMATION: {
                if (mapView.drawAnimation(empty, map, camera)) {
                    gameState = LEVEL;
                }
                break;
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 256, 256);
    }
}
