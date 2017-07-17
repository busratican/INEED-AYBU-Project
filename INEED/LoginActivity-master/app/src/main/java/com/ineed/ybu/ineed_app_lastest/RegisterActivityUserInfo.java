package com.ineed.ybu.ineed_app_lastest;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class RegisterActivityUserInfo extends AppCompatActivity implements View.OnClickListener{

    private EditText nameText,surnameText,departmentText;
    private Button okButton;
    private Spinner classSpinner;
    private String selected_class;
    private FirebaseAuth mAuth;
    private boolean isComplete=true;


    //for spinner class
    ArrayList<String> objects = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user_info);


        //firebase
        mAuth =FirebaseAuth.getInstance();

        nameText=(EditText)findViewById(R.id.et_register2_name);
        surnameText =(EditText)findViewById(R.id.et_register2_surname);
        departmentText =(EditText)findViewById(R.id.et_register2_department);
        okButton =(Button) findViewById(R.id.btn_register2_create);
        classSpinner = (Spinner) findViewById(R.id.et_register2_spinner);

        // classSpinner.setOnClickListener(this);
        okButton.setOnClickListener(this);


        //call for Spinner
        selectClass();

    }



    @Override
    public void onClick(View v) {

        if(v == okButton && isComplete == true)
        {

            //Getting values from database
            String name = nameText.getText().toString();
            String surname = surnameText.getText().toString();
            String department = departmentText.getText().toString();

            //checking if email and passwords are empty
            if(name.matches("") ||  name.length()<=0){
                Toast.makeText(this,"Please enter name",Toast.LENGTH_LONG).show();
                isComplete=false;
                return;
            }

            if(surname.matches("") || surname.length()<=0){
                Toast.makeText(this,"Please enter surname",Toast.LENGTH_LONG).show();
                isComplete=false;
                return;

            }

            if(department.matches("") || department.length()<=0){
                Toast.makeText(this,"Please enter department",Toast.LENGTH_LONG).show();
                isComplete=false;
                return;

            }


            isComplete = true;
            

            if(isComplete == true) {
                // String genre = selected_radioButton.getText().toString();
                //getting the current logged in user
                FirebaseUser user = mAuth.getCurrentUser();

                //creating a userinformation object
                final UserInformation userInformation = new UserInformation(user.getUid(), name, surname, department, selected_class, "Etlik Batı Kampüsü", 100.0f);

                // Write a message to the database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference();
                myRef.child("Users").child(user.getUid()).setValue(userInformation);
                //displaying a success toast
                Toast.makeText(this, "User Information Saved...", Toast.LENGTH_LONG).show();



                finish();
                startActivity(new Intent(this,SearchActivity.class));
            }


        }

    }


    public void selectClass()
    {

        objects.add("1");
        objects.add("2");
        objects.add("3");
        objects.add("4");
        // add hint as last item
        objects.add("Select Class");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, objects);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        classSpinner.getBackground().setColorFilter(ContextCompat.getColor(this, R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        classSpinner.setAdapter(dataAdapter);
        classSpinner.setSelection(0);
        classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_class = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(RegisterActivityUserInfo.this, "Select Class !", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
