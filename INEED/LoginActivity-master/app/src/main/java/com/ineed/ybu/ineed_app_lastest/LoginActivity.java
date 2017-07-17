package com.ineed.ybu.ineed_app_lastest;

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

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText emailText,passwordText;
    private Button loginButton;
    private TextView createAccount, forgotPassword;
    private String TAG = "LoginActivity";


    //firebase
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //firebase
        mAuth = FirebaseAuth.getInstance();


        //get instances

        emailText =(EditText) findViewById(R.id.usermail);
        passwordText =(EditText) findViewById(R.id.password);
        createAccount = (TextView) findViewById(R.id.register);
        forgotPassword = (TextView) findViewById(R.id.forgotpass);
        loginButton = (Button) findViewById(R.id.loginButton);

        //set click property.
        createAccount.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == loginButton)
        {
            user_login();
        }
        else if(v==createAccount)
        {
            open_createAccount();
        }
        else if(v==forgotPassword)
        {
            open_forgotPassword();
        }
    }

    public void open_createAccount()
    {
        finish();
        startActivity(new Intent(this,RegisterActivity.class));
    }

    public void open_forgotPassword()
    {
        finish();
        startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
    }

    public void user_login()
    {
        //get email and password.
        String email= emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }

        if(!email.contains("@ybu.edu.tr"))
        {
            Toast.makeText(this,"Please enter example@ybu.edu.tr",Toast.LENGTH_LONG).show();
            return;
        }



        //user logging via Firebase checking.
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(
                this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //If sign in is Succesfull
                        if(task.isSuccessful())
                        {

                            //open the save user information file
                            finish();
                            startActivity(new Intent(getApplicationContext(),SearchActivity.class));

                        }
                        else if (!task.isSuccessful())
                        {
                            Log.w(TAG,"signInWithEmail: failed!",task.getException());
                            Toast.makeText(LoginActivity.this,"Please check your email/password.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }
}
