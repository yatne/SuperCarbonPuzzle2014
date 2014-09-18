package view.buttons;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import view.Text;

public class BasicButton extends Button {

    public BasicButton(Texture texture, String textString, float posX, float posY, BitmapFont font, OrthographicCamera camera) {
        super(posX,posY,texture);

        float width = camera.viewportWidth / 2;
        float height = camera.viewportWidth / 6;
        this.setSize(width, height);

        float textPosX = ((2 * posX) + width) / 2 - font.getBounds(textString).width / 2;
        float textPosY = posY + height - ((height - font.getBounds(textString).height) / 3);

        text = new Text((int) textPosX, (int) textPosY, textString);

    }

    @Override
    public boolean addListener(EventListener listener) {
        return super.addListener(listener);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void draw(Batch batch, float parentAlpha, BitmapFont font) {
        super.draw(batch, parentAlpha);    //To change body of overridden methods use File | Settings | File Templates.
        font.draw(batch,text.getText(),text.getPosX(),text.getPosY());
    }
}
