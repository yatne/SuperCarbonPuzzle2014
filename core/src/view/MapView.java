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

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class MapView {


    private SpriteBatch batch;
    private HashMap<String, Texture> textureHashMap;
    private ArrayList<Sprite> sprites;

    public MapView(Texture img, Map map, OrthographicCamera camera) {

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

        createSpritesList(img, map, camera);
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

        for(Sprite sprite :sprites){
            sprite.draw(batch);
        }

        batch.end();

        //createSpritesList(img, map, camera);
    }

    public void drawAnimation(Texture img, Map map, OrthographicCamera camera, ArrayList<Move> moves) {


        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);


        float scaleX = camera.viewportWidth / (map.getMapWidth() * img.getWidth());
        float scaleY = camera.viewportHeight / (map.getMapHeight() * img.getHeight());

        int end = animationLength(moves);
        ArrayList<MovingSprite> movingSprites = new ArrayList<>();

        for (Sprite sprite : sprites) {

            for (Move move : moves) {
                Point spritePoint = new Point((int) sprite.getX(), (int) sprite.getY());
                Point movePoint = new Point(move.getFromX(), move.getFromY());
                movePoint = fromMapCellToSpritePos(movePoint, camera, img, map);
                if (spritePoint.equals(movePoint)) {
                    movingSprites.add(new MovingSprite(sprite, new Point(move.getFromX(), move.getFromY()),
                            new Point(move.getToX(), move.getToY())));
                }
            }
        }

        for (int k = 0; k <= 10 * end; k++) {

            for (MovingSprite movingSprite : movingSprites) {

                movingSprite.moveSprite(k, scaleX, scaleY);
                sprites.add(movingSprite.getSprite());


            }
        }



    }

    public Point fromMapCellToSpritePos(Point point, OrthographicCamera camera, Texture img, Map map) {

        float scaleX = camera.viewportWidth / (map.getMapWidth() * img.getWidth());
        float scaleY = camera.viewportHeight / (map.getMapHeight() * img.getHeight());

        Point newPoint = new Point((int) (point.getX() * img.getWidth() * scaleX), (int) (point.getY() * img.getHeight() * scaleY));
        return newPoint;
    }

    public int animationLength(ArrayList<Move> moves) {

        int i = 0;
        for (Move move : moves) {
            if (Math.abs(move.fromX - move.getToX()) > i) {
                i = Math.abs(move.fromX - move.getToX());
            } else if (Math.abs(move.fromY - move.getToY()) > i) {
                i = Math.abs(move.fromY - move.getToY());
            }
        }
        return i;
    }
}
