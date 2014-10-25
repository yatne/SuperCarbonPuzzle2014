package view;

import com.badlogic.gdx.graphics.Texture;
import help.utils.ObjectsReader;
import org.w3c.dom.Element;

import java.util.HashMap;

public class TextureHashMap {

    private HashMap<String, Texture> textureHashMap;

    public TextureHashMap() {
        textureHashMap = new HashMap<>();
    }

    public Texture get(String key) {

        if (!textureHashMap.containsKey(key)) {
            System.out.println(" added");
            Element block = ObjectsReader.getObjectElement(key);
            textureHashMap.put(
                    block.getAttribute("enum"),
                    new Texture("classic/" + block.getAttribute("texture"))
            );

        }
        return textureHashMap.get(key);
    }

    public void put(String key, Texture value) {
        textureHashMap.put(key, value);
    }
}
