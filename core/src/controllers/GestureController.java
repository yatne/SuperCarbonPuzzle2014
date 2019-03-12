package controllers;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import enums.Controls;

public class GestureController implements GestureDetector.GestureListener {
    private float startX;
    private float endX;
    private float startY;
    private float endY;
    private Controls control;
    private boolean controlTaken;
    private int horizontal;
    private int vertical;

    public void setGestureField(float startX, float endX, float startY, float endY) {
        this.startX = startX;
        this.endX = endX;
        this.startY = startY;
        this.endY = endY;
        control = Controls.NONE;
        horizontal = 0;
        vertical = 0;
    }

    public Controls getControl() {
        if (!controlTaken) {
            controlTaken = true;
            return control;
        } else return Controls.NONE;
    }

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {

        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {

        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {

        if (x > startX && x < endX && y > startY && y < endY) {
            if (Math.abs(deltaX) > Math.abs(deltaY)) {
                if (deltaX < 0)
                    horizontal--;
                else
                    horizontal++;
            } else {
                if (deltaY < 0)
                    vertical++;
                else
                    vertical--;
            }

        } else
            control = Controls.NONE;
        return false;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {

        if (Math.abs(horizontal) > Math.abs(vertical)) {
            if (horizontal < 0)
                control = Controls.LEFT;
            else
                control = Controls.RIGHT;
        } else {
            if (vertical < 0)
                control = Controls.DOWN;
            else
                control = Controls.UP;
        }

        vertical=0;
        horizontal=0;

        controlTaken = false;
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }

}