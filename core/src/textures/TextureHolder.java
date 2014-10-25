package textures;

import com.badlogic.gdx.graphics.Texture;

public class TextureHolder {

    public static Texture buttonsTexture;
    public static Texture lockedButton;
    public static Texture levelButtonsTexture;
    public static Texture lockedLevelTexture;
    public static Texture goldenStar;
    public static Texture grayStar;
    public static Texture alertTexture;

    public static void loadTextures() {
        buttonsTexture = new Texture("menus/mainbuttons.png");
        lockedButton = new Texture("menus/locked.png");
        levelButtonsTexture = new Texture("menus/levelbuttons.png");
        goldenStar = new Texture("menus/star_golden.png");
        grayStar = new Texture("menus/star_gray.png");
        alertTexture = new Texture("menus/alert.png");
    }
}

