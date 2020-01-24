package com.example.gerrayjr.startup;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

public class InfoWindow implements GoogleMap.InfoWindowAdapter {
    private final View mWindow;
    private Context mContext;
   // private TextView link_tv;

    public InfoWindow(Context mContext) {
        this.mContext = mContext;
        mWindow = LayoutInflater.from(mContext).inflate(R.layout.activity_info_window,null);

    }

    private void rendowWindowText(Marker marker,View view)
    {
        String title = marker.getTitle();
        TextView tvTitle = (TextView)view.findViewById(R.id.title);

        if(!title.equals(""))
        {
            tvTitle.setText(title);
        }


    }


    @Override
    public View getInfoWindow(Marker marker) {
        rendowWindowText(marker,mWindow);
        return mWindow;
    }

    @Override
    public View getInfoContents(Marker marker) {
        rendowWindowText(marker,mWindow);
        return mWindow;
    }
}
