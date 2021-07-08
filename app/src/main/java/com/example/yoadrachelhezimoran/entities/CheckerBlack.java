package com.example.yoadrachelhezimoran.entities;

import android.widget.ImageView;

public class CheckerBlack extends Checker{
    private ImageView checkerImageView;

    public CheckerBlack(int xPlaceOnBoard, int yPlaceOnBoard) {
        super(xPlaceOnBoard, yPlaceOnBoard);
    }

    @Override
    public String toString() {
        return "b" + super.toString();
    }

    @Override
    public ImageView getCheckerImageView() {
        return checkerImageView;
    }

    @Override
    public void setCheckerImageView(ImageView imageView) {

    }
}
