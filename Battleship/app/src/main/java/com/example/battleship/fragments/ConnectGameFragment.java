package com.example.battleship.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.battleship.R;
import com.example.battleship.activities.ConnectGameActivity;
import com.example.battleship.models.GameData;
import com.example.battleship.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConnectGameFragment extends DialogFragment {
    TextView idInput;
    TextView  hostTextView;
    TextView guestTextView;

    Button connectButton;

    FirebaseUser currentUser;
    FirebaseAuth mAuth;

    DatabaseReference gamesDatabaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connect_game, container, false);
        ProgressBar progressBar = view.findViewById(R.id.progressBar2);
        hostTextView = view.findViewById(R.id.hostTextView);
        guestTextView = view.findViewById(R.id.guestTextView);
        idInput = view.findViewById(R.id.idInput);
        connectButton = view.findViewById(R.id.connectButton);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        connectButton.setOnClickListener(v -> {
            progressBar.setVisibility(View.VISIBLE);
            gamesDatabaseReference = FirebaseDatabase.getInstance().getReference().child("games").child(idInput.getText().toString());
            gamesDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @SuppressLint("ShowToast")
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    progressBar.setVisibility(View.INVISIBLE);
                    GameData game = snapshot.getValue(GameData.class);
                    if (game != null)
                        EstablishGameConnection(game);
                    else {
                        Toast toast = Toast.makeText(getContext(), "Game with such ID was not found", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP, 0, 160);
                        toast.show();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        });

        return view;
    }

    private void EstablishGameConnection(GameData game){
        if (game.getHostUser() != null) {
            game.setConnectedUser(new User(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getPhotoUrl().toString()));
            gamesDatabaseReference.setValue(game);
            Intent intent = new Intent(getContext(), ConnectGameActivity.class);
            intent.putExtra("game", (Parcelable) game);
            startActivity(intent);
        }
    }
}