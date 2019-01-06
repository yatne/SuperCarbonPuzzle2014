package view;

public class Star {

    private float posX;
    private float posY;
    private boolean gold;

    public Star(float posX, float posY, boolean gold) {
        this.posX = posX;
        this.posY = posY;
        this.gold = gold;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public boolean isGold() {
        return gold;
    }
}
