package com.ineed.ybu.ineed_app_lastest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RateActivity extends AppCompatActivity implements View.OnClickListener{

    Button okBtn;
    TextView name,surname,rate;
    public int current_rate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate);


        okBtn = (Button) findViewById(R.id.btnOk);
        okBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==okBtn)
        {
            startActivity(new Intent(RateActivity.this,SearchActivity.class));
        }
    }

}
