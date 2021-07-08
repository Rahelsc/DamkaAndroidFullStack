package com.example.yoadrachelhezimoran.entities;

import android.widget.ImageView;

import com.example.yoadrachelhezimoran.MainActivity;
import com.example.yoadrachelhezimoran.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CheckerBoard {
    // static variable checkerboard of type CheckerBoard
    private static CheckerBoard checkerboard = null;
    private static Square[][] checkersMatrix;
    private static Square current;
    private static HashMap<Square, Square> enemyPositions;
    private static ArrayList<Index> positionsCheckerCanMoveTo;
    private static ArrayList<Square> whiteCheckers = new ArrayList<>();
    private static ArrayList<Square> blackCheckers = new ArrayList<>();

    public static ArrayList<Index> getPositionsCheckerCanMoveTo() {
        findNeighboursOfChecker();
        findNeighboursCheckerCanEat();
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
                return true;
            }
        }
        return false;
    }

    public boolean canBlackPlayerMove(){
        for (Square blackSquare : blackCheckers) {
            setAndGetActiveSquare(blackSquare);
            if (positionsCheckerCanMoveTo.size()>0) {
                current = null;
                return true;
            }
        }
        return false;
    }

    public static Square setAndGetActiveSquare(Square s) {
        CheckerBoard.current = s;
        getPositionsCheckerCanMoveTo();
        return CheckerBoard.current;
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
        int x = current.getChecker().getXPlaceOnBoard();
        int y = current.getChecker().getYPlaceOnBoard();
        System.out.println("returnNeighboursOfChecker: " + x + ", " + y);
        int offSet = 0;
        if (checkersMatrix[x][y].getChecker() instanceof CheckerWhite)
            offSet = 1;
        else if (checkersMatrix[x][y].getChecker() instanceof CheckerBlack)
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

        Checker extracted = null;
        try {
            extracted = checkersMatrix[x + offSet][y - 1].getChecker();
            if (extracted == null) {
                positionsCheckerCanMoveTo.add(new Index(x + offSet, y - 1));
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }


        try {
            extracted = checkersMatrix[x + offSet][y + 1].getChecker();
            if (extracted == null)
                positionsCheckerCanMoveTo.add(new Index(x + offSet, y + 1));
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public static void findNeighboursCheckerCanEat(){
        int x = current.getChecker().getXPlaceOnBoard();
        int y = current.getChecker().getYPlaceOnBoard();
        int offSet = 0;
        int enemyOffset = 0;
        Checker currentChecker = checkersMatrix[x][y].getChecker();
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

        Checker potentialEnemy = null;
        // only if target position is null, there is a reason to check if there is anyone to eat
        try {
            if (checkersMatrix[x + offSet] [y - 2].getChecker() == null) {
                potentialEnemy = checkersMatrix[x + enemyOffset][y - 1].getChecker();
                if (potentialEnemy != null && potentialEnemy.getClass() != currentChecker.getClass()){
                    Index index = new Index(x + offSet, y - 2);
                    positionsCheckerCanMoveTo.add(index);
                    enemyPositions.put(checkersMatrix[x + offSet][y + 2], checkersMatrix[x + enemyOffset][y + 1]);
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        try {
            if (checkersMatrix[x + offSet] [y + 2].getChecker() == null) {
                potentialEnemy = checkersMatrix[x + enemyOffset][y + 1].getChecker();
                if (potentialEnemy != null && potentialEnemy.getClass() != currentChecker.getClass()){
                    Index index = new Index(x + offSet, y + 2);
                    positionsCheckerCanMoveTo.add(index);
                    enemyPositions.put(checkersMatrix[x + offSet][y + 2], checkersMatrix[x + enemyOffset][y + 1]);
                }
            }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
    }

    public static void moveCheckerOnCheckerBoard(Square target){
        eat(target);
        Checker tempChecker = current.getChecker();
        current.setChecker(null);
        tempChecker.setIndex(target.getIndex().getX(), target.getIndex().getY());
        target.setChecker(tempChecker);
        kingCheckerIfNeeded(target);
        positionsCheckerCanMoveTo.clear();
        current = null;
    }

    private static void kingCheckerIfNeeded(Square target){
        if (target.getChecker() instanceof CheckerWhite && target.getChecker().getxPlaceOnBoard() == checkersMatrix.length-1
                || target.getChecker() instanceof CheckerBlack && target.getChecker().getxPlaceOnBoard() == 0){
            target.getChecker().turnToKing();
        }
    }

    private static void eat(Square target){
        Square enemyPosition = enemyPositions.get(target);
        if (enemyPosition!=null) {
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
