package GUI.client;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.TrayIcon;
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

    private BigDecimal balance;
    private BigDecimal price;

    private int totalTime; // Tổng thời gian (tính bằng giây)
    private int ratePerHour; // Giá mỗi giờ
    private int ratePerSecond; // Giá mỗi giây
    private int usedTime; // Thời gian đã sử dụng (tính bằng giây)
    private int remainingTime; 
    private Timer timer;

    public Cilent() {
        setUndecorated(true);
        initComponents();
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

    public void logout() {
        try {
            MainClient.client.logout();
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Cilent.class.getName()).log(Level.SEVERE, null, ex);
        }
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        DangNhapJDialog dangNhap = new DangNhapJDialog(frame, true);
        dangNhap.setVisible(true);
        this.setVisible(false);
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
//        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
//        TinNhanJDialog tinnhan = new TinNhanJDialog(frame, true);
//        tinnhan.setVisible(true);
    }

    public class TimeUsage {

        int hoursUsed = 0;
        int minutesUsed = 0;
        int secondsUsed = 0;
        BigDecimal amountUsed = BigDecimal.ZERO;
    }

    private void setBalanceClient(BigDecimal balance, BigDecimal price) {
        txtSoDu.setText(balance.toString());
        System.out.println("Đang sử dụng máy có giá: " + price);
        BigDecimal totalTimeDecimal = balance.divide(price, RoundingMode.DOWN);
        totalTime = totalTimeDecimal.intValue();

        BigDecimal remainingBalance = balance.remainder(price);
        int remainingMinutes = remainingBalance.multiply(new BigDecimal("60")).divide(price, RoundingMode.DOWN).intValue();

        TimeUsage timeUsage = new TimeUsage();

        Thread timerThread = new Thread(() -> {
            try {
                while (true) {
                    // Tăng số giây đã sử dụng
                    timeUsage.secondsUsed++;
                    if (timeUsage.secondsUsed == 60) {
                        timeUsage.secondsUsed = 0;
                        timeUsage.minutesUsed++;
                        if (timeUsage.minutesUsed == 60) {
                            timeUsage.minutesUsed = 0;
                            timeUsage.hoursUsed++;
                        }
                    }

                    // Tính số tiền đã sử dụng
                    timeUsage.amountUsed = price.multiply(new BigDecimal(timeUsage.hoursUsed))
                                      .add(price.multiply(new BigDecimal(timeUsage.minutesUsed))
                                                .divide(new BigDecimal(60), RoundingMode.DOWN))
                                      .add(price.multiply(new BigDecimal(timeUsage.secondsUsed))
                                                .divide(new BigDecimal(3600), RoundingMode.DOWN));

                    // Tính thời gian còn lại
                    int remainingHours = totalTime - timeUsage.hoursUsed;
                    int remainingMinutesUpdate = remainingMinutes - timeUsage.minutesUsed;
                    int remainingSecondsUpdate = 60 - timeUsage.secondsUsed;
                    if (remainingSecondsUpdate == 60) {
                        remainingSecondsUpdate = 0;
                        remainingMinutesUpdate--;
                    }
                    if (remainingMinutesUpdate < 0) {
                        remainingMinutesUpdate += 60;
                        remainingHours--;
                    }

                    // Định dạng thời gian đã sử dụng và còn lại thành 00:00:00
                    String formattedUsedTime = String.format("%02d:%02d:%02d", timeUsage.hoursUsed, timeUsage.minutesUsed, timeUsage.secondsUsed);
                    String formattedRemainingTime = String.format("%02d:%02d:%02d", remainingHours, remainingMinutesUpdate, remainingSecondsUpdate);

                    // Cập nhật giao diện người dùng một cách thread-safe
                    SwingUtilities.invokeLater(() -> {
                        txtThoiGianSuDung.setText(formattedUsedTime);
                        txtThoiGianConLai.setText(formattedRemainingTime);
                        txtTienDaSuDung.setText(timeUsage.amountUsed + "Đ");
                    });

                    if (remainingHours <= 0 && remainingMinutesUpdate <= 0 && remainingSecondsUpdate <= 0) {
                        System.out.println("Thời gian sử dụng đã hết.");
                        break;
                    }

                    // Tạm dừng luồng 1 giây (1000 ms)
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Luồng gặp lỗi khi chạy");
            }
        });

        // Khởi động luồng
        timerThread.start();

        // Định dạng thời gian tổng cộng thành 00:00:00
        String formattedTime = String.format("%02d:%02d:%02d", totalTime, remainingMinutes, 0);
        txtTongThoiGian.setText(formattedTime);
    }

    public void getBalaceClient() {
        if (this.isVisible()) {
            balance = MainClient.listBalanceClient.get(0);
            price = MainClient.listBalanceClient.get(1);
            this.setBalanceClient(balance, price);
        }
    }

    public void systemTray(String response) {
        Xnoti.showTrayMessage("New response from SERVER", response, TrayIcon.MessageType.INFO);
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
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        lblAnhBaner = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        btnDichVu = new javax.swing.JButton();
        btnDoiMatKhau = new javax.swing.JButton();
        btnTinNhan = new javax.swing.JButton();
        btnDangXuat = new javax.swing.JButton();
        txtTongThoiGian = new javax.swing.JTextField();
        txtThoiGianSuDung = new javax.swing.JTextField();
        txtThoiGianConLai = new javax.swing.JTextField();
        txtSoDu = new javax.swing.JTextField();
        txtTienDaSuDung = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();

        jLabel8.setText("jLabel8");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlChinh.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlChinh.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N
        jLabel1.setText("Tổng thời gian:");

        jLabel2.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N
        jLabel2.setText("Thời gian sử dụng:");

        jLabel3.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N
        jLabel3.setText("Thời gian còn lại:");

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

        txtThoiGianConLai.setEditable(false);
        txtThoiGianConLai.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N

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
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtThoiGianSuDung)
                            .addComponent(txtThoiGianConLai)
                            .addComponent(txtSoDu)
                            .addComponent(txtTienDaSuDung, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtTongThoiGian, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnTinNhan)
                            .addComponent(btnDichVu, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTongThoiGian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtThoiGianSuDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtThoiGianConLai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSoDu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addGap(22, 22, 22)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTienDaSuDung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(30, 30, 30)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDoiMatKhau)
                    .addComponent(btnDichVu))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTinNhan)
                    .addComponent(btnDangXuat))
                .addGap(21, 21, 21))
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Cilent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Cilent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Cilent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Cilent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Cilent().setVisible(true);

            }
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
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblAnhBaner;
    private javax.swing.JPanel pnlChinh;
    private javax.swing.JTextField txtSoDu;
    private javax.swing.JTextField txtThoiGianConLai;
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

}
