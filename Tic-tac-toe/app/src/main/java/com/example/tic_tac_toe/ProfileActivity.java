package com.example.tic_tac_toe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    TextView email, username;

    SimpleDraweeView profileImage;

    FirebaseUser user;

    Button uploadImage, saveProfile;

    ProfileViewModel profileViewModel;

    Uri selectedImage;

    Switch useGravatar;

    private final int PICK_IMAGE_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.activity_profile);

        user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        String name = user.getDisplayName();
        String userEmail = user.getEmail();
        Uri photoUrl = user.getPhotoUrl();

        profileViewModel = new ProfileViewModel(false, user);

        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        profileImage = findViewById(R.id.profileImage);
        useGravatar = findViewById(R.id.gravatar);

        email.setText(userEmail);
        username.setText(name);

        uploadImage = findViewById(R.id.imageUpload);
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Image from here..."),
                        PICK_IMAGE_REQUEST);
            }
        });

        saveProfile = findViewById(R.id.saveProfile);
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    profileViewModel.saveProfile(getApplicationContext());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        useGravatar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                profileViewModel.useGravatar.setValue(isChecked);
                profileViewModel.setGravatarImage();
            }
        });

        username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable username) {
                profileViewModel.username.setValue(username.toString());
            }
        });

        profileViewModel.profileImageUri.observe(this, new Observer<Uri>() {
            @Override
            public void onChanged(Uri uri) {
                profileImage.setImageURI(profileViewModel.profileImageUri.getValue());
                if (profileViewModel.profileImageUri.getValue() == null) {
                    profileImage.setImageURI(
                            "https://pics.freeicons.io/uploads/icons/png/19339625881548233621-512.png");
                }
            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,
                resultCode,
                data);
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            selectedImage = data.getData();
            profileViewModel.profileImageUri.setValue(selectedImage);
            profileViewModel.uploadPhoto(this);
        }
    }
}