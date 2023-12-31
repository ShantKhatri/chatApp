package client;

import ui.ClientChatWindow;
import util.ChatClientObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import encryptionalgorithm.AES_ENCRYPTION;

public class ChatClient implements Runnable {
    private final int port;
    private final String hostAddress;
    private Socket socket;

    private PrintWriter out;
    private BufferedReader in;

    private boolean connected;
    private String country = null;
    private List<ChatClientObserver> observers;

    public ChatClient(String hostAddress, int port) {
        this.observers = new LinkedList<>();
        this.hostAddress = hostAddress;
        this.port = port;
    }

    public void connect() {
        try {
            this.socket = new Socket(hostAddress, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(new PrintWriter(socket.getOutputStream()));
            connected = true;
            new Thread(this).start();
        } catch (IOException e) {
            System.out.println("Error connecting to server: " + e.getMessage());
            try {
                socket.close();
            } catch (IOException ex) {
                System.out.println("Error occured while closing the client: " + ex.getMessage());
            }
        }
    }

    @Override
    public void run() {
        while(connected) {
            try {
                String line = in.readLine();

                String decrypted = null;
                try {
                    if (line!=null)
                        decrypted = AES_ENCRYPTION.decrypt(line);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                decrypted = filterString(decrypted);
                line=decrypted;
                if(line == null || line.trim().equals("STOP")) {
                    disconnect();
                    return;
                }
                String finalLine = line;
                observers.forEach(o->o.receivedMessage(finalLine));
                System.out.println("Server: " + line);
            } catch (IOException e) {
                System.out.println("Error occurred: " + e.getMessage());
                System.out.println("Disconnecting...");
                disconnect();
            }
        }
        disconnect();
    }
    public synchronized void sendMessage(String message)  {
        String finalMessage = null;
        try {
             finalMessage = AES_ENCRYPTION.encrypt(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        out.println(finalMessage);
        out.flush();
        observers.forEach(o->o.sentMessage(message));
    }

    public synchronized void disconnect() {
        out.println("STOP");
        out.flush();
        connected = false;
        try {
            socket.close();
            System.out.println("Disconnected successfully.");
            observers.forEach(ChatClientObserver::disconnected);
        } catch (IOException e) {
            System.out.println("Error while disconnecting: " + e.getMessage());
        }
    }

    public synchronized String filterString(String line){
        String[] ntvWords = {"kill", "fan", "test"};
        for (int i = 0; i < ntvWords.length; i++) {
            String regex = "\\s*\\b" + ntvWords[i] + "\\b\\s*";
            line = line.replaceAll(regex, "");
        }
        if (country == null){
            String[] bulletWords = {"USA", "India", "Pakistan", "Australia", "UK", "Canada", "Brazil"};
            Random random = new Random();
            int randomIndex = random.nextInt(bulletWords.length);
            country = bulletWords[randomIndex];
        }
        String regex = "\\s*\\b" + "country" + "\\b\\s*";
        line = line.replaceAll(regex, country);
        return line;
    }


    public void addObserver(ChatClientObserver observer) {
        this.observers.add(observer);
    }
}
