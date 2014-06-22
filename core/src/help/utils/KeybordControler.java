package help.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import enums.Controls;

import static enums.Controls.*;

public class KeybordControler {

    boolean leftPressed;
    boolean rightPressed;
    boolean upPressed;
    boolean downPressed;
    boolean backspacePressed;

    public KeybordControler() {
        leftPressed = false;
        rightPressed = false;
        upPressed = false;
        downPressed = false;
        backspacePressed = false;
    }

    public Controls checkForControl() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (!leftPressed) {
                leftPressed = true;
                return LEFT;
            }
        } else
            leftPressed = false;

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (!rightPressed) {
                rightPressed = true;
                return RIGHT;
            }
        } else
            rightPressed = false;

        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if (!upPressed) {
                upPressed = true;
                return UP;
            }
        } else
            upPressed = false;

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if (!downPressed) {
                downPressed = true;
                return DOWN;
            }
        } else
            downPressed = false;

        if (Gdx.input.isKeyPressed(Input.Keys.BACKSPACE)) {
            if (!backspacePressed) {
                backspacePressed = true;
                return RESET;
            }
        } else
            backspacePressed = false;

        return NONE;


    }
}
