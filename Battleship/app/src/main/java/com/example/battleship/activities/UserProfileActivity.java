package com.example.battleship.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.battleship.MainActivity;
import com.example.battleship.R;
import com.example.battleship.helpers.MD5;
import com.example.battleship.models.User;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class UserProfileActivity extends AppCompatActivity {

    int PICK_IMAGE = 1;

    SimpleDraweeView profileImageView;

    Button signOutButton;
    Button confirmButton;
    Button imageUploadButton;

    EditText usernameEdit;
    EditText emailEdit;

    SwitchMaterial gravatarSwitch;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    Uri selectedPhotoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        imageUploadButton = findViewById(R.id.uploadImage);
        profileImageView = findViewById(R.id.profileImage);
        confirmButton = findViewById(R.id.confirmButton);
        signOutButton = findViewById(R.id.signOutButton);
        gravatarSwitch = findViewById(R.id.gravatarSwitch);
        usernameEdit = findViewById(R.id.usernameEdit);
        emailEdit = findViewById(R.id.emailEdit);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        usernameEdit.setText(currentUser.getDisplayName());
        emailEdit.setText(currentUser.getEmail());

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();

        confirmButton.setOnClickListener(v -> {
            uploadPhotoToFirebaseStorage();
            finish();
        });

        imageUploadButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE);
        });

        gravatarSwitch.setOnCheckedChangeListener((buttonView, isGravatarUsed) -> {
            editor.putBoolean("gravatarUsing", isGravatarUsed);
            editor.apply();
            profileImageView.setImageURI(getProfileImageUri(isGravatarUsed));
        });

        signOutButton.setOnClickListener(v -> AuthUI.getInstance().signOut(getApplicationContext()).addOnCompleteListener(task -> {
            Toast.makeText(UserProfileActivity.this, "Successfully signed out!", Toast.LENGTH_SHORT).show();
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);

        }));

        boolean gravatarUsing = sharedPreferences.getBoolean("gravatarUsing", false);
        profileImageView.setImageURI(getProfileImageUri(gravatarUsing));
        gravatarSwitch.setChecked(gravatarUsing);
    }

    private void uploadPhotoToFirebaseStorage(){
        if(selectedPhotoUri == null) return;

        String filename = UUID.randomUUID().toString();
        StorageReference reference =  FirebaseStorage.getInstance().getReference("/images/" + filename);

        reference.putFile(selectedPhotoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        saveChangesToFirebaseDatabase(uri);
                    }
                });
            }
        });
    }

    private void saveChangesToFirebaseDatabase(Uri profileImageUri) {
        if (currentUser != null) {
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("/users/" + uid);
            User user = new User(uid, usernameEdit.getText().toString(), profileImageUri.toString());
            databaseReference.setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d("ProfileActivity", "Changes saved to database");
                    UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(user.username)
                            .setPhotoUri(profileImageUri)
                            .build();
                    currentUser.updateProfile(changeRequest);
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            selectedPhotoUri = data.getData();
            profileImageView.setImageURI(selectedPhotoUri);
        }
    }
    private Uri getProfileImageUri(boolean isGravatarUsed){
        Uri profileImageUri;

        if (isGravatarUsed) {
            String email = currentUser.getEmail();
            assert email != null;
            String hash = MD5.md5Hex(email.trim().toLowerCase());
            profileImageUri = Uri.parse("https://www.gravatar.com/avatar/" + hash + "?size=256");
        } else {
            if (currentUser.getPhotoUrl() != null) {
                profileImageUri = Uri.parse(currentUser.getPhotoUrl().toString());
            } else {
                profileImageUri = Uri.parse("https://www.vippng.com/png/detail/355-3554387_create-digital-profile-icon-blue-profile-icon-png.png");
            }
        }
        return profileImageUri;
    }
}