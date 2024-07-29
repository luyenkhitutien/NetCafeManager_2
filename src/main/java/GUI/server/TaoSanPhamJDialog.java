/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.server;

import Interface.UpdateListener;
import dao.ProductDAO;
import entity.Product;
import java.awt.Dimension;
import java.io.File;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import utils.Ximage;
import utils.Xnoti;

/**
 *
 * @author ASUS
 */
public class TaoSanPhamJDialog extends javax.swing.JDialog {

    JFileChooser filechooser = new JFileChooser("D:\\");
    DefaultComboBoxModel model = new DefaultComboBoxModel();
    ProductDAO pro = new ProductDAO();
    private UpdateListener listener;

    public void setProductListener(UpdateListener listener) {
        this.listener = listener;
    }

    public void fillOnUpdate() {
        if (listener != null) {
            listener.onUpdate();
        }
    }

    public TaoSanPhamJDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        setLocation(550, 130);
        init();
    }

    public void init() {
        // Đặt kích thước cho JDialog
        setSize(new Dimension(1000, 900));

        // Đặt kích thước ưu tiên cho JPanel pnlChinh
        pnlChinh.setPreferredSize(new Dimension(1000, 900));
        fillToCbo();
    }

    void fillToCbo() {
        try {
            DefaultComboBoxModel cboModel = (DefaultComboBoxModel) cboType.getModel();
            cboModel.removeAllElements();
            Set<String> type = new HashSet<>();
            List<Product> listProduct = pro.selectAll();
            for (Product p : listProduct) {
                type.add(p.getType());
            }
            for (String proType : type) {
                cboModel.addElement(proType);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Product getFormInsert() {
        Product product = new Product();
        product.setName(txtTenSanPham.getText());
        product.setPrice(BigDecimal.valueOf(Double.parseDouble(txtGia.getText())));
        product.setDescription(lblPicture.getToolTipText());
        product.setType(String.valueOf(cboType.getSelectedItem()));
        return product;
    }

    Product getForm() {
        Product product = new Product();
        product.setId(Integer.parseInt(txtIDSanPham.getText()));
        product.setName(txtTenSanPham.getText());
        product.setPrice(BigDecimal.valueOf(Double.parseDouble(txtGia.getText())));
        product.setDescription(lblPicture.getToolTipText());
        product.setType(String.valueOf(cboType.getSelectedItem()));
        return product;
    }

    public void setImage() {
        if (filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = filechooser.getSelectedFile();
            Ximage.save(file);
            ImageIcon icon = Ximage.read(file.getName());
            lblPicture.setIcon(icon);
            lblPicture.setToolTipText(file.getName());
        }
    }

    private void insert() {
        if (valiDateForm_insert()) {
            Product product = getFormInsert();
            ProductDAO proDao = new ProductDAO();
            try {
                proDao.insert(product);
                System.out.println("Them thanh cong");
                fillOnUpdate();
                System.out.println(listener);
                JOptionPane.showMessageDialog(this, "Them Thanh Cong");
            } catch (Exception e) {
                System.out.println("Them that bai");
            }
        }

    }

    private void update() {

        if (valiDateForm_update()) {
            Product product = getForm();
            ProductDAO proDao = new ProductDAO();
            try {
                proDao.update(product);
                fillOnUpdate();
                System.out.println("Cap nhat thanh cong");
            } catch (Exception e) {
                System.out.println("cap nhat that bai");
                e.printStackTrace();
            }
        }
    }

    private void reset() {

        txtIDSanPham.setText(null);
        txtGia.setText(null);
        txtTenSanPham.setText(null);
        lblPicture.setText(null);
        lblPicture.setIcon(null);
    }
    
    private void delete() {
        if (!txtIDSanPham.getText().trim().isEmpty()) {
            
            ProductDAO proDao = new ProductDAO();
            int IDSanPham = Integer.parseInt(txtIDSanPham.getText());
            try {
                proDao.delete(IDSanPham);
                System.out.println("Sản phẩm có ID là: "+IDSanPham);
                fillOnUpdate();
            } catch (Exception ex) {
                Logger.getLogger(TaoSanPhamJDialog.class.getName()).log(Level.SEVERE, null, ex);
            }       
            Xnoti.msg(this, "Xóa thành công sản phẩm có ID: "+IDSanPham, "Thông báo");
            
        } else {
            Xnoti.msg(this, "erro: ID Sản phẩm rỗng!", "Thông báo");
        }
        
    }

    private boolean valiDateForm_update() {

        if (txtIDSanPham.getText().isEmpty()) {
            Xnoti.msg(this, "erro: ID Sản phẩm rỗng!", "Thông báo");
            return false;
        }
        if (txtTenSanPham.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Tên sản phẩm rỗng!", "Thông báo");
            txtTenSanPham.requestFocus();
            return false;
        }
        if (txtGia.getText().trim().isEmpty()) {
            Xnoti.msd(this, "erro: Giá sản phẩm rỗng! ", "Thông báo");
            txtGia.requestFocus();
            return false;
        }
        if (lblPicture.getIcon() == null) {
            Xnoti.msg(this, "Erro: Ảnh sản phẩm rỗng!", "Thông báo");
            lblPicture.requestFocus();
            return false;
        }

        return true;
    }

    private boolean valiDateForm_insert() {

        if (txtTenSanPham.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Tên sản phẩm rỗng!", "Thông báo");
            txtTenSanPham.requestFocus();
            return false;
        }
        if (txtGia.getText().trim().isEmpty()) {
            Xnoti.msd(this, "erro: Giá sản phẩm rỗng! ", "Thông báo");
            txtGia.requestFocus();
            return false;
        }
        if (lblPicture.getIcon() == null) {
            Xnoti.msg(this, "Erro: Ảnh sản phẩm rỗng!", "Thông báo");
            lblPicture.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlChinh = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        txtTenSanPham = new javax.swing.JTextField();
        txtGia = new javax.swing.JTextField();
        btnLuu = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        lblPicture = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtIDSanPham = new javax.swing.JTextField();
        cboType = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlChinh.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel2.setText("SẢN PHẨM");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel3.setText("Tên sản phẩm:");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel4.setText("Giá:");

        jLabel7.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel7.setText("Mô tả:");

        txtTenSanPham.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        txtGia.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        btnLuu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Luu1.png"))); // NOI18N
        btnLuu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLuuActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Xoa1.png"))); // NOI18N
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LamMoi1.png"))); // NOI18N
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/CapNhap1.png"))); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        lblPicture.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lblPicture.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                lblPictureMousePressed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel5.setText("ID sản phẩm:");

        txtIDSanPham.setEditable(false);
        txtIDSanPham.setFont(new java.awt.Font("Dialog", 1, 11)); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(278, 278, 278))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 117, Short.MAX_VALUE)
                .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(cboType, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(63, 63, 63)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtTenSanPham)
                                    .addComponent(txtIDSanPham, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel7))
                                .addGap(152, 152, 152)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtGia, javax.swing.GroupLayout.DEFAULT_SIZE, 352, Short.MAX_VALUE)
                                    .addComponent(lblPicture, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(127, 127, 127)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTenSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addGap(26, 26, 26)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtIDSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, Short.MAX_VALUE)
                        .addComponent(lblPicture, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cboType, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(12, 12, 12))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(121, 121, 121)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnLuu, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(70, 70, 70))
        );

        pnlChinh.add(jPanel2);
        jPanel2.setBounds(140, 130, 750, 610);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/_7a32bf4f-f89f-4178-b0ae-b5b0b9cb57eb.jpg"))); // NOI18N
        pnlChinh.add(jLabel1);
        jLabel1.setBounds(0, 0, 1024, 1024);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChinh, javax.swing.GroupLayout.DEFAULT_SIZE, 1026, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChinh, javax.swing.GroupLayout.DEFAULT_SIZE, 903, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed

        delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void lblPictureMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblPictureMousePressed
        // TODO add your handling code here:    
        setImage();
    }//GEN-LAST:event_lblPictureMousePressed

    private void btnLuuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLuuActionPerformed
        // TODO add your handling code here
        insert();
    }//GEN-LAST:event_btnLuuActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here: 
        update();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        reset();
    }//GEN-LAST:event_btnLamMoiActionPerformed

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
            java.util.logging.Logger.getLogger(TaoSanPhamJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(TaoSanPhamJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(TaoSanPhamJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(TaoSanPhamJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                TaoSanPhamJDialog dialog = new TaoSanPhamJDialog(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnLuu;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboType;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblPicture;
    private javax.swing.JPanel pnlChinh;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtIDSanPham;
    private javax.swing.JTextField txtTenSanPham;
    // End of variables declaration//GEN-END:variables

    public JButton getBtnHuy() {
        return btnXoa;
    }

    public void setBtnHuy(JButton btnHuy) {
        this.btnXoa = btnHuy;
    }

    public JButton getBtnLuu() {
        return btnLuu;
    }

    public void setBtnLuu(JButton btnLuu) {
        this.btnLuu = btnLuu;
    }

    public JPanel getPnlChinh() {
        return pnlChinh;
    }

    public void setPnlChinh(JPanel pnlChinh) {
        this.pnlChinh = pnlChinh;
    }

    public JTextField getTxtGia() {
        return txtGia;
    }

    public void setTxtGia(JTextField txtGia) {
        this.txtGia = txtGia;
    }

    public JTextField getTxtIDSanPham() {
        return txtIDSanPham;
    }

    public void setTxtIDSanPham(JTextField txtIDSanPham) {
        this.txtIDSanPham = txtIDSanPham;
    }

    public JTextField getTxtTenSanPham() {
        return txtTenSanPham;
    }

    public void setTxtTenSanPham(JTextField txtTenSanPham) {
        this.txtTenSanPham = txtTenSanPham;
    }

    public JLabel getLblPicture() {
        return lblPicture;
    }

    public void setLblPicture(JLabel lblPicture) {
        this.lblPicture = lblPicture;
    }

    public JComboBox<String> getCboType() {
        return cboType;
    }

    public void setCboType(JComboBox<String> cboType) {
        this.cboType = cboType;
    }

}
