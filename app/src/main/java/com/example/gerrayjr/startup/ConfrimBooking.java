package com.example.gerrayjr.startup;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
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

public class ConfrimBooking extends AppCompatActivity {

    public static String TAG = "ConfirmBooking";
    private String URL = "http://192.168.43.32:1234/parkpoa/testing.php";
    private EditText car_plate;
    private Spinner model,rate;
    ProgressBar pb;
    Button btnBook;
    TextView venueId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confrim_booking);

        String venueName = getIntent().getStringExtra("parkName");
        final int parkId = getIntent().getIntExtra("venueID",0);
        //Toast.makeText(this, "ParkID: " + parkId, Toast.LENGTH_SHORT).show();
        venueId = (TextView)findViewById(R.id.parkid);
        if(parkId !=0)
        {
           // Toast.makeText(this, "ParkID is " +parkId, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onCreate: parkID obtained");
            venueId.setText(String.valueOf(parkId));
        }

        car_plate = findViewById(R.id.bk_plateNo);
        rate = (Spinner)findViewById(R.id.bk_rate);
        model = (Spinner)findViewById(R.id.bk_models);
        pb = (ProgressBar)findViewById(R.id.progress_booking);
        btnBook = (Button)findViewById(R.id.btn_book);



        btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookPark();
            }
        });
    }

    private void bookPark()
    {
        pb.setVisibility(View.VISIBLE);
        btnBook.setVisibility(View.GONE);

        final String carPlate = car_plate.getText().toString().trim();
        final String carType = model.getSelectedItem().toString().trim();
        final String duration = rate.getSelectedItem().toString().trim();
        final String parkident = venueId.getText().toString();

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
                            Log.d(TAG, "onResponse: Successful Booking...");
                            Toast.makeText(ConfrimBooking.this, "Successful Booking", Toast.LENGTH_SHORT).show();

                            String venueName = getIntent().getStringExtra("parkName");
                            Intent intent = new Intent(ConfrimBooking.this, Receipt.class);
                            intent.putExtra("parkName",venueName);
                            startActivity(intent);

                            pb.setVisibility(View.GONE);
                            btnBook.setVisibility(View.VISIBLE);

                        }else
                        {
                            Log.d(TAG, "onResponse: Unsuccessful Booking...");
                            Toast.makeText(ConfrimBooking.this, "Wrong Formaat of Car Plate Input", Toast.LENGTH_LONG).show();
                            pb.setVisibility(View.GONE);
                            btnBook.setVisibility(View.VISIBLE);
                        }
                    }
                    catch (Exception e)
                    {
                        Log.d(TAG, "onResponse: UnSuccessful login: " +e.getMessage());
                        e.printStackTrace();
                        Toast.makeText(ConfrimBooking.this, "Booking Failed: " + e.toString(), Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                        btnBook.setVisibility(View.VISIBLE);

                    }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(TAG, "onErrorResponse: UnSuccessful Login: "+ volleyError.getMessage());
                        Toast.makeText(ConfrimBooking.this, "Booking Failed: " + volleyError.toString(), Toast.LENGTH_SHORT).show();
                        pb.setVisibility(View.GONE);
                        btnBook.setVisibility(View.VISIBLE);

                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("plateNo",carPlate);
                params.put("carType",carType);
                params.put("duration",duration);
                params.put("parkId",parkident);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

}
