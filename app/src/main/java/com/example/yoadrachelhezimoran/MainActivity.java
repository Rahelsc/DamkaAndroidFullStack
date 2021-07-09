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
import com.example.yoadrachelhezimoran.entities.Square;
import com.example.yoadrachelhezimoran.entities.typeOfPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    public static final Player blackPlayer = new Player(typeOfPlayer.BLACK);
    public static final Player whitePlayer = new Player(typeOfPlayer.WHITE);
    public static LinearLayout visualCheckersBoard;
    public static TextView whiteTurnText;
    public static TextView blackTurnText;
    private static TextView winAnnouncement;
    private static Player currentPlayer = whitePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        whiteTurnText = findViewById(R.id.turnWhiteText);
        blackTurnText = findViewById(R.id.turnBlackText);
        winAnnouncement = findViewById(R.id.PlayerWins);
        CheckerBoard c= CheckerBoard.getInstance();
        visualCheckersBoard = findViewById(R.id.VisualCheckerBoard);
        CheckerBoard.setContext(MainActivity.this);
        setImages();
        setOnClickOfAllCheckers();

    }

    // this is win - we need to handle tie
    public static void endOfGameAnnouncement(){
        if (CheckerBoard.isGameOver()){
            for (Square blackChecker : CheckerBoard.getBlackCheckers()) {
                blackChecker.getVisualSquare().setOnClickListener(null);
            }

            for (Square whiteChecker : CheckerBoard.getWhiteCheckers()) {
                whiteChecker.getVisualSquare().setOnClickListener(null);
            }

            String tempAnnouncement = currentPlayer.nameOfPlayer() + " " + winAnnouncement.getText().toString();
            winAnnouncement.setText(tempAnnouncement);

        }

    }



    // fills the squares with the appropriate views
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

    // add event listener to player that is active
    public static void setOnClickOfAllCheckers(){
        Log.d("hezi", "setOnClickOfAllCheckers: \n" + CheckerBoard.getInstance());
        changePlayer();
        if (Player.isIsWhitePlayerTurn()) {
            for (Square blackChecker : CheckerBoard.getBlackCheckers()) {
                blackChecker.getVisualSquare().setOnClickListener(null);
            }
            for (Square whiteChecker : CheckerBoard.getWhiteCheckers()) {
                whiteChecker.getVisualSquare().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckerBoard.setAndGetActiveSquare(whiteChecker);
                    }
                });
            }
            currentPlayer = whitePlayer;
        }
        else {
            for (Square whiteChecker : CheckerBoard.getWhiteCheckers()) {
                whiteChecker.getVisualSquare().setOnClickListener(null);
            }

            for (Square blackChecker : CheckerBoard.getBlackCheckers()) {
                blackChecker.getVisualSquare().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckerBoard.setAndGetActiveSquare(blackChecker);
                    }
                });
            }

            currentPlayer = blackPlayer;
        }
    }

    // changes the text indicator of whose turn it is --> put in checker
    public static void changePlayer() {
        if (Player.isIsWhitePlayerTurn()) {
            whiteTurnText.setVisibility(View.VISIBLE);
            blackTurnText.setVisibility(View.GONE);
        } else {
            blackTurnText.setVisibility(View.VISIBLE);
            whiteTurnText.setVisibility(View.GONE);
        }
    }


}