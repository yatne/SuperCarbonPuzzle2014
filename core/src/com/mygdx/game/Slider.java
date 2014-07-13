package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import help.utils.BlocksReader;
import help.utils.KeyboardController;
import help.utils.ObjectsReader;
import map.Map;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import view.MapView;
import view.Move;

import java.util.ArrayList;
import java.util.HashMap;

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
        mapView = new MapView();
    }

    @Override
    public void render() {


        ArrayList<Move> moves = map.makeMove(keyboardController.checkForControl());

        if (moves.size() > 0) {
           // System.out.println(moves.toString());
           // System.out.println(help.utils.HelpUtils.combineMoves(moves).toString());
                mapView.drawAnimation(empty,map,camera,help.utils.HelpUtils.combineMoves(moves),moves.size());
        }

        mapView.drawMap(empty, map, camera);


    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 256, 256);
    }
}
