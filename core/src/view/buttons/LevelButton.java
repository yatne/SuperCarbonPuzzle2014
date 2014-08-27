package view.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import player.Player;
import view.Star;
import view.Text;

import java.util.ArrayList;

public class LevelButton extends Button {

    Texture goldenStar;
    Texture grayStar;
    private ArrayList<Star> stars;
    private float starSize;

    public LevelButton(Texture texture, int worldNumber, int levelNumber, Player player, OrthographicCamera camera, BitmapFont font, Color color, Texture goldenStar, Texture grayStar) {

        super(texture);
        this.goldenStar = goldenStar;
        this.grayStar = grayStar;

        float levelButtonWidth = camera.viewportWidth / 6;
        float levelButtonHeight = 30 * levelButtonWidth / 25;
        float levelButtonSpan = (camera.viewportWidth - (4 * levelButtonWidth)) / 5;
        starSize = (float) (levelButtonWidth / 4.3);

        float posX = levelButtonSpan + (levelButtonWidth + levelButtonSpan) * (levelNumber % 4);
        float posY = (float) (camera.viewportHeight - (levelButtonHeight + 2 * levelButtonSpan + Math.floor(levelNumber / 4) * (levelButtonHeight + levelButtonSpan)));

        this.setSize(levelButtonWidth, levelButtonHeight);
        float textPosY = (float) (posY + levelButtonHeight - ((levelButtonWidth - font.getCapHeight()) / 2.2));

        this.setPosition(posX, posY);

        float textPosX = posX + (levelButtonWidth / 2) - (font.getBounds(Integer.toString(levelNumber + 1)).width / 2);
        text = new Text((int) textPosX, (int) textPosY, levelNumber + 1, color);


        int levelStars = help.utils.HelpUtils.levelStarsCount(worldNumber, levelNumber + 1);

        stars = new ArrayList<>();
        for (int j = 1; j <= levelStars; j++) {
            if (player.getStarsFromLevel(worldNumber, levelNumber + 1) >= j)
                stars.add(new Star(posX, posY, true));
            else
                stars.add(new Star(posX, posY, false));

            posX = posX + starSize;
        }

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

    public LevelButton(TextureRegion region, int worldNumber, int levelNumber, Player player, OrthographicCamera camera, BitmapFont font, Color color, Texture goldenStar, Texture grayStar) {
        super(region);

        this.goldenStar = goldenStar;
        this.grayStar = grayStar;

        float levelButtonWidth = camera.viewportWidth / 6;
        float levelButtonHeight = 30 * levelButtonWidth / 25;
        float levelButtonSpan = (camera.viewportWidth - (4 * levelButtonWidth)) / 5;
        starSize = (float) (levelButtonWidth / 4.3);

        float posX = levelButtonSpan + (levelButtonWidth + levelButtonSpan) * (levelNumber % 4);
        float posY = (float) (camera.viewportHeight - (levelButtonHeight + 2 * levelButtonSpan + Math.floor(levelNumber / 4) * (levelButtonHeight + levelButtonSpan)));

        this.setSize(levelButtonWidth, levelButtonHeight);
        float textPosY = (float) (posY + levelButtonHeight - ((levelButtonWidth - font.getCapHeight()) / 2.2));

        this.setPosition(posX, posY);

        float textPosX = posX + (levelButtonWidth / 2) - (font.getBounds(Integer.toString(levelNumber + 1)).width / 2);
        text = new Text((int) textPosX, (int) textPosY, levelNumber + 1, color);


        int levelStars = help.utils.HelpUtils.levelStarsCount(worldNumber, levelNumber + 1);

        stars = new ArrayList<>();
        for (int j = 1; j <= levelStars; j++) {
            if (player.getStarsFromLevel(worldNumber, levelNumber + 1) >= j)
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


}
