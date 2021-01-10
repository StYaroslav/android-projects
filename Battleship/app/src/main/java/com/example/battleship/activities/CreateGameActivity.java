package com.example.battleship.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.battleship.R;
import com.example.battleship.models.GameData;
import com.example.battleship.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CreateGameActivity extends AppCompatActivity {

    TextView gameIdTextView, generateGameId;

    ProgressBar progressBar;

    int MIN_ID = 100000;
    int MAX_ID = 1000000;

    User host;
    String gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_game);

        gameIdTextView = findViewById(R.id.gameIdTextView);
        generateGameId = findViewById(R.id.generatingTextView);

        progressBar = findViewById(R.id.progressBar);

        String hostUid = FirebaseAuth.getInstance().getUid();
        gameId = String.valueOf((int) ((Math.random() * ((MAX_ID - MIN_ID) + 1)) + MIN_ID));

        DatabaseReference usersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(hostUid);
        DatabaseReference gamesDatabaseReference = FirebaseDatabase.getInstance().getReference("/games/" + gameId);

        usersDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                host = snapshot.getValue(User.class);

                progressBar.setVisibility(View.INVISIBLE);
                generateGameId.setVisibility(View.INVISIBLE);
                gameIdTextView.setVisibility(View.VISIBLE);

                gameIdTextView.append(gameId);

                GameData game = new GameData(host, gameId);
                gamesDatabaseReference.setValue(game);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("games").child(gameId);
        databaseReference.removeValue();
    }
}