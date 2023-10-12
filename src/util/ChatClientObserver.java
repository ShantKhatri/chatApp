package util;

public interface ChatClientObserver {
    void receivedMessage(String message);
    void sentMessage(String message);
    void disconnected();
}
