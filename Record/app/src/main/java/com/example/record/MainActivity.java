package com.example.record;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1;
//all premissins
    String[] premission = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_CONTACTS };
    ToggleButton startRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for (String premissions : premission) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, premissions) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, premission, REQUEST_CODE);
            }

            startRecord = findViewById(R.id.btn_recorder);
            startRecord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((ToggleButton) v).isChecked();
                    if (checked) {
                        // to sure that take your all permissions
                        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                                Manifest.permission.RECORD_AUDIO) + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) +
                                ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                + ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)+
                                ContextCompat.checkSelfPermission(MainActivity.this,
                                        Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED)
                        {
                            Toast.makeText(MainActivity.this, "You have already granted this premission!", Toast.LENGTH_SHORT).show();
                        } else {
                            requestRecordPremission();
                        }
                        // Start Service to Start Record
                       Intent intent = new Intent(MainActivity.this, Recording.class);
                        startService(intent);
                        Toast.makeText(getApplicationContext(), "Call Recording Start", Toast.LENGTH_LONG).show();


                           }
                   else
                    {
                        // Stop Service to Stop Record
                        Intent intent = new Intent(MainActivity.this, Recording.class);
                        stopService(intent);
                        Toast.makeText(getApplicationContext(), "Call Recording Stop", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    //Request premissions

    public void requestRecordPremission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.RECORD_AUDIO)||ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.CALL_PHONE)||ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)||ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) ){
            new AlertDialog.Builder(this)
                    .setTitle("premission needed")
                    .setMessage("You should allow this premission!")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,premission,REQUEST_CODE);
                        }

                    })
                    .setNegativeButton("cancal", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }
        else {
            ActivityCompat.requestPermissions(MainActivity.this, premission, 1);

        }
    }

        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            if (requestCode == REQUEST_CODE) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Premission GRANTED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Premission DENIED", Toast.LENGTH_SHORT).show();
                }
            }
        }

}


