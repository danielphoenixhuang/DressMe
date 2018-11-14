package da.dressme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static da.dressme.AuthenticationUtil.authenticateAndGetUserID;

public class MainActivity extends AppCompatActivity {

    private Button cameraButton;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser user;
    private String userID;

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
