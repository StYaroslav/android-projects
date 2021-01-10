package com.example.battleship.models;

import com.example.battleship.helpers.Constants;

public class BattleshipMatrixCell {
    int rotation;
    public int type;
    public boolean isHead;

    public BattleshipMatrixCell() {
        this.type = Constants.EMPTY_CELL;
    }

    public BattleshipMatrixCell(boolean isHead, int type) {
        this.type = type;
        this.isHead = isHead;
    }
}
