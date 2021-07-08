package com.example.yoadrachelhezimoran.entities;

import android.widget.ImageView;

public abstract class Checker {
    private int xPlaceOnBoard;
    private int yPlaceOnBoard;
    private boolean isKing;

    public Checker(int xPlaceOnBoard, int yPlaceOnBoard) {
        this.xPlaceOnBoard = xPlaceOnBoard;
        this.yPlaceOnBoard = yPlaceOnBoard;
        this.isKing = false;
    }

    public int getXPlaceOnBoard() {
        return xPlaceOnBoard;
    }

    public void setXPlaceOnBoard(int xPlaceOnBoard) {
        this.xPlaceOnBoard = xPlaceOnBoard;
    }

    public int getYPlaceOnBoard() {
        return yPlaceOnBoard;
    }

    public void setYPlaceOnBoard(int yPlaceOnBoard) {
        this.yPlaceOnBoard = yPlaceOnBoard;
    }

    public void setIndex(int x, int y){
        setXPlaceOnBoard(x);
        setYPlaceOnBoard(y);
    }

    public int getxPlaceOnBoard() {
        return xPlaceOnBoard;
    }

    public int getyPlaceOnBoard() {
        return yPlaceOnBoard;
    }

    public Index getIndex(){
        return new Index(getXPlaceOnBoard(), getYPlaceOnBoard());
    }

    public boolean isKing() {
        return isKing;
    }

    public void turnToKing() {
        isKing = true;
    }

    @Override
    public String toString() {
        return "(" + xPlaceOnBoard +", " + yPlaceOnBoard + "), " + "king? " + isKing;
    }

    public abstract ImageView getCheckerImageView();
    public abstract void setCheckerImageView(ImageView imageView);
}
