package com.example.yoadrachelhezimoran;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    Button login;
    Button gotoSignUp;
    Button signUp;
    EditText email;
    EditText password;
    String userEmail;
    String userPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = findViewById(R.id.loginButton);
        gotoSignUp = findViewById(R.id.gotoSignUp);
        signUp = findViewById(R.id.signUp4Real);
        email = findViewById(R.id.loginEmail);
        password = findViewById(R.id.loginPassword);
        mAuth = FirebaseAuth.getInstance();

        login.setVisibility(View.VISIBLE);
        gotoSignUp.setVisibility(View.VISIBLE);
        signUp.setVisibility(View.GONE);
    }

    public void goToSignUpScreen(View view) {
        login.setVisibility(View.GONE);
        gotoSignUp.setVisibility(View.GONE);
        signUp.setVisibility(View.VISIBLE);
    }

    // using startActivity here and not from main activity.
    // tried using start activity for result and it was deprecated...
    // so we saw no reason to start from the main screen app
    public void signIn(View view){
        getEmailAndPassword();
        // check to see if data was entered
        if (userEmail.isEmpty() || userPassword.isEmpty())
        {
            Toast.makeText(LoginActivity.this, "Authentication failed.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void signUp(View view){
        getEmailAndPassword();
        // check to see if data was entered, if no check app crashes
        if (userEmail.isEmpty() || userPassword.isEmpty())
        {
            Toast.makeText(LoginActivity.this, "sign up failed, must fill all details.",
                    Toast.LENGTH_LONG).show();
            return;
        }
        mAuth.createUserWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Write a message to the database
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign up fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, "sign up failed.",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void getEmailAndPassword(){
        userEmail = email.getText().toString();
        userPassword = password.getText().toString();
    }

}