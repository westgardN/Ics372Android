package edu.metrostate.ics372.thatgroup.clinicaltrial.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onStart() {
        super.onStart();

    }

    protected void onPause() {
        super.onPause();
    }

    protected void openClinic(View view){
        Intent intent =  new Intent(MainActivity.this,ClinicActivity.class);
        startActivity(intent);
    }


}
