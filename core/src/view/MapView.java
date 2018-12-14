package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import controllers.GestureController;
import enums.Controls;
import enums.Coordinates;
import enums.Sounds;
import help.utils.BlocksReader;
import help.utils.Constants;
import help.utils.ObjectsReader;
import map.Field;
import map.Map;
import map.Object;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import sound.SoundActivator;
import textures.TextureHolder;
import view.buttons.Button;

import java.util.ArrayList;
import java.util.HashMap;

public class MapView {

    private InputMultiplexer inputMultiplexer;
    private GestureDetector gestureDetector;
    private HashMap<String, Texture> textureHashMap;
    private HashMap<Sounds, Sound> soundHashMap;
    private ArrayList<Sprite> sprites;
    private HashMap<Sprite, Integer> spriteStates;
    private HashMap<Sprite, Coordinates> portalStates;
    private GestureController gestureController;
    private TextureRegionDrawable topPanelRegion;
    private TextureRegionDrawable bottomPanelRegion;
    private Button reset;
    private Button menu;
    private BitmapFont counterFont;
    private BitmapFont buttonFont;
    private Controls control;
    private int watchDog;
    private ArrayList<TextureRegion> backgrounds;
    private ObjectiveStripe objectiveStripe;
    //----------------------------------

    public MapView(OrthographicCamera camera) {


        watchDog = 0;
        gestureController = new GestureController();
        gestureDetector = new GestureDetector(gestureController);

        createFonts(camera);
        NodeList blocksList = BlocksReader.getBlocksList();
        NodeList objectsList = ObjectsReader.getObjectsList();

        backgrounds = new ArrayList<>();
        for (int i = 0; i < Constants.howManyWorlds; i++) {
            backgrounds.add(new TextureRegion(new Texture("menus/background" + (i + 1) + ".png")));

        }

        textureHashMap = new HashMap<>();
        for (int i = 0; i < blocksList.getLength(); i++) {
            Element block = (Element) blocksList.item(i);
            textureHashMap.put(
                    block.getAttribute("enum"),
                    new Texture("classic/" + block.getAttribute("texture"))
            );
        }
        for (int i = 0; i < objectsList.getLength(); i++) {
            Element object = (Element) objectsList.item(i);
            textureHashMap.put(
                    object.getAttribute("enum"),
                    new Texture(Constants.skin + "/" + object.getAttribute("texture"))
            );
        }


        soundHashMap = SoundActivator.makeSoundHashMap();

        inputMultiplexer = new InputMultiplexer();

        float panelsHeight = (camera.viewportHeight - camera.viewportWidth) / 2;

        reset = new Button(TextureHolder.buttonsTexture, "Reset", (1 * camera.viewportWidth) / 10, panelsHeight / 5, (3 * camera.viewportWidth) / 10, (3 * panelsHeight) / 5, buttonFont);
        menu = new Button(TextureHolder.buttonsTexture, "Menu", (6 * camera.viewportWidth) / 10, panelsHeight / 5, (3 * camera.viewportWidth) / 10, (3 * panelsHeight) / 5, buttonFont);


        reset.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                control = Controls.RESET;
                reset.setDrawable(reset.getTextureRegionDrawable());
            }
        });


        menu.addListener(new ClickListener() {
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                control = Controls.MENU;
                menu.setDrawable(menu.getTextureRegionDrawable());
            }
        });


        sprites = new ArrayList<>();
        spriteStates = new HashMap<>();
        portalStates = new HashMap<>();

        topPanelRegion = new TextureRegionDrawable();
        bottomPanelRegion = new TextureRegionDrawable();
        objectiveStripe = new ObjectiveStripe(0, camera.viewportHeight - panelsHeight, camera.viewportWidth, panelsHeight - Constants.adHeight, (int) ((camera.viewportHeight - camera.viewportWidth) / 5));

        control = Controls.NONE;


    }

    public void prepareMapUI(OrthographicCamera camera, Map map, Stage stage) {

        control = Controls.NONE;
        inputMultiplexer.clear();
        inputMultiplexer.addProcessor(gestureDetector);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        stage.clear();

        if (map.getMapWorld() == 1 || map.getMapWorld() == 2 || map.getMapWorld() == 5) {
            counterFont.setColor(Color.BLACK);
        } else if (map.getMapWorld() == 3) {
            counterFont.setColor(Constants.thirdWorldTextColor);
        } else if (map.getMapWorld() == 4) {
            counterFont.setColor(Constants.fourthWorldTextColor);
        }

        stage.addActor(reset);
        stage.addActor(menu);

        menu.setButtonWorld(map.getMapWorld());
        reset.setButtonWorld(map.getMapWorld());


        float panelsHeight = (camera.viewportHeight - camera.viewportWidth) / 2;

        backgrounds.get(map.getMapWorld() - 1).setRegion(0, 0, 450, 200);
        topPanelRegion.setRegion(backgrounds.get(map.getMapWorld() - 1));
        objectiveStripe.prepareStripe(map.getGoals(), map.getMapWorld());
        objectiveStripe.prepareToDraw(map.getMovesTaken());

        backgrounds.get(map.getMapWorld() - 1).setRegion(0, 300, 450, 200);
        bottomPanelRegion.setRegion(backgrounds.get(map.getMapWorld() - 1));


        gestureController.setGestureField(0, camera.viewportWidth, panelsHeight, panelsHeight + camera.viewportWidth);

    }

    public void prepareMap(OrthographicCamera camera, Map map) {

        objectiveStripe.prepareToDraw(map.getMovesTaken());
        createSpritesList(map, camera);
        control = Controls.NONE;

    }

    public void drawMap(Map map, OrthographicCamera camera, SpriteBatch batch) {

        drawStaticMap(map, camera, batch);
        batch.begin();

        for (Sprite sprite : sprites) {
            sprite.draw(batch);
        }

        for (Sprite sprite : sprites) {
            if (sprite.getTexture().equals(textureHashMap.get("DESTROYER")) || sprite.getTexture().equals(textureHashMap.get("BALL"))
                    || sprite.getTexture().equals(textureHashMap.get("BALL2")) || sprite.getTexture().equals(textureHashMap.get("BOX"))
                    || sprite.getTexture().equals(textureHashMap.get("RAILED")))

                sprite.draw(batch);
        }
        batch.end();
        drawUI(map, camera, batch);

    }

    public void prepareAnimation(Map map) {
        objectiveStripe.prepareToDraw(map.getMovesTaken());
        gestureController.setGestureField(0, 0, 0, 0);
        if (Constants.soundOn)
            soundHashMap.get(Sounds.SLIDE).play();
    }

    public void afterAnimation(OrthographicCamera camera) {
        float panelsHeight = (camera.viewportHeight - camera.viewportWidth) / 2;
        gestureController.setGestureField(0, camera.viewportWidth, panelsHeight, panelsHeight + camera.viewportWidth);
    }

    public boolean drawAnimation(Map map, OrthographicCamera camera, SpriteBatch batch) {

        watchDog++;
        drawStaticMap(map, camera, batch);

        batch.begin();

        ArrayList<Sprite> wantedSprites = loadSpritesList(map, camera);

        boolean allSpritesReady = true;

        for (int i = 0; i < sprites.size(); i++) {

            if (!isSpriteTheSame(sprites.get(i), wantedSprites.get(i))) {
                animateSprite(sprites.get(i), wantedSprites.get(i));
                allSpritesReady = false;
            }

            if (!isSpriteInTheSamePlace(sprites.get(i), wantedSprites.get(i)) || !(portalStates.get(sprites.get(i)) == Coordinates.NONE)) {
                if (portalStates.get(sprites.get(i)) != Coordinates.NONE)
                    portalMoveSprite(sprites.get(i), camera);
                else
                    moveSprite(sprites.get(i), wantedSprites.get(i));
                allSpritesReady = false;
            } else if (!isSpriteTheSame(sprites.get(i), wantedSprites.get(i))) {
                animateSprite(sprites.get(i), wantedSprites.get(i));
                allSpritesReady = false;
            }
        }

        if (allSpritesReady)
            createSpritesList(map, camera);

        for (Sprite sprite : sprites) {
            sprite.draw(batch);
        }

        for (Sprite sprite : sprites) {
            if (sprite.getTexture().equals(textureHashMap.get("DESTROYER")) || sprite.getTexture().equals(textureHashMap.get("BALL"))
                    || sprite.getTexture().equals(textureHashMap.get("BALL2")) || sprite.getTexture().equals(textureHashMap.get("BOX"))
                    || sprite.getTexture().equals(textureHashMap.get("RAILED")))
                sprite.draw(batch);
        }


        batch.end();
        drawUI(map, camera, batch);

        if (watchDog > 45) {
            createSpritesList(map, camera);
            allSpritesReady = true;
        }

        if (allSpritesReady) {
            watchDog = 0;

        }

        return allSpritesReady;
    }

    private void moveSprite(Sprite sprite, Sprite desiredSprite) {

        if (sprite.getX() < desiredSprite.getX())
            sprite.translateX(Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime());
        else if (sprite.getX() > desiredSprite.getX())
            sprite.translateX(-Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime());
        else if (sprite.getY() > desiredSprite.getY())
            sprite.translateY(-Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime());
        else if (sprite.getY() < desiredSprite.getY())
            sprite.translateY(Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime());


    }

    private void portalMoveSprite(Sprite sprite, OrthographicCamera camera) {                               //todo
        if (portalStates.get(sprite) == Coordinates.RIGHT) {
            sprite.translateX(Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime());
            if (sprite.getX() > camera.viewportWidth) {
                sprite.setPosition(-sprite.getWidth(), sprite.getY());
                portalStates.put(sprite, Coordinates.NONE);
            }
        } else if (portalStates.get(sprite) == Coordinates.LEFT) {
            sprite.translateX(-Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime());
            if (sprite.getX() < -sprite.getWidth()) {
                sprite.setPosition(camera.viewportWidth, sprite.getY());
                portalStates.put(sprite, Coordinates.NONE);
            }
        } else if (portalStates.get(sprite) == Coordinates.UP) {
            float panelHeight = (camera.viewportHeight - camera.viewportWidth) / 2;
            sprite.translateY(Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime());
            if (sprite.getY() > panelHeight + camera.viewportWidth) {
                sprite.setPosition(sprite.getX(), panelHeight - sprite.getHeight());
                portalStates.put(sprite, Coordinates.NONE);
            }
        } else if (portalStates.get(sprite) == Coordinates.DOWN) {
            float panelHeight = (camera.viewportHeight - camera.viewportWidth) / 2;
            sprite.translateY(-Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime());
            if (sprite.getY() < panelHeight - sprite.getHeight()) {
                portalStates.put(sprite, Coordinates.NONE);
                sprite.setPosition(sprite.getX(), panelHeight + camera.viewportWidth);
            }
        }
    }

    private void animateSprite(Sprite sprite, Sprite desiredSprite) {

        if (sprite.getTexture().equals(textureHashMap.get("BALL")) || spriteStates.get(sprite) != 0)
            if (desiredSprite.getTexture().equals(textureHashMap.get("FINISHED"))) {
                if (checkSpriteInThatPlace(sprite, textureHashMap.get("END"))) {

                    sprite.setTexture(textureHashMap.get("FINISHED"));
                    if (Constants.soundOn) {
                        soundHashMap.get(Sounds.END).play();
                    }
                }

            }
        if (sprite.getTexture().equals(textureHashMap.get("BALL2")) || spriteStates.get(sprite) != 0)
            if (desiredSprite.getTexture().equals(textureHashMap.get("FINISHED2"))) {

                if (checkOtherSpriteInThatPlace(sprite)) {
                    sprite.setTexture(textureHashMap.get("FINISHED2"));
                    if (Constants.soundOn) {

                        soundHashMap.get(Sounds.END).play();
                    }
                }

            }
        if (sprite.getTexture().equals(textureHashMap.get("END")) || sprite.getTexture().equals(textureHashMap.get("END2"))) {
            if (desiredSprite.getTexture().equals(textureHashMap.get("ENDBALL"))) {
                if (checkSpriteInThatPlace(sprite, textureHashMap.get("FINISHED")) || checkSpriteInThatPlace(sprite, textureHashMap.get("FINISHED2"))
                        )
                    sprite.setTexture(desiredSprite.getTexture());
            }
        }

        if (desiredSprite.getTexture().equals(textureHashMap.get("NOTHING"))) {
            if (checkOtherSpriteInThatPlace(sprite))
                sprite.setTexture(textureHashMap.get("NOTHING"));
        }


        if (desiredSprite.getTexture().equals(textureHashMap.get("TRAPA"))) {
            if (checkOtherSpriteInThatPlace(sprite)) {
                sprite.setTexture(textureHashMap.get("TRAPA"));
                if (Constants.soundOn)
                    soundHashMap.get(Sounds.RED).play();
            }
        }

        if (desiredSprite.getTexture().equals(textureHashMap.get("KILLED"))) {
            if (isSpriteInTheSamePlace(sprite, desiredSprite))
                sprite.setTexture(textureHashMap.get("KILLED"));
        }

        if (sprite.getTexture().equals(textureHashMap.get("GHOSTLY")) || spriteStates.get(sprite) != 0)
            if (desiredSprite.getTexture().equals(textureHashMap.get("CREATED"))) {
                if (checkOtherSpriteInThatPlace(sprite)) {
                    sprite.setTexture(desiredSprite.getTexture());
                    if (Constants.soundOn)
                        soundHashMap.get(Sounds.GREEN).play();
                }
            }

        if (sprite.getTexture().equals(textureHashMap.get("TBD")) || spriteStates.get(sprite) != 0)
            if (desiredSprite.getTexture().equals(textureHashMap.get("DESTROYED"))) {
                if (checkOtherSpriteInThatPlace(sprite)) {
                    sprite.setTexture(desiredSprite.getTexture());
                    if (Constants.soundOn)
                        soundHashMap.get(Sounds.DESTROY).play();
                }
            }

        if (sprite.getTexture().equals(textureHashMap.get("GHOSTLY")) || spriteStates.get(sprite) != 0)
            if (desiredSprite.getTexture().equals(textureHashMap.get("GWB"))) {
                if (checkOtherSpriteInThatPlace(sprite))
                    sprite.setTexture(desiredSprite.getTexture());
            }

        if (sprite.getTexture().equals(textureHashMap.get("GWB")) || spriteStates.get(sprite) != 0)
            if (desiredSprite.getTexture().equals(textureHashMap.get("CREATED"))) {
                if (!checkOtherSpriteInThatPlace(sprite)) {
                    sprite.setTexture(desiredSprite.getTexture());
                    if (Constants.soundOn)
                        soundHashMap.get(Sounds.GREEN).play();
                }
            }

        if (sprite.getTexture().equals(textureHashMap.get("GWRB")) || spriteStates.get(sprite) != 0)
            if (desiredSprite.getTexture().equals(textureHashMap.get("CREATED"))) {
                if (!checkOtherSpriteInThatPlace(sprite))
                    sprite.setTexture(desiredSprite.getTexture());
            }
    }

    private boolean isSpriteInTheSamePlace(Sprite spriteA, Sprite spriteB) {

        if (Math.abs(spriteA.getX() - spriteB.getX()) < Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime() && Math.abs(spriteA.getY() - spriteB.getY()) < Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime()) {
            spriteA.setPosition(spriteB.getX(), spriteB.getY());
            return true;
        } else
            return false;

    }

    private boolean isSpriteTheSame(Sprite spriteA, Sprite spriteB) {

        return (spriteA.getTexture().equals(spriteB.getTexture()));

    }

    public void createSpritesList(Map map, OrthographicCamera camera) {

        sprites.clear();
        spriteStates.clear();
        portalStates.clear();

        int width = textureHashMap.get("EMPTY").getWidth();

        float scaleX = camera.viewportWidth / (map.getMapWidth() * width);
        float scaleY = camera.viewportWidth / (map.getMapHeight() * width);
        float offset = camera.viewportHeight - camera.viewportWidth;

        ArrayList<Object> objects = map.getObjects();
        help.utils.HelpUtils.sortById(objects);

        for (Object object : objects) {

            String objectEnum = object.getObjectsType().toString();

            Texture texture = textureHashMap.get(objectEnum);
            Sprite sprite = new Sprite(texture);
            sprite.setPosition(object.getX() * width * scaleX, object.getY() * width * scaleY + (offset / 2));
            sprite.setSize(width * scaleX, width * scaleY);
            sprites.add(sprite);
            spriteStates.put(sprite, 0);
            portalStates.put(sprite, Coordinates.NONE);


        }

    }

    public ArrayList<Sprite> loadSpritesList(Map map, OrthographicCamera camera) {

        ArrayList<Sprite> spritesList = new ArrayList<>();

        Texture img = textureHashMap.get("EMPTY");

        float scaleX = camera.viewportWidth / (map.getMapWidth() * img.getWidth());
        float scaleY = camera.viewportWidth / (map.getMapHeight() * img.getHeight());
        float offset = camera.viewportHeight - camera.viewportWidth;


        ArrayList<Object> objects = map.getObjects();
        help.utils.HelpUtils.sortById(objects);

        for (Object object : objects) {
            Texture texture = textureHashMap.get(object.getObjectsType().toString());
            Sprite sprite = new Sprite(texture);
            sprite.setPosition(object.getX() * img.getWidth() * scaleX, object.getY() * img.getWidth() * scaleY + (offset / 2));
            sprite.setSize(img.getWidth() * scaleX, img.getWidth() * scaleY);
            spritesList.add(sprite);
        }
        return spritesList;
    }

    public void checkForPortalMoves(Map map) {

        ArrayList<Object> objects = map.getObjects();
        help.utils.HelpUtils.sortById(objects);
        for (int j = 0; j < sprites.size(); j++) {
            if (objects.get(j).isGoneThroughTele() != Coordinates.NONE)
                portalStates.put(sprites.get(j), objects.get(j).isGoneThroughTele());                            //todo
        }

    }

    public void cleanEndSprite(Sprite sprite) {

        for (Sprite chosenSprite : sprites) {
            if (isSpriteInTheSamePlace(chosenSprite, sprite)) {
                if (sprite.getTexture().equals(textureHashMap.get("END")) || sprite.getTexture().equals(textureHashMap.get("END2")))
                    chosenSprite.setTexture(textureHashMap.get("NOTHING"));
            }
        }
    }

    public boolean checkOtherSpriteInThatPlace(Sprite sprite) {
        boolean isThere = false;
        for (Sprite chosenSprite : sprites) {
            if (isSpriteInTheSamePlace(chosenSprite, sprite)) {
                if (!chosenSprite.equals(sprite))
                    isThere = true;
            }
        }
        return isThere;
    }

    public boolean checkSpriteInThatPlace(Sprite sprite, Texture texture) {
        boolean isThere = false;
        for (Sprite chosenSprite : sprites) {
            if (isSpriteInTheSamePlace(chosenSprite, sprite)) {
                if (!chosenSprite.equals(sprite) && chosenSprite.getTexture().equals(texture))
                    isThere = true;
            }
        }
        return isThere;
    }

    public int getIndexOfOtherSpriteInThatPlace(Sprite sprite) {

        for (int i = 0; i < sprites.size(); i++) {
            if (isSpriteInTheSamePlace(sprites.get(i), sprite)) {
                if (!sprites.get(i).equals(sprite)) {
                    return i;
                }
            }
        }
        return -1;
    }

    public void switchDoors() {

        for (Sprite sprite : sprites) {
            if (sprite.getTexture().equals(textureHashMap.get("DOORO"))) {
                sprite.setTexture(textureHashMap.get("DOORC"));
            } else if (sprite.getTexture().equals(textureHashMap.get("DOORC"))) {
                sprite.setTexture(textureHashMap.get("DOORO"));
            }
        }

    }

    private ArrayList<Sprite> loadSwitches(ArrayList<Sprite> spritesList) {
        ArrayList<Sprite> switches = new ArrayList<>();
        for (Sprite sprite : spritesList) {
            if (sprite.getTexture().equals(textureHashMap.get("SWITCH")))
                switches.add(sprite);
        }
        return switches;
    }

    private void drawStaticMap(Map map, OrthographicCamera camera, SpriteBatch batch) {

        batch.begin();

        Texture img = textureHashMap.get("EMPTY");

        float scaleX = camera.viewportWidth / (map.getMapWidth() * img.getWidth());
        float scaleY = camera.viewportWidth / (map.getMapHeight() * img.getHeight());
        float offset = camera.viewportHeight - camera.viewportWidth;


        for (ArrayList<Field> row : map.getFields()) {
            for (Field field : row) {

                batch.draw(textureHashMap.get(field.getFieldType().toString()),
                        field.getX() * img.getWidth() * scaleX,
                        field.getY() * img.getWidth() * scaleY + (offset / 2),
                        img.getWidth() * scaleX,
                        img.getWidth() * scaleY);
            }
        }


        batch.end();
    }

    public void drawUI(Map map, OrthographicCamera camera, SpriteBatch batch) {
        batch.begin();
        if (map.getMapWorld() == 5) {
            buttonFont.setColor(Constants.fifthWorldButtonColor);
        }

        float panelsHeight = (camera.viewportHeight - camera.viewportWidth) / 2;
        topPanelRegion.draw(batch, 0, camera.viewportHeight - panelsHeight, camera.viewportWidth, panelsHeight);
        bottomPanelRegion.draw(batch, 0, 0, camera.viewportWidth, panelsHeight);
        objectiveStripe.draw(batch);
        menu.draw(batch, 1, buttonFont);
        reset.draw(batch, 1, buttonFont);

        String moves;
        if (map.getMovesTaken() <= 99) {
            moves = Integer.toString(map.getMovesTaken());
        } else
            moves = "99";

        counterFont.draw(batch, moves, (camera.viewportWidth / 2) - (counterFont.getBounds(moves).width / 2), (camera.viewportHeight - camera.viewportWidth) * 7 / 20);

        //levelNameFont.draw(batch, levelName.getText(), levelName.getPosX(), levelName.getPosY());

        buttonFont.setColor(Color.BLACK);
        batch.end();
    }

    public void createFonts(OrthographicCamera camera) {
        FileHandle fontFile = Gdx.files.internal("menufont.ttf");
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(fontFile);
        counterFont = generator.generateFont((int) ((camera.viewportHeight - camera.viewportWidth) / 4));
        buttonFont = generator.generateFont((int) ((camera.viewportHeight - camera.viewportWidth) / 5));
        counterFont.setColor(Color.BLACK);
        buttonFont.setColor(Color.BLACK);
        generator.dispose();


    }

    public Controls getControl() {
        if (control != Controls.NONE) {
            return control;
        } else {
            return gestureController.getControl();
        }

    }

}
