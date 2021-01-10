 package com.example.battleship;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.battleship.activities.UserProfileActivity;
import com.example.battleship.fragments.ConnectGameFragment;
import com.example.battleship.fragments.CreateGameFragment;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

 public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1;
    Button signInButton;
    Button profileButton;
    Button connectGameButton;
    Button createGameButton;

    TextView usernameLabel;
    FirebaseAuth mAuth;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);

        usernameLabel = findViewById(R.id.usernameLabel);
        mAuth = FirebaseAuth.getInstance();

        profileButton = findViewById(R.id.profileButton);
        profileButton.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                Intent profileIntent = new Intent(getApplicationContext(), UserProfileActivity.class);
                startActivity(profileIntent);
            }
            else {
                Toast.makeText(this, "Sign in to view your profile!", Toast.LENGTH_SHORT).show();
            }
        });

        connectGameButton = findViewById(R.id.findGameButton);
        createGameButton = findViewById(R.id.createGameButton);

        createGameButton.setOnClickListener(v -> {
            DialogFragment createGameFragment = new CreateGameFragment();
            createGameFragment.show(getSupportFragmentManager(), "createGameFragment");
        });

        connectGameButton.setOnClickListener(v -> {
            DialogFragment connectGameFragment = new ConnectGameFragment();
            connectGameFragment.show(getSupportFragmentManager(), "connectGameFragment");
        });

        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener(v -> {
            List<AuthUI.IdpConfig> providers = Arrays.asList(
                    new AuthUI.IdpConfig.EmailBuilder().build(),
                    new AuthUI.IdpConfig.GoogleBuilder().build());

            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .build(),
                    RC_SIGN_IN);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                FirebaseUser user = mAuth.getCurrentUser();
                updateUI();
            } else {
                Toast.makeText(this, "Authentication problem!", Toast.LENGTH_SHORT).show();

            }
        }
    }
    
    private void updateUI() {
    }

}