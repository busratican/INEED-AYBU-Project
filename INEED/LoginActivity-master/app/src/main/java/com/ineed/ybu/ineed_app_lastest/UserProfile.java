package com.ineed.ybu.ineed_app_lastest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity implements View.OnClickListener{
    TextView name, surname, department, year,pleasure;
    Button btn_ok, message_button;
    FirebaseAuth mAuth;
    static String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        name = (TextView) findViewById(R.id.tvName);
        surname = (TextView) findViewById(R.id.tvSurname);
        department = (TextView) findViewById(R.id.tvDepartment);
        year = (TextView) findViewById(R.id.tvClass);
        pleasure = (TextView) findViewById(R.id.tvPlrate);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        message_button = (Button) findViewById(R.id.btn_message);

        btn_ok.setOnClickListener(this);
        message_button.setOnClickListener(this);
        /**********************************/
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();




        NotificationBodyInfo notificationBodyInfo = new NotificationBodyInfo();

        Log.d("UserProfile","Body "+notificationBodyInfo.notification_body);
        // Read from the database
        myRef.child("Body").child(NotificationsActivity.body).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("profile", "girdim aq.");
                NotificationBodyInfo value = dataSnapshot.getValue(NotificationBodyInfo.class);
                user_id = value.user_id;
                Log.d("UserProfile", "User_id" + user_id);

                // Read from the database
                myRef.child("Users").child(user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // This method is called once with the initial value and again
                        // whenever data at this location is updated.
                        UserInformation value = dataSnapshot.getValue(UserInformation.class);

                        name.setText(value.name);
                        Log.d("ProfileActivity", "Value is: " + value.name);
                        surname.setText(value.surname);
                        department.setText("Department: "+value.department);
                        year.setText("Class: "+value.clas);
                        pleasure.setText("Pleasure rate: "+(int) value.pleasure_rate);
                        MessageInfo.chatWith = value.name + " " + value.surname;
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Failed to read value
                        Log.w("ProfileActivity", "Failed to read value.", error.toException());
                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });
    }
            @Override
    public void onClick(View v) {
        if (v == btn_ok) {
            finish();
            startActivity(new Intent(UserProfile.this, SearchActivity.class));
        }

       if (v == message_button) {
            finish();
            startActivity(new Intent(getApplicationContext(), ChatActivity.class));
        }
    }
}
