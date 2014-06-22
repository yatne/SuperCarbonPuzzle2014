package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import help.utils.BlocksReader;

import help.utils.KeybordControler;
import help.utils.ObjectsReader;
import map.Map;
import map.MapView;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.HashMap;

public class Slider extends ApplicationAdapter {

    private SpriteBatch batch;
    private OrthographicCamera camera;
    private Texture empty;
    private Map map;
    private HashMap<String, Texture> textureHashMap;
    private KeybordControler keybordControler;

    @Override
    public void create() {

        keybordControler = new KeybordControler();

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


        map = new Map(5);

    }

    @Override
    public void render() {

            map.makeMove(keybordControler.checkForControl());

        MapView.drawMap(batch, empty, map, camera, textureHashMap);

    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, 256, 256);
    }
}
