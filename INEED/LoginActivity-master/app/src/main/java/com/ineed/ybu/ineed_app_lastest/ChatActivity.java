package com.ineed.ybu.ineed_app_lastest;

/**
 * Created by Büşra GÜL on 10.06.2017.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ChatActivity extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;
    ImageButton backButton;


    /************pop-up***********************/


    private PopupWindow pw;

    /**********************************************/
    public static String[] messageArray = new  String[100];
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        backButton =(ImageButton)  findViewById(R.id.BackButton);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //getting current user
        final FirebaseUser user = mAuth.getCurrentUser();

        //  MessageInfo msg = new MessageInfo();
        MessageInfo.name = SearchActivity.u_name;
        MessageInfo.surname = SearchActivity.u_surname;



        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://ineedapp-b621f.firebaseio.com/messages/" + MessageInfo.name + " " + MessageInfo.surname + "_" + MessageInfo.chatWith);
        reference2 = new Firebase("https://ineedapp-b621f.firebaseio.com/messages/" + MessageInfo.chatWith + "_" + MessageInfo.name + " " + MessageInfo.surname);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                initiatePopupWindow(v);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", MessageInfo.name + " " + MessageInfo.surname);
                    Log.d("ChatActivity", "Message:" + messageText);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);


                    messageArea.setText("");
                    final MyFirebaseInstanceIdService notificationSender = new MyFirebaseInstanceIdService();
                    // final MyFirebaseMessagingService message = new MyFirebaseMessagingService();

                    // Write a message to the database
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    final DatabaseReference myRef = database.getReference();

                    if (UserProfile.user_id != null) {


                        //Read from the database
                        myRef.child("tokens").child(UserProfile.user_id).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
                                NotificationInformation post = dataSnapshot.getValue(NotificationInformation.class);

                                if (post.userid.equals(UserProfile.user_id)) {
                                    try {
                                        notificationSender.sendAndroidNotification(post.token, ("You have a new message from " + SearchActivity.u_name + " " + SearchActivity.u_surname), "INEED APP");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                                else {
                                    try {
                                        notificationSender.sendAndroidNotification(post.token,("You have a new message from "+ MessageInfo.chatWith), "INEED APP");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w("ChatActivity", "Failed to read value.", error.toException());
                            }
                        });
                    }
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                Log.d("Chat Activity","chatWith:"+MessageInfo.chatWith);

                messageArray[0]=message;
                if(userName.equals(MessageInfo.name+" "+MessageInfo.surname)){
                    addMessageBox(userName+"\n\n" + message, 1);
                }
                else{
                    Log.d("ChatActivity","Buraya girdim");
                    addMessageBox(MessageInfo.chatWith + "\n\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(ChatActivity.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }


    private void initiatePopupWindow(View v) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) ChatActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            TextView mResultText = (TextView) layout.findViewById(R.id.question_text);
            Button cancelButton = (Button) layout.findViewById(R.id.cancel_button);
            Button yesButton = (Button) layout.findViewById(R.id.yes_button);
            cancelButton.setOnClickListener(cancel_button_click_listener);
            yesButton.setOnClickListener(yes_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
            startActivity(new Intent(ChatActivity.this,SearchActivity.class));
        }
    };

    private View.OnClickListener yes_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            //cancel notification
            Toast.makeText(getApplicationContext(), "Your item deleted from notifications list. Thank you !", Toast.LENGTH_LONG).show();
            pw.dismiss();
            startActivity(new Intent(ChatActivity.this,RateActivity.class));
        }
    };
}