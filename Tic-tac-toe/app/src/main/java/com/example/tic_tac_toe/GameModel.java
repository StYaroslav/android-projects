package com.example.tic_tac_toe;

import java.io.Serializable;

public class GameModel implements Serializable {
    private User hostUser;
    private User connectedUser;
    private String gameId;
    private boolean hostReady;
    private boolean guestReady;

    public GameModel(User hostUser, User connectedUser, String gameId, boolean hostReady, boolean guestReady) {
        this.hostUser = hostUser;
        this.connectedUser = connectedUser;
        this.gameId = gameId;
        this.hostReady = hostReady;
        this.guestReady = guestReady;
    }

    public boolean isHostReady() {
        return hostReady;
    }

    public void setHostReady(boolean hostReady) {
        this.hostReady = hostReady;
    }

    public boolean isGuestReady() {
        return guestReady;
    }

    public void setGuestReady(boolean guestReady) {
        this.guestReady = guestReady;
    }

    public  GameModel(){

    }

    public GameModel(User hostUser, String gameId){
        this.hostUser = hostUser;
        this.gameId = gameId;
        this.hostReady = false;
        this.guestReady = false;
    }

    public User getHostUser(){
        return hostUser;
    }

    public User getConnectedUser(){
        return connectedUser;
    }

    public String getGameId(){
        return gameId;
    }

    public void setConnectedUser(User connectedUser){
        this.connectedUser = connectedUser;
    }

}