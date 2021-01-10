package com.example.battleship.models;

import com.example.battleship.helpers.Constants;

import java.io.Serializable;

public class BattleshipMatrixCell  implements Serializable {
    private int rotation;
    private int type;

    private int xCoordinate;
    private int yCoordinate;

    private boolean isHead;

    public BattleshipMatrixCell(){
        this.type = Constants.EMPTY_CELL;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isHead() {
        return isHead;
    }

    public void setHead(boolean head) {
        isHead = head;
    }
}
