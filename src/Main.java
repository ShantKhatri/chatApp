
import client.ChatClient;
import server.ChatServer;
import ui.ClientChatWindow;
import ui.ServerChatWindow;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Enter 1 to start as a server or 2 to start as a client");
        Scanner sc = new Scanner(System.in);

        byte mode = sc.nextByte();

        switch (mode) {
            case 1 -> {
                System.out.println("This is a full-duplex communication channel.\n" +
                        "You can send to and receive messages from multiple clients simultaneously.\n" +
                        "To send message to a client, write your message in the given message format and then press Enter.\n" +
                        "Message format:\t <client id>#<message>");
                System.out.print("Enter the port: ");
                int port = sc.nextInt();
                ChatServer server = new ChatServer(port);
                ServerChatWindow window = new ServerChatWindow(server);
                server.addObserver(window);
                server.exec();
            }
            case 2 -> {
                try {
                    System.out.println("Enter the host address: ");
                    String address = sc.next();
                    System.out.print("Enter the port: ");
                    int port = sc.nextInt();
                    System.out.println("You ");
                    ChatClient client = new ChatClient(address, port);
                    ClientChatWindow window = new ClientChatWindow(client);
                    client.addObserver(window);
                    client.connect();
                } catch (Exception e) {
                    System.out.println("An error occurred: " + e.getMessage());
                    System.out.println("Exiting...");
                    System.exit(0);
                }
            }
        }
    }


}
