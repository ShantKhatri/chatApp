package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private final int id;

    public int getId() {
        return id;
    }

    public boolean isRunning() {
        return running;
    }

    private boolean running;
    private ChatServer server;
    public ClientHandler(int id, Socket clientSocket, ChatServer server) {
        this.id = id;
        this.server = server;
        this.clientSocket = clientSocket;
        try {
            this.out = new PrintWriter(clientSocket.getOutputStream());
            this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
           System.out.println("An error occurred while initiating communication with client " + id);
        }
    }

    @Override
    public void run() {
        running = true;
        while(running) {
            try {
                String line = in.readLine();
                if(line == null || line.trim().equals("STOP")) {
                    disconnect();
                }
                server.messageReceived(id, line);
                System.out.println("Client " + id + ": " + line);

            } catch (IOException e) {;
//                System.out.println("Client " + id + " disconnected.");
                running = false;
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
        out.flush();
        server.messageSent(id, message);
        System.out.println("Sent message to Client " + id + " : " + message);
    }

    public void disconnect() {
        running = false;
        try {
            clientSocket.close();
            server.disconnected(id);
            System.out.print("Client " + id + " disconnected successfully.");
        } catch (IOException e) {
            System.out.println("Error occurred while disconnecting: " + e.getMessage());
        }
    }
}
