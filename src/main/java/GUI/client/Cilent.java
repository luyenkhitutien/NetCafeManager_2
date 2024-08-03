package GUI.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import main_client.MainClient;
import utils.Xnoti;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author ASUS
 */
public class Cilent extends javax.swing.JFrame {

    private BigDecimal amountUsedFromOrder = BigDecimal.ZERO;

    private BigDecimal balance;
    private BigDecimal price;
    private int hoursUsed = 0;
    private int minutesUsed = 0;
    private BigDecimal amountUsed = BigDecimal.ZERO;
    private boolean fiveMinuteWarningShown = false;
    private Timer timer;

    public Cilent() {
        setUndecorated(true);
        initComponents();
        init();
        setLocationToRight();
    }

    private void setLocationToRight() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int width = getWidth();
        int height = getHeight();
        int x = screenSize.width - width;
        int y = 0;  // or any other value depending on your requirement
        setLocation(x, y);
    }

    private void init() {
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void logout() {
        try {
            MainClient.isIncorrect = true;
            MainClient.isGuest = false;
            MainClient.client.logout();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Cilent.class.getName()).log(Level.SEVERE, null, ex);
        }

        this.setVisible(false);
        MainClient.dangNhapJDialog.setVisible(true);
    }

    public void changePasswordFormTrue() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        DoiMatKhauJDialog doimatkhau = new DoiMatKhauJDialog(frame, true);
        doimatkhau.setVisible(true);
    }

    public void serviceFormTrue() {
        dichvu_text dichvu_text = new dichvu_text();
        dichvu_text.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        dichvu_text.setVisible(true);
    }

    public void MessageFormTrue() {
        MainClient.tinNhanForm.setVisible(true);
    }

    public void systemTray(String response) {
        Xnoti.showTrayMessage("New response from SERVER", response, TrayIcon.MessageType.INFO);
    }

// Phương thức thiết lập số dư cho khách hàng
    public void setBalanceClient(BigDecimal newBalance, BigDecimal price) {

        //Xử lý khi có số dư mới để cập nhật chính xác số dư ở thời gian thực
        BigDecimal moneyUsing = getTxtTienDaSuDung();

        this.balance = newBalance.subtract(moneyUsing);
        this.price = price;

        if (MainClient.isGuest) { //Khách vãng lai
            txtSoDu.setText("0");
            txtTongThoiGian.setText("");
        } else {
            txtSoDu.setText(balance.toString());
            recalculateTimeBasedOnBalance(balance);
        }

        if (timer == null || !timer.isRunning()) {
            startTimer();
        }
    }

    public void setForm() {
        // Cập nhật hiển thị thời gian đã sử dụng
        String formattedUsedTime = String.format("%02d:%02d", hoursUsed, minutesUsed);
        txtThoiGianSuDung.setText(formattedUsedTime);

        // Cập nhật hiển thị số tiền đã sử dụng
        txtTienDaSuDung.setText(amountUsed.add(amountUsedFromOrder).toString() + "Đ");

        if (!MainClient.isGuest) {
            BigDecimal currentBalance = getTxtSoDu();
            recalculateTimeBasedOnBalance(currentBalance);
        }
    }

    private void recalculateTimeBasedOnBalance(BigDecimal balance) {
        BigDecimal pricePerMinute = price.divide(new BigDecimal(60), RoundingMode.DOWN);
        int totalMinutesAvailable = balance.divide(pricePerMinute, RoundingMode.DOWN).intValue();

        int remainingHours = totalMinutesAvailable / 60;
        int remainingMinutes = totalMinutesAvailable % 60;
        String formattedRemainingTime = String.format("%02d:%02d", remainingHours, remainingMinutes);
        txtTongThoiGian.setText(formattedRemainingTime);
    }

    private void startTimer() {
        timer = new Timer(6000, new ActionListener() { // 60000 milliseconds = 1 phút
            @Override
            public void actionPerformed(ActionEvent e) {
                minutesUsed++;
                if (minutesUsed == 60) {
                    minutesUsed = 0;
                    hoursUsed++;
                }

                BigDecimal pricePerMinute = price.divide(new BigDecimal(60), RoundingMode.DOWN);
                amountUsed = amountUsed.add(pricePerMinute);

                // Chỉ cần cộng dồn amountUsedFromOrder một lần, sau đó không cần thêm vào nữa
                if (amountUsedFromOrder.compareTo(BigDecimal.ZERO) > 0) {
                    amountUsed = amountUsed.add(amountUsedFromOrder);
                    amountUsedFromOrder = BigDecimal.ZERO; // Reset lại để không cộng thêm nữa
                }

                setForm();

                if (!MainClient.isGuest) {
                    BigDecimal currentBalance = getTxtSoDu().subtract(pricePerMinute);
                    
                    if (currentBalance.compareTo(BigDecimal.ZERO) <= 0) {
                        Xnoti.msg(MainClient.clientForm, "Số dư đã hết", "Thông báo");
                        stopTimer();
                        logout();
                    } else {
                        txtSoDu.setText(currentBalance.toString());
                        recalculateTimeBasedOnBalance(currentBalance);
                    }
                }
            }
        });
        timer.setInitialDelay(0); // Bắt đầu ngay lập tức
        timer.start();
    }

// Phương thức dừng timer
    private void stopTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        timer = null; // Reset Timer
    }

    private void resetForm() {
        // Reset các trường văn bản
        txtTongThoiGian.setText("");
        txtSoDu.setText("");
        txtThoiGianSuDung.setText("");
        txtTienDaSuDung.setText("");

        // Reset các biến liên quan đến thời gian và số tiền đã sử dụng
        hoursUsed = 0;
        minutesUsed = 0;
        amountUsed = BigDecimal.ZERO;
        amountUsedFromOrder = BigDecimal.ZERO;
        balance = BigDecimal.ZERO;

        // Reset cảnh báo năm phút còn lại
        fiveMinuteWarningShown = false;

        // Dừng và reset Timer nếu đang chạy
        stopTimer();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        pnlChinh = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblAnhBaner = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnDichVu = new javax.swing.JButton();
        btnDoiMatKhau = new javax.swing.JButton();
        btnTinNhan = new javax.swing.JButton();
        btnDangXuat = new javax.swing.JButton();
        txtTongThoiGian = new javax.swing.JTextField();
        txtThoiGianSuDung = new javax.swing.JTextField();
        txtSoDu = new javax.swing.JTextField();
        txtTienDaSuDung = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();

        jLabel8.setText("jLabel8");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                formComponentHidden(evt);
            }
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        pnlChinh.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlChinh.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N
        jLabel1.setText("Tổng thời gian:");

        jLabel2.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N
        jLabel2.setText("Thời gian sử dụng:");

        jLabel4.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N
        jLabel4.setText("Số dư:");

        lblAnhBaner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BanerCilent2.png"))); // NOI18N

        jLabel6.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N
        jLabel6.setText("Tiền đã sử dụng:");

        btnDichVu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/CilentDichVu.png"))); // NOI18N
        btnDichVu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDichVuActionPerformed(evt);
            }
        });

        btnDoiMatKhau.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/CilentDoiMatKhau.png"))); // NOI18N
        btnDoiMatKhau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDoiMatKhauActionPerformed(evt);
            }
        });

        btnTinNhan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/CilentTinNhan.png"))); // NOI18N
        btnTinNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTinNhanActionPerformed(evt);
            }
        });

        btnDangXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/CilentDanXuat.png"))); // NOI18N
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });

        txtTongThoiGian.setEditable(false);
        txtTongThoiGian.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N

        txtThoiGianSuDung.setEditable(false);
        txtThoiGianSuDung.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N

        txtSoDu.setEditable(false);
        txtSoDu.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N
        txtSoDu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSoDuActionPerformed(evt);
            }
        });

        txtTienDaSuDung.setEditable(false);
        txtTienDaSuDung.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblAnhBaner, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtThoiGianSuDung, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                            .addComponent(txtSoDu, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtTienDaSuDung, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtTongThoiGian)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTinNhan))
                        .addGap(89, 89, 89)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDoiMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(lblAnhBaner, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTongThoiGian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtThoiGianSuDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(58, 58, 58)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtSoDu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTienDaSuDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(62, 62, 62)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDichVu, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDoiMatKhau))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnDangXuat)
                    .addComponent(btnTinNhan))
                .addGap(53, 53, 53))
        );

        pnlChinh.add(jPanel1);
        jPanel1.setBounds(0, 0, 340, 460);

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/GiaoDienCilent.png"))); // NOI18N
        pnlChinh.add(jLabel10);
        jLabel10.setBounds(-10, -220, 360, 1070);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChinh, javax.swing.GroupLayout.DEFAULT_SIZE, 341, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChinh, javax.swing.GroupLayout.DEFAULT_SIZE, 811, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtSoDuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSoDuActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSoDuActionPerformed

    private void btnDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
        // TODO add your handling code here:
        this.logout();
    }//GEN-LAST:event_btnDangXuatActionPerformed

    private void btnDichVuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDichVuActionPerformed
        // TODO add your handling code here:
        this.serviceFormTrue();
    }//GEN-LAST:event_btnDichVuActionPerformed

    private void btnDoiMatKhauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDoiMatKhauActionPerformed
        // TODO add your handling code here:
        this.changePasswordFormTrue();
    }//GEN-LAST:event_btnDoiMatKhauActionPerformed

    private void btnTinNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTinNhanActionPerformed
        // TODO add your handling code here:
        this.MessageFormTrue();
    }//GEN-LAST:event_btnTinNhanActionPerformed

    private void formComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentHidden
        // TODO add your handling code here:
        resetForm();
    }//GEN-LAST:event_formComponentHidden

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        BigDecimal balanceClient = MainClient.listBalanceClient.get(0);
        BigDecimal priceClient = MainClient.listBalanceClient.get(1);
        setBalanceClient(balanceClient, priceClient);
    }//GEN-LAST:event_formComponentShown

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cilent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cilent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cilent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cilent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cilent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new Cilent().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JButton btnDichVu;
    private javax.swing.JButton btnDoiMatKhau;
    private javax.swing.JButton btnTinNhan;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblAnhBaner;
    private javax.swing.JPanel pnlChinh;
    private javax.swing.JTextField txtSoDu;
    private javax.swing.JTextField txtThoiGianSuDung;
    private javax.swing.JTextField txtTienDaSuDung;
    private javax.swing.JTextField txtTongThoiGian;
    // End of variables declaration//GEN-END:variables

    public JButton getBtnTinNhan() {
        return btnTinNhan;
    }

    public void setBtnTinNhan(JButton btnTinNhan) {
        this.btnTinNhan = btnTinNhan;
    }

    public void updateTxtSoDu(BigDecimal balance, BigDecimal amountFromOrder) {
        amountUsedFromOrder = amountUsedFromOrder.add(amountFromOrder);
        txtSoDu.setText(balance.toString());
        recalculateTimeBasedOnBalance(balance); // Tính lại thời gian dựa trên số dư mới
    }
    
    public BigDecimal getTxtSoDu() {
        try {
            return new BigDecimal(txtSoDu.getText());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    public void updateTxtTienSuDung(BigDecimal money) {
        amountUsedFromOrder = money; // Cập nhật số tiền từ order cho khách vãng lai
        txtTienDaSuDung.setText(amountUsed.add(amountUsedFromOrder).toString() + "Đ");
    }

    public BigDecimal getTxtTienDaSuDung() {
        try {
            // Loại bỏ ký tự không cần thiết và khoảng trắng thừa
            String text = txtTienDaSuDung.getText().replace("Đ", "").trim();
            return new BigDecimal(text);
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
