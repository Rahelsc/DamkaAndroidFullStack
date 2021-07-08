package com.example.yoadrachelhezimoran.entities;

public class Player {
    public static boolean isWhitePlayerTurn = true;
    private boolean giveUp;
    private int wins;
    private int movesCount;
    private typeOfPlayer type;

    public Player(typeOfPlayer type) {
        this.giveUp = false;
        this.wins = 0;
        this.movesCount = 0;
        this.type = type;
    }

    public int getMovesCount() {
        return movesCount;
    }

    public void addMovesCount(){
        this.movesCount++;
    }

    public int getWins() {
        return wins;
    }

    public void addWin(){
        this.wins++;
    }

    public boolean isGiveUp() {
        return giveUp;
    }

    public void giveUpNow() {
        this.giveUp = true;
    }

    public static boolean isIsWhitePlayerTurn() {
        return isWhitePlayerTurn;
    }

    public static void changeTurn() {
        Player.isWhitePlayerTurn = !isWhitePlayerTurn;
    }

    public typeOfPlayer getType() {
        return type;
    }

    public void setType(typeOfPlayer type) {
        this.type = type;
    }

    public int getNumberOfCheckersLeft(){
        int size = this.type == typeOfPlayer.BLACK ?
                CheckerBoard.getBlackCheckers().size() :
                CheckerBoard.getWhiteCheckers().size();
        return size;
    }

    public boolean hasMove(){
        return this.type == typeOfPlayer.BLACK ?
                CheckerBoard.getInstance().canBlackPlayerMove() :
                CheckerBoard.getInstance().canWhitePlayerMove();
    }
}
