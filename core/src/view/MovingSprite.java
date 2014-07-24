package view;

import com.badlogic.gdx.graphics.g2d.Sprite;

import java.awt.*;

public class MovingSprite {

    private Sprite sprite;
    private Point moveFrom;
    private Point moveTo;
    private double dist;

    public MovingSprite(Sprite sprite, Point moveFrom, Point moveTo) {
        this.sprite = new Sprite(sprite);
        this.moveFrom = moveFrom;
        this.moveTo = moveTo;
        dist = Math.abs(moveFrom.getX() - moveTo.getX()) + Math.abs(moveFrom.getY() - moveTo.getY());
    }

    public void moveSprite(int frame, int animationLength, float scaleX, float scaleY) {

        if (frame >= (animationLength - dist) * ViewConstants.FRAMES_PER_MOVE) {
            if (moveFrom.getX() < moveTo.getX()) {
                this.sprite.setPosition(sprite.getX() + (sprite.getWidth() * scaleX / (ViewConstants.FRAMES_PER_MOVE-2)), sprite.getY());
            } else if (moveFrom.getX() > moveTo.getX()) {
                this.sprite.setPosition(sprite.getX() - (sprite.getWidth() * scaleX / (ViewConstants.FRAMES_PER_MOVE-2) ), sprite.getY());
            } else if (moveFrom.getY() < moveTo.getY()) {
                this.sprite.setPosition(sprite.getX(), sprite.getY() + (sprite.getHeight() * scaleY / (ViewConstants.FRAMES_PER_MOVE-2)));
            } else if (moveFrom.getY() > moveTo.getY()) {
                this.sprite.setPosition(sprite.getX(), sprite.getY() - (sprite.getHeight() * scaleY / (ViewConstants.FRAMES_PER_MOVE-2)));


            }
        }

    }

    public Sprite getSprite() {
        return sprite;
    }
}
