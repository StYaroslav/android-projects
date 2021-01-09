package com.example.battleship;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.battleship.activities.UserProfileActivity;
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
    TextView usernameLabel;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    @SuppressLint("SetTextI18n")
    private void updateUI() {
        usernameLabel.setText("You are welcome, " + Objects.requireNonNull(mAuth.getCurrentUser()).getDisplayName());

    }

}