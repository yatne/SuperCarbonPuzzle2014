package view;

import enums.Animations;

public class Move {

    int fromX;
    int fromY;
    Animations animationType;
    int toX;
    int toY;

    public Move(int fromX, int fromY, Animations animationType) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.animationType = animationType;
    }

    public Move(int fromX, int fromY, Animations animationType, int toX, int toY) {
        this.fromX = fromX;
        this.fromY = fromY;
        this.animationType = animationType;
        this.toX = toX;
        this.toY = toY;
    }

    public String toString(){
        return "type: " + animationType.toString() + " from X: " + fromX + " to X: " + toX + " from Y: " + fromY + " to Y: " + toY;
    }

    public int getFromX() {
        return fromX;
    }

    public int getFromY() {
        return fromY;
    }

    public Animations getAnimationType() {
        return animationType;
    }

    public int getToX() {
        return toX;
    }

    public int getToY() {
        return toY;
    }
}
