package com.example.yoadrachelhezimoran;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.yoadrachelhezimoran.entities.CheckerBoard;
import com.example.yoadrachelhezimoran.entities.Index;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;


public class Client {
    private static int [][] matrixForServer = CheckerBoard.getPositions();
    private static Action action = Action.getNeighbors;
    private static int[] index = new int[]{2,2};
    private static int offSet = 1;
    private static int enemyOffSet = 2;
    private static int newPosition = 0;

    public static int getNewPosition() {
        return newPosition;
    }

    public static void setNewPosition(int newPosition) {
        Client.newPosition = newPosition;
    }

    public static int getEnemyOffSet() {
        return enemyOffSet;
    }

    public static void setEnemyOffSet(int enemyOffSet) {
        Client.enemyOffSet = enemyOffSet;
    }

    public static void setIndex(int[] index) {
        if (index.length == 2)
            Client.index = index;
    }

    public static void setAction(Action action) {
        Client.action = action;
    }

    public static void setOffSet(int offSet) {
        Client.offSet = offSet;
    }

    private static ArrayList<int[]> AdjacentIndices;
    public static Callable<List<int[]>> runClient = ()->{
        AdjacentIndices = new ArrayList<>();
        matrixForServer = CheckerBoard.getPositions();
        Socket socket = null;
        try {
            socket = new Socket("10.0.2.2",8010);
            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
            System.out.println("client: Created Socket");

            //TODO: create a loop to get commands from user.
            // upon sending an index: input should be of the type (rowNumber,columnNumber)

            //send "matrix" command then write 2d array to socket
            toServer.writeObject(Action.matrix.toString());
            //TODO: check that matrix is no bigger than  20X20 & jagged array (number of columns is same in all rows)
            toServer.writeObject(matrixForServer);

            //send command then write an index to socket
            toServer.writeObject(action.toString());
            // TODO: get index from command prompt as: (rowNumber,columnNumber)
            toServer.writeObject(index);
            toServer.writeObject(offSet);
            // get neighboring indices as list
            AdjacentIndices =
                    new ArrayList<int[]>((List<int[]>) fromServer.readObject());
            for (int[] adjacentIndex : AdjacentIndices) {
                System.out.println(Arrays.toString(adjacentIndex));
            }

            toServer.writeObject(Action.stop.toString());
            System.out.println("client: Close all streams");
            fromServer.close();
            toServer.close();
            socket.close();

            System.out.println("client: Closed operational socket");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return AdjacentIndices;
    };

    public static Callable<List<int[]>> findEatingMoves = ()->{
        matrixForServer = CheckerBoard.getPositions();
        AdjacentIndices = new ArrayList<>();

        Socket socket = null;
        try {

            socket = new Socket("10.0.2.2",8010);
            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
            System.out.println("client: Created Socket");

            //TODO: create a loop to get commands from user.
            // upon sending an index: input should be of the type (rowNumber,columnNumber)

            //send "matrix" command then write 2d array to socket
            toServer.writeObject(Action.matrix.toString());
            //TODO: check that matrix is no bigger than  20X20 & jagged array (number of columns is same in all rows)
            toServer.writeObject(matrixForServer);

            //send command then write an index to socket
            toServer.writeObject(action.toString());
            // TODO: get index from command prompt as: (rowNumber,columnNumber)
            toServer.writeObject(index);
            toServer.writeObject(offSet);
            toServer.writeObject(enemyOffSet);
            // get neighboring indices as list
            AdjacentIndices =
                    new ArrayList<int[]>((List<int[]>) fromServer.readObject());
            for (int[] adjacentIndex : AdjacentIndices) {
                System.out.println(Arrays.toString(adjacentIndex));
            }

            toServer.writeObject(Action.stop.toString());
            System.out.println("client: Close all streams");
            fromServer.close();
            toServer.close();
            socket.close();

            System.out.println("client: Closed operational socket");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return AdjacentIndices;
    };


    public static Callable<int[]> findWhoIAte = ()->{
        int[] positionOfEatenChecker = new int[2];
        Socket socket = null;
        try {

            socket = new Socket("10.0.2.2",8010);
            ObjectOutputStream toServer = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream fromServer = new ObjectInputStream(socket.getInputStream());
            System.out.println("client: Created Socket");

            //send command then write an index to socket
            toServer.writeObject(action.toString());
            // TODO: get index from command prompt as: (rowNumber,columnNumber)
            toServer.writeObject(newPosition);
            // get neighboring indices as list
            positionOfEatenChecker = (int[]) fromServer.readObject();

            toServer.writeObject(Action.stop.toString());
            System.out.println("client: Close all streams");
            fromServer.close();
            toServer.close();
            socket.close();

            System.out.println("client: Closed operational socket");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return positionOfEatenChecker;
    };

//    public static void closeClient(){
//        toServer.writeObject(Action.stop.toString());
//        System.out.println("client: Close all streams");
//        fromServer.close();
//        toServer.close();
//        socket.close();
//
//        System.out.println("client: Closed operational socket");
//    }


}
