package com.example.tic_tac_toe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class CreateGameFragment extends DialogFragment {

    TextView gameId;

    User host;

    DatabaseReference userDatabaseReference;
    DatabaseReference gameDatabaseReference;
    ValueEventListener guestConnectionEventListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.create_game_dialog, container, false);

        Random random = new Random();
        int uniqueID = random.nextInt(999999);

        gameId = v.findViewById(R.id.gameID);
        gameId.setText(String.valueOf(uniqueID));

        String hostUid = FirebaseAuth.getInstance().getUid();

        userDatabaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(hostUid);
        gameDatabaseReference =  FirebaseDatabase.getInstance().getReference("games/" + uniqueID);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        User host = new User(user.getUid(), user.getDisplayName(), user.getPhotoUrl().toString());

        GameModel game = new GameModel(host, String.valueOf(uniqueID));
        gameDatabaseReference.setValue(game);
        WaitGuest();

        return v;
    }

    private void WaitGuest() {
        guestConnectionEventListener = gameDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                GameModel game = snapshot.getValue(GameModel.class);
                if (game.getConnectedUser() != null)
                    StartGame(game);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void StartGame(GameModel game){
        getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        if(guestConnectionEventListener != null)
            gameDatabaseReference.removeEventListener(guestConnectionEventListener);
        Intent intent = new Intent(getContext(), LobbyActivity.class);
        intent.putExtra("game", game);
        startActivity(intent);
    }
}
