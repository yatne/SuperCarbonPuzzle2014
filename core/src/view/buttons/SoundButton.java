package view.buttons;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import help.utils.Constants;
import sound.SoundActivator;

public class SoundButton extends Image {

    TextureRegionDrawable onTextureRegion;
    TextureRegionDrawable offTextureRegion;

    public SoundButton(Texture onTexture, Texture offTexture) {

        super(onTexture);
        SoundActivator.loadSettings();
        this.onTextureRegion = new TextureRegionDrawable(new TextureRegion(onTexture));
        this.offTextureRegion = new TextureRegionDrawable(new TextureRegion(offTexture));

        if (Constants.soundOn) {
            setDrawable(this.onTextureRegion);
        } else {
            setDrawable(this.offTextureRegion);
        }

        addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);

                if (Constants.soundOn) {
                    setDrawable(offTextureRegion);
                    SoundActivator.setSound(false);
                } else {
                    setDrawable(onTextureRegion);
                    SoundActivator.setSound(true);
                }

            }
        });


    }
}
