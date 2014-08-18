package view;

import com.badlogic.gdx.graphics.Color;

public class Text {

    private int posX;
    private int posY;
    private int number;
    private  String text;
    private   Color color;

    public Text(int posX, int posY, String text) {
        this.posX = posX;
        this.posY = posY;
        this.text = text;
        color = Color.BLACK;
    }

    public Text(int posX, int posY, int number) {
        this.posX = posX;
        this.posY = posY;
        this.number = number;
        color = Color.BLACK;
    }

    public Text(int posX, int posY, int number, Color color) {
        this.posX = posX;
        this.posY = posY;
        this.number = number;
        this.color = color;
    }

    public String getStringNumber() {
        return Integer.toString(number);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public String getText() {
        return text;
    }

    public int getNumber() {
        return number;
    }

    public Color getColor() {
        return color;
    }
}
