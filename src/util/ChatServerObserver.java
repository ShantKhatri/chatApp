package util;

import server.ClientHandler;

public interface ChatServerObserver {

    void addedClient(ClientHandler clientHandler);
    void receivedMessage(int clientId, String message);
    void sentMessage(int clientId, String message);

    void disconnected(int clientId);
}
