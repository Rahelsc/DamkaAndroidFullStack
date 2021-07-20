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
    private static Socket socket = null;
    private static ObjectOutputStream toServer = null;
    private static ObjectInputStream fromServer = null;

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
        try {
            createClientSocket();

            //send "matrix" command then write 2d array to socket
            toServer.writeObject(Action.matrix.toString());
            toServer.writeObject(matrixForServer);

            //send command then write an index to socket
            toServer.writeObject(action.toString());
            toServer.writeObject(index);
            toServer.writeObject(offSet);

            // get neighboring indices as list
            AdjacentIndices =
                    new ArrayList<int[]>((List<int[]>) fromServer.readObject());

            System.out.println("regular movement: ");
            for (int[] adjacentIndex : AdjacentIndices) {
                System.out.println(Arrays.toString(adjacentIndex));
            }

            closeClient();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return AdjacentIndices;
    };

    public static Callable<List<int[]>> findEatingMoves = ()->{
        matrixForServer = CheckerBoard.getPositions();
        AdjacentIndices = new ArrayList<>();

        try {

            createClientSocket();

            //send "matrix" command then write 2d array to socket
            toServer.writeObject(Action.matrix.toString());
            toServer.writeObject(matrixForServer);

            //send command then write an index to socket
            toServer.writeObject(action.toString());

            toServer.writeObject(index);
            toServer.writeObject(offSet);
            toServer.writeObject(enemyOffSet);
            // get neighboring indices as list
            AdjacentIndices =
                    new ArrayList<int[]>((List<int[]>) fromServer.readObject());

            System.out.println("eating moves: ");
            for (int[] adjacentIndex : AdjacentIndices) {
                System.out.println(Arrays.toString(adjacentIndex));
            }

            closeClient();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return AdjacentIndices;
    };


    public static Callable<int[]> findWhoIAte = ()->{
        int[] positionOfEatenChecker = new int[2];
        try {
            createClientSocket();
            //send command then write an index to socket
            toServer.writeObject(action.toString());

            toServer.writeObject(newPosition);
            // get neighboring indices as list
            positionOfEatenChecker = (int[]) fromServer.readObject();
            System.out.println("enemy eaten of [" + (newPosition/10)+ ", " + (newPosition%10) + "]: " );
            System.out.println(Arrays.toString(positionOfEatenChecker));
            closeClient();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }


        return positionOfEatenChecker;
    };

    public static void closeClient() throws IOException {
        if (socket != null) {
            toServer.writeObject(Action.stop.toString());
            System.out.println("client: Close all streams");
            fromServer.close();
            toServer.close();
            socket.close();

            System.out.println("client: Closed operational socket");
        }
    }

    public static void createClientSocket() throws IOException {
        socket = new Socket("10.0.2.2", 8010);
        toServer = new ObjectOutputStream(socket.getOutputStream());
        fromServer = new ObjectInputStream(socket.getInputStream());

        System.out.println("client: Created Socket");
    }

}
