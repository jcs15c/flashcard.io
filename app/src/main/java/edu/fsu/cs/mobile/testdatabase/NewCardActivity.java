package edu.fsu.cs.mobile.testdatabase;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// Activity to add a user-inputted card to the database
public class NewCardActivity extends AppCompatActivity {
    public static final String EXTRA_REPLY = "com.android.cardlistsql.REPLY";
    private EditText mEditFront, mEditBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences("MySettings", Context.MODE_PRIVATE);
        boolean darkModeOn = settings.getBoolean("dark_mode", false);
        if(darkModeOn)
            setTheme(R.style.DarkTheme);
        else
            setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_card);
        mEditFront = findViewById(R.id.edit_front);
        mEditBack = findViewById(R.id.edit_back);

        final Button scanButton = findViewById(R.id.camera_button);
        scanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent cameraIntent = new Intent(NewCardActivity.this, OCRActivity.class);
                startActivity(cameraIntent);

                finish();
            }
        });

        final Button button = findViewById(R.id.button_save);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                String[] info = {mEditFront.getText().toString(),
                                 mEditBack.getText().toString()};

                replyIntent.putExtra(EXTRA_REPLY, info);
                setResult(RESULT_OK, replyIntent);

                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null){
            mEditFront.setText(bundle.getString("editFront"));
            mEditBack.setText(bundle.getString("editBack"));
        }
    }
}
