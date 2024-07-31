package main_client;

import GUI.client.Cilent;
import GUI.client.DangNhapJDialog;
import GUI.client.TinNhanJDialog;
import audio.AudioPlayer;
import entity.Product;
import io.IOClient;
import java.awt.TrayIcon;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.SwingUtilities;
import utils.Xnoti;

public class MainClient {

    public static final String HOST = "localhost";
    public static final int PORT = 1235;
    public static IOClient client;
    public static final int COMPUTER_ID = 3;
    public static List<Product> listProducts;
    public static List<BigDecimal> listBalanceClient;
    public static Cilent clientForm;
    public static DangNhapJDialog dangNhapJDialog;
    public static TinNhanJDialog tinNhanForm;
    public static volatile boolean isIncorrect = true;
    public static volatile boolean isGuest = false;
    private static AudioPlayer audioPlayer;

    public static void main(String[] args) {
        com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme.setup();

        java.awt.EventQueue.invokeLater(() -> {
            clientForm = new Cilent();
            tinNhanForm = new TinNhanJDialog(clientForm, true);
            dangNhapJDialog = new DangNhapJDialog(clientForm, false);
            dangNhapJDialog.setVisible(true);

            // Thêm phát âm thanh khi ứng dụng khởi động
            audioPlayer = new AudioPlayer();
            audioPlayer.play("/resources/Audio_Tale-of-Immortal.wav", true); // Phát lặp lại

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

                if (response.startsWith("Server response: Successfully open computer for guest")) {
                    handleGuestOpenComputerResponse();
                    return;
                }
                
                if(response.startsWith("Server response: Login successful with client ID")){
                    isIncorrect = false;
                    return;
                }
                
                if(response.startsWith("Server response: Invalid credentials")){
                    isIncorrect = true;
                    return;
                }

                handleServerMessage(response);
                handleLoginResponse(response);
            };

            // Khởi tạo client và bắt đầu lắng nghe với callback
            client = new IOClient(tinNhanForm);
            client.startListening(callback);
        });
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
        isGuest = true;
        MainClient.client.importBalance();
        dangNhapJDialog.setVisible(false);
        clientForm.setVisible(true);
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
}
