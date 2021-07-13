package com.example.yoadrachelhezimoran.entities;

public class Player {
    public static boolean isWhitePlayerTurn = true;
    private typeOfPlayer type;


    public Player(typeOfPlayer type) {
        this.type = type;
    }

    public void giveUpNow() {
        CheckerBoard.setGameOver(true);
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

    public boolean checkWin(){
        return this.getNumberOfCheckersLeft() == 0 || !this.hasMove();
    }

    public String nameOfPlayer(){
        if (type == typeOfPlayer.BLACK)
            return "Black";
        else return "White";
    }
}
