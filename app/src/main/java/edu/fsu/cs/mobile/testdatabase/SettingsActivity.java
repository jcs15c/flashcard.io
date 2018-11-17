package edu.fsu.cs.mobile.testdatabase;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    private Switch nightSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        Log.d("TAG", String.valueOf(toolbar == null));

        setSupportActionBar(toolbar);
        Log.d("TAG", String.valueOf(getSupportActionBar() == null));

        getSupportActionBar().setTitle( "Settings" );
        nightSwitch = (Switch)findViewById(R.id.night_switch);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            nightSwitch.setChecked(true);
        }
        nightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    restartApp();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.MODE_NIGHT_NO));
                    restartApp();
                }
            }
        });

        Intent data = getIntent();
        String info = data.getStringExtra(MainActivity.EXTRA_MESSAGE);


    }

    public void restartApp() {
        Intent myIntent = new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(myIntent);
        overridePendingTransition(0,0); // To skip animation
        finish();
    }



}
