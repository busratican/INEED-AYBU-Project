package com.ineed.ybu.ineed_app_lastest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener {

    public final static String AUTH_KEY_FCM =
            "AAAAeQj7Ud0:APA91bHZeqOi96N0zC36obW1kWv7SigN-zcokRaM9_GgQ6DWenUomvu2w8cHiMY_lsfxB4373N9Sj2I6-2yRzQdJF4BuA515tztRIfIdLJ7FhC9z4_pqCophzpTb8leYt6stFGfXrXKz";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    private static final int SIGN_IN_REQUEST_CODE = 100;
    private TextView nameText, surnameText, locationText;
    private Button  notificationButton, messageButton,profileButton,btnLogout, searchButton;
    private EditText search_text;
    //private ImageView profilePic;
    private String notificationBody;
    private FirebaseAuth mAuth;
    public String username;
    private String TAG = "MainPageActivity";
    static String u_name,u_surname;
    static String  message_body;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        //initializing firebase authentication object
        mAuth = FirebaseAuth.getInstance();

        nameText = (TextView) findViewById(R.id.tv_mainPage_Name);
        surnameText = (TextView) findViewById(R.id.tv_mainPage_surname);
        locationText = (TextView) findViewById(R.id.tv_mainPage_location);

        search_text = (EditText) findViewById(R.id.et_mainPage_searchText);

        messageButton = (Button) findViewById(R.id.btn_messages) ;
        searchButton = (Button) findViewById(R.id.searchButton);
        notificationButton = (Button) findViewById(R.id.btn_mainPage_notifications);
        profileButton = (Button)findViewById(R.id.btn_Profile);
        btnLogout = (Button) findViewById(R.id.buttonLogout);
        btnLogout.setOnClickListener(this);


        //getting current user
        FirebaseUser user = mAuth.getCurrentUser();

        if (!user.isEmailVerified()) {
            Toast.makeText(SearchActivity.this, "Verify email first !!", Toast.LENGTH_SHORT).show();
        } else if (user.isEmailVerified()) {
            searchButton.setOnClickListener(this);
            profileButton.setOnClickListener(this);
            notificationButton.setOnClickListener(this);
            messageButton.setOnClickListener(this);
            // Get token
            String token = FirebaseInstanceId.getInstance().getToken();

            //store the id from firebase database


            NotificationInformation notificationInformation = new NotificationInformation(user.getUid(), token);

            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference();
            myRef.child("tokens").child(user.getUid()).setValue(notificationInformation);
            // [START subscribe_topics]
            FirebaseMessaging.getInstance().subscribeToTopic("ineed");
            // [END subscribe_topics]
            //displaying a success toast
            Toast.makeText(this, "Notification Information Saved...", Toast.LENGTH_LONG).show();
        }


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();

        // Read from the database
        myRef.child("Users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                UserInformation value = dataSnapshot.getValue(UserInformation.class);
                nameText.setText(value.name);
                surnameText.setText(value.surname);
                locationText.setText(value.location);
                u_name = value.name;
                u_surname = value.surname;

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        if(FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Start sign in/sign up activity
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .build(),
                    SIGN_IN_REQUEST_CODE
            );
        } else {
            // User is already signed in. Therefore, display
            // a welcome Toast
            Toast.makeText(this,
                    "Welcome " + FirebaseAuth.getInstance()
                            .getCurrentUser().getUid(),
                    Toast.LENGTH_LONG)
                    .show();
        }

    }

    @Override
    public void onClick(View v) {
        if (v == searchButton) {


            // Get token
            final String token = FirebaseInstanceId.getInstance().getToken();

            //store the id from firebase database

            final FirebaseUser user = mAuth.getCurrentUser();

            // Log and toast
            String msg = getString(R.string.msg_token_fmt, token);
            Log.d(TAG, msg);
            Toast.makeText(SearchActivity.this, msg, Toast.LENGTH_SHORT).show();

             final MyFirebaseInstanceIdService notificationSender = new MyFirebaseInstanceIdService();
             message_body = search_text.getText().toString();
            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference();

            try {

                // Read from the database
                myRef.child("tokens").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        NotificationInformation post = dataSnapshot.getValue(NotificationInformation.class);

                            try {
                                if(post.userid.equals(user.getUid())) {
                                    notificationSender.sendAndroidNotification(post.token, u_name+" "+u_surname+" need " +message_body, "INEED APP");
                                    NotificationBodyInfo notificationBodyInfo = new NotificationBodyInfo(user.getUid(),message_body,token);
                                    // Write a message to the database
                                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                                    DatabaseReference myRef = database.getReference();
                                    myRef.child("Body").child(message_body).setValue(notificationBodyInfo);

                                }

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w(TAG, "Failed to read value.", error.toException());
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
/*********************************************************************/

    }


      if (v == notificationButton) {

          startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                    finish();

        }
        if(v == profileButton)
        {

            startActivity(new Intent(SearchActivity.this, ProfileActivity.class));
            finish();
        }

        if (v == btnLogout) {

            AuthUI.getInstance().signOut(this)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(SearchActivity.this,
                                    "You have been signed out.",
                                    Toast.LENGTH_LONG)
                                    .show();

                            // Close activity
                            finish();
                        }
                    });
        }

        if( v== messageButton)
        {

            startActivity(new Intent(SearchActivity.this, Messages.class));
            finish();
        }
    }

}
