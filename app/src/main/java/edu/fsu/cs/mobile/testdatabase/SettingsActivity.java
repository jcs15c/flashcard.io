package edu.fsu.cs.mobile.testdatabase;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

public class SettingsActivity extends AppCompatActivity {
    private CardViewModel mCardViewModel;

    private Switch nightSwitch;
    SharedPreferences settings;
    SharedPreferences.Editor editor;
    boolean darkModeOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        settings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        editor = settings.edit();
        darkModeOn = settings.getBoolean("dark_mode", false);

        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
        }
        else setTheme(R.style.AppTheme);

        if(darkModeOn)
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        Log.d("TAG", String.valueOf(toolbar == null));

        setSupportActionBar(toolbar);
        Log.d("TAG", String.valueOf(getSupportActionBar() == null));

        getSupportActionBar().setTitle( "Settings" );
        mCardViewModel = ViewModelProviders.of(this).get(CardViewModel.class);

        nightSwitch = (Switch)findViewById(R.id.night_switch);
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            nightSwitch.setChecked(true);
        }

        final Button format_button = findViewById(R.id.format_button);
        format_button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SettingsActivity.this);
                builder.setTitle("Are you sure you want to delete all cards?");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        mCardViewModel.deleteAllCards();
                        MainActivity.selectedPosition = -1;
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });


        nightSwitch.setChecked(darkModeOn);
        nightSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    editor.putBoolean("dark_mode", true);
                    editor.commit();
                    restartApp();
                }
                else{
                    AppCompatDelegate.setDefaultNightMode((AppCompatDelegate.MODE_NIGHT_NO));
                    editor.putBoolean("dark_mode", false);
                    editor.commit();
                    restartApp();
                }
            }
        });

        Intent data = getIntent();
        String info = data.getStringExtra(MainActivity.EXTRA_MESSAGE);


    }

    public void restartApp() {
        /*Intent myIntent = new Intent(getApplicationContext(),SettingsActivity.class);
        startActivity(myIntent);
        overridePendingTransition(0,0); // To skip animation
        finish();*/

        //True restart of app
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage( getBaseContext().getPackageName() );
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }



}
