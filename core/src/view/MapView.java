package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import help.utils.BlocksReader;
import help.utils.ObjectsReader;
import map.Field;
import map.Map;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.HashMap;

public class MapView {


    private SpriteBatch batch;
    private HashMap<String, Texture> textureHashMap;
    private ArrayList<Sprite> sprites;

    public MapView() {

        batch = new SpriteBatch();

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
    }

    public void createSpritesList(Texture img, Map map, OrthographicCamera camera) {

        sprites = new ArrayList<>();
        float scaleX = camera.viewportWidth / (map.getMapWidth() * img.getWidth());
        float scaleY = camera.viewportHeight / (map.getMapHeight() * img.getHeight());

        for (ArrayList<Field> row : map.getFields()) {
            for (Field field : row) {
                if (field.hasObject()) {
                    Texture texture = textureHashMap.get(field.getObject().getObjectsType().toString());
                    Sprite sprite = new Sprite(texture);
                    sprite.setPosition(field.getX() * img.getWidth() * scaleX, field.getY() * img.getWidth() * scaleY);
                    sprite.setSize(img.getWidth() * scaleX, img.getWidth() * scaleY);
                    sprites.add(sprite);
                }
            }
        }

    }

    public void drawMap(Texture img, Map map, OrthographicCamera camera) {

        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        createSpritesList(img, map, camera);

        float scaleX = camera.viewportWidth / (map.getMapWidth() * img.getWidth());
        float scaleY = camera.viewportHeight / (map.getMapHeight() * img.getHeight());

        for (ArrayList<Field> row : map.getFields()) {
            for (Field field : row) {

                batch.draw(textureHashMap.get(field.getFieldType().toString()),
                        field.getX() * img.getWidth() * scaleX,
                        field.getY() * img.getWidth() * scaleY,
                        img.getWidth() * scaleX,
                        img.getWidth() * scaleY);
            }
        }
        for (Sprite sprite : sprites) {
            sprite.draw(batch);
        }

        batch.end();

    }

    public void drawAnimation(Texture empty, Map map, OrthographicCamera camera, ArrayList<Move> moves, int i) {

        for (int k = 0; k < 50; k++)
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();
    }
}
