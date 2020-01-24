package com.example.gerrayjr.startup;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Receipt extends AppCompatActivity {
    TextView venueName;
    TextView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.land_activity_receipt);

        String venuename = getIntent().getStringExtra("parkName");

        venueName = (TextView)findViewById(R.id.rec_name);
        if(!venueName.equals(""))
        {
            venueName.setText(venuename);
        }

        back = findViewById(R.id.back_menu);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapBack = new Intent(Receipt.this,MapActivity.class);
                startActivity(mapBack);
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog builder = new AlertDialog.Builder(Receipt.this)
                .setTitle(R.string.app_name)
                .setIcon(R.drawable.applogo)
                .setMessage("Write a Parking FeedBack Before you Leave? ")
                .setCancelable(true)
                .setPositiveButton("Okay,I will", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String venuename = getIntent().getStringExtra("parkName");
                        Intent alert = new Intent(Receipt.this,Feedback.class);
                        alert.putExtra("parkName",venuename);
                        startActivity(alert);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent refusal = new Intent(Receipt.this, BookingInfo.class);
                        startActivity(refusal);
                    }
                })
                .show();
    }
}
