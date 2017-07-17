package com.ineed.ybu.ineed_app_lastest;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class Messages extends AppCompatActivity implements  View.OnClickListener {

    ListView usersList;
    TextView noUsersText;
    Button back;

    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    static String body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);


        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(this);
        pd = new ProgressDialog(Messages.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://ineedapp-b621f.firebaseio.com/messages.json";

        final StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                Log.d("Messages class","Request: "+s);
             /*   String[] splitted = s.split("\\t|,|;|\\.|\\?|!|-|:|@|\\[|\\]|\\(|\\)|\\{|\\}|_|\\*|/");
                String s1 = splitted[0];
                String s2 = splitted[1];*/

                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Messages.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

               String user = al.get(position);
                String[] splitted = user.split("_");
                 //String user1 = splitted[0];
                  String user2 = splitted[1];
                    MessageInfo.chatWith = user2;

                Log.d("Messages","Chatwith"+MessageInfo.chatWith);
                startActivity(new Intent(Messages.this, ChatActivity.class));
            }
        });
    }

    public void doOnSuccess(String s){

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        //getting current user

        FirebaseUser user = mAuth.getCurrentUser();


        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
                Log.d("users","key "+key);
                //burdaki if true false ile ilgili olmalı !!
                al.add(key);
                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers < 0){
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            usersList.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al));
        }

        pd.dismiss();
    }


    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
            startActivity(new Intent(Messages.this, SearchActivity.class));
        }
    }
}