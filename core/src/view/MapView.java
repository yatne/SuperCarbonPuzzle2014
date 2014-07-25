package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import help.utils.BlocksReader;
import help.utils.Constants;
import help.utils.ObjectsReader;
import map.Field;
import map.Map;
import map.Object;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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

        for (Sprite sprite : sprites) {
            sprite.draw(batch);
        }

        batch.end();
    }

    public boolean drawAnimation(Texture img, Map map, OrthographicCamera camera) {

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

        ArrayList<Sprite> wantedSprites = loadSpritesList(img, map, camera);
        boolean allSpritesReady = true;

        for (int i = 0; i < sprites.size(); i++) {
            if (!isSpriteInTheSamePlace(sprites.get(i), wantedSprites.get(i))) {
                moveSprite(sprites.get(i), wantedSprites.get(i));
                allSpritesReady = false;
            } else if (!isSpriteTheSame(sprites.get(i), wantedSprites.get(i))) {
                animateSprite(sprites.get(i), wantedSprites.get(i));
                allSpritesReady = false;
            }

        }

        if (allSpritesReady)
            createSpritesList(img, map, camera);

        for (Sprite sprite : sprites) {
            sprite.draw(batch);
        }

        batch.end();
        return allSpritesReady;
    }

    private void moveSprite(Sprite sprite, Sprite desiredSprite) {

        if (sprite.getX() < desiredSprite.getX())
            sprite.translateX(Constants.spritesMovingSpeed);
        else if (sprite.getX() > desiredSprite.getX())
            sprite.translateX(-Constants.spritesMovingSpeed);
        else if (sprite.getY() > desiredSprite.getY())
            sprite.translateY(-Constants.spritesMovingSpeed);
        else if (sprite.getY() < desiredSprite.getY())
            sprite.translateY(Constants.spritesMovingSpeed);

    }

    private void animateSprite(Sprite sprite, Sprite desiredSprite) {

        if (sprite.getTexture().equals(textureHashMap.get("BALL"))) {
            if (desiredSprite.getTexture().equals(textureHashMap.get("FINISHED"))) {

                //tutaj animacja
                cleanEndSprite(desiredSprite);

                sprite.setTexture(textureHashMap.get("FINISHED"));
            }
        }

    }

    private boolean isSpriteInTheSamePlace(Sprite spriteA, Sprite spriteB) {

        if (Math.abs(spriteA.getX() - spriteB.getX()) < Constants.spritesMovingSpeed && Math.abs(spriteA.getY() - spriteB.getY()) < Constants.spritesMovingSpeed) {
            spriteA.setPosition(spriteB.getX(), spriteB.getY());
            return true;
        } else
            return false;

    }

    private boolean isSpriteTheSame(Sprite spriteA, Sprite spriteB) {

        return (spriteA.getTexture().equals(spriteB.getTexture()));

    }

    public void createSpritesList(Texture img, Map map, OrthographicCamera camera) {

        sprites = new ArrayList<>();
        float scaleX = camera.viewportWidth / (map.getMapWidth() * img.getWidth());
        float scaleY = camera.viewportHeight / (map.getMapHeight() * img.getHeight());

        ArrayList<Object> objects = map.getObjects();
        help.utils.HelpUtils.sortById(objects);

        for (Object object : objects) {
            Texture texture = textureHashMap.get(object.getObjectsType().toString());
            Sprite sprite = new Sprite(texture);
            sprite.setPosition(object.getX() * img.getWidth() * scaleX, object.getY() * img.getWidth() * scaleY);
            sprite.setSize(img.getWidth() * scaleX, img.getWidth() * scaleY);
            sprites.add(sprite);
        }

    }

    public ArrayList<Sprite> loadSpritesList(Texture img, Map map, OrthographicCamera camera) {

        ArrayList<Sprite> spritesList = new ArrayList<>();
        float scaleX = camera.viewportWidth / (map.getMapWidth() * img.getWidth());
        float scaleY = camera.viewportHeight / (map.getMapHeight() * img.getHeight());

        ArrayList<Object> objects = map.getObjects();
        help.utils.HelpUtils.sortById(objects);

        for (Object object : objects) {
            Texture texture = textureHashMap.get(object.getObjectsType().toString());
            Sprite sprite = new Sprite(texture);
            sprite.setPosition(object.getX() * img.getWidth() * scaleX, object.getY() * img.getWidth() * scaleY);
            sprite.setSize(img.getWidth() * scaleX, img.getWidth() * scaleY);
            spritesList.add(sprite);
        }
        return spritesList;
    }

    public void cleanEndSprite(Sprite sprite) {

        for (Sprite chosenSprite : sprites) {
            if (isSpriteInTheSamePlace(chosenSprite, sprite)) {
                chosenSprite.setTexture(textureHashMap.get("NOTHING"));
            }
        }
    }
}
