package sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import enums.Sounds;
import help.utils.Constants;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class SoundActivator {

    public static void setSound(boolean on) {

        Constants.soundOn = on;
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        try {

            dBuilder = builderFactory.newDocumentBuilder();
            Document document = dBuilder.newDocument();
            Element sound = document.createElement("sound");
            document.appendChild(sound);

            int isOn;
            if (on)
                isOn = 1;
            else
                isOn = 0;

            sound.setAttribute("on", Integer.toString(isOn));
            document.normalize();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            Source xmlSource = new DOMSource(document);
            Result outputTarget = new StreamResult(outputStream);
            TransformerFactory.newInstance().newTransformer().transform(xmlSource, outputTarget);
            InputStream is = new ByteArrayInputStream(outputStream.toByteArray());

            FileHandle file = Gdx.files.local("sound.xml");
            file.write(is, false);


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void loadSettings() {

        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {

            dBuilder = builderFactory.newDocumentBuilder();
            Document document;
            FileHandle file = Gdx.files.local("sound.xml");
            if (file.exists()) {
                document = dBuilder.parse(file.read());
            } else {
                setSound(true);
                document = dBuilder.parse(file.read());
            }

            document.normalize();

            NodeList rootNodes = document.getElementsByTagName("sound");
            Node rootNode = rootNodes.item(0);
            Element sound = (Element) rootNode;
            String on = sound.getAttribute("on");

            if (on.equals("1"))
                Constants.soundOn = true;
            else Constants.soundOn = false;

        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    public static HashMap<Sounds, Sound> soundHashMap() {
        HashMap<Sounds, Sound> soundHashMap = new HashMap<>();
        soundHashMap.put(Sounds.SLIDE, Gdx.audio.newSound(Gdx.files.internal("sounds/slide.wav")));
        soundHashMap.put(Sounds.END, Gdx.audio.newSound(Gdx.files.internal("sounds/button.wav")));
        soundHashMap.put(Sounds.RED, Gdx.audio.newSound(Gdx.files.internal("sounds/button.wav")));
        soundHashMap.put(Sounds.GREEN, Gdx.audio.newSound(Gdx.files.internal("sounds/button.wav")));
        soundHashMap.put(Sounds.DESTROY, Gdx.audio.newSound(Gdx.files.internal("sounds/gray.wav")));
        return soundHashMap;
    }

}



