package server;

import pojo.MatrixHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.ServerException;
import java.util.concurrent.*;

/**
 * This class represents a TCP server that can handle multiple clients
 * concurrently. It can solve different algorithmic problems using
 * dedicated handler types
 */
public class InternetServer {
    private final int port;
    /*
     Happens-Before guarantee does NOT ensure thread-safety nor executed quickly.
     It makes sure that if only one thread can change the value at a time, the data will be updated before any other thread can
      access it
     */
    private volatile boolean stopServer; // TODO: transparency between threads
    private ThreadPoolExecutor clientsPool; // handle multiple clients concurrently
    private IHandler requestHandler;

    public InternetServer(int port){
        this.port = port;
        this.clientsPool = null;
        this.requestHandler = null;
        this.stopServer = false; // if server should handle clients' requests
    }

    public void stop(){
        if (!stopServer){
            stopServer = true;
            if (clientsPool!=null)
                clientsPool.shutdown();
        }

    }

    public static void main(String[] args) {
        InternetServer server = new InternetServer(8010);
        server.supportClients(new MatrixHandler());
    }
    public void supportClients(IHandler concreteHandler) {
        this.requestHandler = concreteHandler;

        /*
         * No matter if handling one client or multiple clients,
         * once a server has several operations at the same time,
         * we ought to define different executable paths (threads)
         */

        Runnable clientHandling = () -> {
            this.clientsPool = new ThreadPoolExecutor(
                    10, 15, 200, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>()
            );

            try {
                ServerSocket serverSocket = new ServerSocket(this.port, 50);

                while (!stopServer) {
                    // listen + accept (phases 3+4) are done by accept method
                    Runnable acceptThread = () -> {
                        try {
                            Socket clientToServerConnection = serverSocket.accept();
                            System.out.println("Server: accepting client in " + Thread.currentThread().getName() + " Thread");


                            // Once a client is accepted, pass it to the specific client handling thread
                            Runnable specificClientHandling = () -> {
                                System.out.println("Server: Handling a client in " + Thread.currentThread().getName() + " Thread");

                                try {
                                    requestHandler.handleClient(clientToServerConnection.getInputStream(),
                                            clientToServerConnection.getOutputStream());
                                } catch (IOException | ClassNotFoundException ioException) {
                                    //ioException.printStackTrace();
                                    try {
                                        throw new ServerException("problem getting input or output stream.");
                                    } catch (ServerException e) {
                                        //..
                                    }
                                }

                                // We stopped handling the specific client
                                try {
                                    clientToServerConnection.getOutputStream().close();
                                    //clientToServerConnection.getInputStream().close();
                                    clientToServerConnection.close();
                                } catch (IOException ioException) {
                                    ioException.printStackTrace();
                                }
                            };

                            clientsPool.execute(specificClientHandling);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    };
                    new Thread(acceptThread).start();
                }

                serverSocket.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        };

        new Thread(clientHandling).start();
    }

}
