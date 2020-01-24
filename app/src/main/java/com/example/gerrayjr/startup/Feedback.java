package com.example.gerrayjr.startup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    EditText tv_improve;
    TextView tv_name;
    Button btn;
    ProgressBar pb;

    public static String TAG = "FeedBack";
    private String URL = "http://192.168.43.32:1234/parkpoa/booking-android.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        String parkname = getIntent().getStringExtra("parkName");
        tv_name = findViewById(R.id.name);
        if(!parkname.equals(""))
        {
            Log.d(TAG, "onCreate: Location Name Retrieved");
            tv_name.setText(parkname);
        }
        tv_improve = findViewById(R.id.improve);

        btn = findViewById(R.id.btn_post);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postFeed();
            }
        });

        pb = findViewById(R.id.progress);
    }

    private void postFeed()
    {
        pb.setVisibility(View.VISIBLE);
        btn.setVisibility(View.GONE);

        final String parkName = tv_name.getText().toString().trim();
        final String improvement = tv_improve.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try
                        {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("success");

                            if(success.equals("1"))
                            {
                                Log.d(TAG, "onResponse: Feedback sent..");
                                Toast.makeText(Feedback.this, "feedback Received", Toast.LENGTH_SHORT).show();
                                finish();

                                pb.setVisibility(View.GONE);
                                btn.setVisibility(View.VISIBLE);

                            }
                        }
                        catch (Exception e)
                        {
                            Log.d(TAG, "onResponse: \"Feedback Failed: " +e.getMessage());
                            e.printStackTrace();
                            Toast.makeText(Feedback.this, "Feedback Failed: " + e.toString(), Toast.LENGTH_SHORT).show();
                            pb.setVisibility(View.GONE);
                            btn.setVisibility(View.VISIBLE);

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "onErrorResponse: Feedback Failed: "+ volleyError.getMessage());
                        Toast.makeText(Feedback.this, "Feedback Failed: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                        btn.setVisibility(View.VISIBLE);

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name",parkName);
                params.put("improve",improvement);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}
