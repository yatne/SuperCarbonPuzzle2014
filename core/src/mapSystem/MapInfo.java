package mapSystem;

public class MapInfo {

    private int starsToUnlock;
    private int starsToObtain;

    public MapInfo(int worldNumber, int mapNumber) {
        this.starsToObtain = help.utils.HelpUtils.levelStarsCount(worldNumber, mapNumber);
        this.starsToUnlock = help.utils.MapsReader.starsToUnlock(worldNumber, mapNumber);
    }

    public int getStarsToUnlock() {
        return starsToUnlock;
    }

    public int getStarsToObtain() {
        return starsToObtain;
    }
}
