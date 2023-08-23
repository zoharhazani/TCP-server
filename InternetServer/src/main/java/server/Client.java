package server;

import pojo.Index;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.*;

public class Client {
    public static void main(String[] args) throws ClassNotFoundException {
        try {
            Socket clientSocket = new Socket("127.0.0.1",8010);
            System.out.println("Socket created");

            ObjectOutputStream toServer = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream fromServer = new ObjectInputStream(clientSocket.getInputStream());

            int[][] sourceArray = {
                    {1,1,1},
                    {1,1,1},
                    {1,1,1}
            };

            toServer.writeObject("matrix");
            toServer.writeObject(sourceArray);

            Index index1 = new Index(0,0);
            Index index2 = new Index(2,0);

            // ---------------------- 1
            toServer.writeObject("all connected components");
            Set<Set<Index>> allConnectedComponent = new LinkedHashSet<>((HashSet<Set<Index>>)fromServer.readObject());
            System.out.println("all Connected Component are:  "+ allConnectedComponent);
            // end ------------------ 1

            // ---------------------- 2
            toServer.writeObject("all shortest paths");
            toServer.writeObject(index1);
            toServer.writeObject(index2);
            Set<Set<Index>> allShortestPaths = new LinkedHashSet<>((HashSet<Set<Index>>)fromServer.readObject());
            System.out.println("all Shortest Paths:  "+ allShortestPaths);
            //end ------------------ 2

            // ---------------------- 3
            toServer.writeObject("submarines");
            int validSubmarines = (int)fromServer.readObject();
            System.out.println("valid Submarines is " + validSubmarines);
            // end ------------------ 3

            int[][] weightedArray = {
                    {600,100,600},
                    {100,100,100},
                    {600,100,600}
            };
            Index index3 = new Index(1,0);
            Index index4 = new Index(1,2);

            // ---------------------- 4
            toServer.writeObject("matrix");
            toServer.writeObject(weightedArray);
            toServer.writeObject("find the lowest weight paths");
            toServer.writeObject(index3);
            toServer.writeObject(index4);
            Set<Set<Index>> allLowestWeightPaths = new LinkedHashSet<>((HashSet<Set<Index>>)fromServer.readObject());
            System.out.println("all Lowest Weight Paths:  "+ allLowestWeightPaths);
            // end ------------------ 4

            toServer.writeObject("stop");

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

}
