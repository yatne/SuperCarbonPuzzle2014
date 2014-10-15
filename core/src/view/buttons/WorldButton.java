package view.buttons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import view.Text;

public class WorldButton extends Button {

    private boolean isLocked;
    private int worldNumber;
    private Texture starTexture;
    private Texture lockTexture;
    private Sprite star;


    public WorldButton(Texture texture, int worldNumber, String worldName, float levelSelectWidth, float levelSelectHeight, BitmapFont font) {

        super(texture);

        this.worldNumber = worldNumber;
        float width = levelSelectWidth * 6 / 7;
        float height = levelSelectHeight / 7;

        float verticalSideSpan = height / 2;
        float verticalSpan = height / 4;

        float posX = levelSelectWidth / 14;
        float posY = (2 * levelSelectHeight - levelSelectWidth - (height + verticalSideSpan + ((worldNumber - 1) * (height + verticalSpan))));

        this.setPosition(posX, posY);
        this.setSize(width, height);

        starTexture = new Texture("menus/star_golden.png");
        lockTexture = new Texture("menus/lock.png");

        star = new Sprite(starTexture);
        star.setSize(height / 3, height / 3);

        float textPosX = ((2 * posX) + width) / 2 - ((font.getBounds(worldName + " ").width + star.getWidth()) / 2);
        float textPosY = posY + font.getBounds(worldName).height + ((height - font.getBounds(worldName).height) / 2);

        float starPosX = textPosX + font.getBounds(worldName + " ").width;
        float starPosY = posY + ((height - star.getHeight()) / 2);

        text = new Text((int) textPosX, (int) textPosY, worldName);
        star.setPosition(starPosX, starPosY);
    }

    public void adjustStar(BitmapFont font, String worldName, float levelSelectWidth) {

        float width = levelSelectWidth * 6 / 7;
        float posX = levelSelectWidth / 14;
        float textPosX = ((2 * posX) + width) / 2 - ((font.getBounds(worldName + " ").width + star.getWidth()) / 2);
        float starPosX = textPosX + font.getBounds(worldName + " ").width;
        star.setPosition(starPosX, star.getY());

    }

    public void setText(String text) {
        this.text.setText(text);
    }

    @Override
    public boolean addListener(EventListener listener) {
        return super.addListener(listener);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    public void draw(Batch batch, float parentAlpha, BitmapFont font) {
        super.draw(batch, parentAlpha);    //To change body of overridden methods use File | Settings | File Templates.
        font.draw(batch, text.getText(), text.getPosX(), text.getPosY());
        if (isLocked) {
            star.setTexture(lockTexture);
        } else {
            star.setTexture(starTexture);
        }
        star.draw(batch);
    }

    public void drawWithAltText(Batch batch, float parentAlpha, BitmapFont font, String string, float width) {
        super.draw(batch, parentAlpha);    //To change body of overridden methods use File | Settings | File Templates.
        font.draw(batch, string, text.getPosX(), text.getPosY());
        if (isLocked) {
            star.setTexture(lockTexture);
            star.setX((width / 2) + (font.getBounds(string + "  ").width / 2));
        } else {
            star.setTexture(starTexture);
        }
        star.draw(batch);
    }

    public boolean isLocked() {
        return isLocked;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }

    public int getWorldNumber() {
        return worldNumber;
    }
}
