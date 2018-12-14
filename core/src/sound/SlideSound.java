package sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;

public class SlideSound {
    public static Sound slideSound;

    public static void loadSound() {
        slideSound = Gdx.audio.newSound(Gdx.files.internal("sounds/slide.wav"));
    }

}
