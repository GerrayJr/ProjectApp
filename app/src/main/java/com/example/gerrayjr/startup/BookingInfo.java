package com.example.gerrayjr.startup;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.internal.gmsg.HttpClient;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;

public class BookingInfo extends AppCompatActivity {

    private static final String TAG = "BookingInfo";

    RequestQueue rq;
    Button bExit,bBooking;
    TextView location,brief,hour,day,security,survey,operate,capacity;
    ArrayAdapter<String> adapter;

    String sBrief;
    String sHour;
    String sDay;
    String sSecurity;
    String sSurvey;
    String sOperate;
    int sCapacity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_info);
        new connection().execute();
        Log.d(TAG, "onCreate: Activity created");

        rq = Volley.newRequestQueue(this);



        final String venueName = getIntent().getStringExtra("title");

        location = (TextView)findViewById(R.id.textView);
        if(!venueName.equals(""))
        {
            Log.d(TAG, "onCreate: Location Name Retrieved");
            location.setText(venueName);
        }

        brief = (TextView)findViewById(R.id.tv_brief);
        hour = (TextView)findViewById(R.id.tv_hour);
        day = (TextView)findViewById(R.id.tv_day);
        security = (TextView)findViewById(R.id.tv_security);
        survey = (TextView)findViewById(R.id.tv_survey);
        operate = (TextView)findViewById(R.id.tv_operate);
        capacity = (TextView)findViewById(R.id.tv_capacity);


        final Integer parkingId = getIntent().getIntExtra("venueID",0);
        //Toast.makeText(this, "ParkID: " + parkingId, Toast.LENGTH_SHORT).show();
        if(sCapacity != 0)
        {
            bBooking = (Button)findViewById(R.id.btn_confirm_booking);
            bBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Integer venueID = parkingId;
                    Intent confirm = new Intent(BookingInfo.this,ConfrimBooking.class);
                    confirm.putExtra("venueID",venueID);
                    confirm.putExtra("parkName",venueName);
                    startActivity(confirm);
                }
        });
            }else
                {
                    Toast.makeText(this, "Capacity is equal to Zero, You cannot register", Toast.LENGTH_SHORT).show();
                }



        bExit = (Button)findViewById(R.id.btn_exit);
        bExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exitIntent = new Intent(BookingInfo.this,MapActivity.class);
                startActivity(exitIntent);
            }
        });

    }


    class connection extends AsyncTask<String, String, String>
    {
        Integer parkId = getIntent().getIntExtra("parkId",0);
        @Override
        protected String doInBackground(String... strings) {
            //int parkId = getIntent().getIntExtra("parkId",0);
            String result = "";
            String host = "http://192.168.43.32:1234/parkpoa/requestInfo"+ parkId +".php";
            try
            {
                org.apache.http.client.HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(host));
                HttpResponse response = client.execute(request);
                BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

                StringBuffer stringBuffer = new StringBuffer("");
                String line = "";
                while ((line = reader.readLine())!= null)
                {
                    stringBuffer.append(line);
                    break;
                }
                reader.close();
                result = stringBuffer.toString();
            }catch (Exception e)
            {
                Log.d(TAG, "doInBackground: There has been an exception: " + e.getMessage());
            }
            return result;
        }

        protected void onPostExecute(String result)
        {
            try
            {
                JSONObject jsonResult = new JSONObject(result);
                int success = jsonResult.getInt("success");

                if(success == 1)
                {
                    JSONArray parking = jsonResult.getJSONArray("parking");
                    for(int i=0;i < parking.length(); i++)
                    {
                        JSONObject park = parking.getJSONObject(i);

                        sBrief = park.getString("briefdescription");
                        sDay = park.getString("priceday");
                        sHour = park.getString("pricehour");
                        sSecurity = park.getString("securityoption");
                        sSurvey = park.getString("surveillanceoption");
                        sOperate = park.getString("hoursoption");
                        sCapacity = park.getInt("capacity");

                        brief.setText(sBrief);
                        day.setText(sDay);
                        hour.setText(sHour);
                        security.setText(sSecurity);
                        survey.setText(sSurvey);
                        operate.setText(sOperate);
                        capacity.setText(String.valueOf(sCapacity));

                    }
                }
                else
                {
                    Toast.makeText(BookingInfo.this, "Infomation Not Obtained", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onPostExecute: Information not fetched" );
                }

            }catch (JSONException e)
            {
                Toast.makeText(BookingInfo.this, "JsonException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }


    }

}
