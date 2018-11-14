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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static da.dressme.AuthenticationUtil.authenticateAndGetUserID;

public class MainActivity extends AppCompatActivity {

    private Button cameraButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private String userID;

    private StorageReference uploadRef;
    private StorageReference storageRef = FirebaseStorage.getInstance().getReference();

    private UploadTask uploadTask;

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

            uploadRef = storageRef.child(userID).child("uploads");

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
            final String imageFileName = "image_" + timeStamp + ".jpeg";

            uploadTask = uploadRef.putFile(filePath);
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Thanks for clogging our servers", Toast.LENGTH_LONG).show();

                            uploadRef.child("users").child(userID).child("profilePicture").setValue("profilePic.png");
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
    }
}
