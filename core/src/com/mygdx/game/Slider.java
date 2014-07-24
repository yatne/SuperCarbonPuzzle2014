package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import enums.Controls;
import help.utils.KeyboardController;
import map.Map;
import view.MapView;
import view.Move;

import java.util.ArrayList;

public class Slider extends ApplicationAdapter {


    private OrthographicCamera camera;
    private Texture empty;
    private Map map;
    private MapView mapView;
    private KeyboardController keyboardController;

    @Override
    public void create() {
        keyboardController = new KeyboardController();
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        empty = new Texture("empty.png");
        map = new Map(1);
        mapView = new MapView(empty, map, camera);
    }

    @Override
    public void render() {
        Controls control = keyboardController.checkForControl();
        ArrayList<Move> moves = help.utils.HelpUtils.combineMoves(map.makeMove(control));
        /*if (mapView.getState() == 0) {
            if (control != Controls.NONE) {
                mapView.calculateState(moves, camera, map);
            } else {
                mapView.drawMap(empty, map, camera);
            }
        } else {
            mapView.drawAnimation(empty, map, camera, moves);
          */

        mapView.drawAnimation(empty, map, camera, moves);
        mapView.drawMap(empty, map, camera);

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 256, 256);
    }
}
