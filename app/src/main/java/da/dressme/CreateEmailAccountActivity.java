package da.dressme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateEmailAccountActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = "CreateEmail";

    private Button registerButton;
    private EditText emailRegisterText;
    private EditText passwordRegisterText;
    private TextView signedupTextView;
    private EditText firstLastNameRegisterText;
    private EditText usernameRegisterText;
    private EditText phoneNumberRegisterText;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_email_account);

        mAuth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        if(mAuth.getCurrentUser() != null)
        {
            //load profile activity instead
            //finish();
            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        registerButton = findViewById(R.id.button_registerButton);
        emailRegisterText = findViewById(R.id.editText_registerEmail);
        passwordRegisterText = findViewById(R.id.editText_registerPassword);
        signedupTextView = findViewById(R.id.textView_alreadyRegistered);
        firstLastNameRegisterText = findViewById(R.id.editText_FirstLastName);
        usernameRegisterText = findViewById(R.id.editText_username);
        phoneNumberRegisterText = findViewById(R.id.editText_phoneNumber);

        registerButton.setOnClickListener(this);
        signedupTextView.setOnClickListener(this);

    }

    private void registerUser() {

        //TODO: Make "Register User" Button unclickable until the fields are filled out correctly

        final String email = emailRegisterText.getText().toString().trim();
        String password = passwordRegisterText.getText().toString().trim();
        final String name = firstLastNameRegisterText.getText().toString().trim();
        final String username = usernameRegisterText.getText().toString().trim();
        final String phone = phoneNumberRegisterText.getText().toString().trim();


        if (TextUtils.isEmpty(email))
        {
            emailRegisterText.setError("How hard can it be to enter an email?");
            return;
        }

        if  (TextUtils.isEmpty(password))
        {
            passwordRegisterText.setError("Enter a password, dumbass");
            return;
        }

        if(TextUtils.isEmpty(name))
        {
            firstLastNameRegisterText.setError("We want to know your name");
            return;
        }

        if  (TextUtils.isEmpty(username))
        {
            usernameRegisterText.setError("Choose an alias please!");
            return;
        }

        if  (TextUtils.isEmpty(phone))
        {
            phoneNumberRegisterText.setError("What's yo numbah?");
            return;
        }


        Log.d(TAG, "registerUser: email: " + email + " pass: " + password);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        {
                            if (task.isSuccessful())
                            {
                                //Start Profile Activity or something
                                databaseRef.child("users").child(mAuth.getUid()).child("email").setValue(email);
                                databaseRef.child("users").child(mAuth.getUid()).child("name").setValue(name);
                                databaseRef.child("users").child(mAuth.getUid()).child("sortName").setValue(name.toLowerCase());
                                databaseRef.child("users").child(mAuth.getUid()).child("username").setValue(username);
                                databaseRef.child("users").child(mAuth.getUid()).child("phone").setValue(phone);

                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                            }

                            else {
                                Toast.makeText(CreateEmailAccountActivity.this, "Registration Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


    }

    @Override
    public void onClick(View view) {
        if (view == registerButton)
        {
            registerUser();
        }

        if (view == signedupTextView)
        {
            finish();
            //go to login page here
            startActivity(new Intent(this, EmailLoginActivity.class));
        }

    }


}

