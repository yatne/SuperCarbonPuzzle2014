package view.buttons;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import help.utils.Constants;
import view.Text;

public class Button extends Image {

    Text text;
    Texture texture;
    TextureRegionDrawable textureRegionDrawable;
    TextureRegionDrawable textureRegionDrawablePressed;
    TextureRegion textureRegion;
    TextureRegion textureRegionPressed;


    public Button(float posX, float posY, Texture texture) {
        super(texture);
        this.texture = texture;
        this.setPosition(posX, posY);
        textureRegion = new TextureRegion(texture);
        textureRegionPressed = new TextureRegion(texture);
        textureRegion.setRegion(0, 0, textureRegion.getRegionWidth(), (textureRegion.getRegionHeight() / 2));
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);

        textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0, (textureRegion.getRegionHeight() / 2), textureRegion.getRegionWidth(), (textureRegion.getRegionHeight() / 2));
        textureRegionDrawablePressed = new TextureRegionDrawable(textureRegion);

        setDrawable(textureRegionDrawable);

        addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                setDrawable(textureRegionDrawablePressed);

                return true;
            }
        });
    }

    public Button(Texture texture) {

        super(texture);
        this.texture = texture;
        textureRegion = new TextureRegion(texture);
        textureRegionPressed = new TextureRegion(texture);
        textureRegion.setRegion(0, 0, textureRegion.getRegionWidth(), (textureRegion.getRegionHeight() / 2));
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);

        textureRegion = new TextureRegion(texture);
        textureRegion.setRegion(0, (textureRegion.getRegionHeight() / 2), textureRegion.getRegionWidth(), (textureRegion.getRegionHeight() / 2));
        textureRegionDrawablePressed = new TextureRegionDrawable(textureRegion);

        setDrawable(textureRegionDrawable);

        addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                setDrawable(textureRegionDrawablePressed);
                return true;
            }
        });
    }


    public Button(Texture texture, String textString, float posX, float posY, BitmapFont font, OrthographicCamera camera) {
        super(texture);
        this.texture = texture;
        this.setPosition(posX, posY);
        textureRegion = new TextureRegion(texture);
        textureRegionPressed = new TextureRegion(texture);
    }

    public Button(Texture texture, String textString, float posX, float posY, float width, float height, BitmapFont font) {
        super(texture);
        this.texture = texture;
        this.setPosition(posX, posY);
        this.setSize(width, height);
        float textPosX = ((2 * posX) + width) / 2 - font.getBounds(textString).width / 2;
        float textPosY = posY + height - ((height - font.getBounds(textString).height) / 4);

        text = new Text((int) textPosX, (int) textPosY, textString);

        textureRegion = new TextureRegion(texture);
        textureRegionPressed = new TextureRegion(texture);

        textureRegion.setRegion(0, 0, textureRegion.getRegionWidth(), (textureRegion.getRegionHeight() / 2));
        textureRegionDrawable = new TextureRegionDrawable(textureRegion);

        textureRegion.setRegion(0, (textureRegion.getRegionHeight() / 2), textureRegion.getRegionWidth(), (textureRegion.getRegionHeight() / 2));
        textureRegionDrawablePressed = new TextureRegionDrawable(textureRegion);

        setDrawable(textureRegionDrawable);

        addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                super.touchDown(event, x, y, pointer, button);
                setDrawable(textureRegionDrawablePressed);
                return true;
            }
        });
    }

    public Button(TextureRegion region) {
        super(region);
    }

    public void setButtonWorld(int buttonWorld) {


        textureRegion.setRegion(0, 0, texture.getWidth(), texture.getHeight());
        textureRegionPressed.setRegion(0, 0, texture.getWidth(), texture.getHeight());
        int buttonWidth = (textureRegion.getRegionWidth() / Constants.howManyWorlds);
        textureRegion.setRegion((buttonWorld - 1) * buttonWidth, 0, buttonWidth, textureRegion.getRegionHeight() / 2);
        textureRegionDrawable.setRegion(textureRegion);
        setDrawable(textureRegionDrawable);
        textureRegionPressed.setRegion((buttonWorld - 1) * buttonWidth, textureRegionPressed.getRegionHeight() / 2, buttonWidth, textureRegionPressed.getRegionHeight() / 2);
        textureRegionDrawablePressed.setRegion(textureRegionPressed);

    }

    @Override
    public boolean addListener(EventListener listener) {
        return super.addListener(listener);    //To change body of overridden methods use File | Settings | File Templates.
    }

    public void draw(Batch batch, float parentAlpha, BitmapFont font) {
        super.draw(batch, parentAlpha);    //To change body of overridden methods use File | Settings | File Templates.
        font.draw(batch, text.getText(), text.getPosX(), text.getPosY());
    }

    public TextureRegionDrawable getTextureRegionDrawable() {
        return textureRegionDrawable;
    }

    public void setTextureRegionDrawable(TextureRegionDrawable textureRegionDrawable) {
        this.textureRegionDrawable = textureRegionDrawable;
    }

    public TextureRegionDrawable getTextureRegionDrawablePressed() {
        return textureRegionDrawablePressed;
    }

    public void setTextureRegionDrawablePressed(TextureRegionDrawable textureRegionDrawablePressed) {
        this.textureRegionDrawablePressed = textureRegionDrawablePressed;
    }
}
