package com.example.tic_tac_toe;

import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

public class ProfileViewModel {
    FirebaseUser user;
    FirebaseStorage storage;
    StorageReference storageReference;

    public MutableLiveData<Boolean> useGravatar = new MutableLiveData<>(false);
    public MutableLiveData<Uri> profileImageUri = new MutableLiveData<Uri>();
    public MutableLiveData<String> username = new MutableLiveData<>("");

    public ProfileViewModel(boolean useGravatar, FirebaseUser user) {
        this.user = user;
        this.useGravatar.setValue(useGravatar);
        this.username.setValue(user.getDisplayName());
        this.profileImageUri.setValue(user.getPhotoUrl());
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
    }

    public void setProfileImage(Context context) {
        StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
        ref.putFile(profileImageUri.getValue()).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(context, "Image Uploaded!!",
                        Toast.LENGTH_SHORT).show();
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        profileImageUri.setValue(uri);
                    }
                });
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed " + e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void saveProfile(Context context) throws InterruptedException {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(this.username.getValue())
                .setPhotoUri(this.profileImageUri.getValue())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Saved",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void setGravatarImage() {
        if (this.useGravatar.getValue()) {
            String email = user.getEmail();
            assert email != null;
            String hash = MD5Util.md5Hex(email.trim().toLowerCase());
            profileImageUri.setValue(Uri.parse("https://www.gravatar.com/avatar/" + hash + "?size=256"));
        } else {
            profileImageUri.setValue(user.getPhotoUrl());
        }
    }
}
