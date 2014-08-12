package view;

public class LevelNumber {

    int posX;
    int posY;
    int number;

    public LevelNumber(int posX, int posY, int number) {
        this.posX = posX;
        this.posY = posY;
        this.number = number;
    }

    public String getStringNumber(){
        return Integer.toString(number);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getNumber() {
        return number;
    }
}
