package ui;

import client.ChatClient;
import server.ChatServer;
import server.ClientHandler;
import util.ChatClientObserver;
import util.ChatServerObserver;

import javax.swing.*;
import java.awt.*;

public class ClientChatWindow extends JFrame implements ChatClientObserver {
    private final JTextArea transcript;

    private final JPanel centerPanel;

    private ChatClient client;
    public ClientChatWindow(ChatClient client) {
        super("Chat Client");
        this.client = client;
        setLayout(new BorderLayout());

        transcript = new JTextArea(30, 50);
        transcript.setEditable(false);
        transcript.setBackground(Color.BLUE);
        transcript.setForeground(SystemColor.MAGENTA);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        JTextField inputField = new JTextField(50);
        inputPanel.add(inputField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send Message");
        JButton disconnectButton = new JButton("Disconnect");

        sendButton.addActionListener(e -> {
            client.sendMessage(inputField.getText());
        });
        disconnectButton.addActionListener(e-> {
            client.disconnect();
        });

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new GridLayout(1, 2, 5, 5));

        buttonsPanel.add(sendButton);
        buttonsPanel.add(disconnectButton);

        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(buttonsPanel, BorderLayout.EAST);
        centerPanel = new JPanel();
        centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        centerPanel.add(inputPanel);
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

    public void receivedMessage(String message) {
        transcript.setText(transcript.getText() + "\n" + "Received: \t" + message);
    }

    public void sentMessage(String message) {
        transcript.setText(transcript.getText() + "\n" + "Sent: \t" + message);
    }

    @Override
    public void disconnected() {
        transcript.setText(transcript.getText() + "\n" + "Disconnected");
    }
}
