package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.deploy.util.Waiter;
import enums.Animations;
import map.Field;
import map.Map;

import java.util.ArrayList;
import java.util.HashMap;

public class MapView {

    public static void drawMap(SpriteBatch batch, Texture img, Map map, OrthographicCamera camera, HashMap<String, Texture> textureHashMap) {


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

                if (!field.getObject().getObjectsType().toString().equals("NONE")) {
                    batch.draw(textureHashMap.get(field.getObject().getObjectsType().toString()),
                            field.getX() * img.getWidth() * scaleX,
                            field.getY() * img.getWidth() * scaleY,
                            img.getWidth() * scaleX,
                            img.getWidth() * scaleY);

                }

            }
        }
        batch.end();

    }

    public static void drawAnimation(SpriteBatch batch, Texture empty, Map map, OrthographicCamera camera, HashMap<String, Texture> textureHashMap, ArrayList<Move> moves, int i) {
        Gdx.gl.glClearColor(1, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.end();
    }
}
