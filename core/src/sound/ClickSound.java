package sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class ClickSound {

    public static Sound clickSound;

    public static void loadSound() {
        clickSound = Gdx.audio.newSound(Gdx.files.internal("sounds/button.wav"));
    }
}
