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

    public void moveSprite(int frame, float scaleX, float scaleY) {

        if (frame <= dist * 10) {
            if (moveFrom.getX() < moveTo.getX()) {
                this.sprite.setPosition(sprite.getX() + (sprite.getWidth() * scaleX / 10), sprite.getY());
            } else if (moveFrom.getX() > moveTo.getX()) {
                this.sprite.setPosition(sprite.getX() - (sprite.getWidth() * scaleX / 10), sprite.getY());
            } else if (moveFrom.getY() < moveTo.getY()) {
                this.sprite.setPosition(sprite.getX(), sprite.getY() + (sprite.getHeight() * scaleY / 10));
            } else if (moveFrom.getY() > moveTo.getY()) {
                this.sprite.setPosition(sprite.getX(), sprite.getY() - (sprite.getHeight() * scaleY / 10));


            }
        }

    }

    public Sprite getSprite() {
        return sprite;
    }
}
