package view.buttons;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import player.Player;
import view.Text;

import java.util.ArrayList;

public class LevelButton extends Button {

    private ArrayList<Image> stars;

    public LevelButton(Texture texture, int worldNumber, int levelNumber, Player player, OrthographicCamera camera, BitmapFont font, Color color) {

        super(texture);

        float levelButtonWidth = camera.viewportWidth / 6;
        float levelButtonHeight = 30 * levelButtonWidth / 25;
        float levelButtonSpan = (camera.viewportWidth - (4 * levelButtonWidth)) / 5;
        float starsSize = (float) (levelButtonWidth / 4.3);

        float posX = levelButtonSpan + (levelButtonWidth + levelButtonSpan) * (levelNumber % 4);
        float posY = (float) (camera.viewportHeight - (levelButtonHeight + 2 * levelButtonSpan + Math.floor(levelNumber / 4) * (levelButtonHeight + levelButtonSpan)));

        this.setSize(levelButtonWidth, levelButtonHeight);
        float textPosY = (float) (posY + levelButtonHeight - ((levelButtonWidth - font.getCapHeight()) / 2.2));

        this.setPosition(posX, posY);

        float textPosX = posX + (levelButtonWidth / 2) - (font.getBounds(Integer.toString(levelNumber + 1)).width / 2);
        text = new Text((int) textPosX, (int) textPosY, levelNumber + 1, color);


        int levelStars = help.utils.HelpUtils.levelStarsCount(worldNumber, levelNumber + 1);
        posY = posY + (levelButtonHeight - levelButtonWidth) / 3;

        if (levelStars == 1) {
            posX = posX + (levelButtonWidth / 2) - (starsSize / 2);
        }
        if (levelStars == 2) {
            posX = posX + (levelButtonWidth / 2) - (starsSize);
        }
        if (levelStars == 3) {
            posX = posX + (levelButtonWidth / 2) - 3 * (starsSize / 2);
        }
        if (levelStars == 4) {
            posX = posX + (levelButtonWidth / 2) - (starsSize * 2);
        }
        stars = new ArrayList<>();
        for (int j = 1; j <= levelStars; j++) {
            Image star;
            if (player.getStarsFromLevel(worldNumber, levelNumber + 1) >= j)
                star = new Image(new Texture("menus/star_golden.png"));
            else
                star = new Image(new Texture("menus/star_gray.png"));
            star.setPosition((int) posX, (int) posY);
            star.setSize((int) starsSize, (int) starsSize);
            stars.add(star);
            posX = posX + starsSize;
        }

    }

    @Override
    public void draw(Batch batch, float parentAlpha, BitmapFont font) {
        super.draw(batch, parentAlpha);    //To change body of overridden methods use File | Settings | File Templates.
        font.setColor(text.getColor());
        font.draw(batch, text.getStringNumber(), text.getPosX(), text.getPosY());
        for (Image star : this.stars) {
            star.draw(batch, 1);

        }
    }

}
