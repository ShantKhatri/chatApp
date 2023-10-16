package server;

import util.ChatServerObserver;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChatServer {

    private ServerSocket serverSocket;
    private int port;
    private boolean listening;

    private List<ClientHandler> clientHandlers;

    private List<ChatServerObserver> observers;

    public ChatServer(int port) {
        this.port = port;
        this.clientHandlers = new LinkedList<>();
        this.observers = new LinkedList<>();
    }

    public void exec() {
        try {
            this.serverSocket = new ServerSocket(port);
            this.port = serverSocket.getLocalPort();
            listening = true;
            new Thread(this::run).start();
            System.out.println("Server started successfully. Listening on port " + port);
        } catch (IOException e) {
            System.out.println("Error occurred while starting the server: " + e.getMessage());

            try {
                serverSocket.close();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Exiting...");
            System.exit(0);
        }

    }

    public void run() {
        while(listening) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected. Client id: " + clientHandlers.size());
                ClientHandler clientHandler = new ClientHandler(clientHandlers.size(), clientSocket, this);
                Thread clientHandlerThread = new Thread(clientHandler);
                clientHandlers.add(clientHandler);
                observers.forEach(o->o.addedClient(clientHandler));
                clientHandlerThread.start();
            } catch (IOException e) {
                System.out.println("Error while connecting to client: " + e.getMessage());
                try {
                    serverSocket.close();
                } catch (IOException e1) {
                    System.out.println("Error occurred while closing server socket:" + e1.getMessage());
                } finally {
                    listening = false;
                }
            }
        }
        try {
            serverSocket.close();
        } catch (IOException e) {
            System.out.println("Error occurred while closing server socket:" + e.getMessage());
        }
        System.out.println("Server stopped.");
    }

    public void stop() {
        listening = false;
    }

    public List<ClientHandler> clientHandlers() {
        return clientHandlers;
    }

    public void addObserver(ChatServerObserver observer) {
        observers.add(observer);
    }

    public synchronized void messageSent(int clientId, String message) {
        observers.forEach(o->o.sentMessage(clientId, message));
    }

    public synchronized void messageReceived(int clientId, String message) {
        observers.forEach(o->o.receivedMessage(clientId, message));
    }

    public void disconnected(int clientId) {
        for (ChatServerObserver observer : observers) {
            observer.disconnected(clientId);
        }
    }
}
