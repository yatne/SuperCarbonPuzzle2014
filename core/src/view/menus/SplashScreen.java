package view.menus;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SplashScreen {


    private Texture carbonTexture;
    private Texture goldStar;
    private Texture grayStar;

    public SplashScreen() {
        carbonTexture = new Texture("menus/carbon.png");
        goldStar = new Texture("menus/menustargold.png");
        grayStar = new Texture("menus/menustargray.png");

    }

    public void draw(SpriteBatch batch, OrthographicCamera camera, int state) {

        batch.begin();
        batch.draw(carbonTexture, 0, 0, camera.viewportWidth, camera.viewportHeight);
        float starSize = camera.viewportHeight / 8;
        float starSpan = starSize / 3;
        float posX = camera.viewportWidth / 2 - starSize / 2 - starSize - starSpan;
        float posY = 0.53f * camera.viewportWidth;

        for (int i = 1; i <= 3; i++) {
            if (state < i)
                batch.draw(grayStar, posX, posY, starSize, starSize);
            else
                batch.draw(goldStar, posX, posY, starSize, starSize);
            posX = posX + starSize + starSpan;
        }
        batch.end();
    }
}


