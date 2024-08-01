/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.client;

import entity.Product;
import io.IOServer;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import main_client.MainClient;
import utils.Xnoti;

/**
 *
 * @author ASUS
 */
public class GioHangJDialog extends javax.swing.JDialog {
    
    public static int soluong = 0;
    public static Integer price = 0;
    private DefaultTableModel tablemodel = new DefaultTableModel();
    public IOServer ioServer;
    private List<Product> list = MainClient.listProducts;
    private static GioHangJDialog instance;

    public static GioHangJDialog getInstance(JFrame parent) {
        if (instance == null) {
            instance = new GioHangJDialog(parent, true);
        }
        return instance;
    }

    /**
     * Creates new form GioHangJDialog
     *
     * @param parent
     * @param modal
     */
    public GioHangJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        tablemodel = (DefaultTableModel) tblGioHang.getModel();

        //  Tính tổng tiền sản phẩm
        tblGioHang.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
            tongTien();
        });
    }

   private void order() {
        BigDecimal totalOrderAmount = calculateTotalOrderAmount();

        if (MainClient.isGuest) {
            updateGuestAmountUsed(totalOrderAmount);
        } else {
            updateMemberBalance(totalOrderAmount);
        }

        // Cập nhật thời gian sử dụng và số dư còn lại trên clientForm
        MainClient.clientForm.setForm();
    }

    private BigDecimal calculateTotalOrderAmount() {
        BigDecimal totalOrderAmount = BigDecimal.ZERO;

        for (int i = 0; i < tablemodel.getRowCount(); i++) {
            String proName = (String) tablemodel.getValueAt(i, 1);
            int quantt = (int) tablemodel.getValueAt(i, 2);
            BigDecimal amount = new BigDecimal(tablemodel.getValueAt(i, 3).toString());

            try {
                MainClient.client.orderProduct(proName, quantt);
                totalOrderAmount = totalOrderAmount.add(amount);
            } catch (IOException e) {
                Xnoti.msg(this, "Order không thành công", "Thông báo");
            }
        }

        return totalOrderAmount;
    }

    private void updateGuestAmountUsed(BigDecimal totalOrderAmount) {
        BigDecimal currentAmountUsed = getCurrentAmountUsed();
        BigDecimal newAmountUsed = currentAmountUsed.add(totalOrderAmount);
        MainClient.clientForm.updateTxtTienSuDung(totalOrderAmount);

        lblTongTien.setText("");
        Xnoti.msg(this, "Tổng tiền đã sử dụng: " + newAmountUsed.toString(), "Thông báo");

        tablemodel.setRowCount(0);
    }

    private void updateMemberBalance(BigDecimal totalOrderAmount) {
        BigDecimal currentBalance = getCurrentBalance();

        if (currentBalance.compareTo(totalOrderAmount) >= 0) {
            BigDecimal balance = currentBalance.subtract(totalOrderAmount);
            MainClient.clientForm.updateTxtSoDu(balance, totalOrderAmount);

            lblTongTien.setText("");

            Xnoti.msg(this, "Số dư mới: " + balance.toString(), "Thông báo");

            tablemodel.setRowCount(0);
        } else {
            Xnoti.msg(this, "Số dư không đủ để thực hiện giao dịch!", "Thông báo");
        }
    }

    private BigDecimal getCurrentAmountUsed() {
        try {
            return new BigDecimal(MainClient.clientForm.getTxtTienSuDung());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getCurrentBalance() {
        try {
            return new BigDecimal(MainClient.clientForm.getTxtSoDu());
        } catch (NumberFormatException e) {
            Xnoti.msg(this, "Số dư không hợp lệ!", "Thông báo");
            return BigDecimal.ZERO;
        }
    }

    public void tongTien() {
        double tongTien = 0;
        for (int row = 0; row < tablemodel.getRowCount(); row++) {
            Object value = tablemodel.getValueAt(row, 3); // Lấy giá trị ở cột 3 (cột giá)
            if (value != null) {
                if (value instanceof Double) {
                    tongTien += (Double) value;
                } else if (value instanceof Integer) {
                    tongTien += (Integer) value;
                } else if (value instanceof String) {
                    tongTien += Double.parseDouble((String) value);
                }
            }
        }
        lblTongTien.setText(String.valueOf(tongTien));
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnXoaLuaChon = new javax.swing.JButton();
        btnXoaTatCa = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblGioHang = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        btnOder = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                formComponentShown(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Source Code Pro", 1, 24)); // NOI18N
        jLabel1.setText("Sản phẩm trong giỏ hàng của bạn");

        btnXoaLuaChon.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        btnXoaLuaChon.setText("Xóa lựa chọn");
        btnXoaLuaChon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaLuaChonActionPerformed(evt);
            }
        });

        btnXoaTatCa.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        btnXoaTatCa.setText("Xóa tất cả");
        btnXoaTatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaTatCaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 422, Short.MAX_VALUE)
        );

        tblGioHang.setFont(new java.awt.Font("Source Code Pro", 0, 16)); // NOI18N
        tblGioHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Thứ tự", "Tên SP", "Số lượng", "Thành tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblGioHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGioHangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblGioHang);

        jLabel2.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel2.setText("Tổng tiền:");

        lblTongTien.setFont(new java.awt.Font("Source Sans Pro", 1, 18)); // NOI18N

        btnOder.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        btnOder.setText("ODER");
        btnOder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOderActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(230, 230, 230)
                        .addComponent(btnOder, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 587, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(21, 21, 21))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(btnXoaLuaChon)
                                .addGap(45, 45, 45)
                                .addComponent(btnXoaTatCa)
                                .addGap(18, 18, 18)))))
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 75, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnXoaLuaChon)
                            .addComponent(btnXoaTatCa))
                        .addGap(26, 26, 26)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(31, 31, 31)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(21, 21, 21)
                .addComponent(btnOder, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 585, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnXoaTatCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaTatCaActionPerformed
        // TODO add your handling code here:
        DefaultTableModel model = (DefaultTableModel) tblGioHang.getModel();
        model.setRowCount(0);
        Xnoti.msg(this, "Đã xóa tất cả các sản phẩm khỏi giỏ hàng.", "Thông báo trống");
    }//GEN-LAST:event_btnXoaTatCaActionPerformed

    private void btnXoaLuaChonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaLuaChonActionPerformed
        // TODO add your handling code here:
        int selectedRow = tblGioHang.getSelectedRow();
        if (selectedRow != -1) {
            // Xóa dòng đã chọn từ model
            DefaultTableModel model = (DefaultTableModel) tblGioHang.getModel();
            model.removeRow(selectedRow);

            Xnoti.msg(this, "Đã xóa sản phẩm!", "Thông báo trống" + selectedRow);
        } else {
            Xnoti.msg(this, "vui lòng chọn sản phẩm bạn muốn xóa!", "Thông báo trống");
        }
    }//GEN-LAST:event_btnXoaLuaChonActionPerformed

    private void btnOderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOderActionPerformed
        // TODO add your handling code here:
        order();
    }//GEN-LAST:event_btnOderActionPerformed

    private void tblGioHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGioHangMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tblGioHangMouseClicked

    private void formComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_formComponentShown
        // TODO add your handling code here:
        tongTien();
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
            java.util.logging.Logger.getLogger(GioHangJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(GioHangJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(GioHangJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GioHangJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                GioHangJDialog dialog = new GioHangJDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnOder;
    private javax.swing.JButton btnXoaLuaChon;
    private javax.swing.JButton btnXoaTatCa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JTable tblGioHang;
    // End of variables declaration//GEN-END:variables

    public DefaultTableModel getTablemodel() {
        return tablemodel;
    }

    public void setTablemodel(DefaultTableModel tablemodel) {
        this.tablemodel = tablemodel;
    }

    public List<Product> getList() {
        return list;
    }

    public void setList(List<Product> list) {
        this.list = list;
    }

    public JTable getTblGioHang() {
        return tblGioHang;
    }

    public void setTblGioHang(JTable tblGioHang) {
        this.tblGioHang = tblGioHang;
    }

    public JLabel getTxtTongTien() {
        return lblTongTien;
    }

    public void setTxtTongTien(JLabel lblTongTien) {
        this.lblTongTien = lblTongTien;
    }

    public JButton getBtnOder() {
        return btnOder;
    }

    public void setBtnOder(JButton btnOder) {
        this.btnOder = btnOder;
    }

}
