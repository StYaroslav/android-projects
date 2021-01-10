package com.example.battleship.viewModels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.battleship.models.BattleshipMatrix;
import com.example.battleship.models.GameData;
import com.example.battleship.models.User;

public class ConnectGameViewModel extends ViewModel {
    public MutableLiveData<User> hostUser;
    public MutableLiveData<User> guestUser;
    public MutableLiveData<BattleshipMatrix> playerMatrix;
    public MutableLiveData<BattleshipMatrix> opponentMatrix;
    public MutableLiveData<Boolean> hostIsReady;
    public MutableLiveData<Boolean> guestIsReady;
    public MutableLiveData<String> gameId;
    public MutableLiveData<String> gameState;
    public MutableLiveData<Boolean> hostStep;

    public ConnectGameViewModel(GameData game) {
        this.hostUser = new MutableLiveData<>();
        this.guestUser = new MutableLiveData<>();
        this.hostIsReady = new MutableLiveData<>();
        this.guestIsReady = new MutableLiveData<>();
        this.gameId = new MutableLiveData<>();
        this.gameState = new MutableLiveData<>();
        this.opponentMatrix = new MutableLiveData<>();
        this.playerMatrix = new MutableLiveData<>();
        this.hostStep = new MutableLiveData<>();

        this.playerMatrix.setValue(game.getPlayerMatrix());
        this.opponentMatrix.setValue(game.getOpponentMatrix());
        this.gameState.setValue(game.getGameState());
        this.gameId.setValue(game.getGameId());
        this.hostUser.setValue(game.getHostUser());
        this.guestUser.setValue(game.getConnectedUser());
        this.hostIsReady.setValue(false);
        this.guestIsReady.setValue(false);
        this.hostStep.setValue(true);
    }


    public void SetPlayerMatrix(BattleshipMatrix matrix) {
        this.playerMatrix.setValue(matrix);
    }

    public void SetOpponentMatrix() {
        BattleshipMatrix matrix = new BattleshipMatrix();
        this.opponentMatrix.setValue(matrix);
    }

    public GameData GetGameInstance() {
        return new GameData(
                hostUser.getValue(),
                guestUser.getValue(),
                playerMatrix.getValue(),
                opponentMatrix.getValue(),
                gameState.getValue(),
                gameId.getValue(),
                hostIsReady.getValue(),
                guestIsReady.getValue());
    }
}
