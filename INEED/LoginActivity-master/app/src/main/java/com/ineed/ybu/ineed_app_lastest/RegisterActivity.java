package com.ineed.ybu.ineed_app_lastest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText emailText,passwordText;
    private TextView loginText;
    private Button registerButton;
    boolean isOK=true;

    //for Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();
        //get and set the instances.
        emailText = (EditText) findViewById(R.id.usermail);
        passwordText =(EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.registerButton);
        //loginText =(TextView) findViewById(R.id.login);

        //set click to register button
        registerButton.setOnClickListener(this);
        //loginText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == registerButton)
        {
            firebaseAuthentication();
        }
     /*   if(v==loginText)
        {
            finish();
            startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
        }*/
    }

    //click back button and return login.
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    public void firebaseAuthentication() {
        //get the email and password for save firebase authentcation.
        String email = emailText.getText().toString().trim();
        String password = passwordText.getText().toString().trim();

        //checking if email and passwords are empty
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
         //   isOK = false;
            return;
        } else if (email.length() < 19) {
            emailText.setError("email too short");
         //   isOK = false;
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_LONG).show();
           // isOK = false;
            return;
        } else if (password.length() < 5) {
            passwordText.setError("at least 5 characters long");

            for(int i= 0; i<password.length(); i++)
            {

                if(!Character.isLetter(password.charAt(i)))
                {
                    passwordText.setError("password should contains letter");
                    isOK = false;
                    return;
                }
            }
            //isOK = false;
            return;

        }



        if (!email.contains("@ybu.edu.tr")) {
            Toast.makeText(this, "Please enter example@ybu.edu.tr", Toast.LENGTH_LONG).show();
           // isOK = false;
            return;
        }


            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                    this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // If sign in fails, display a message to the user. If sign in succeeds
                            // the auth state listener will be notified and logic to handle the
                            // signed in user can be handled in the listener.

                            if (task.isSuccessful()) {
                                //if Successfull verify email and verify email screen.
                                sendVerificationEmailToUser();
                            } else if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Authentication Failed !!!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
            );
        }

    //for sending verification email
    public void sendVerificationEmailToUser()
    {
        //get the current user.
        final FirebaseUser current_user = mAuth.getCurrentUser();

        current_user.sendEmailVerification().addOnCompleteListener(
                this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //if email send

                        if(task.isSuccessful())
                        {
                            finish();
                            Toast.makeText(RegisterActivity.this, "Verification email send ! ",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),RegisterActivityUserInfo.class));
                        }
                        //if error
                        else if(!task.isSuccessful())
                        {
                            Toast.makeText(RegisterActivity.this, "Verification email not send ! ERROR !!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }


}
