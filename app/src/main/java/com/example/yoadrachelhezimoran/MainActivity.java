package com.example.yoadrachelhezimoran;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yoadrachelhezimoran.entities.CheckerBoard;
import com.example.yoadrachelhezimoran.entities.Player;
import com.example.yoadrachelhezimoran.entities.Square;
import com.example.yoadrachelhezimoran.entities.typeOfPlayer;

import java.io.IOException;

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
        CheckerBoard c = CheckerBoard.getInstance();
        visualCheckersBoard = findViewById(R.id.VisualCheckerBoard);
        CheckerBoard.setContext(MainActivity.this);
        setImages();
        try {
            setOnClickOfAllCheckers();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // this is win - we need to handle tie
    public static void endOfGameAnnouncement(){
        String tempAnnouncement = currentPlayer.nameOfPlayer() + " " + winAnnouncement.getText().toString();
        winAnnouncement.setText(tempAnnouncement);
        winAnnouncement.setVisibility(View.VISIBLE);
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

    // add event listener to game pieces of active player
    public static void setOnClickOfAllCheckers() throws IOException {
        Log.d("hezi", "setOnClickOfAllCheckers: \n" + CheckerBoard.getInstance());

        // removes event listeners from all pieces on the board
        for (Square whiteChecker : CheckerBoard.getWhiteCheckers()) {
            whiteChecker.getVisualSquare().setOnClickListener(null);
        }

        for (Square blackChecker : CheckerBoard.getBlackCheckers()) {
            blackChecker.getVisualSquare().setOnClickListener(null);
        }

        // if game is over, don't continue with flow of function
        if (CheckerBoard.isGameOver()) {
            endOfGameAnnouncement();
            return;
        }

        changePlayer();
        if (Player.isIsWhitePlayerTurn()) {

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

    // changes the text indicator of whose turn it is
    public static void changePlayer() {
        if (Player.isIsWhitePlayerTurn()) {
            whiteTurnText.setVisibility(View.VISIBLE);
            blackTurnText.setVisibility(View.GONE);
        } else {
            blackTurnText.setVisibility(View.VISIBLE);
            whiteTurnText.setVisibility(View.GONE);
        }
    }

    // linked to the `concede defeat` button
    // stops the game
    public void yieldGame(View view) throws IOException {

        findViewById(R.id.askDefeat).setOnClickListener(null);
        // checks if game wasn't already over, if player lost before press concede
        // don't announce losing again
        if (!CheckerBoard.isGameOver()) {
            currentPlayer.giveUpNow(); // sets game to over
            // switch the player that's currently playing with the other player, since player conceded defeat
            // is important for game announcement
            currentPlayer = currentPlayer.nameOfPlayer().equals("White") ? blackPlayer : whitePlayer;
            // checks game is over
            setOnClickOfAllCheckers();
        }
    }
}