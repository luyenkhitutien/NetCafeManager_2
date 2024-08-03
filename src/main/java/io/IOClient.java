package io;

import GUI.client.TinNhanJDialog;
import entity.Product;
import java.io.*;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import static main_client.MainClient.HOST;
import static main_client.MainClient.PORT;

public class IOClient {
    
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private Thread listeningThread;
    private volatile boolean isLogout;
    private ResponseCallback callback;
    private List<Product> listProducts;
    private List<BigDecimal> listBalanceClient;
    private TinNhanJDialog tinNhan;

    public IOClient(String HOST, int PORT) {
        try {
            this.socket = new Socket(HOST, PORT);
            out = new ObjectOutputStream(socket.getOutputStream());
            out.flush(); // Ensure the stream header is written immediately
            in = new ObjectInputStream(socket.getInputStream());
            isLogout = false;
        } catch (IOException ex) {
            Logger.getLogger(IOClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void setTinNhanJDialog(TinNhanJDialog tinNhan){
        this.tinNhan = tinNhan;
    }

    public synchronized void openComputer() {
        String request = "WAITING_OPEN";
        try {
            out.writeObject(request);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(IOClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void toMaintain() {
        String request = "MAINTENANCE";
        try {
            out.writeObject(request);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(IOClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private synchronized void openGuest() throws IOException {
        String request = "GUEST";
        out.writeObject(request);
        out.flush();
    }

    public synchronized void login(String username, String password) throws IOException {
        String request = "LOGIN;" + username + ";" + password;
        out.writeObject(request);
        out.flush();

        // Kiểm tra và khởi động lại thread nếu cần thiết
        if (listeningThread == null || !listeningThread.isAlive() || listeningThread.isInterrupted()) {
            startListening(this.callback); // Sử dụng biến thành viên callback
        }
    }

    public synchronized void changePassword(String newPassword) throws IOException {
        String request = "CHANGE_PASSWORD;" + newPassword;
        out.writeObject(request);
        out.flush();
    }

    public synchronized void orderProduct(String productName, int quantity) throws IOException {
        String request = "ORDER_PRODUCT;" + productName + ";" + quantity;
        out.writeObject(request);
        out.flush();
    }

    public synchronized void logout() throws IOException, ClassNotFoundException {
        isLogout = true; // Thiết lập isLogout

        String request = "LOGOUT";
        out.writeObject(request);
        out.flush();
    }

    public synchronized void shutdown() {

        String request = "SHUTDOWN";
        try {
            out.writeObject(request);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(IOClient.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            // Đảm bảo logout được thực hiện trước khi shutdown
            if (!isLogout) {
                logout();
                // Ngắt thread lắng nghe

            }
            if (listeningThread != null && listeningThread.isAlive()) {
                listeningThread.interrupt();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Exit the application
            System.exit(0);
        }
    }

    public synchronized void sendMessageToServer(String message){
        String request = "SEND_MESSAGE;" + message;
        try {
            
            out.writeObject(request);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(IOClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void importListProduct() {
        try {
            String request = "IMPORT_LIST_PRODUCT";
            out.writeObject(request);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(IOClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized void importBalance() {
        String request = "GET_BALANCE";
        try {
            out.writeObject(request);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(IOClient.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public synchronized void sendComputerID(int computerID) {
        String request = "CHECK_COMPUTER_ID;" + computerID;
        try {
            out.writeObject(request);
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(IOClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    public synchronized void startListening(ResponseCallback callback) {
        this.callback = callback; // Lưu trữ callback trong biến thành viên
        listeningThread = new Thread(() -> {
            try {
                while (true) {
                    if (Thread.currentThread().isInterrupted()) {
                        break; // Thoát khỏi vòng lặp nếu thread bị ngắt
                    }

                    String responseType = (String) in.readObject();

                    if (responseType != null) {
                        String[] parts = responseType.split(";");
                        String command = parts[0];
                        System.out.println("command client => " + command);
                        switch (command) {
                            case "RESPONSE_TEXT" -> {
                                String response = (String) in.readObject();
                                callback.onResponseReceived("Server response: " + response);
                            }
                            case "SHUTDOWN" -> {
                                this.logout();
                                this.shutdown();
                                callback.onResponseReceived("Server response: SHUTDOWN");
                                break; // Thoát khỏi vòng lặp
                            }
                            case "OPEN_GUEST" -> {
                                this.openGuest();
                                callback.onResponseReceived("Server response: OPEN_GUEST");
                            }
                            case "RESPONSE_PRODUCT_LIST" -> {
                                this.listProducts = (List<Product>) in.readObject();
                                System.out.println("List IOClient: " + listProducts.size());
                                callback.onResponseReceived("Received product list with size: " + listProducts.size());
                            }
                            case "RESPONSE_BALANCE" -> {
                                this.listBalanceClient = (List<BigDecimal>) in.readObject();
                                System.out.println("io.IOClient.startListening().getBalance(): " + listBalanceClient);
                                callback.onResponseReceived("Client getBalance(): " + listBalanceClient);
                            }
                            case "SERVER_CLIENTS" -> {
                                if (parts.length > 1) {
                                    String message = parts[1];
                                    callback.onResponseReceived(message);
                                } else {
                                    callback.onResponseReceived("Server response: Missing message content");
                                }
                            }
                            default -> {
                                callback.onResponseReceived("Server response: " + responseType);
                            }
                        }
                    } else {
                        callback.onResponseReceived("Received null response. Continuing...");
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                if (!isLogout) { // Chỉ in lỗi nếu không phải do isLogout
                    e.printStackTrace();
                }
            }
        });

        listeningThread.start();
    }

    private synchronized void receiveMessage(String message) throws SQLException {
        String fullMessage = "Message from Server: " + message;
        System.out.println(fullMessage);
        if (tinNhan != null) {
            System.out.println("tinNhanKhongNull: =>" + tinNhan);
            SwingUtilities.invokeLater(() -> tinNhan.appendMessage(fullMessage));
        } else {
            System.out.println("IOClient => Khong nhan duoc tin nhan");
        }
    }

    //Phương thức để truy cập listProducts
    public List<Product> getListProducts() {
        return listProducts;
    }

    public synchronized List<BigDecimal> getListBalanceClient() {
        return listBalanceClient;
    }

    public interface ResponseCallback {

        void onResponseReceived(String response);
    }
    
    public static boolean testConnection(String host, int port) {
    try (Socket socket = new Socket()) {
        socket.connect(new InetSocketAddress(host, port), 2000); // Thời gian chờ 2 giây
        OutputStream output = socket.getOutputStream();
        output.write("CHECK_CONNECTION".getBytes());
        output.flush();
        socket.close();
        return true;
    } catch (IOException e) {
        return false;
    } 
}
}
