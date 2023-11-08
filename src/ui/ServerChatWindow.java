package ui;

import server.ChatServer;
import server.ClientHandler;
import util.ChatServerObserver;

import javax.swing.*;
import java.awt.*;

public class ServerChatWindow extends JFrame implements ChatServerObserver {
    private final JTextArea transcript;

    private final JPanel centerPanel;
    public ServerChatWindow(ChatServer server) {
        super("Chat Server");
        setLayout(new BorderLayout());

        transcript = new JTextArea(30, 50);
        transcript.setEditable(false);
        transcript.setBackground(Color.GRAY);
        transcript.setForeground(SystemColor.BLACK);

        centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for(ClientHandler clientHandler : server.clientHandlers()) {
            if (clientHandler.isRunning()) {
                initInputPanel(clientHandler);
            }
        }

        JLabel transcriptLabel = new JLabel("Chat Transcript");
        transcriptLabel.setBackground(Color.BLACK);
        transcriptLabel.setForeground(Color.YELLOW);
        transcriptLabel.setOpaque(true);

        JPanel transcriptPanel = new JPanel();
        transcriptPanel.setLayout(new BorderLayout());
        transcriptPanel.add(transcriptLabel, BorderLayout.NORTH);
        transcriptPanel.add(transcript, BorderLayout.CENTER);

        setLayout(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);
        add(new JScrollPane(transcriptPanel), BorderLayout.SOUTH);

        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    public void initInputPanel(ClientHandler clientHandler) {
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        JLabel label = new JLabel("Client " + clientHandler.getId());
        label.setOpaque(true);
        label.setBackground(Color.BLACK);
        label.setForeground(Color.YELLOW);

        JTextField inputField = new JTextField(50);
        JButton sendButton = new JButton("Send Message");
        JButton disconnectButton = new JButton("Disconnect");

        sendButton.addActionListener(e -> {
            clientHandler.sendMessage(inputField.getText());
        });
        disconnectButton.addActionListener(e-> {
            clientHandler.disconnect();
            centerPanel.remove(inputPanel);
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 5, 5));

        buttonsPanel.add(sendButton);
        buttonsPanel.add(disconnectButton);

        inputPanel.add(label, BorderLayout.WEST);
        inputPanel.add(inputField);
        inputPanel.add(buttonsPanel, BorderLayout.EAST);
        centerPanel.add(inputPanel);
        repaint();
    }

    public void receivedMessage(int clientId, String message) {
        transcript.setText(transcript.getText() + "\n" + "Client " + clientId + " to Server: \t" + message);
    }

    public void sentMessage(int clientId, String message) {
        transcript.setText(transcript.getText() + "\n" + "Server to Client " + clientId + " : \t" + message);
    }

    @Override
    public void disconnected(int clientId) {
        transcript.setText(transcript.getText() + "\n" + "Disconnected Client " + clientId + " : \t");
    }

    @Override
    public void addedClient(ClientHandler clientHandler) {
        System.out.println("ADDED CLIENT");
        initInputPanel(clientHandler);
    }
}
