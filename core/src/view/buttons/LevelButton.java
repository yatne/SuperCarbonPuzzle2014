package view.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import view.Star;
import view.Text;

import java.util.ArrayList;

public class LevelButton extends Button {

    private Texture goldenStar;
    private Texture grayStar;
    private ArrayList<Star> stars;
    private float starSize;
    private boolean locked;

    private int level;

    public LevelButton(TextureRegion region, int levelNumber, float levelSelectWidth, float levelSelectHeight, BitmapFont font, Color color, Texture goldenStar, Texture grayStar, int level) {
        super(region);

        this.goldenStar = goldenStar;
        this.grayStar = grayStar;
        this.level = level;

        float levelButtonWidth = levelSelectWidth / 7;
        float levelButtonHeight = levelSelectHeight / 7;

        float horizontalSideSpan=levelButtonWidth/2;
        float horizontalSpan=levelButtonWidth/4;
        float verticalSideSpan=levelButtonHeight/2;
        float verticalSpan=levelButtonHeight/4;

        starSize = (float) (levelButtonWidth / 4.3);

        float posX = horizontalSideSpan + ((levelButtonWidth + horizontalSpan) * (levelNumber % 5));
        float posY = (float) (2*levelSelectHeight - levelSelectWidth - (levelButtonHeight + verticalSideSpan + Math.floor(levelNumber / 5) * (levelButtonHeight + verticalSpan)));

        this.setSize(levelButtonWidth, levelButtonHeight);
        float textPosY = (float) (posY + levelButtonHeight - ((levelButtonWidth - font.getCapHeight()) / 2.2));

        this.setPosition(posX, posY);

        float textPosX = posX + (levelButtonWidth / 2) - (font.getBounds(Integer.toString(levelNumber + 1)).width / 2);
        text = new Text((int) textPosX, (int) textPosY, levelNumber + 1, color);


        stars = new ArrayList<>();


    }

    public void setStars(int starsToObtain, int starsObtained) {

        stars.clear();
        float posX = this.getX();

        float posY = this.getY() + (this.getHeight() - this.getWidth()) / 3;

        if (starsToObtain == 1) {
            posX = posX + (this.getWidth() / 2) - (starSize / 2);
        }
        if (starsToObtain == 2) {
            posX = posX + (this.getWidth() / 2) - (starSize);
        }
        if (starsToObtain == 3) {
            posX = posX + (this.getWidth() / 2) - 3 * (starSize / 2);
        }
        if (starsToObtain == 4) {
            posX = posX + (this.getWidth() / 2) - (starSize * 2);
        }

        for (int j = 1; j <= starsToObtain; j++) {
            if (starsObtained >= j)
                stars.add(new Star(posX, posY, true));
            else
                stars.add(new Star(posX, posY, false));

            posX = posX + starSize;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha, BitmapFont font) {
        super.draw(batch, parentAlpha);    //To change body of overridden methods use File | Settings | File Templates.
        font.setColor(text.getColor());
        font.draw(batch, text.getStringNumber(), text.getPosX(), text.getPosY());
        for (Star star : this.stars) {
            if (star.isGold()) {
                batch.draw(goldenStar, star.getPosX(), star.getPosY(), starSize, starSize);
            } else {
                batch.draw(grayStar, star.getPosX(), star.getPosY(), starSize, starSize);
            }
        }
    }

    public void drawLocked(Batch batch, float parentAlpha, BitmapFont font, TextureRegionDrawable lockedTexture) {

        this.setDrawable(lockedTexture);

        super.draw(batch, parentAlpha);
        font.setColor(text.getColor());
        font.draw(batch, text.getStringNumber(), text.getPosX(), text.getPosY());
        for (Star star : this.stars) {
            if (star.isGold()) {
                batch.draw(goldenStar, star.getPosX(), star.getPosY(), starSize, starSize);
            } else {
                batch.draw(grayStar, star.getPosX(), star.getPosY(), starSize, starSize);
            }
        }
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public int getLevel() {
        return level;
    }

}
