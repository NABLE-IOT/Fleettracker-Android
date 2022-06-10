package com.example.mqttgpsn_able;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    public LocationManager locationmanager;
    private Handler mHandler = new Handler();
    TextView curruntlocation;
    EditText delay;
    EditText clientID;
    EditText brockerurl;
    String lat,longt;
    int seconds=30;
    boolean connectionokflag=false;
    double LG,LA,lstLG,LastLA,speed;
    float distence[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        curruntlocation = findViewById(R.id.curruntlocation);
        delay = findViewById(R.id.delay);
        brockerurl=findViewById(R.id.brockerurl);
        clientID=findViewById(R.id.clientID);

        locationmanager= (LocationManager) getSystemService(LOCATION_SERVICE);
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION , Manifest.permission.ACCESS_FINE_LOCATION},1);}

  locationmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 5, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
               LG = location.getLatitude();
               LA = location.getLongitude();
               speed=location.getSpeed();
              // location.distanceBetween(LastLA,lstLG,LA, LG,distence);

                lat = String.format("%.6f", LG);
                longt = String.format("%.6f", LA);
                curruntlocation.setText(lat + ", " + longt);
            }
        });
    }



    public void startRepeating(View v) {

        seconds=Integer.parseInt(delay.getText().toString());

        Toast.makeText(MainActivity.this,"connected",Toast.LENGTH_LONG).show();
                    connectionokflag=true;
        mToastRunnable.run();

    }
    public void stopRepeating(View v) {
        mHandler.removeCallbacks(mToastRunnable);
        curruntlocation.setText("");



    }



 private Runnable mToastRunnable = new Runnable() {
        @Override
        public void run() {
            requestHTTP();
            mHandler.postDelayed(this, seconds * 1000);
        }
    };


    private void requestHTTP() {
        OkHttpClient client = new OkHttpClient();
        String url=brockerurl.getText().toString();
        String clientId=clientID.getText().toString();
        String tt="afsaf";
        Long tsLong = System.currentTimeMillis();
        String ts = tsLong.toString();
        lstLG=LG;LastLA=LA;
        url+="?data={long:'"+longt+"',lat:'"+lat+"',sped:"+speed+",dist:0,tistm:'"+ts+"',VID:'"+clientId+"'}";

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull okhttp3.Response response) throws IOException {

            }

            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                e.printStackTrace();
            }

//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()) {
//                    final String myResponse = response.body().string();
//
//                    MapsActivity.this.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                JSONObject jsonObj = new JSONObject(myResponse);
//                                addnew_MAPmarks(jsonObj);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//                }
//            }
        });
    }

}