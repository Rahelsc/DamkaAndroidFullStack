package com.example.yoadrachelhezimoran;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;

public class MainActivity extends AppCompatActivity {
    LinearLayout prev;
    int[][] damkaMatrix;
    boolean whiteTurn;
    TextView whiteTurnText;
    TextView blackTurnText;
    ArrayList<LinearLayout> graphicNeighbours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        matrixInit();
        Log.d("hezi", printMatrix());
        whiteTurn = true;
        whiteTurnText = findViewById(R.id.turnWhiteText);
        blackTurnText = findViewById(R.id.turnBlackText);
        graphicNeighbours = new ArrayList<>();
    }

    public String printMatrix() {
        String temp = "";
        for (int[] row : damkaMatrix) {
            for (int col : row) {
                temp += col + ",";
            }
            temp += "\n";
        }
        return temp;
    }

    public void matrixInit() {
        damkaMatrix = new int[8][];
        for (int i = 0; i < damkaMatrix.length; i++) {
            damkaMatrix[i] = new int[8];
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < damkaMatrix[row].length; col++) {
                if (row % 2 == 0 && col % 2 == 0)
                    damkaMatrix[row][col] = 1;
                else if (row % 2 != 0 && col % 2 != 0)
                    damkaMatrix[row][col] = 1;
            }
        }

        for (int row = 5; row < 8; row++) {
            for (int col = 0; col < damkaMatrix[row].length; col++) {
                if (row % 2 == 0 && col % 2 == 0)
                    damkaMatrix[row][col] = 2;
                else if (row % 2 != 0 && col % 2 != 0)
                    damkaMatrix[row][col] = 2;
            }
        }
    }

    public void turn(LinearLayout current, ArrayList<Integer[]> neighbours) {
        current.setBackgroundColor(Color.rgb(255, 0, 0));
        graphicNeighbours = getGraphicNeighbours(neighbours);
        prev = current;


    }

    public void moveChecker(View view) {
        ArrayList<Integer[]> neighbours = new ArrayList<>();
        LinearLayout current = (LinearLayout) view;
        resetTurn();
        int[] colIndex = getColId(current);
        if (prev != null)
            prev.setBackgroundColor(Color.rgb(178, 94, 60));
        if (current.getChildCount() > 0) {
//            if (whiteTurn && damkaMatrix[colIndex[0]][colIndex[1]] == 2)
//                return;
//            if (!whiteTurn && damkaMatrix[colIndex[0]][colIndex[1]] == 1)
//                return;

            if (whiteTurn && damkaMatrix[colIndex[0]][colIndex[1]] == 1) {
                neighbours = (ArrayList) getNeighborsWhite(colIndex[0], colIndex[1]);
                if (neighbours.size() > 0)
                    turn(current, neighbours);

            } else if (!whiteTurn && damkaMatrix[colIndex[0]][colIndex[1]] == 2) {
                neighbours = (ArrayList) getNeighborsBlack(colIndex[0], colIndex[1]);
                if (neighbours.size() > 0)
                    turn(current, neighbours);
            }


        }

    }

    public ArrayList<LinearLayout> getGraphicNeighbours(ArrayList<Integer[]> neighbours) {
        ArrayList<LinearLayout> layoutArr = new ArrayList<>();
        for (Integer[] neighbour : neighbours) {
            int id = getResources().getIdentifier("Col" + neighbour[0] + "_" + neighbour[1], "id", getPackageName());
            LinearLayout nextStep = findViewById(id);
            nextStep.setBackgroundResource(R.drawable.border);
            nextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ImageView innerTemp = (ImageView) prev.getChildAt(0);
                    prev.removeView(innerTemp);
                    nextStep.addView(innerTemp);
                    prev.setBackgroundColor(Color.rgb(178, 94, 60));
                    resetTurn();
                    int[] colIndex = getColId(prev);
                    int[] nextStepIndex = getColId(nextStep);
                    eatChecker(colIndex[0], nextStepIndex[1], colIndex[1]);
                    changeTurnStatus();
                    damkaMatrix[nextStepIndex[0]][nextStepIndex[1]] = damkaMatrix[colIndex[0]][colIndex[1]];
                    damkaMatrix[colIndex[0]][colIndex[1]] = 0;
                }
            });
            layoutArr.add(nextStep);
        }
        return layoutArr;
    }

    public void eatChecker(int prevRow, int nextCol, int prevCol) {
        LinearLayout toEat = null;
        if (whiteTurn) {
            if (nextCol - prevCol == 2) {
                int id = getResources().getIdentifier("Col" + (prevRow + 1) + "_" + (prevCol + 1), "id", getPackageName());
                Log.d("the id1", "Col" + (prevRow + 1) + "_" + (prevCol + 1));

                toEat = findViewById(id);
            } else if (nextCol - prevCol == -2) {
                int id = getResources().getIdentifier("Col" + (prevRow + 1) + "_" + (prevCol - 1), "id", getPackageName());
                Log.d("the id2", "Col" + (prevRow + 1) + "_" + (prevCol - 1));

                toEat = findViewById(id);
            }
        } else if (!whiteTurn) {
            if (nextCol - prevCol == 2) {
                int id = getResources().getIdentifier("Col" + (prevRow - 1) + "_" + (prevCol + 1), "id", getPackageName());
                Log.d("the id3", "Col" + (prevRow - 1) + "_" + (prevCol - 1));
                toEat = findViewById(id);
            } else if (nextCol - prevCol == -2) {
                int id = getResources().getIdentifier("Col" + (prevRow - 1) + "_" + (prevCol - 1), "id", getPackageName());
                Log.d("the id4", "Col" + (prevRow - 1) + "_" + (prevCol + 1));

                toEat = findViewById(id);
            }
        }

        if (toEat != null) {
            toEat.removeView(toEat.getChildAt(0));
            int[] toEatIndex = getColId(toEat);
            damkaMatrix[toEatIndex[0]][toEatIndex[1]] = 0;
        }

    }

    public void resetTurn() {
        for (LinearLayout graphicNeighbour : graphicNeighbours) {
            graphicNeighbour.setBackgroundColor(Color.rgb(178, 94, 60));
            graphicNeighbour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    moveChecker(view);
                }
            });
        }
    }

    public void changeTurnStatus() {
        whiteTurn = !whiteTurn;
        if (whiteTurn) {
            whiteTurnText.setVisibility(View.VISIBLE);
            blackTurnText.setVisibility(View.GONE);
        } else {
            blackTurnText.setVisibility(View.VISIBLE);
            whiteTurnText.setVisibility(View.GONE);
        }
    }

    public int[] getColId(View view) {
        LinearLayout current = (LinearLayout) view;
        String id = current.getResources().getResourceEntryName(current.getId());
        int[] colIndex = new int[2];
        char[] indexInText = id.substring(3).replace("_", "").toCharArray();
        colIndex[0] = indexInText[0] - '0';
        colIndex[1] = indexInText[1] - '0';
        return colIndex;
    }

    public Collection<Integer[]> getNeighborsBlack(int row, int column) {
        int typeOfChecker = whiteTurn ? 1 : 2;
        Collection<Integer[]> list = new ArrayList<>();
        int extracted = -1;
        try {
            extracted = damkaMatrix[row - 1][column - 1];
            if (extracted == 0)
                list.add(new Integer[]{row - 1, column - 1});
            else if (extracted != typeOfChecker)
                try {
                    extracted = damkaMatrix[row - 2][column - 2];
                    if (extracted == 0)
                        list.add(new Integer[]{row - 2, column - 2});
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }
        try {
            extracted = damkaMatrix[row - 1][column + 1];
            if (extracted == 0)
                list.add(new Integer[]{row - 1, column + 1});
            else if (extracted != typeOfChecker)
                try {
                    extracted = damkaMatrix[row - 2][column + 2];
                    if (extracted == 0)
                        list.add(new Integer[]{row - 2, column + 2});
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }

        return list;
    }


    public Collection<Integer[]> getNeighborsWhite(int row, int column) {
        int typeOfChecker = whiteTurn ? 1 : 2;
        ArrayList<Integer[]> list = new ArrayList<>();
        int extracted = -1;
        boolean keepEating = true;


        try {
            extracted = damkaMatrix[row + 1][column - 1];
            if (extracted == 0) {
                list.add(new Integer[]{row + 1, column - 1});
            } else if (extracted != typeOfChecker)
                while (keepEating) {
                    try {
                        int potentialEnemy = damkaMatrix[row + 1][column - 1];
                        extracted = damkaMatrix[row + 2][column - 2];
                        if (extracted == 0 && potentialEnemy!=typeOfChecker) {
                            list.add(new Integer[]{row + 2, column - 2});
                            row+=2;
                            column-=2;
                        }else
                            keepEating=false;

                    } catch (ArrayIndexOutOfBoundsException ignored) {
                        Log.d("hezi","maaa kore");
                    }
                }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }


        try {
            extracted = damkaMatrix[row + 1][column + 1];
            if (extracted == 0)
                list.add(new Integer[]{row + 1, column + 1});
            else if (extracted != typeOfChecker)
                try {
                    extracted = damkaMatrix[row + 2][column + 2];
                    if (extracted == 0)
                        list.add(new Integer[]{row + 2, column + 2});
                } catch (ArrayIndexOutOfBoundsException ignored) {
                }
        } catch (ArrayIndexOutOfBoundsException ignored) {
        }


        return list;
    }
}