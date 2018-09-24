package com.flash.apps.logmein;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Dashboard extends AppCompatActivity {

private TextView loggedinUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        loggedinUser = findViewById(R.id.loggedinUser);
        String loggednUser = getIntent().getExtras().getString("email");
        loggedinUser.setText(loggednUser);

    }

}
