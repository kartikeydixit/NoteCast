package com.example.notecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView registerBtn;
    private EditText email, password ;
    private Button loginBtn;
    private FirebaseAuth mauth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);

        loginBtn = (Button) findViewById(R.id.loginBtn);
        loginBtn.setOnClickListener(this);

        mauth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.enterEmail);
        password = (EditText) findViewById(R.id.enterPassword);

        progressBar = (ProgressBar)findViewById(R.id.loginProgressBar);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerBtn :
                startActivity(new Intent(this , RegisterActivity.class));
                this.finish();
                break;

            case  R.id.loginBtn :
                loginUser();
                break;
        }
    }

    private void loginUser() {
        final String getEmail = email.getText().toString().trim();
        final String getPassword = password.getText().toString().trim();

        if(getEmail.isEmpty()){
            email.setError("Email is Required");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()){
            email.setError("Please enter a valid email ID");
        }
        if(getPassword.isEmpty()){
            password.setError("Password is Required");
            password.requestFocus();
            return;
        }
        if(getPassword.length() < 6){
            password.setError("Password must be of more than 6 characters");
            password.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mauth.signInWithEmailAndPassword(getEmail,getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //redirect to homepage
                    startActivity(new Intent(MainActivity.this,HomePage.class));
                    progressBar.setVisibility(View.GONE);
                    finish();
                }
                else {
                    Toast.makeText(MainActivity.this, "Login Credentials are wrong\nPlease try again", Toast.LENGTH_LONG).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}