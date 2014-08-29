package mapSystem;

import help.utils.MapsReader;
import org.w3c.dom.NodeList;

import java.util.HashMap;

public class MapsInfo {

    private HashMap<Integer, HashMap<Integer, MapInfo>> mapsInfo;

    public MapsInfo() {
        mapsInfo = new HashMap<>();

        for (int i = 1; i <= 2; i++) {
            HashMap<Integer, MapInfo> levels = new HashMap<>();
            NodeList maps = MapsReader.getMapsList("/resources/maps" + i + ".xml");
            for (int j = 1; j <= maps.getLength(); j++) {
                MapInfo mapInfo = new MapInfo(i, j);
                levels.put(j, mapInfo);
            }
            mapsInfo.put(i, levels);
        }
    }

    public int getStarsToUnlock(int world, int level) {

        if (mapsInfo.get(world) == null) {
            return -1;
        } else if (mapsInfo.get(world).get(level) == null) {
            return -1;
        } else {
            return mapsInfo.get(world).get(level).getStarsToUnlock();
        }

    }

    public int getStarsToObtain(int world, int level) {

        if (mapsInfo.get(world) == null) {
            return -1;
        } else if (mapsInfo.get(world).get(level) == null) {
            return -1;
        } else {
            return mapsInfo.get(world).get(level).getStarsToObtain();
        }

    }

    public int getStarsToUnlockWorld(int world) {
        if (mapsInfo.get(world) == null) {
            return -1;
        } else {
            return mapsInfo.get(world).get(1).getStarsToUnlock();
        }
    }

    public int getMapsCountInWorld(int world){
        if (mapsInfo.get(world) == null) {
            return -1;
        } else {
            return mapsInfo.get(world).size();
        }
    }
}
