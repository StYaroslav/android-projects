package com.example.battleship.activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.battleship.R;
import com.example.battleship.adapters.BattleshipMatrixAdapter;
import com.example.battleship.adapters.OnCellClickListener;
import com.example.battleship.fragments.StatisticFragment;
import com.example.battleship.helpers.Constants;
import com.example.battleship.models.BattleshipMatrix;
import com.example.battleship.models.BattleshipMatrixCell;
import com.example.battleship.models.GameData;
import com.example.battleship.models.Ship;
import com.example.battleship.models.Statistic;
import com.example.battleship.models.User;
import com.example.battleship.viewModels.ConnectGameViewModel;
import com.example.battleship.viewModels.ConnectGameViewModelFactory;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class CreateGameActivity extends AppCompatActivity implements OnCellClickListener {
    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    TextView playerNicknameText;
    TextView opponentNicknameText;

    SimpleDraweeView playerImage;
    SimpleDraweeView opponentImage;

    RecyclerView playerRecyclerView;
    RecyclerView opponentRecyclerView;

    ImageView playerStepImage;
    ImageView opponentStepImage;

    BattleshipMatrixAdapter playerAdapter;
    BattleshipMatrixAdapter opponentAdapter;

    ConnectGameViewModel gameViewModel;

    ValueEventListener playerMatrixValueEventListener;
    ValueEventListener hostStepValueEventListener;

    DatabaseReference gameDatabaseReference;
    DatabaseReference statisticsDatabaseReference;

    String opponentMatrixPath;
    String playerMatrixPath;

    String opponentShipsPath;
    String playerShipsPath;

    boolean isHost;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_create_game);
        Objects.requireNonNull(getSupportActionBar()).hide();

        opponentShipsPath = isHost ? "connectedShips" : "hostShips";
        playerShipsPath = !isHost ? "connectedShips" : "hostShips";

        Intent intent = getIntent();
        GameData game = (GameData) intent.getSerializableExtra(Constants.GAME_EXTRA);
        gameViewModel = new ViewModelProvider(this, new ConnectGameViewModelFactory(game))
                .get(ConnectGameViewModel.class);

        statisticsDatabaseReference = FirebaseDatabase.getInstance().getReference("statistics");

        gameDatabaseReference = FirebaseDatabase.getInstance().getReference("games").
                child(Objects.requireNonNull(gameViewModel.gameId.getValue()));


        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        isHost = Objects.equals(currentUser.getDisplayName(), game.getHostUser().username);

        playerStepImage = findViewById(R.id.playerStepImage);
        opponentStepImage = findViewById(R.id.opponentStepImage);

        opponentMatrixPath = isHost ? "connectedMatrix" : "hostMatrix";
        playerMatrixPath = !isHost ? "connectedMatrix" : "hostMatrix";

        playerNicknameText = findViewById(R.id.playerNicknameText);
        opponentNicknameText = findViewById(R.id.opponentNicknameText);

        playerImage = findViewById(R.id.playerImage);
        opponentImage = findViewById(R.id.opponentImage);

        InitializeDisplaying();

        playerRecyclerView = findViewById(R.id.playerRecyclerView);
        opponentRecyclerView = findViewById(R.id.opponentRecyclerView);

        playerRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 10));
        opponentRecyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 10));

        playerAdapter = new BattleshipMatrixAdapter(this,
                Objects.requireNonNull(gameViewModel.opponentMatrix.getValue()).matrix,
                false, this, false);
        playerRecyclerView.setAdapter(playerAdapter);


        Context context = this;
        gameDatabaseReference.child(opponentMatrixPath).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    BattleshipMatrixCell[][] opponentMatrix = new BattleshipMatrixCell[10][10];
                    int row = 0;
                    int column = 0;
                    for (DataSnapshot rowSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot columnSnapshot : rowSnapshot.getChildren()) {
                            opponentMatrix[row][column] = columnSnapshot.getValue
                                    (BattleshipMatrixCell.class);
                            column++;
                        }
                        row++;
                        column = 0;
                    }
                    gameViewModel.opponentMatrix.getValue().matrix = opponentMatrix;
                    opponentAdapter = new BattleshipMatrixAdapter(getApplicationContext(),
                            gameViewModel.opponentMatrix.getValue().matrix,
                            true, (OnCellClickListener) context, gameViewModel.hostStep.getValue() == isHost);
                    opponentRecyclerView.setAdapter(opponentAdapter);
                    gameDatabaseReference.child(opponentMatrixPath).removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gameDatabaseReference.child(opponentShipsPath).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    List<Ship> ships = new ArrayList<>();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ships.add(dataSnapshot.getValue(Ship.class));
                    }
                    gameViewModel.opponentMatrix.getValue().ships = ships;
                    gameDatabaseReference.child(opponentShipsPath).removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        playerMatrixValueEventListener = gameDatabaseReference.child(playerMatrixPath).addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    BattleshipMatrixCell[][] playerMatrix = new BattleshipMatrixCell[10][10];
                    int row = 0;
                    int column = 0;
                    for (DataSnapshot rowSnapshot : snapshot.getChildren()) {
                        for (DataSnapshot columnSnapshot : rowSnapshot.getChildren()) {
                            playerMatrix[row][column] = columnSnapshot.getValue(BattleshipMatrixCell.class);
                            column++;
                        }
                        column = 0;
                        row++;
                    }
                    Objects.requireNonNull(gameViewModel.playerMatrix.getValue()).matrix = playerMatrix;
                    playerAdapter.UpdateMatrix(gameViewModel.playerMatrix.getValue().matrix);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        hostStepValueEventListener = gameDatabaseReference.child("hostStep").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean isHostStep = snapshot.getValue(Boolean.class);
                gameViewModel.hostStep.setValue(isHostStep);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        gameViewModel.hostStep.observe(this, isHostStep -> {
            if (isHostStep) {
                if (isHost)
                    ShowPlayerStepImage();
                else
                    ShowOpponentStepImage();
            } else {
                if (isHost)
                    ShowOpponentStepImage();
                else
                    ShowPlayerStepImage();
            }

            if (opponentAdapter != null)
                opponentAdapter.SetClickable(isHostStep == isHost);
        });

        gameViewModel.hitsToWin.observe(this, hitsToWin -> {
            if (hitsToWin == 0) {
                gameDatabaseReference.child("hostWin").setValue(isHost);
                gameDatabaseReference.child("gameState").setValue(Constants.FINISHED_STATE);
            }
        });

        gameDatabaseReference.child("hostWin").addValueEventListener(new ValueEventListener() {
            @SuppressLint("ShowToast")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Boolean hostWin = (Boolean) snapshot.getValue();
                if (hostWin != null) {
                    if (hostWin)
                        Toast.makeText(context,
                                Objects.requireNonNull(gameViewModel.hostUser.getValue())
                                        .username + " wins!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(context, gameViewModel.guestUser.getValue()
                                .username + " wins!", Toast.LENGTH_LONG).show();
                    opponentAdapter.SetClickable(false);
                    opponentAdapter.ShowShipsAfterDefeat();
                    if (isHost)
                        SaveResult(hostWin);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        statisticsDatabaseReference.child(gameViewModel.gameId.getValue()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null) {
                    DialogFragment statisticsFragment = new StatisticFragment();
                    statisticsFragment.show(getSupportFragmentManager(), "statisticsFragment");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void SaveResult(boolean hostWin) {
        User winner;
        User loser;
        if (hostWin) {
            winner = gameViewModel.hostUser.getValue();
            loser = gameViewModel.guestUser.getValue();
        } else {
            winner = gameViewModel.guestUser.getValue();
            loser = gameViewModel.hostUser.getValue();
        }
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatForDateNow = new SimpleDateFormat("HH:mm:ss yyyy.MM.dd ");
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat formatForTimeNow = new SimpleDateFormat("HH:mm:ss");

        Statistic statistic = new Statistic(winner, loser, formatForDateNow.format(new Date()),
                formatForTimeNow.format(new Date()));

        statisticsDatabaseReference.child(gameViewModel.gameId.getValue()).setValue(statistic);
    }

    private void ShowPlayerStepImage() {
        playerStepImage.setVisibility(View.VISIBLE);
        opponentStepImage.setVisibility(View.INVISIBLE);
    }

    private void ShowOpponentStepImage() {
        playerStepImage.setVisibility(View.INVISIBLE);
        opponentStepImage.setVisibility(View.VISIBLE);
    }

    private void InitializeDisplaying() {
        if (isHost) {
            playerNicknameText.setText(Objects.requireNonNull(gameViewModel.hostUser.getValue()).username);
            opponentNicknameText.setText(Objects.requireNonNull(gameViewModel.guestUser.getValue()).username);
            playerImage.setImageURI(gameViewModel.hostUser.getValue().profileImageUrl);
            opponentImage.setImageURI(gameViewModel.guestUser.getValue().profileImageUrl);
        } else {
            playerNicknameText.setText(Objects.requireNonNull(gameViewModel.guestUser.getValue()).username);
            opponentNicknameText.setText(Objects.requireNonNull(gameViewModel.hostUser.getValue()).username);
            playerImage.setImageURI(gameViewModel.guestUser.getValue().profileImageUrl);
            opponentImage.setImageURI(gameViewModel.hostUser.getValue().profileImageUrl);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onCellClicked(int row, int column, int result) {
        switch (result) {
            case Constants.RESULT_HIT: {
                Objects.requireNonNull(gameViewModel.opponentMatrix.getValue()).
                        matrix[row][column].setType(Constants.HIT_CELL);
                gameViewModel.opponentMatrix.getValue().CheckShip(row, column);
                opponentAdapter.UpdateMatrix(gameViewModel.opponentMatrix.getValue().matrix);
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
                gameViewModel.hitsToWin.setValue(gameViewModel.hitsToWin.getValue() - 1);
                break;
            }
            case Constants.RESULT_MISS: {
                Objects.requireNonNull(gameViewModel.opponentMatrix.getValue()).
                        matrix[row][column].setType(Constants.CHECKED_CELL);
                gameDatabaseReference.child("hostStep").setValue(!isHost);
            }
        }
        List<List<BattleshipMatrixCell>> matrixList = gameViewModel.opponentMatrix.getValue().GetList();
        gameDatabaseReference.child(opponentMatrixPath).setValue(matrixList);
    }

    @Override
    public void onBackPressed() {
        final AlertDialog aboutDialog = new AlertDialog.Builder(
                CreateGameActivity.this).setMessage("Are you sure tou want to leave this game?")
                .setPositiveButton("Yes", (dialog, which) -> finish())
                .setNegativeButton("No", (dialog, which) -> {
                }).create();

        aboutDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gameDatabaseReference.removeEventListener(hostStepValueEventListener);
        gameDatabaseReference.removeEventListener(playerMatrixValueEventListener);
    }
}