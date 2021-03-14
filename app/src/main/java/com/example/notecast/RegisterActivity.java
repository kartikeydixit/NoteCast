package com.example.notecast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private FirebaseAuth mAuth;
    private Button registerBtn;
    private EditText name , age , email , password;
    private TextView loginBtn;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();

        name =(EditText) findViewById(R.id.enterName);
        age =(EditText)  findViewById(R.id.enterAge);
        email = (EditText) findViewById(R.id.enterEmail);
        password =(EditText)  findViewById(R.id.enterPassword);

        loginBtn = (TextView) findViewById(R.id.login);

        progressBar = findViewById(R.id.progressBar);

        registerBtn = findViewById(R.id.register);
        registerBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.login :
                startActivity(new Intent(this,MainActivity.class));
                this.finish();
                break;

            case R.id.register:
                registerUser();
                break;
                
        }

    }

    private void registerUser() {
        String getEmail = email.getText().toString().trim();
        String getName = name.getText().toString().trim();
        String getPassword  = password.getText().toString().trim();
        String getAge = age.getText().toString().trim();

        if(getAge.isEmpty()){
            age.setError("Age is Required");
            age.requestFocus();
            return;
        }
        if(getEmail.isEmpty()){
            email.setError("Email is Required");
            email.requestFocus();
            return;
        }
//        if(!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()){
//            email.setError("Please provide valid email ID");
//            email.requestFocus();
//            return;
//        }
        if(getName.isEmpty()){
            name.setError("Name is Required");
            name.requestFocus();
            return;
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
        mAuth.createUserWithEmailAndPassword(getEmail,getPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(getName , getAge , getEmail);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Registration Successfully", Toast.LENGTH_SHORT).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, "1st", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(RegisterActivity.this, "2nd", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }
                    }
                });



    }
}