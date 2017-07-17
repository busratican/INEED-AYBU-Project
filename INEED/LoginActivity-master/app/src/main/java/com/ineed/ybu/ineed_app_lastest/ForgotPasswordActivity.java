package com.ineed.ybu.ineed_app_lastest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private Button okButton;
    private EditText mailText;
    private FirebaseAuth mAuth;
    private String TAG = "ForgotPasswordActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //firebase instances
        mAuth = FirebaseAuth.getInstance();

        //set instances.
        okButton = (Button) findViewById(R.id.btn_forgotPassword_ok);
        mailText =(EditText) findViewById(R.id.usermail);
        //set click property
        okButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v == okButton)
        {
            resetPasswordEmailSend();

        }
    }

    //click back button and return login.
    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivityForResult(myIntent, 0);
        return true;
    }

    private void resetPasswordEmailSend() {

        //get user mail and send mail them.
        String mail = mailText.getText().toString().trim();

        //send and control if send or getting error.
        mAuth.sendPasswordResetEmail(mail).addOnCompleteListener(
                this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        //success
                        if(task.isSuccessful())
                        {
                            Toast.makeText(ForgotPasswordActivity.this,"Reset link send to mail address !",
                                    Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                        }
                        //not success
                        else if(!task.isSuccessful())
                        {
                            Log.w(TAG,"SendResetEmailTest: failed!",task.getException());
                            Toast.makeText(ForgotPasswordActivity.this,"Reset password email not send !! ERROR !!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
        finish();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
    }
}

