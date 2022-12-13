package com.example.recycle_exmapreparation;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainpageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("헬창튼튼");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpage);

        Button page1button = (Button) findViewById(R.id.button);

        page1button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
            }

        });
        Button page2button = (Button) findViewById(R.id.button2);

        page2button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), page2.class); //page2 ->foodlist
                startActivity(intent1);
            }

        });
        Button page3button = (Button) findViewById(R.id.button3);

        page3button.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(), page3.class);
                startActivity(intent1);
            }

        });
    }
}