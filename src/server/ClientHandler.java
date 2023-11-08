package server;

import encryptionalgorithm.AES_ENCRYPTION;

import java.io.*;
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
                }


                server.messageReceived(id, decrypted);

                System.out.println(line);
                if (isNumeric(line)) {
                    String fileName = "client" + this.id + ".csv";
                    String filepath = "clientcsvfiles" + File.separator + fileName;
                    createCSVFile(filepath, line);
                } else {
                    System.out.println("Received a non-numeric string: " + line);
                }
                System.out.println("Client " + id + ": " + line);

            } catch (IOException e) {
//                System.out.println("Client " + id + " disconnected.");
                running = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        String finalMessage = null;
        try {
            finalMessage = AES_ENCRYPTION.encrypt(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
        out.println(finalMessage);
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


    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public synchronized String filterString(String line){
        String[] ntvWords = {"kill", "fan", "test"};
        for (int i = 0; i < ntvWords.length; i++) {
            String regex = "\\s*\\b" + ntvWords[i] + "\\b\\s*";
            line = line.replaceAll(regex, "");
        }
        return line;
    }

    private static void createCSVFile(String filepath, String numericData) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true));
            writer.write(numericData);
            writer.newLine();
            writer.flush();
            writer.close();
            System.out.println("CSV file created: " + filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
