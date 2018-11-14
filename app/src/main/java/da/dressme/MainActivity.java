package da.dressme;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import static da.dressme.AuthenticationUtil.authenticateAndGetUserID;

public class MainActivity extends AppCompatActivity {

    private Button cameraButton;
    private Button uploadButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private String userID;

    private StorageReference uploadRef;
    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private UploadTask uploadTask;

    private StorageReference userFolderStorageRef;
    private DatabaseReference userDatabaseRef;

    private String imageFileName;

    private Uri filePath;
    public final static int PICK_IMAGE_REQUEST_CODE = 420;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        authenticate();

        cameraButton = findViewById(R.id.open_camera_button);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CameraActivity.class));
            }
        });

        uploadButton = findViewById(R.id.uploadbutt);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhoto();
            }
        });

    }

    private void pickPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Choose Your Profile Picture!"), PICK_IMAGE_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
        }

        uploadFile();
    }

    private void uploadFile() {

        if(filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading your ugly photo....");
            progressDialog.show();

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            imageFileName = "image_" + timeStamp + ".jpeg";

            uploadRef = mStorageRef.child(userID).child("uploads/" + imageFileName);

            uploadTask = uploadRef.putFile(filePath);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Thanks for clogging our servers", Toast.LENGTH_LONG).show();
                            updateDatabase(uploadRef);
                            //uploadRef.child("users").child(userID).child("profilePicture").setValue("profilePic.png");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage(((int) progress) + "% Uploaded...");
                        }
                    });
        }

        else
        {
            Toast.makeText(getApplicationContext(), "Something is broken!", Toast.LENGTH_LONG).show();
        }
    }

    private void updateDatabase(final StorageReference uLR) {
        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return uLR.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    final String key = userDatabaseRef.push().getKey();
                    String imageKey = key;
                    HashMap<String, Object> updates = new HashMap<>();
                    updates.put("/users/" + userID + "/uploads/" + key, imageFileName);
                    updates.put("/uploads/" + key, downloadUri.toString());

                    FirebaseDatabase.getInstance().getReference().updateChildren(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(MainActivity.this, "Database Updated", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Database upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                }
            }
        });
    }

    private void authenticate() {
        //If user is not logged in, go to login activity
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(this, EmailLoginActivity.class));
        }

        // Collect userID
        user = mAuth.getCurrentUser();

        // If userID is null attempt to log them in again, else set the userID field
        if(user==null) {
            userID = "";
            finish();
            startActivity(new Intent(this, EmailLoginActivity.class));

        }
        else {
            userID = user.getUid();
        }

        //Assign Storage Reference to User's folder
        userFolderStorageRef = mStorageRef.child(userID);
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child(userID).child("uploads");
    }
}
