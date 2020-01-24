package com.example.gerrayjr.startup;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {
    LinearLayout lay1,lay2;
    Animation uptodown,downtoup;

    private static int SPLASH_TIME = 5000; //This is 4 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lay1 = findViewById(R.id.llayout1);
        lay2 = findViewById(R.id.llayout2);

        uptodown = AnimationUtils.loadAnimation(this,R.anim.uptodown);
        lay1.setAnimation(uptodown);

        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup);
        lay2.setAnimation(downtoup);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent mySuperIntent = new Intent(MainActivity.this, BeforeMAp.class);
                startActivity(mySuperIntent);
                finish();
            }
        }, SPLASH_TIME);
    }
    @Override
    public void onBackPressed() {
        AlertDialog builder = new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.app_name)
                .setIcon(R.drawable.applogo)
                .setMessage("Write a Parking FeedBack Before you Leave? ")
                .setCancelable(true)
                .setPositiveButton("Okay,I will", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent alert = new Intent(MainActivity.this,Feedback.class);
                        startActivity(alert);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();
    }

}
