package mapSystem;

import help.utils.Constants;
import help.utils.MapsReader;
import org.w3c.dom.NodeList;

import java.util.HashMap;

public class MapsInfo {

    private HashMap<Integer, HashMap<Integer, MapInfo>> mapsInfo;

    public MapsInfo() {
        mapsInfo = new HashMap<>();

        for (int i = 1; i <= Constants.howManyWorlds; i++) {
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

    public int getMapsCountInWorld(int world) {
        if (mapsInfo.get(world) == null) {
            return -1;
        } else {
            return mapsInfo.get(world).size();
        }
    }

    public int getStarsToObtainInWorld(int world) {

        int stars = 0;
        int size = mapsInfo.get(world).size();

        if (mapsInfo.get(world) == null) {
            return 0;
        } else {
            for (int i = 1; i <= size; i++) {
                if (mapsInfo.get(world).get(i) == null) {
                    size++;
                } else
                    stars = stars + mapsInfo.get(world).get(i).getStarsToObtain();
            }
        }
        return stars;
    }
}
