package com.example.battleship.viewModels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.battleship.models.GameData;

public class ConnectGameViewModelFactory implements ViewModelProvider.Factory  {
    private final GameData game;

    public ConnectGameViewModelFactory(GameData game){
        this.game = game;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(ConnectGameViewModel.class)){
            return (T) new ConnectGameViewModel(game);
        }
        throw new IllegalArgumentException("Incorrect ViewModel class");
    }
}
