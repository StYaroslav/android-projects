package com.example.battleship.models;

import java.io.Serializable;
import java.util.List;

public class Ship implements Serializable {
    List<BattleshipMatrixCell> shipCells;
    List<BattleshipMatrixCell> surroundCells;
    int cellsRemain;

    public List<BattleshipMatrixCell> getShipCells() {
        return shipCells;
    }

    public void setShipCells(List<BattleshipMatrixCell> shipCells) {
        this.shipCells = shipCells;
    }

    public List<BattleshipMatrixCell> getSurroundCells() {
        return surroundCells;
    }

    public void setSurroundCells(List<BattleshipMatrixCell> surroundCells) {
        this.surroundCells = surroundCells;
    }

    public int getCellsRemain() {
        return cellsRemain;
    }

    public void setCellsRemain(int cellsRemain) {
        this.cellsRemain = cellsRemain;
    }

    public Ship(){

    }

    public Ship(List<BattleshipMatrixCell> shipCells, List<BattleshipMatrixCell> surroundCells){
        this.shipCells = shipCells;
        this.surroundCells = surroundCells;
        this.cellsRemain = shipCells.size();
    }

}