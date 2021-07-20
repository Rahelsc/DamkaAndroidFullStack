package com.example.yoadrachelhezimoran.entities;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.yoadrachelhezimoran.Action;
import com.example.yoadrachelhezimoran.Client;
import com.example.yoadrachelhezimoran.MainActivity;
import com.example.yoadrachelhezimoran.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CheckerBoard {
    // static variable checkerboard of type CheckerBoard
    private static CheckerBoard checkerboard = null;
    private static Square[][] checkersMatrix;
    private static Square current;
    private static HashMap<Square, Square> enemyPositions;
    private static ArrayList<Index> positionsCheckerCanMoveTo;
    private static ArrayList<Square> whiteCheckers = new ArrayList<>();
    private static ArrayList<Square> blackCheckers = new ArrayList<>();
    private static boolean gameOver = false;
    private static Context context;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(3);


    public static int[][] getPositions(){
        int[][] positions = new int[8][];
        for (int i = 0; i < positions.length; i++) {
            positions[i] = new int[8];
        }

        for (Square whiteChecker : whiteCheckers) {
            int posX = whiteChecker.getIndex().getX();
            int posY = whiteChecker.getIndex().getY();
            positions[posX][posY] = 1; // 1 represents the white checkers
        }

        for (Square blackChecker : blackCheckers) {
            int posX = blackChecker.getIndex().getX();
            int posY = blackChecker.getIndex().getY();
            positions[posX][posY] = 2; // 2 represents the black checkers
        }

        return positions;
    }

    public static Context getContext() {
        return context;
    }

    public static void setContext(Context context) {
        CheckerBoard.context = context;
    }

    public static boolean isGameOver() {
        return gameOver;
    }

    public static void setGameOver(boolean gameOver) {
        CheckerBoard.gameOver = gameOver;
    }

    public static ArrayList<Index> setPositionsCheckerCanMoveTo() {
        if (current.getChecker() != null) {
            findNeighboursCheckerCanEat();
            findNeighboursOfChecker();
        }
        return positionsCheckerCanMoveTo;
    }

    public static Square[][] getCheckersMatrix() {
        return checkersMatrix;
    }

    public static ArrayList<Square> getWhiteCheckers() {
        return whiteCheckers;
    }

    public static ArrayList<Square> getBlackCheckers() {
        return blackCheckers;
    }

    // private constructor restricted to this class itself
    private CheckerBoard(){
        checkersMatrixInit();
        enemyPositions = new HashMap<>();
        positionsCheckerCanMoveTo = new ArrayList<>();
    }

    // static method to create instance of CheckerBoard class
    public static CheckerBoard getInstance(){
        if (checkerboard == null)
            checkerboard = new CheckerBoard();
        return checkerboard;
    }

    private void checkersMatrixInit() {
        checkersMatrix = new Square[8][];
        for (int i = 0; i < checkersMatrix.length; i++) {
            checkersMatrix[i] = new Square[8];
        }

        //initialize all checkerboard squares and all white checkers in them
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < checkersMatrix[row].length; col++) {
                if ((row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0)) {
                    Checker temp =  new CheckerWhite(row, col);
                    checkersMatrix[row][col] = new Square(row, col,temp);
                    whiteCheckers.add(checkersMatrix[row][col]);
                }
                else
                    checkersMatrix[row][col] = new Square(row,col);
            }
        }

        //initialize all checkerboard squares and all black checkers in them
        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < checkersMatrix[row].length; col++) {
                if ((row % 2 == 0 && col % 2 == 0) || (row % 2 != 0 && col % 2 != 0)) {
                    Checker temp = new CheckerBlack(row, col);
                    checkersMatrix[row][col] = new Square(row, col, temp);
                    blackCheckers.add(checkersMatrix[row][col]);
                }
                else
                    checkersMatrix[row][col] = new Square(row,col);
            }
        }

        // init blank square lines
        for (int row = 3; row < 5; row++) {
            for (int col = 0; col < checkersMatrix[row].length; col++) {
                checkersMatrix[row][col] = new Square(row, col);
            }
        }
    }

    public boolean canWhitePlayerMove(){
        for (Square whiteSquare : whiteCheckers) {
            setAndGetActiveSquare(whiteSquare);
            if (positionsCheckerCanMoveTo.size()>0){
                current = null;
                clearPreviousPositions();
                return true;
            }
        }
        clearPreviousPositions();
        return false;
    }

    public boolean canBlackPlayerMove(){
        for (Square blackSquare : blackCheckers) {
            setAndGetActiveSquare(blackSquare);
            if (positionsCheckerCanMoveTo.size()>0) {
                current = null;
                clearPreviousPositions();
                return true;
            }
        }
        clearPreviousPositions();
        return false;
    }

    public static Square setAndGetActiveSquare(Square s) {
        clearPreviousPositions();
        CheckerBoard.current = s;
        setPositionsCheckerCanMoveTo();
        setOnClickMovement();
        return CheckerBoard.current;
    }

    public static Square setAndGetActiveSquare(Square s, boolean haventEaten) {
        clearPreviousPositions();
        CheckerBoard.current = s;
        findNeighboursCheckerCanEat();
        setOnClickMovement();
        return CheckerBoard.current;
    }

    private static void setOnClickMovement(){
        for (Index index : positionsCheckerCanMoveTo) {
            checkersMatrix[index.getX()][index.getY()].getVisualSquare().setBackgroundResource(R.drawable.border);
            checkersMatrix[index.getX()][index.getY()].getVisualSquare().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        moveCheckerOnCheckerBoard(checkersMatrix[index.getX()][index.getY()]);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public static void clearPreviousPositions(){
        for (Index index : positionsCheckerCanMoveTo) {
            checkersMatrix[index.getX()][index.getY()].getVisualSquare().setBackgroundColor(Color.rgb(178, 94, 60));
            checkersMatrix[index.getX()][index.getY()].getVisualSquare().setOnClickListener(null);
        }
        
        enemyPositions.clear();
        positionsCheckerCanMoveTo.clear();
    }

    public static Square getActiveSquare(){
        return CheckerBoard.current;
    }

    public static void printPossibleMovements(){
        for (Index index : positionsCheckerCanMoveTo) {
            System.out.print(index+" ");
        }
    }

    public static void findNeighboursOfChecker(){
        Checker currentChecker = current.getChecker();
        int offSet = 0;
        if (currentChecker instanceof CheckerWhite)
            offSet = 1;
        else if (currentChecker instanceof CheckerBlack)
            offSet = -1;
        getMoves(offSet);
        if (current.getChecker().isKing()){
            getMoves(offSet*-1);
        }
    }

    private static void getMoves(int offSet){
        int x = current.getChecker().getXPlaceOnBoard();
        int y = current.getChecker().getYPlaceOnBoard();
        if (offSet == 0) return; // if there is no checker in given position return.

        Client.setAction(Action.getNeighbors);
        Client.setIndex(new int[]{x,y});
        Client.setOffSet(offSet);
        Future<List<int[]>> futureResult = threadPool.submit(Client.runClient);

        try{
            List<int[]> res = futureResult.get();
            for (int[] adjacentIndex : res) {
                positionsCheckerCanMoveTo.add(new Index(adjacentIndex[0], adjacentIndex[1]));
            }
        }catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void findNeighboursCheckerCanEat(){

        Checker currentChecker = current.getChecker();

        int offSet = 0;
        int enemyOffset = 0;

        if (currentChecker instanceof CheckerWhite){
            offSet = 2;
            enemyOffset = 1;
        }
        else if (currentChecker instanceof CheckerBlack){
            offSet = -2;
            enemyOffset = -1;
        }
        getMovesToEat(offSet, enemyOffset, currentChecker);

        if (current.getChecker().isKing()){
            getMovesToEat(offSet*-1, enemyOffset*-1, currentChecker);
        }

    }

    private static void getMovesToEat(int offSet, int enemyOffset, Checker currentChecker){
        int x = current.getChecker().getXPlaceOnBoard();
        int y = current.getChecker().getYPlaceOnBoard();

        if (offSet == 0) return; // if there is no checker in given position return.

        Client.setAction(Action.getNeighborsToEat);
        Client.setIndex(new int[]{x,y});
        Client.setOffSet(offSet);
        Client.setEnemyOffSet(enemyOffset);

        Future<List<int[]>> futureResult = threadPool.submit(Client.findEatingMoves);

        try{
            List<int[]> res = futureResult.get();
            for (int[] adjacentIndex : res) {
                positionsCheckerCanMoveTo.add(new Index(adjacentIndex[0], adjacentIndex[1]));
                Client.setAction(Action.getPossibleEnemiesPosition);
                Client.setNewPosition(adjacentIndex[0]*10+adjacentIndex[1]);
                Future<int[]> futureEnemy = threadPool.submit(Client.findWhoIAte);
                try {
                    int[] enemyRes = futureEnemy.get();
                    enemyPositions.put(checkersMatrix[adjacentIndex[0]][adjacentIndex[1]], checkersMatrix[enemyRes[0]][enemyRes[1]]);
                }catch (ExecutionException e){
                    e.printStackTrace();
                }
            }
        }catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void moveCheckerOnCheckerBoard(Square target) throws IOException {
        visualMovement(target);
        eat(target);
        Checker tempChecker = current.getChecker();
        current.setChecker(null);
        tempChecker.setIndex(target.getIndex().getX(), target.getIndex().getY());
        target.setChecker(tempChecker);
        addToCorrectCheckersArr(target);
        kingCheckerIfNeeded(target);
        target.getVisualSquare().setOnClickListener(null);
        clearPreviousPositions();

        // to keep if last move was to eat
        boolean haventEaten = true;
        // if difference between moves was 2 then last move we did eat
        if (Math.abs(target.getIndex().getY() - current.getIndex().getY())==2){
            haventEaten = false;
        }

        if (Player.isIsWhitePlayerTurn() &&
                (!checkerboard.canBlackPlayerMove() || MainActivity.blackPlayer.getNumberOfCheckersLeft()==0)
        || !Player.isIsWhitePlayerTurn() &&
                (!checkerboard.canWhitePlayerMove() || MainActivity.whitePlayer.getNumberOfCheckersLeft()==0)) {
            gameOver = true;
        }

        // clear the calls to canBlackPlayerMove && canWhitePlayerMove from results
        clearPreviousPositions();

        // checking from new position can we continue eating
        current = target;
        findNeighboursCheckerCanEat();
        // if we can't eat now or we didn't eat last turn, we let the other player play
        if (positionsCheckerCanMoveTo.size()==0 || haventEaten) {
            current = null;
            Player.changeTurn();
            MainActivity.setOnClickOfAllCheckers();
        } else {
            // if we can eat, we make sure all pieces on the board can't move other than our own
            for (Square whiteChecker : CheckerBoard.getWhiteCheckers()) {
                whiteChecker.getVisualSquare().setOnClickListener(null);
            }

            for (Square blackChecker : CheckerBoard.getBlackCheckers()) {
                blackChecker.getVisualSquare().setOnClickListener(null);
            }
            clearPreviousPositions();

            // we find the next target we can move to from current position
            // calling a different `setAndGetActiveSquare` that only checks for neighbours you can eat
            setAndGetActiveSquare(target, haventEaten);
        }

        System.out.println("black size: "+blackCheckers.size());
        System.out.println("white size: "+whiteCheckers.size());

    }

    public static void visualMovement(Square target){
        LinearLayout visualSquare = current.getVisualSquare();
        ImageView visualChecker = (ImageView) current.getVisualSquare().getChildAt(0);
        visualSquare.removeAllViews();
        if (visualChecker!=null) {
            target.getVisualSquare().addView(visualChecker);
        }
        visuallyRemovePositionsCheckerCanMoveTo();
    }

    public static void visuallyRemovePositionsCheckerCanMoveTo(){
        for (Index index : positionsCheckerCanMoveTo) {
            checkersMatrix[index.getX()][index.getY()].getVisualSquare().setBackgroundColor(Color.rgb(178, 94, 60));
            checkersMatrix[index.getX()][index.getY()].getVisualSquare().setOnClickListener(null);
        }
    }

    private static void kingCheckerIfNeeded(Square target){
        if (target.getChecker() instanceof CheckerWhite &&
                target.getChecker().getxPlaceOnBoard() == checkersMatrix.length-1){
            target.getChecker().turnToKing();
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.mipmap.white_queen_foreground);
            target.getVisualSquare().removeAllViews();
            target.getVisualSquare().addView(imageView);
        }
        else if (target.getChecker() instanceof CheckerBlack &&
                target.getChecker().getxPlaceOnBoard() == 0){
            target.getChecker().turnToKing();
            ImageView imageView = new ImageView(context);
            imageView.setImageResource(R.mipmap.black_queen_foreground);
            target.getVisualSquare().removeAllViews();
            target.getVisualSquare().addView(imageView);
        }
    }

    private static void addToCorrectCheckersArr(Square target){
        if (target.getChecker() instanceof CheckerWhite){
            target.getVisualSquare().setOnClickListener(null);
            current.getVisualSquare().setOnClickListener(null);
            whiteCheckers.remove(current);
            whiteCheckers.add(target);
        }
        else if (target.getChecker() instanceof CheckerBlack){
            target.getVisualSquare().setOnClickListener(null);
            current.getVisualSquare().setOnClickListener(null);
            blackCheckers.remove(current);
            blackCheckers.add(target);
        }
    }

    private static void eat(Square target){
        Square enemyPosition = enemyPositions.get(target);
        if (enemyPosition!=null) {
            enemyPosition.getVisualSquare().removeAllViews();
            if (enemyPosition.getChecker() instanceof CheckerWhite)
                whiteCheckers.remove(enemyPosition);
            else blackCheckers.remove(enemyPosition);
            checkersMatrix[enemyPosition.getIndex().getX()][enemyPosition.getIndex().getY()].setChecker(null);
        }
        enemyPositions.clear();
    }

    @Override
    public String toString() {
        String checkers = "";
        for (Square[] matrix : checkersMatrix) {
            for (Square square : matrix) {
                checkers += square;
            }
            checkers+="\n";
        }
        return checkers;
    }
}