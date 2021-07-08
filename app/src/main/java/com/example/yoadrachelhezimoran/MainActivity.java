package com.example.yoadrachelhezimoran;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yoadrachelhezimoran.entities.CheckerBoard;
import com.example.yoadrachelhezimoran.entities.Player;
import com.example.yoadrachelhezimoran.entities.typeOfPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    public static final Player blackPlayer = new Player(typeOfPlayer.BLACK);
    public static final Player whitePlayer = new Player(typeOfPlayer.WHITE);
    public static LinearLayout visualCheckersBoard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckerBoard c= CheckerBoard.getInstance();
        visualCheckersBoard = findViewById(R.id.VisualCheckerBoard);
        setImages();

        
    }

    public void setImages(){
        for (int i = 0; i < 8; i++) {
            LinearLayout row = (LinearLayout) visualCheckersBoard.getChildAt(i);
            for (int j = 0; j < 8; j++) {
                CheckerBoard.getCheckersMatrix()[i][j].setVisualSquare((LinearLayout) row.getChildAt(j));
                if (CheckerBoard.getCheckersMatrix()[i][j].getChecker() != null) {
                    CheckerBoard.getCheckersMatrix()[i][j].getChecker().setCheckerImageView((ImageView) ((LinearLayout) row.getChildAt(j)).getChildAt(0));
                }
            }
        }
    }

    // stopped here - changing the checker works, here should change turns and plays as needed
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        ImageView imageView = new ImageView(MainActivity.this);
        imageView.setImageResource(R.mipmap.black_queen_foreground);
        CheckerBoard.getCheckersMatrix()[0][0].getVisualSquare().removeAllViews();

        CheckerBoard.getCheckersMatrix()[0][0].getVisualSquare().addView(imageView);
        return super.onTouchEvent(event);
    }
}