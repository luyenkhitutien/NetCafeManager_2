package GUI.server;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import main_server.MainTest;
import static main_server.MainTest.chatGUI;
import utils.Auth;
import utils.Xnoti;

/**
 *
 * @author ASUS
 */
public class ServerMain extends javax.swing.JFrame {

    public TrangChuJPanel home = new TrangChuJPanel();
    public QuanLyHoaDonJPanel managerInvoice = new QuanLyHoaDonJPanel();
    private CardLayout cardLayout;
    public JPanel mainPanel;

    public ServerMain() {
        setUndecorated(true);
        initComponents();
        initPanel();
        ChageLabel();
    }

    public void initPanel() {
        this.setExtendedState(MAXIMIZED_BOTH);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.setPreferredSize(new Dimension(800, 600));

        // Initialize your panels here
        home = new TrangChuJPanel();
        QuanLyTaiKhoanJPanel managerAcount = new QuanLyTaiKhoanJPanel();
        QuanLySanPhamJPanel managerProduct = new QuanLySanPhamJPanel();
        managerInvoice = new QuanLyHoaDonJPanel();
        QuanLyMayJPanel managerComputer = new QuanLyMayJPanel();
        ThongKeDoanhThuJPanel thongKeDoanhThu = new ThongKeDoanhThuJPanel();
        ThongKeLuongNhanVienJPanel thongKeLuongNhanVien = new ThongKeLuongNhanVienJPanel();

        mainPanel.add(home, "Home");
        mainPanel.add(managerAcount, "ManagerAcount");
        mainPanel.add(managerProduct, "ManagerProduct");
        mainPanel.add(managerInvoice, "ManagerInvoice");
        mainPanel.add(managerComputer, "ManagerComputer");
        mainPanel.add(thongKeDoanhThu, "ThongKeDoanhThu");
        mainPanel.add(thongKeLuongNhanVien, "ThongKeLuongNhanVien");

        pnlNoiDungChinh.setLayout(new BorderLayout());
        pnlNoiDungChinh.add(mainPanel, BorderLayout.CENTER);

        pnlNoiDungChinh.revalidate();
        pnlNoiDungChinh.repaint();
    }

    public void ChageLabel() {
        addHoverEffect(lblTrangChu, pnlControler);
        addHoverEffect(lblThongKeLuongNhanVien, pnlControler);
        addHoverEffect(lblQuanLyMay, pnlControler);
        addHoverEffect(lblQuanLyHoaDon, pnlControler);
        addHoverEffect(lblQuanLySanPham, pnlControler);
        addHoverEffect(lblQuanLyTaiKhoan, pnlControler);
        addHoverEffect(lblThongKeDoanhThu, pnlControler);
        addHoverEffect(lblTinNhan, pnlControler);
    }

    private void addHoverEffect(JLabel label, JPanel panel) {
        label.setOpaque(true); // Ensure label is opaque
        label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                label.setBackground(new Color(64, 223, 175, 255)); // Change background color on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                label.setBackground(panel.getBackground()); // Reset to panel background color on exit
            }
        });
    }
    
    

    public JLabel getLblUserName() {
        return lblUserName;
    }

    public void setLblUserName(JLabel lblUserName) {
        this.lblUserName = lblUserName;
    }
    
    

    private void shutdown() {
        MainTest.server.calculateBalanceForEmloyee();
        MainTest.server.shutdownServer();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlBaner = new javax.swing.JPanel();
        lblBaner = new javax.swing.JLabel();
        pnlControler = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        lblUserName = new javax.swing.JLabel();
        btnDangXuat = new javax.swing.JButton();
        pnlTitle = new javax.swing.JPanel();
        lblQuanLy = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblQuanLyTaiKhoan = new javax.swing.JLabel();
        lblQuanLySanPham = new javax.swing.JLabel();
        lblThongKeLuongNhanVien = new javax.swing.JLabel();
        lblQuanLyMay = new javax.swing.JLabel();
        lblThongKe = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        lblQuanLyHoaDon = new javax.swing.JLabel();
        lblThongKeDoanhThu = new javax.swing.JLabel();
        lblAnhGr = new javax.swing.JLabel();
        lblTrangChu = new javax.swing.JLabel();
        lblTinNhan = new javax.swing.JLabel();
        pnlNoiDungChinh = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lblBaner.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/NetBaner3.png"))); // NOI18N

        pnlControler.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Monospaced", 2, 14)); // NOI18N
        jLabel1.setText("Xin chào:");

        lblUserName.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        lblUserName.setText("Admin");

        btnDangXuat.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btnDangXuat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/DangXuat.png"))); // NOI18N
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTitleLayout = new javax.swing.GroupLayout(pnlTitle);
        pnlTitle.setLayout(pnlTitleLayout);
        pnlTitleLayout.setHorizontalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 144, Short.MAX_VALUE)
        );
        pnlTitleLayout.setVerticalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 56, Short.MAX_VALUE)
        );

        lblQuanLy.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        lblQuanLy.setText("       QUẢN LÝ");
        lblQuanLy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblQuanLyMouseClicked(evt);
            }
        });

        lblQuanLyTaiKhoan.setFont(new java.awt.Font("Source Code Pro", 3, 18)); // NOI18N
        lblQuanLyTaiKhoan.setText("-- Quản lý tài khoản");
        lblQuanLyTaiKhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblQuanLyTaiKhoanMouseClicked(evt);
            }
        });

        lblQuanLySanPham.setFont(new java.awt.Font("Source Code Pro", 3, 18)); // NOI18N
        lblQuanLySanPham.setText("-- Quản lý sản phẩm");
        lblQuanLySanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblQuanLySanPhamMouseClicked(evt);
            }
        });

        lblThongKeLuongNhanVien.setFont(new java.awt.Font("Source Code Pro", 3, 18)); // NOI18N
        lblThongKeLuongNhanVien.setText("-- Thống kê lương nhân viên");
        lblThongKeLuongNhanVien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThongKeLuongNhanVienMouseClicked(evt);
            }
        });

        lblQuanLyMay.setFont(new java.awt.Font("Source Code Pro", 3, 18)); // NOI18N
        lblQuanLyMay.setText("-- Quản lý máy");
        lblQuanLyMay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblQuanLyMayMouseClicked(evt);
            }
        });

        lblThongKe.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        lblThongKe.setText("      THỐNG KÊ");
        lblThongKe.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThongKeMouseClicked(evt);
            }
        });

        lblQuanLyHoaDon.setFont(new java.awt.Font("Source Code Pro", 3, 18)); // NOI18N
        lblQuanLyHoaDon.setText("-- Quản lý hóa đơn");
        lblQuanLyHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblQuanLyHoaDonMouseClicked(evt);
            }
        });

        lblThongKeDoanhThu.setFont(new java.awt.Font("Source Code Pro", 3, 18)); // NOI18N
        lblThongKeDoanhThu.setText("-- Thống kê doanh thu");
        lblThongKeDoanhThu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblThongKeDoanhThuMouseClicked(evt);
            }
        });

        lblAnhGr.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/GR5.png"))); // NOI18N

        lblTrangChu.setFont(new java.awt.Font("Monospaced", 1, 24)); // NOI18N
        lblTrangChu.setText("      TRANG CHỦ");
        lblTrangChu.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        lblTrangChu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTrangChuMouseClicked(evt);
            }
        });

        lblTinNhan.setFont(new java.awt.Font("Source Code Pro", 3, 18)); // NOI18N
        lblTinNhan.setText("-- Tin Nhắn");
        lblTinNhan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblTinNhanMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlControlerLayout = new javax.swing.GroupLayout(pnlControler);
        pnlControler.setLayout(pnlControlerLayout);
        pnlControlerLayout.setHorizontalGroup(
            pnlControlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblQuanLy, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addComponent(lblTrangChu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlControlerLayout.createSequentialGroup()
                .addGroup(pnlControlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblQuanLySanPham, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblQuanLyTaiKhoan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblQuanLyMay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addComponent(lblThongKe, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblThongKeDoanhThu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblThongKeLuongNhanVien, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlControlerLayout.createSequentialGroup()
                        .addComponent(lblQuanLyHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlControlerLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(pnlControlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlControlerLayout.createSequentialGroup()
                                .addComponent(lblAnhGr, javax.swing.GroupLayout.PREFERRED_SIZE, 195, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlControlerLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(pnlControlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(pnlTitle, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(btnDangXuat, javax.swing.GroupLayout.Alignment.TRAILING)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlControlerLayout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblTinNhan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlControlerLayout.setVerticalGroup(
            pnlControlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlControlerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlControlerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(lblUserName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(pnlTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTrangChu, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblQuanLy, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(lblQuanLyTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblQuanLySanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblQuanLyHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblQuanLyMay, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblTinNhan, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblThongKeDoanhThu, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblThongKeLuongNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblAnhGr)
                .addGap(18, 18, 18)
                .addComponent(btnDangXuat)
                .addContainerGap())
        );

        pnlNoiDungChinh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        javax.swing.GroupLayout pnlNoiDungChinhLayout = new javax.swing.GroupLayout(pnlNoiDungChinh);
        pnlNoiDungChinh.setLayout(pnlNoiDungChinhLayout);
        pnlNoiDungChinhLayout.setHorizontalGroup(
            pnlNoiDungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlNoiDungChinhLayout.setVerticalGroup(
            pnlNoiDungChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlBanerLayout = new javax.swing.GroupLayout(pnlBaner);
        pnlBaner.setLayout(pnlBanerLayout);
        pnlBanerLayout.setHorizontalGroup(
            pnlBanerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblBaner, javax.swing.GroupLayout.PREFERRED_SIZE, 1315, Short.MAX_VALUE)
            .addGroup(pnlBanerLayout.createSequentialGroup()
                .addComponent(pnlControler, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlNoiDungChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlBanerLayout.setVerticalGroup(
            pnlBanerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBanerLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(lblBaner)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlBanerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlNoiDungChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlControler, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBaner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlBaner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void lblQuanLyMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblQuanLyMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_lblQuanLyMouseClicked

    private void lblQuanLyTaiKhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblQuanLyTaiKhoanMouseClicked
        // TODO add your handling code here:
        cardLayout.show(mainPanel, "ManagerAcount");

    }//GEN-LAST:event_lblQuanLyTaiKhoanMouseClicked

    private void lblQuanLySanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblQuanLySanPhamMouseClicked
        // TODO add your handling code here:
        cardLayout.show(mainPanel, "ManagerProduct");

    }//GEN-LAST:event_lblQuanLySanPhamMouseClicked

    private void lblThongKeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongKeMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblThongKeMouseClicked

    private void lblTrangChuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTrangChuMouseClicked
        // TODO add your handling code here:
        cardLayout.show(mainPanel, "Home");

    }//GEN-LAST:event_lblTrangChuMouseClicked

    private void btnDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
        // TODO add your handling code here:
        shutdown();
    }//GEN-LAST:event_btnDangXuatActionPerformed

    private void lblQuanLyHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblQuanLyHoaDonMouseClicked
        // TODO add your handling code here:
        cardLayout.show(mainPanel, "ManagerInvoice");
    }//GEN-LAST:event_lblQuanLyHoaDonMouseClicked

    private void lblQuanLyMayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblQuanLyMayMouseClicked
        // TODO add your handling code here:
        cardLayout.show(mainPanel, "ManagerComputer");
    }//GEN-LAST:event_lblQuanLyMayMouseClicked

    private void lblThongKeDoanhThuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongKeDoanhThuMouseClicked
        // TODO add your handling code here:
        if(Auth.isAdminLogin){
             cardLayout.show(mainPanel, "ThongKeDoanhThu");
        }else{
            Xnoti.msg(this, "Bạn không có quyền để truy cập!", "Thông báo");
        }
       
    }//GEN-LAST:event_lblThongKeDoanhThuMouseClicked

    private void lblThongKeLuongNhanVienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblThongKeLuongNhanVienMouseClicked
        // TODO add your handling code here:
        if(Auth.isAdminLogin){
            cardLayout.show(mainPanel, "ThongKeLuongNhanVien");
        }else{
            Xnoti.msg(this, "Bạn không có quyền để truy cập!", "Thông báo");
        }
    }//GEN-LAST:event_lblThongKeLuongNhanVienMouseClicked

    private void lblTinNhanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblTinNhanMouseClicked
        // TODO add your handling code here:
                MainTest.chatGUI.setVisible(true);
    }//GEN-LAST:event_lblTinNhanMouseClicked

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) throws Exception {
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
            java.util.logging.Logger.getLogger(ServerMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ServerMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ServerMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ServerMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
        }
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                try {
                    new ServerMain().setVisible(true);
                } catch (Exception ex) {
                    Logger.getLogger(ServerMain.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblAnhGr;
    private javax.swing.JLabel lblBaner;
    private javax.swing.JLabel lblQuanLy;
    private javax.swing.JLabel lblQuanLyHoaDon;
    private javax.swing.JLabel lblQuanLyMay;
    private javax.swing.JLabel lblQuanLySanPham;
    private javax.swing.JLabel lblQuanLyTaiKhoan;
    private javax.swing.JLabel lblThongKe;
    private javax.swing.JLabel lblThongKeDoanhThu;
    private javax.swing.JLabel lblThongKeLuongNhanVien;
    private javax.swing.JLabel lblTinNhan;
    private javax.swing.JLabel lblTrangChu;
    private javax.swing.JLabel lblUserName;
    private javax.swing.JPanel pnlBaner;
    private javax.swing.JPanel pnlControler;
    private javax.swing.JPanel pnlNoiDungChinh;
    private javax.swing.JPanel pnlTitle;
    // End of variables declaration//GEN-END:variables
}
