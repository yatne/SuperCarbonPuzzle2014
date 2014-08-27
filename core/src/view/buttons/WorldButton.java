package view.buttons;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import view.Text;

public class WorldButton extends Button {
    public WorldButton(Texture texture, int worldNumber, String worldName, OrthographicCamera camera, BitmapFont font) {

        super(texture);

        float width = camera.viewportWidth * 9 / 10;
        float height = camera.viewportWidth / 7;

        float posX = camera.viewportWidth / 20;
        float posY = camera.viewportHeight - camera.viewportHeight / 10 - (worldNumber * height * 4 / 3);

        this.setPosition(posX, posY);
        this.setSize(width, height);

        float textPosX = ((2 * posX) + width) / 2 - font.getBounds(worldName).width / 2;
        float textPosY = posY + height - ((height - font.getBounds(worldName).height) / 3);

        text = new Text((int) textPosX, (int) textPosY, worldName);
    }

    @Override
    public boolean addListener(EventListener listener) {
        return super.addListener(listener);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void draw(Batch batch, float parentAlpha, BitmapFont font) {
        super.draw(batch, parentAlpha);    //To change body of overridden methods use File | Settings | File Templates.
        font.draw(batch, text.getText(), text.getPosX(), text.getPosY());
    }
}
