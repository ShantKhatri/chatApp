# Chat Application

The Chat Application is a Java-based program that enables communication between clients and a central server using socket programming. The application includes features such as secure message encryption, dynamic client addition to the server, and graphical user interfaces for both clients and the server administrator.

## Table of Contents

- [Overview](#overview)
- [Components](#components)
    - [ChatClient Class](#chatclient-class)
    - [ChatServer Class](#chatserver-class)
    - [ClientHandler Class](#clienthandler-class)
    - [AES_ENCRYPTION Class](#aes_encryption-class)
    - [ClientChatWindow Class](#clientchatwindow-class)
    - [ServerChatWindow Class](#serverchatwindow-class)
    - [ChatServerObserver Interface](#chatserverobserver-interface)
- [Usage](#usage)
- [Dependencies](#dependencies)
- [Installation](#installation)
- [Contributing](#contributing)
- [License](#license)

## Overview

The Chat Application is a Java-based program that facilitates communication between clients and a central server using socket programming. It includes secure message encryption, dynamic client addition to the server, and graphical user interfaces for both clients and the server administrator.

## Components

### ChatClient Class

The `ChatClient` class represents a client in the Chat Application. It handles the establishment of a connection to the server, message encryption and decryption using the AES algorithm, and communication with the server. The client also includes a graphical user interface (`ClientChatWindow`) for user interaction.

### ChatServer Class

The `ChatServer` class functions as the central hub for client communication. It listens for incoming client connections, creates a `ClientHandler` for each connected client, and manages the overall communication flow. The server includes a graphical user interface (`ServerChatWindow`) for the server administrator to monitor and interact with connected clients.

### ClientHandler Class

The `ClientHandler` class is responsible for managing the communication between the server and an individual client. Each connected client has its own `ClientHandler` instance. It handles message decryption, filtering, and interaction with the server.

### AES_ENCRYPTION Class

The `AES_ENCRYPTION` class provides methods for encrypting and decrypting messages using the Advanced Encryption Standard (AES) algorithm. This class is utilized for securing the communication between clients and the server.

### ClientChatWindow Class

The `ClientChatWindow` class is a graphical user interface for the chat client. It allows users to view the chat transcript, input messages, and interact with the server. Messages are encrypted before being sent, and decrypted upon receipt.

### ServerChatWindow Class

The `ServerChatWindow` class is the graphical user interface for the chat server. It provides a window for the server administrator to view the chat transcript, input messages to individual clients, and manage client connections.

### ChatServerObserver Interface

The `ChatServerObserver` interface defines methods for observing events in the chat server. Classes that implement this interface can react to client addition, message reception, message sending, and client disconnection events.

## Usage

To use the Chat Application, follow these steps:

1. Start the server by running the `ChatServer` class.
2. Instantiate `ChatClient` instances for each client, providing the server's address and port.
3. Clients can then connect, send encrypted messages, and interact with the server through the graphical user interfaces.

```java
// Server instantiation
ChatServer chatServer = new ChatServer(8080);
chatServer.exec();

// Client instantiation
ChatClient chatClient = new ChatClient("localhost", 8080);
ClientChatWindow clientWindow = new ClientChatWindow(chatClient);
chatClient.connect();
