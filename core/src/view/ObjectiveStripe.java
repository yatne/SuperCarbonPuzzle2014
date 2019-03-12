package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import help.utils.Constants;
import textures.TextureHolder;

import java.util.ArrayList;

public class ObjectiveStripe {

    ArrayList<Integer> goals;
    private float x;
    private float y;
    private float width;
    private float height;
    private BitmapFont goalsFont;
    private Text text;
    private int goal;
    private int goldenStars;
    private int firstStarPosX;
    private int firstStarPosY;
    private float starSize;

    public ObjectiveStripe(float x, float y, float width, float height, int maxFontSize) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.goals = new ArrayList<>();
        FreeTypeFontGenerator.FreeTypeFontParameter maxFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        FreeTypeFontGenerator.FreeTypeFontParameter notMaxFontParameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        maxFontParameter.size = maxFontSize;
        notMaxFontParameter.size = (int) (5 * height / 6);

        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        if ((int) (5 * height / 6) < maxFontSize) {
            goalsFont = generator.generateFont(notMaxFontParameter);
            starSize = (5 * height / 9);
        } else {
            goalsFont = generator.generateFont(maxFontParameter);
            starSize = 2 * maxFontSize / 3;
        }
        goalsFont.setColor(Color.BLACK);
        generator.dispose();

        text = new Text(0, 0, "");

    }

    public void prepareStripe(ArrayList<Integer> goals, int world) {
        this.goals.clear();
        for (Integer i : goals) {
            this.goals.add(i);
        }

        if (world == 1 || world == 2 || world == 5) {
            goalsFont.setColor(Color.BLACK);
        } else if (world == 3) {
            goalsFont.setColor(Constants.thirdWorldTextColor);
        } else if (world == 4) {
            goalsFont.setColor(Constants.fourthWorldTextColor);
        }


        float starSpan = starSize / 3;
        int starCount = goals.size() + 1;

        GlyphLayout layout = new GlyphLayout(goalsFont, "Goal: 99  ");

        int posX = (int) (width / 2 - ((layout.width + (starCount * starSize) + ((starCount - 1) * starSpan)) / 2));
        int posY = (int) (y + goalsFont.getCapHeight() + ((height - goalsFont.getCapHeight()) / 2));

        text.setPosX(posX);
        text.setPosY(posY);

        firstStarPosX = (int) (text.getPosX() + layout.width);
        firstStarPosY = (int) (y + ((height - goalsFont.getCapHeight()) / 2));

    }

    public void prepareToDraw(int movesTaken) {
        goal = 99;
        goldenStars = 0;
        for (Integer i : goals) {
            if (i < goal && i >= movesTaken)
                goal = i;
            if (movesTaken <= i)
                goldenStars++;

        }
    }

    public void draw(SpriteBatch batch) {


        float starSpan = starSize / 3;

        goalsFont.draw(batch, "goal: " + goal + "  ", text.getPosX(), text.getPosY());

        batch.draw(TextureHolder.goldMenuStar, firstStarPosX, firstStarPosY, starSize, starSize);
        for (int i = 1; i <= goals.size(); i++) {
            if (i <= goldenStars) {
                batch.draw(TextureHolder.goldMenuStar, firstStarPosX + (i * (starSize + starSpan)), firstStarPosY, starSize, starSize);
            } else {
                batch.draw(TextureHolder.grayMenuStar, firstStarPosX + (i * (starSize + starSpan)), firstStarPosY, starSize, starSize);
            }

        }
    }

}
