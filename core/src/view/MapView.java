package view;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import controllers.GestureController;
import enums.Controls;
import help.utils.BlocksReader;
import help.utils.Constants;
import help.utils.MapsReader;
import help.utils.ObjectsReader;
import map.Field;
import map.Map;
import map.Object;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import view.buttons.Button;

import java.util.ArrayList;
import java.util.HashMap;

public class MapView {

    private InputMultiplexer inputMultiplexer;
    private GestureDetector gestureDetector;
    private HashMap<String, Texture> textureHashMap;
    private ArrayList<Sprite> sprites;
    private HashMap<Sprite, Integer> spriteStates;
    private HashMap<Sprite, Integer> portalStates;
    private GestureController gestureController;
    private Image topPanel;
    private Image bottomPanel;
    private Button reset;
    private Button menu;
    private BitmapFont counterFont;
    private BitmapFont buttonFont;
    private Controls control;
    private Text levelName;
    private int watchDog;
    private int switchTimer;

    public MapView(OrthographicCamera camera) {

        switchTimer = 0;
        watchDog = 0;
        gestureController = new GestureController();
        gestureDetector = new GestureDetector(gestureController);

        createFonts(camera);
        NodeList blocksList = BlocksReader.getBlocksList();
        NodeList objectsList = ObjectsReader.getObjectsList();

        textureHashMap = new HashMap<>();
        for (int i = 0; i < blocksList.getLength(); i++) {
            Element block = (Element) blocksList.item(i);
            textureHashMap.put(
                    block.getAttribute("enum"),
                    new Texture(Constants.skin + "/" + block.getAttribute("texture"))
            );
        }
        for (int i = 0; i < objectsList.getLength(); i++) {
            Element object = (Element) objectsList.item(i);
            textureHashMap.put(
                    object.getAttribute("enum"),
                    new Texture(Constants.skin + "/" + object.getAttribute("texture"))
            );
        }

        inputMultiplexer = new InputMultiplexer();

        float panelsHeight = (camera.viewportHeight - camera.viewportWidth) / 2;
        bottomPanel = new Image(new Texture("bottom_panel.png"));
        topPanel = new Image(new Texture("upper_panel.png"));

        reset = new Button(new Texture("menus/button.png"), "Reset", (1 * camera.viewportWidth) / 10, panelsHeight / 5, (3 * camera.viewportWidth) / 10, (3 * panelsHeight) / 5, buttonFont);
        menu = new Button(new Texture("menus/button.png"), "Menu", (6 * camera.viewportWidth) / 10, panelsHeight / 5, (3 * camera.viewportWidth) / 10, (3 * panelsHeight) / 5, buttonFont);


        bottomPanel.setPosition(0, 0);
        bottomPanel.setSize(camera.viewportWidth, panelsHeight);
        topPanel.setPosition(0, camera.viewportHeight - panelsHeight);
        topPanel.setSize(camera.viewportWidth, panelsHeight);

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

        levelName = new Text(0,
                (int) (camera.viewportHeight - ((camera.viewportHeight - camera.viewportWidth) / 2) + counterFont.getCapHeight() + (((camera.viewportHeight - camera.viewportWidth) / 2) - counterFont.getCapHeight()) / 2),
                "");

        sprites = new ArrayList<>();
        spriteStates = new HashMap<>();
        portalStates = new HashMap<>();

        control = Controls.NONE;
    }

    public void prepareMapUI(OrthographicCamera camera, Map map, Stage stage) {

        control = Controls.NONE;
        inputMultiplexer.clear();
        inputMultiplexer.addProcessor(gestureDetector);
        inputMultiplexer.addProcessor(stage);
        Gdx.input.setInputProcessor(inputMultiplexer);
        stage.clear();

        stage.addActor(reset);
        stage.addActor(menu);

        float panelsHeight = (camera.viewportHeight - camera.viewportWidth) / 2;
        gestureController.setGestureField(0, camera.viewportWidth, panelsHeight, panelsHeight + camera.viewportWidth);
        levelName.setText(help.utils.MapsReader.getMapName(map));
        levelName.setPosX((int) ((camera.viewportWidth / 2) - (counterFont.getBounds(MapsReader.getMapName(map)).width / 2)));


    }

    public void prepareMap(OrthographicCamera camera, Map map) {

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
            if (sprite.getTexture().equals(textureHashMap.get("BALL")) || sprite.getTexture().equals(textureHashMap.get("BOX")))
                sprite.draw(batch);
        }
        batch.end();
        drawUI(map, camera, batch);
    }

    public void prepareAnimation() {
        gestureController.setGestureField(0, 0, 0, 0);
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
            if (!isSpriteInTheSamePlace(sprites.get(i), wantedSprites.get(i))) {
                if (portalStates.get(sprites.get(i)) == 1)
                    portalMoveSprite(sprites.get(i), wantedSprites.get(i), camera);
                else
                    moveSprite(sprites.get(i), wantedSprites.get(i));
                allSpritesReady = false;
            } else if (!isSpriteTheSame(sprites.get(i), wantedSprites.get(i))) {
                animateSprite(sprites.get(i), wantedSprites.get(i));
                allSpritesReady = false;
            }
            if (sprites.get(i).getTexture().equals(textureHashMap.get("SWITCH"))) {
                if (checkOtherSpriteInThatPlace(sprites.get(i)))
                    switchDoors();
            }
        }


        if (allSpritesReady)
            createSpritesList(map, camera);

        for (Sprite sprite : sprites) {
            sprite.draw(batch);
        }

        for (Sprite sprite : sprites) {
            if (sprite.getTexture().equals(textureHashMap.get("BALL")) || sprite.getTexture().equals(textureHashMap.get("BOX")))
                sprite.draw(batch);
        }


        batch.end();
        drawUI(map, camera, batch);

        if (watchDog > 50) {
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

    private void portalMoveSprite(Sprite sprite, Sprite desiredSprite, OrthographicCamera camera) {
        if (sprite.getX() > desiredSprite.getX()) {
            sprite.translateX(Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime());
            if (sprite.getX() > camera.viewportWidth) {
                sprite.setPosition(-sprite.getWidth(), sprite.getY());
                portalStates.put(sprite, 0);
            }
        } else if (sprite.getX() < desiredSprite.getX()) {
            sprite.translateX(-Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime());
            if (sprite.getX() < -sprite.getWidth()) {
                sprite.setPosition(camera.viewportWidth, sprite.getY());
                portalStates.put(sprite, 0);
            }
        } else if (sprite.getY() > desiredSprite.getY()) {
            float panelHeight = (camera.viewportHeight - camera.viewportWidth) / 2;
            sprite.translateY(Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime());
            if (sprite.getY() > panelHeight + camera.viewportWidth) {
                sprite.setPosition(sprite.getX(), panelHeight - sprite.getHeight());
                portalStates.put(sprite, 0);
            }
        } else if (sprite.getY() < desiredSprite.getY()) {
            float panelHeight = (camera.viewportHeight - camera.viewportWidth) / 2;
            sprite.translateY(-Constants.spritesMovingSpeed * Gdx.graphics.getDeltaTime());
            if (sprite.getY() < panelHeight - sprite.getHeight()) {
                portalStates.put(sprite, 0);
                sprite.setPosition(sprite.getX(), panelHeight + camera.viewportWidth);
            }
        }
    }

    private void animateSprite(Sprite sprite, Sprite desiredSprite) {

        if (sprite.getTexture().equals(textureHashMap.get("BALL")) || spriteStates.get(sprite) != 0)
            if (desiredSprite.getTexture().equals(textureHashMap.get("FINISHED"))) {
                cleanEndSprite(desiredSprite);
                sprite.setTexture(textureHashMap.get("FINISHED"));
            }


        if (desiredSprite.getTexture().equals(textureHashMap.get("NOTHING"))) {
            if (checkOtherSpriteInThatPlace(sprite))
                sprite.setTexture(textureHashMap.get("NOTHING"));
        }

        if (desiredSprite.getTexture().equals(textureHashMap.get("TRAPA"))) {
            if (checkOtherSpriteInThatPlace(sprite))
                sprite.setTexture(textureHashMap.get("TRAPA"));
        }

        if (sprite.getTexture().equals(textureHashMap.get("GHOSTLY")) || spriteStates.get(sprite) != 0)
            if (desiredSprite.getTexture().equals(textureHashMap.get("CREATED"))) {
                if (checkOtherSpriteInThatPlace(sprite))
                    sprite.setTexture(desiredSprite.getTexture());
            }

        if (sprite.getTexture().equals(textureHashMap.get("GHOSTLY")) || spriteStates.get(sprite) != 0)
            if (desiredSprite.getTexture().equals(textureHashMap.get("GWB"))) {
                if (checkOtherSpriteInThatPlace(sprite))
                    sprite.setTexture(desiredSprite.getTexture());
            }

        if (sprite.getTexture().equals(textureHashMap.get("GWB")) || spriteStates.get(sprite) != 0)
            if (desiredSprite.getTexture().equals(textureHashMap.get("CREATED"))) {
                if (!checkOtherSpriteInThatPlace(sprite))
                    sprite.setTexture(desiredSprite.getTexture());
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
            Texture texture = textureHashMap.get(object.getObjectsType().toString());
            Sprite sprite = new Sprite(texture);
            sprite.setPosition(object.getX() * width * scaleX, object.getY() * width * scaleY + (offset / 2));
            sprite.setSize(width * scaleX, width * scaleY);
            sprites.add(sprite);
            spriteStates.put(sprite, 0);
            portalStates.put(sprite, 0);
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
            if (objects.get(j).isGoneThroughTele())
                portalStates.put(sprites.get(j), 1);
        }

    }

    public void cleanEndSprite(Sprite sprite) {

        for (Sprite chosenSprite : sprites) {
            if (isSpriteInTheSamePlace(chosenSprite, sprite)) {
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

    public int getIndexOfOtherSpriteInThatPlace(Sprite sprite) {
        boolean isThere = false;
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

        topPanel.draw(batch, 1);
        bottomPanel.draw(batch, 1);
        menu.draw(batch, 1, buttonFont);
        reset.draw(batch, 1, buttonFont);

        String moves;
        if (map.getMovesTaken() <= 99) {
            moves = Integer.toString(map.getMovesTaken());
        } else
            moves = "99";

        counterFont.draw(batch, moves, (camera.viewportWidth / 2) - (counterFont.getBounds(moves).width / 2), (camera.viewportHeight - camera.viewportWidth) * 7 / 20);

        counterFont.draw(batch, levelName.getText(), levelName.getPosX(), levelName.getPosY());

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
