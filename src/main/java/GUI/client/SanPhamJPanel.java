/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.client;

import entity.Product;
import java.awt.event.ActionEvent;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import main_client.MainClient;
import utils.Ximage;

/**
 *
 * @author admin
 */
public class SanPhamJPanel extends javax.swing.JPanel {

    private GioHangJDialog gioHangDialogInstance;

    List<Product> list = MainClient.listProducts;
    DefaultTableModel table = new DefaultTableModel();

    public SanPhamJPanel() {
        initComponents();
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        gioHangDialogInstance = GioHangJDialog.getInstance(frame);

        btnThemSp.addActionListener((ActionEvent e) -> {
            // Lấy dữ liệu
            String id = lblId_sp.getText();
            String tensp = lblTen_Sp.getText();
            String giasp = lblGia_Sp.getText();
            table = (DefaultTableModel) gioHangDialogInstance.getTblGioHang().getModel();
        });
    }

    public void setData(int id_sp, String ten_sp, String gia_sp, String hinh_sp) {
        lblId_sp.setText(String.valueOf(id_sp));
        lblTen_Sp.setText(ten_sp);
        lblGia_Sp.setText(gia_sp);
        ImageIcon icon = Ximage.read(hinh_sp.trim());
        lblHinh.setIcon(icon);
        lblHinh.setToolTipText(hinh_sp.trim());
    }
    void loadToCart() {
        Product product = new Product();
        boolean found = false;

        for (Product p : list) {
            if (p.getDescription().equalsIgnoreCase(lblHinh.getToolTipText())) {
                product = p;
                break;
            }
        }

        int soluongsp = (int) snrSP.getValue();

        // Kiểm tra xem sản phẩm đã tồn tại trong giỏ hàng hay chưa
        for (int i = 0; i < table.getRowCount(); i++) {
            String existingProductName = (String) table.getValueAt(i, 1); // tên sản phẩm ở cột thứ 2
            if (existingProductName.equalsIgnoreCase(product.getName())) {
                int currentQuantity = (int) table.getValueAt(i, 2); // rằng số lượng ở cột thứ 3
                int newQuantity = currentQuantity + soluongsp;
                table.setValueAt(newQuantity, i, 2);
                double newTotal = product.getPrice().doubleValue() * newQuantity;
                table.setValueAt(newTotal, i, 3); // tổng tiền ở cột thứ 4
                found = true;
                break;
            }
        }
        
        // Nếu sản phẩm chưa có trong giỏ hàng, thêm sản phẩm mới
        if (!found) {
            double tongtien = product.getPrice().doubleValue() * soluongsp;
            Object[] row = new Object[]{
                table.getRowCount() + 1, // Sử dụng số lượng hàng hiện tại để tính STT
                product.getName(),
                soluongsp,
                tongtien
            };
            table.addRow(row);
        }

        gioHangDialogInstance.setTablemodel(table);
//        gioHangDialogInstance.setVisible(true);
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblHinh = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        snrSP = new javax.swing.JSpinner();
        btnThemSp = new javax.swing.JButton();
        lblTen_Sp = new javax.swing.JLabel();
        lblGia_Sp = new javax.swing.JLabel();
        lblId_sp = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(null, java.awt.Color.gray));
        jPanel1.setForeground(new java.awt.Color(0, 102, 102));
        jPanel1.setPreferredSize(new java.awt.Dimension(400, 600));

        lblHinh.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Source Code Pro", 0, 14)); // NOI18N
        jLabel2.setText("Tên Sản Phẩm:");

        jLabel3.setFont(new java.awt.Font("Source Code Pro", 0, 14)); // NOI18N
        jLabel3.setText("Giá Sản Phẩm:");

        snrSP.setFont(new java.awt.Font("Source Code Pro", 0, 14)); // NOI18N
        snrSP.setModel(new javax.swing.SpinnerNumberModel(1, 1, null, 1));

        btnThemSp.setFont(new java.awt.Font("Source Code Pro", 0, 14)); // NOI18N
        btnThemSp.setText("Thêm Sản Phẩm");
        btnThemSp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSpActionPerformed(evt);
            }
        });

        lblTen_Sp.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTen_Sp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lblGia_Sp.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblGia_Sp.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Source Code Pro", 0, 14)); // NOI18N
        jLabel1.setText("ID Sản Phẩm:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(snrSP, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(btnThemSp))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel2)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblTen_Sp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblGia_Sp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(lblHinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblId_sp)))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblId_sp)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblHinh, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblTen_Sp))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblGia_Sp))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(snrSP, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThemSp))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemSpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSpActionPerformed
        // TODO add your handling code here:
//        String nameSp = lblTen_Sp.getText();
        loadToCart();

    }//GEN-LAST:event_btnThemSpActionPerformed

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
            java.util.logging.Logger.getLogger(dichvu_text.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(dichvu_text.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(dichvu_text.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(dichvu_text.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new dichvu_text().setVisible(true);
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThemSp;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JLabel lblGia_Sp;
    private javax.swing.JLabel lblHinh;
    private javax.swing.JLabel lblId_sp;
    private javax.swing.JLabel lblTen_Sp;
    private javax.swing.JSpinner snrSP;
    // End of variables declaration//GEN-END:variables

}
