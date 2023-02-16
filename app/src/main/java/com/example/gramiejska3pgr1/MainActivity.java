package com.example.gramiejska3pgr1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class MainActivity extends AppCompatActivity {

    int REQUEST_LOCATION_PERMISSION = 0;
    Location ostatnioLokalizacja;
    FusedLocationProviderClient fusedLocationProviderClient;
    TextView  textView ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button =findViewById(R.id.button);
        textView = findViewById(R.id.textView);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pobierzLokalizacje();
                    }
                }
        );

    }

    private void pobierzLokalizacje(){
        if(ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED
        ){
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION
            );
        }
        else{

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(
                    new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if(location != null){
                                ostatnioLokalizacja = location;
                                double szerokoscLokalizacja = ostatnioLokalizacja.getLatitude();
                                double dlugoscLokalizacji = ostatnioLokalizacja.getLongitude();
                                String tekst = "Szerokość geograficzna: "+szerokoscLokalizacja+" Długość geograficzna "+dlugoscLokalizacji;
                                textView.setText(tekst);
                            }
                            else{
                                textView.setText("Nie ma lokalizacji");
                            }


                        }
                    }
            );
            //Toast.makeText(this, "jest zgoda", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResult ){
        super.onRequestPermissionsResult(requestCode,permissions,grantResult);
        if(grantResult.length>0){
            if(grantResult[0] == PackageManager.PERMISSION_GRANTED)
            {
                Log.d("Lokalizacja","Udzielona zgoda");
                pobierzLokalizacje();
            }
        }
        else {
            Log.d("Lokalizacja","Nie udzielono zgody");
            Toast.makeText(this,
                    "nasza aplikacja wymaga Twojej lokalizacji, jeżeli się nie zgodzisz będzie bezużyteczna",
                    Toast.LENGTH_SHORT).show();
        }
    }

}