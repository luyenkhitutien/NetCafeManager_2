package main_client;

import GUI.client.Cilent;
import GUI.client.DangNhapJDialog;
import GUI.client.TinNhanJDialog;
import entity.Product;
import io.IOClient;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class MainClient {

    public static final String HOST = "localhost";
    public static final int PORT = 1235;
    public static IOClient client;
    public static final int COMPUTER_ID = 4;
    public static List<Product> listProducts;
    public static List<BigDecimal> listBalanceClient;
    public static Cilent clientForm;
    public static DangNhapJDialog dangNhapJDialog;
    public static TinNhanJDialog tinNhanForm;

    public static void main(String[] args) {

        com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme.setup();

        java.awt.EventQueue.invokeLater(() -> {
            clientForm = new Cilent();
            tinNhanForm = new TinNhanJDialog(clientForm, true);
//            tinNhanForm.setVisible(true);
            dangNhapJDialog = new DangNhapJDialog(clientForm, false);
            dangNhapJDialog.setVisible(true);
            // Thêm sự kiện cho nút btnTinNhan
            clientForm.getBtnTinNhan().addActionListener(e -> {
                if (!tinNhanForm.isVisible()) {
                    tinNhanForm.setVisible(true);
                }
            });
            // Khởi tạo đối tượng callback
            IOClient.ResponseCallback callback = response -> {
                if (response.startsWith("Received product list with size:")) {
                    listProducts = client.getListProducts();
                    return;
                }

                if (response.startsWith("Client getBalance(): ")) {
                    listBalanceClient = client.getListBalanceClient();
                    clientForm.getBalaceClient();
                    return;
                }

                // Cập nhật giao diện người dùng với tin nhắn từ server
                SwingUtilities.invokeLater(() -> {
                    if (tinNhanForm.getTxtTinNhan().isVisible()) {
                        String[] parts = response.split("[:;]");
                        String tinNhan = parts[parts.length - 1];
                        tinNhanForm.appendMessage("Máy chủ: " + tinNhan); // Gọi phương thức appendMessage trong TinNhanJDialog
                    }
                });

                SwingUtilities.invokeLater(() -> {
                    if (dangNhapJDialog.isVisible()) {
                        dangNhapJDialog.notify(response);
                    } else {
                        System.out.println("MainClient => " + response);
                    }
                });

                SwingUtilities.invokeLater(() -> {
                    if (dangNhapJDialog.isVisible()) {
//                        dangNhapJDialog.notify(response);
                        if (response.equalsIgnoreCase("Server response: Invalid credentials")) {
                            return;
                        } else if (response.startsWith("Server response: Login successful with client ID: ")) {
                            MainClient.client.importBalance();
                            MainClient.clientForm.setVisible(true);
                            dangNhapJDialog.dispose();
                        }
                    }
                });
            };

//             Khởi tạo client và bắt đầu lắng nghe với callback
            client = new IOClient(tinNhanForm);

            client.startListening(callback);

            // Thực hiện các hành động khác của client
            SwingWorker<Void, Void> worker = new SwingWorker<>() {
                @Override
                protected Void doInBackground() {
                    if (dangNhapJDialog.isVisible()) {
                        client.openComputer();
                    }
                    client.importListProduct();

                    return null;
                }

                @Override
                protected void done() {
                    // Cập nhật form hoặc thực hiện bất kỳ hành động cuối cùng nào nếu cần thiết

                }
            };

            worker.execute();
        }
        );
    }

}
