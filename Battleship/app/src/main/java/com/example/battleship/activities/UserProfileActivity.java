package com.example.battleship.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.battleship.MainActivity;
import com.example.battleship.R;
import com.example.battleship.helpers.MD5;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserProfileActivity extends AppCompatActivity {

    Button signOutButton;

    EditText usernameEdit;
    EditText emailEdit;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    SwitchMaterial gravatarSwitch;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mAuth = FirebaseAuth.getInstance();

        currentUser = mAuth.getCurrentUser();

        usernameEdit = findViewById(R.id.usernameEdit);
        emailEdit = findViewById(R.id.emailEdit);

        usernameEdit.setText(currentUser.getDisplayName());
        emailEdit.setText(currentUser.getEmail());

        Uri profileImageUri;

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        boolean usingGravatar = sharedPreferences.getBoolean("usingGravatar", false);
        gravatarSwitch = findViewById(R.id.gravatarSwitch);

        gravatarSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor.putBoolean("usingGravatar", isChecked);
            editor.apply();
        });

        gravatarSwitch.setChecked(usingGravatar);
        if (usingGravatar) {
            String email = currentUser.getEmail();
            assert email != null;
            String hash = MD5.md5Hex(email.trim().toLowerCase());
            profileImageUri = Uri.parse("https://www.gravatar.com/avatar/" + hash + "?size=256");
        } else {
            if (currentUser.getPhotoUrl() != null) {
                profileImageUri = Uri.parse(currentUser.getPhotoUrl().toString());
            } else {
                profileImageUri = Uri.parse("https://yt3.ggpht.com/a/AATXAJwqEcDwWfOxAKdtQUUSm-" +
                        "lLCopCNoBdoFlTv9BsTA=s900-c-k-c0xffffffff-no-rj-mo");
            }
        }

        SimpleDraweeView profileImageView = findViewById(R.id.profileImage);
        profileImageView.setImageURI(profileImageUri);

        signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(v -> AuthUI.getInstance().signOut(getApplicationContext())
                .addOnCompleteListener(task -> {
            Toast.makeText(UserProfileActivity.this, "Successfully signed out!",
                    Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
        }));

    }
}