package com.example.tic_tac_toe;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConnectToGameFragment extends DialogFragment {

    Button gameConnectButton;

    EditText gameId;

    FirebaseUser user;
    FirebaseAuth mAuth;

    DatabaseReference gameDatabaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.game_connect_fragment, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        gameId = v.findViewById(R.id.gameIdInput);

        gameConnectButton = v.findViewById(R.id.connectButton);

        gameConnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameDatabaseReference = FirebaseDatabase.getInstance().getReference().child("games")
                        .child(gameId.getText().toString());
                gameDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @SuppressLint("ShowToast")
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        GameModel game = snapshot.getValue(GameModel.class);
                        if (game != null)
                            StartGame(game);
                        else {
                            Toast toast = Toast.makeText(getContext(), "Game was not found", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP, 0, 160);
                            toast.show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
        return v;
    }

    public void StartGame(GameModel game) {
        if (game.getHostUser() != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
            game.setConnectedUser(new User(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString()));
            gameDatabaseReference.setValue(game);
            Intent intent = new Intent(getContext(), LobbyActivity.class);
            intent.putExtra("game", game);
            startActivity(intent);
        }
    }
}
