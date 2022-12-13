package com.example.recycle_exmapreparation;

import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    TextView tv_name, tv_number, tv_evaluate,tv_time,tv_quantity,tv_date,tv_area;
    private ActivityResultContracts activityResultContract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_detail);

        //Intent 로 데이터를 받아옵니다.
        Intent intent = getIntent ();
        String name = intent.getStringExtra ("name");
        String number = intent.getStringExtra ("number");
        String evaluate = intent.getStringExtra ("evaluate");
        String time = intent.getStringExtra ("time");
        String quantity = intent.getStringExtra ("quantity");
        String date = intent.getStringExtra ("date");
        String area = intent.getStringExtra ("area");


        tv_name = findViewById (R.id.tv_name);
        tv_number = findViewById (R.id.tv_number);
        tv_evaluate = findViewById (R.id.tv_evaluate);
        tv_time = findViewById (R.id.tv_time);
        tv_quantity= findViewById (R.id.tv_quantity);
        tv_date = findViewById (R.id.tv_date);
        tv_area = findViewById (R.id.tv_area);

        //받아온 데이터를 TextView 에 띄워줍니다.
        tv_name.setText (name);
        tv_number.setText (number);
        tv_evaluate.setText (evaluate);
        tv_time.setText (time);
        tv_quantity.setText (quantity);
        tv_date.setText (date);
        tv_area.setText (area);
    }
}