package main_client;

import GUI.client.Cilent;
import GUI.client.ConnectingDialog;
import GUI.client.DangNhapJDialog;
import GUI.client.HelloWorld;
import GUI.client.TinNhanJDialog;
import audio.AudioPlayer;
import entity.Product;
import io.IOClient;
import java.awt.TrayIcon;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import utils.Xnoti;

public class MainClient {

    public static String HOST;
    public static final int PORT = 1235;
    public static IOClient client;
    public static int COMPUTER_ID;
    public static List<Product> listProducts;
    public static List<BigDecimal> listBalanceClient;
    public static HelloWorld hello;
    public static Cilent clientForm;
    public static DangNhapJDialog dangNhapJDialog;
    public static TinNhanJDialog tinNhanForm;
    public static volatile boolean isIncorrect = true;
    public static volatile boolean isGuest = false;
    private static AudioPlayer audioPlayer;
    private static ConnectingDialog connectingDialog;

    public static void main(String[] args) {
        com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme.setup();

        java.awt.EventQueue.invokeLater(() -> {
            hello = new HelloWorld();
            hello.setVisible(true);
            
            // Thêm phát âm thanh khi ứng dụng khởi động
            audioPlayer = new AudioPlayer();
            audioPlayer.play("/resources/Audio_Tale-of-Immortal.wav", true); // Phát lặp lại
        });
    }

    public static void setHostAndComputerID(String host, int computerID) {
        HOST = host;
        COMPUTER_ID = computerID;

        // Hiển thị dialog chờ kết nối
        connectingDialog = new ConnectingDialog();
        connectingDialog.showDialog();

        // Kiểm tra kết nối đến máy chủ trong một SwingWorker
        new SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return testConnection();
            }

            @Override
            protected void done() {
                connectingDialog.closeDialog();
                try {
                    if (get()) {
                        hello.setVisible(false);// Ẩn form HelloWorld
                        startClient();
                        dangNhapJDialog.setVisible(true); // Hiện form dangNhapJDialog
                    } else {
                        JOptionPane.showMessageDialog(hello, "Không thể kết nối đến máy chủ. Vui lòng kiểm tra lại IP.", "Lỗi kết nối", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private static boolean testConnection() {
        try {
            // Thử kết nối đến máy chủ
            Socket socket = new Socket(HOST, PORT);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private static void startClient() {
        clientForm = new Cilent();
        tinNhanForm = new TinNhanJDialog(clientForm, true);
        dangNhapJDialog = new DangNhapJDialog(clientForm, false);

        // Khởi tạo đối tượng callback
        IOClient.ResponseCallback callback = response -> {
            if (response.startsWith("Received product list with size:")) {
                handleProductListResponse();
                return;
            }

            if (response.startsWith("Client getBalance(): ")) {
                handleBalanceResponse();
                return;
            }

            if (response.startsWith("Server response: Opened for guest successfully with client ID")) {
                isGuest = true;
                handleGuestOpenComputerResponse();
                return;
            }

            if (response.startsWith("Server response: Login successful with client ID")) {
                isIncorrect = false;
                return;
            }

            if (response.startsWith("Server response: Invalid credentials")) {
                isIncorrect = true;
                return;
            }

            if (response.startsWith("Server response: Recharges money for the client")) {
                handleRechargeResponse(response);
                return;
            }

            handleServerMessage(response);
            handleLoginResponse(response);
        };

        // Khởi tạo client và bắt đầu lắng nghe với callback
        client = new IOClient(HOST, PORT);
        client.setTinNhanJDialog(tinNhanForm);
        client.startListening(callback);
    }

    // Phương thức để xử lý phản hồi danh sách sản phẩm
    private static void handleProductListResponse() {
        listProducts = client.getListProducts();
    }

    // Phương thức để xử lý phản hồi số dư
    private static void handleBalanceResponse() {
        listBalanceClient = client.getListBalanceClient();
        System.out.println(MainClient.listBalanceClient);
    }

    // Phương thức để xử lý phản hồi mở máy tính cho khách
    private static void handleGuestOpenComputerResponse() {
        // Xử lý khi mở máy tính cho khách vãng lai
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() throws Exception {
                MainClient.client.importBalance();

                // Wait for the response
                int retries = 5; // Adjust as necessary
                while (retries >= 5) {
                    Thread.sleep(200); // Adjust the sleep time as necessary
                    retries--;
                }
                return null;
            }

            @Override
            protected void done() {
                dangNhapJDialog.setVisible(false);
                clientForm.setVisible(true);
            }
        };

        worker.execute();
    }

    // Phương thức để xử lý tin nhắn từ server
    private static void handleServerMessage(String response) {
        String[] parts = response.split("[:;]");
        String tinNhan = parts[parts.length - 1];
        SwingUtilities.invokeLater(() -> {
            if (tinNhanForm.isVisible()) {
                tinNhanForm.appendMessage("Máy chủ: " + tinNhan);
            } else {
                tinNhanForm.appendMessage("Máy chủ: " + tinNhan);
                Xnoti.showTrayMessage("Thông Báo!", "Bạn Có Tin Nhắn Mới! ", TrayIcon.MessageType.INFO);
            }
        });
    }

    // Phương thức để xử lý phản hồi đăng nhập
    private static void handleLoginResponse(String response) {
        SwingUtilities.invokeLater(() -> {
            if (dangNhapJDialog.isVisible()) {
                dangNhapJDialog.notify(response);
            }
        });
    }

    // Phương thức để xử lý phản hồi nạp tiền
    private static void handleRechargeResponse(String response) {
        new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                client.importBalance();
                listBalanceClient = client.getListBalanceClient();
                return null;
            }

            @Override
            protected void done() {

                BigDecimal newBalance = listBalanceClient.get(0);
                BigDecimal priceClient = listBalanceClient.get(1);
                clientForm.setBalanceClient(newBalance, priceClient);
                Xnoti.msg(clientForm, response + " .Số dư mới đã cập nhật", "Thông báo");
            }
        }.execute();
    }
}


