/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.server;

import Interface.UpdateListener;
import dao.AccountDAO;
import dao.EmployeeDAO;
import entity.Account;
import entity.Employee;
import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import utils.Xnoti;

/**
 *
 * @author ASUS
 */
public class ThemTaiKhoanNhanVienJDialog extends javax.swing.JDialog {
public static Integer accountId;
    EmployeeDAO emDao = new EmployeeDAO();
    Employee em = new Employee();
    AccountDAO accDao = new AccountDAO();
    Account acc = new Account();
    private UpdateListener listener;
    /**
     * Creates new form ThemTaiKhoanNhanVienJDialog
     */
     void setEmployeeListener(UpdateListener listener) {
        this.listener = listener;
    }

    void fillOnUpdate() {
        if (listener != null) {
            listener.onUpdate();
        }
    }
    public ThemTaiKhoanNhanVienJDialog(java.awt.Frame parent, boolean modal, Integer accountID) throws Exception {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        this.accountId = accountID;
        checkNull(accountID);

    }
     Account getForm() {
        Account account = new Account();
        if (!txtIDTaiKhoan.getText().trim().isEmpty()) {
            account.setId(Integer.parseInt(txtIDTaiKhoan.getText()));
        }
        account.setUsername(txtTenTaiKhoan.getText());
        account.setPassword(txtMatKhau.getText());
        account.setRole(String.valueOf(cboVaiTro.getSelectedItem()));
        account.setCreatedAt(new Date());
        return account;
    }

    Employee getFormEm() {
        Employee em = new Employee();
        if (!txtIDTaiKhoan.getText().trim().isEmpty()) {
            em.setId(Integer.parseInt(txtIDTaiKhoan.getText()));
        }
        if (!txtIDNhanVIen.getText().trim().isEmpty()) {
            em.setAccountID(Integer.parseInt(txtIDNhanVIen.getText()));
        }
        em.setName(txtTenNhanVien.getText());
        em.setSalaryPerHour(BigDecimal.valueOf(Double.parseDouble(txtLuong.getText())));
        em.setAddress(txtDiaChi.getText());
        em.setPhoneNumber(txtSoDienThoai.getText());
        em.setOtherInformation(txtThongTinKhac.getText());
        return em;
    }

    Employee getFormEm_Update() {
        Employee em = new Employee();
        if (!txtIDTaiKhoan.getText().trim().isEmpty()) {
            em.setAccountID(Integer.parseInt(txtIDTaiKhoan.getText()));
        }
        if (!txtIDNhanVIen.getText().trim().isEmpty()) {
            em.setId(Integer.parseInt(txtIDNhanVIen.getText()));
        }
        em.setName(txtTenNhanVien.getText());
        em.setSalaryPerHour(BigDecimal.valueOf(Double.parseDouble(txtLuong.getText())));
        em.setAddress(txtDiaChi.getText());
        em.setPhoneNumber(txtSoDienThoai.getText());
        em.setOtherInformation(txtThongTinKhac.getText());
        em.setBalance(BigDecimal.valueOf(Double.parseDouble(txtTongLuong.getText())));
        return em;
    }

    void setForm(Integer id) throws Exception {
        acc = accDao.selectByID(id);
        txtIDTaiKhoan.setText(String.valueOf(id));
        txtTenTaiKhoan.setText(acc.getUsername());
        txtMatKhau.setText(acc.getPassword());
        txtTaoLuc.setText(String.valueOf(acc.getCreatedAt()));
        em = emDao.selectByAccountID(acc.getId());
        txtIDNhanVIen.setText(String.valueOf(em.getId()));
        txtTenNhanVien.setText(em.getName());
        txtLuong.setText(String.valueOf(em.getSalaryPerHour()));
        txtSoDienThoai.setText(em.getPhoneNumber());
        txtThongTinKhac.setText(em.getOtherInformation());
        txtDiaChi.setText(em.getAddress());
        txtTongLuong.setText(em.getBalance().toString());
    }
    void checkNull(Integer accountId) throws Exception {
        if (accountId == null) {

        } else {
            setForm(accountId);
        }
    }

    public void checkisNullAcc(Integer accountId) throws Exception {
        if (accountId == null) {

        } else {
            setForm(accountId);
        }
    }

    void insert() {
        try {
            Account account = getForm();
            AccountDAO accDao = new AccountDAO();
            accDao.insert(account);
            System.out.println("inst_acc: " + account);
            txtIDNhanVIen.setText(String.valueOf(account.getId()));
            System.out.println("QLNV ==> Them tai khoan thanh cong");
            try {
                Employee em = getFormEm();
                em.setBalance(BigDecimal.ZERO);
                System.out.println("inst_em: " + em);
                EmployeeDAO emDao = new EmployeeDAO();
                emDao.insert(em);
                Xnoti.msg(this, "Thêm Thành Công", "NetCaFe");
                fillOnUpdate();
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("QLNV ==> Them Tai Khoan Nhan Vien That Bai");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("QLNV ==> Them Tai Khoan That Bai");
        }
    }

    void update() {
        try {
            Employee em = getFormEm_Update();
            em.setBalance(BigDecimal.ONE);
            Account acc = getForm();
            accDao.update(acc);
            emDao.update(em);
            System.out.println("QLNV => " + acc + "\n" + em);
            Xnoti.msg(this, "Cập Nhật Thành Công", "NetCaFe");
            fillOnUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("QLNV => update nhan vien khong thanh cong");
        }
    }

    void delete() {
        try {
            Employee em = getFormEm();
            Account acc = getForm();
            int IDnv = Integer.parseInt(txtIDNhanVIen.getText());
            int IDtk = Integer.parseInt(txtIDTaiKhoan.getText());
            emDao.delete(IDnv);
            System.out.println("QLNV ==> delete thanh cong nhanvien :" + em.getId());
            accDao.delete(IDtk);
            Xnoti.msg(this, "Xóa Thành Công", "NetCaFe");
            fillOnUpdate();
            System.out.println("QLNV ==> delete thanh cong tai khoan :" + acc.getId());
        } catch (Exception e) {
        }
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtIDTaiKhoan = new javax.swing.JTextField();
        txtTenTaiKhoan = new javax.swing.JTextField();
        txtMatKhau = new javax.swing.JTextField();
        cboVaiTro = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtDiaChi = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtThongTinKhac = new javax.swing.JTextArea();
        txtIDNhanVIen = new javax.swing.JTextField();
        txtTenNhanVien = new javax.swing.JTextField();
        txtLuong = new javax.swing.JTextField();
        txtSoDienThoai = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txtTao = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtTaoLuc = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(null);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel2.setText("ID tài khoản:");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(30, 74, 101, 21);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel3.setText("Tên tài khoản:");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(30, 138, 109, 21);

        jLabel4.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel4.setText("Mật khẩu:");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(30, 211, 76, 21);

        jLabel5.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel5.setText("Vai trò:");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(30, 294, 59, 21);

        txtIDTaiKhoan.setFont(new java.awt.Font("Dialog", 0, 17)); // NOI18N
        jPanel2.add(txtIDTaiKhoan);
        txtIDTaiKhoan.setBounds(189, 70, 260, 33);

        txtTenTaiKhoan.setFont(new java.awt.Font("Dialog", 0, 17)); // NOI18N
        jPanel2.add(txtTenTaiKhoan);
        txtTenTaiKhoan.setBounds(189, 134, 260, 33);

        txtMatKhau.setFont(new java.awt.Font("Dialog", 0, 17)); // NOI18N
        jPanel2.add(txtMatKhau);
        txtMatKhau.setBounds(189, 207, 260, 33);

        cboVaiTro.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cboVaiTro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhân Viên", "Admin" }));
        cboVaiTro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboVaiTroActionPerformed(evt);
            }
        });
        jPanel2.add(cboVaiTro);
        cboVaiTro.setBounds(189, 287, 260, 34);

        jLabel6.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel6.setText("ID nhân viên:");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(527, 74, 103, 21);

        jLabel7.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel7.setText("Tên nhân viên:");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(527, 138, 111, 21);

        jLabel8.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel8.setText("Lương  (vnd/giờ)");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(527, 211, 126, 21);

        jLabel9.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel9.setText("Số điện thoại:");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(527, 294, 106, 21);

        jLabel10.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel10.setText("Địa chỉ:");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(527, 354, 62, 21);

        txtDiaChi.setColumns(20);
        txtDiaChi.setRows(5);
        jScrollPane1.setViewportView(txtDiaChi);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(667, 346, 307, 90);

        jLabel11.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel11.setText("Thông tin khác:");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(30, 354, 118, 21);

        txtThongTinKhac.setColumns(20);
        txtThongTinKhac.setRows(5);
        jScrollPane2.setViewportView(txtThongTinKhac);

        jPanel2.add(jScrollPane2);
        jScrollPane2.setBounds(189, 346, 260, 90);

        txtIDNhanVIen.setFont(new java.awt.Font("Dialog", 0, 17)); // NOI18N
        txtIDNhanVIen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIDNhanVIenActionPerformed(evt);
            }
        });
        jPanel2.add(txtIDNhanVIen);
        txtIDNhanVIen.setBounds(667, 70, 307, 33);

        txtTenNhanVien.setFont(new java.awt.Font("Dialog", 0, 17)); // NOI18N
        jPanel2.add(txtTenNhanVien);
        txtTenNhanVien.setBounds(667, 134, 307, 33);

        txtLuong.setFont(new java.awt.Font("Dialog", 0, 17)); // NOI18N
        jPanel2.add(txtLuong);
        txtLuong.setBounds(671, 207, 303, 33);

        txtSoDienThoai.setFont(new java.awt.Font("Dialog", 0, 17)); // NOI18N
        jPanel2.add(txtSoDienThoai);
        txtSoDienThoai.setBounds(667, 290, 307, 33);

        txtTao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Luu1.png"))); // NOI18N

        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sua3.png"))); // NOI18N

        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LamMoi1.png"))); // NOI18N
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/xoa4.png"))); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(126, 126, 126)
                .addComponent(txtTao, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(65, 65, 65)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(68, 68, 68)
                .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 67, Short.MAX_VALUE)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(txtTao, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(1, 1, 1)
                        .addComponent(btnXoa)))
                .addContainerGap())
        );

        jPanel2.add(jPanel3);
        jPanel3.setBounds(1, 493, 985, 64);

        jLabel13.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel13.setText("Tạo lúc:");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(30, 452, 62, 21);

        txtTaoLuc.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        jPanel2.add(txtTaoLuc);
        txtTaoLuc.setBounds(189, 447, 785, 34);

        jLabel12.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        jLabel12.setText("TÀI KHOẢN NHÂN VIÊN");
        jPanel2.add(jLabel12);
        jLabel12.setBounds(400, 10, 211, 21);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(70, 17, 1000, 600);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/mattrang.jpg"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 1140, 640);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1141, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 645, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtIDNhanVIenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIDNhanVIenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIDNhanVIenActionPerformed

    private void cboVaiTroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboVaiTroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboVaiTroActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
        txtDiaChi.setText(null);
        txtIDNhanVIen.setText(null);
        txtIDTaiKhoan.setText(null);
        txtLuong.setText(null);
        txtMatKhau.setText(null);
        txtSoDienThoai.setText(null);
        txtTao.setText(null);
        txtTaoLuc.setText(null);
        txtTenNhanVien.setText(null);
        txtTenTaiKhoan.setText(null);
        txtThongTinKhac.setText(null);
        txtTongLuong.setText(null);
    }     
     private void txtTaoActionPerformed(java.awt.event.ActionEvent evt) {                                       
        // TODO add your handling code here:
        insert();
    }                                      

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {                                       
        // TODO add your handling code here:
         try {
            update();
        } catch (Exception e) {
            e.printStackTrace();
        }
    } 
     private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {                                       
        // TODO add your handling code here:
        try {
            delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
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
            java.util.logging.Logger.getLogger(ThemTaiKhoanNhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThemTaiKhoanNhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThemTaiKhoanNhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThemTaiKhoanNhanVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
         java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ThemTaiKhoanNhanVienJDialog dialog=null;
                try {
                    dialog = new ThemTaiKhoanNhanVienJDialog(new javax.swing.JFrame(), true,accountId);
                } catch (Exception ex) {
                    Logger.getLogger(ThemTaiKhoanNhanVienJDialog.class.getName()).log(Level.SEVERE, null, ex);
                }
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
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboVaiTro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea txtDiaChi;
    private javax.swing.JTextField txtIDNhanVIen;
    private javax.swing.JTextField txtIDTaiKhoan;
    private javax.swing.JTextField txtLuong;
    private javax.swing.JTextField txtMatKhau;
    private javax.swing.JTextField txtSoDienThoai;
    private javax.swing.JButton txtTao;
    private javax.swing.JTextField txtTaoLuc;
    private javax.swing.JTextField txtTenNhanVien;
    private javax.swing.JTextField txtTenTaiKhoan;
    private javax.swing.JTextArea txtThongTinKhac;
    // End of variables declaration//GEN-END:variables
}
