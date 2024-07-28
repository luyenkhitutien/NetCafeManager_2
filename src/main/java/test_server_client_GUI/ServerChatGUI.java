package test_server_client_GUI;

import io.IOServer;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerChatGUI extends JFrame {

    private JTextArea chatArea;
    public JTextField clientIdField;
    private JTextField messageField;
    private JList<String> clientList;
    private DefaultListModel<String> clientListModel;
    private final IOServer server;

    public ServerChatGUI(IOServer server) {
        this.server = server;

        setTitle("Server GUI");
        setSize(600, 400);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        getContentPane().add(panel);
        placeComponents(panel);

        setVisible(true);

        // Update client list periodically
        new Timer(2000, (ActionEvent e) -> {
            updateClientList();
        }).start();

        // Add Window Listener for handling window close event
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                //formWindowClosed(e);
            }
        });
    }

    public void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel chatLabel = new JLabel("Chat:");
        chatLabel.setBounds(10, 10, 80, 25);
        panel.add(chatLabel);

        chatArea = new JTextArea();
        chatArea.setBounds(10, 40, 560, 150);
        chatArea.setEditable(false);
        JScrollPane chatScrollPane = new JScrollPane(chatArea);
        chatScrollPane.setBounds(10, 40, 560, 150);
        panel.add(chatScrollPane);

        JLabel clientIdLabel = new JLabel("ComputerID: ");
        clientIdLabel.setBounds(10, 200, 80, 25);
        panel.add(clientIdLabel);

        clientIdField = new JTextField(20);
        clientIdField.setBounds(100, 200, 165, 25);
        panel.add(clientIdField);

        JLabel messageLabel = new JLabel("Message:");
        messageLabel.setBounds(10, 230, 80, 25);
        panel.add(messageLabel);

        messageField = new JTextField(20);
        messageField.setBounds(100, 230, 165, 25);
        panel.add(messageField);

        JButton sendButton = new JButton("Send");
        sendButton.setBounds(10, 260, 80, 25);
        panel.add(sendButton);

        sendButton.addActionListener((ActionEvent e) -> {
            try {
                sendMessage();
                sendButton.setSelected(false);
            } catch (IOException ex) {
                Logger.getLogger(ServerChatGUI.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        JLabel clientListLabel = new JLabel("Connected Clients:");
        clientListLabel.setBounds(300, 200, 150, 25);
        panel.add(clientListLabel);

        clientListModel = new DefaultListModel<>();
        clientList = new JList<>(clientListModel);
        clientList.setBounds(300, 230, 270, 120);
        JScrollPane clientListScrollPane = new JScrollPane(clientList);
        clientListScrollPane.setBounds(300, 230, 270, 120);
        panel.add(clientListScrollPane);
    }

    private void sendMessage() throws IOException {
        try {
            String clientIdText = clientIdField.getText().trim();
            if (clientIdText.isEmpty()) {
                chatArea.append("Client ID cannot be empty.\n");
                return;
            }

            int clientId = Integer.parseInt(clientIdText);
            String message = messageField.getText().trim();
            if (message.isEmpty()) {
                chatArea.append("Message cannot be empty.\n");
                return;
            }

            server.sendMessageToClient(clientId, message);
            chatArea.append("Server to Client " + clientId + ": " + message + "\n");
            messageField.setText("");
        } catch (NumberFormatException e) {
            chatArea.append("Invalid client ID format.\n");
        }
    }

    private void updateClientList() {
        Map<Integer, IOServer.ClientHandler> clients = IOServer.loggedInClientsMap;
        clientListModel.clear();
        for (Map.Entry<Integer, IOServer.ClientHandler> entry : clients.entrySet()) {
            int clientId = entry.getKey();
            int computerID = entry.getValue().getComputerID();
            clientListModel.addElement("Client " + clientId + " (Máy " + computerID + ")");
        }
    }

    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }
    
//    private void formWindowClosed(WindowEvent evt) {
//        server.shutdownServer();
//    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                IOServer server = new IOServer(1);
                ServerChatGUI gui = new ServerChatGUI(server);
                server.setChatGUI(gui);
                server.start();
            } catch (IOException e) {
            }
        });
    }
}
