package com.example.yoadrachelhezimoran.entities;
import android.widget.LinearLayout;

import java.util.Objects;

public class Square{
    private Index index;
    private Checker checker;
    private LinearLayout visualSquare;

    public Square(int x, int y, Checker checker) {
        this(x,y);
        this.checker = checker;
    }

    public Square(int x, int y) {
        this.index = new Index(x,y);
        checker = null;
    }

    public LinearLayout getVisualSquare() {
        return visualSquare;
    }

    public void setVisualSquare(LinearLayout visualSquare) {
        this.visualSquare = visualSquare;
    }

    public Index getIndex() {
        return index;
    }

    public Checker getChecker() {
        return checker;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return index.equals(square.getIndex()) &&
                checker.equals(square.getChecker());
    }

    @Override
    public String toString() {
        String isChecker ="";
        if (checker!=null && checker.isKing())
            isChecker+="K";
        if (checker instanceof CheckerWhite)
            isChecker += "w";
        else if (checker instanceof CheckerBlack)
            isChecker += "b";
        else isChecker = " ";
        return isChecker+" "+index+"   ";
    }
}
