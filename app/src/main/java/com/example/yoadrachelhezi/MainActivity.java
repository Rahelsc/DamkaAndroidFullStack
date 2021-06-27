package com.example.yoadrachelhezi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void moveChecker(View view) {
        LinearLayout square = (LinearLayout) view;
        if(square.getChildCount()>0){
            LinearLayout target = findViewById(R.id.Col3_1);
            ImageView temp = (ImageView) square.getChildAt(0);
            square.removeView(temp);
            target.addView(temp);
        }
    }
}