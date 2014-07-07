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
import mapSolver.MapSolver;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import view.MapView;
import view.Move;

import java.util.ArrayList;
import java.util.HashMap;

public class Slider extends ApplicationAdapter {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture empty;
    private Map map;
    private HashMap<String, Texture> textureHashMap;
    private KeyboardController keyboardController;

    @Override
    public void create() {


        keyboardController = new KeyboardController();

        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();

        empty = new Texture("empty.png");

        NodeList blocksList = BlocksReader.getBlocksList();
        NodeList objectsList = ObjectsReader.getObjectsList();

        textureHashMap = new HashMap<String, Texture>();
        for (int i = 0; i < blocksList.getLength(); i++) {
            Element block = (Element) blocksList.item(i);
            textureHashMap.put(
                    block.getAttribute("enum"),
                    new Texture(block.getAttribute("texture"))
            );
        }
        for (int i = 0; i < objectsList.getLength(); i++) {
            Element object = (Element) objectsList.item(i);
            textureHashMap.put(
                    object.getAttribute("enum"),
                    new Texture(object.getAttribute("texture"))
            );
        }


        map = new Map(1);

    }

    @Override
    public void render() {

        ArrayList<Move> moves = map.makeMove(keyboardController.checkForControl());

        MapView.drawMap(batch, empty, map, camera, textureHashMap, moves);

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 256, 256);
    }
}
