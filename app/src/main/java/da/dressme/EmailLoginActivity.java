package da.dressme;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class EmailLoginActivity extends AppCompatActivity implements View.OnClickListener{


    private Button loginButton;
    private EditText editTextLoginEmail;
    private EditText editTextPasswordEmail;
    private TextView textViewSignUp;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_login);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null)
        {
            //load profile activity instead
            finish();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }

        editTextLoginEmail = findViewById(R.id.editText_emailLoginField);
        editTextPasswordEmail = findViewById(R.id.editText_emailPasswordField);
        loginButton = findViewById(R.id.button_loginButton);
        textViewSignUp = findViewById(R.id.textView_signupPage);

        loginButton.setOnClickListener(this);
        textViewSignUp.setOnClickListener(this);

    }

    private void login() {
        String email = editTextLoginEmail.getText().toString().trim();
        String password = editTextPasswordEmail.getText().toString().trim();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "YOU FOOL!", Toast.LENGTH_SHORT).show();
            //If either field is not filled in
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if (view == loginButton)
        {
            login();
        }

        if (view == textViewSignUp)
        {
            finish();
            //go to create email account activity
            startActivity(new Intent(this, CreateEmailAccountActivity.class));
        }


    }

}

