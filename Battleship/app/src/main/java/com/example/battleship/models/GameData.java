package com.example.battleship.models;

public class GameData {
    public User hostUser;
    public User connectedUser;
    public String gameId;

    public GameData() {

    }

    public GameData (User hostUser, String gameId) {
        this.hostUser = hostUser;
        this.gameId = gameId;
    }
}
