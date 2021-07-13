package com.example.yoadrachelhezimoran.entities;

import android.widget.ImageView;

public class CheckerWhite extends Checker{
    private ImageView checkerImageView;

    public CheckerWhite(int xPlaceOnBoard, int yPlaceOnBoard) {
        super(xPlaceOnBoard, yPlaceOnBoard);
    }

    @Override
    public String toString() {
        return "w" + super.toString();
    }

    @Override
    public ImageView getCheckerImageView() {
        return checkerImageView;
    }

    @Override
    public void setCheckerImageView(ImageView imageView) {

    }
}
