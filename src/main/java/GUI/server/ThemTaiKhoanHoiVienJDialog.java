/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.server;

import Interface.UpdateListener;
import dao.AccountDAO;
import dao.MemberDAO;
import entity.Account;
import entity.Member;
import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import javax.swing.JTextField;
import utils.Xnoti;

public class ThemTaiKhoanHoiVienJDialog extends javax.swing.JDialog {

    public static Integer accountId;
    List<Member> listMember = new ArrayList<>();
    MemberDAO memberDAO = new MemberDAO();
    List<Account> listAccount = new ArrayList<>();
    Account account = new Account();
    AccountDAO accountDAO =  new AccountDAO();
    
    private UpdateListener listener;

    public void setMemberListener(UpdateListener listener) {
        this.listener = listener;
    }

    public void fillOnUpdate() {
        if (listener != null) {
            listener.onUpdate();
        }
    }
    
    
    public ThemTaiKhoanHoiVienJDialog(java.awt.Frame parent, boolean modal, Integer accountID)throws Exception{
        super(parent, modal);
        initComponents();
        setLocation(420, 250);
        init();
         this.accountId = accountID;
        checkNull(accountID);
    }
    
    public void init(){
        // Đặt kích thước cho JDialog
        setSize(new Dimension(1150, 549));
        
        // Đặt kích thước ưu tiên cho JPanel pnlChinh
        pnlChinh.setPreferredSize(new Dimension(1150, 549));
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
    Member getFormMem_Insert() {
        Member mem = new Member();
        if (!txtIDTaiKhoan.getText().trim().isEmpty()) {
            mem.setId(Integer.parseInt(txtIDTaiKhoan.getText()));
        }
        if (!txtIDHoiVien.getText().trim().isEmpty()) {
            mem.setAccountID(Integer.parseInt(txtIDHoiVien.getText()));
        }
        mem.setName(txtTenHoiVien.getText());
        mem.setBalance(BigDecimal.valueOf(Double.parseDouble(txtSoDu.getText())));
        return mem;
    }

    Member getFormMem_Update() {
        Member mem = new Member();
        if (!txtIDTaiKhoan.getText().trim().isEmpty()) {
            mem.setAccountID(Integer.parseInt(txtIDTaiKhoan.getText()));
        }
        if (!txtIDHoiVien.getText().trim().isEmpty()) {
            mem.setId(Integer.parseInt(txtIDHoiVien.getText()));
        }
        mem.setName(txtTenHoiVien.getText());
        mem.setBalance(BigDecimal.valueOf(Double.parseDouble(txtSoDu.getText())));
        return mem;
    }

    void setForm(Integer id) throws Exception {
        System.out.println("THEMTK:" + id);
        account = accountDAO.selectByID(id);
        System.out.println(account);
        txtIDTaiKhoan.setText(String.valueOf(id));
        txtTenTaiKhoan.setText(account.getUsername());
        txtMatKhau.setText(account.getPassword());
        txtTaoLuc.setText(String.valueOf(account.getCreatedAt()));

        mem = memberDAO.selectByAccountID(account.getId());
        System.out.println(mem);
        txtIDHoiVien.setText(String.valueOf(mem.getId()));
        txtTenHoiVien.setText(mem.getName());
        txtSoDu.setText(String.valueOf(mem.getBalance()));
    }

        void checkNull(Integer accountId) throws Exception {
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
            txtIDHoiVien.setText(String.valueOf(account.getId()));
            System.out.println("inst_acc: " + account);
            System.out.println("QLHV => Them tai khoan thanh cong");
            try {
                Member member = getFormMem_Insert();
                System.out.println("ins_mem: " + member);
                MemberDAO memDao = new MemberDAO();
                memDao.insert(member);
                Xnoti.msg(this, "Thêm Hội Viên Thành Công", "NetCaFe");
                fillOnUpdate();
                System.out.println("QLHV => Them hoi vien thanh cong");
            } catch (Exception e) {
                e.printStackTrace();

                System.out.println("QLHV => Them hoi vien that bai");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("QLHV => Them tai khoan that bai!!");
        }
    }

    void update() {
        Member mem = getFormMem_Update();
        Account acc = getForm();
        System.out.println("=========================");
        System.out.println("memgetform" + mem);
        MemberDAO memDAO = new MemberDAO();
        try {
            memDAO.update(mem);
            System.out.println("mem" + mem);
            accountDAO.update(acc);
            System.out.println("capnhat thanh cong");
            Xnoti.msg(this, "Cập Nhật Thành Công", "NetCaFe");
            fillOnUpdate();
        } catch (Exception e) {
        }
    }

    void delete() {
        try {
//            Member mem = getFormMem();
            int IDhv = Integer.parseInt(txtIDHoiVien.getText());
            int IDtk = Integer.parseInt(txtIDTaiKhoan.getText());
            memberDAO.delete(IDhv);
            System.out.println("QLHV ==> Xoa hoi vien thanh cong: " + IDhv);
            accountDAO.delete(IDtk);
            System.out.println("QLHV ==> Xoa tai khoan co Id" + IDhv + " la hoi vien ");
            Xnoti.msg(this, "Xóa Thành Công", "NetCaFe");
            fillOnUpdate();
        } catch (Exception e) {
            System.out.println("Xoa HoiVien khong thanh cong");
            e.printStackTrace();
        }

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
        pnlTaiKhoan = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtIDTaiKhoan = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTenTaiKhoan = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMatKhau = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cboVaiTro = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtSoDu = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTenHoiVien = new javax.swing.JTextField();
        txtIDHoiVien = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtTaoLuc = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        txtTao = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        lblBackGround = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlChinh.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("TÀI KHOẢN HỘI VIÊN");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel3.setText("ID tài khoản:");

        txtIDTaiKhoan.setEditable(false);
        txtIDTaiKhoan.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel7.setText("Tên tài khoản:");

        txtTenTaiKhoan.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txtTenTaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenTaiKhoanActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel4.setText("Mật khẩu:");

        txtMatKhau.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel6.setText("Vai trò:");

        cboVaiTro.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        cboVaiTro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hội viên" }));
        cboVaiTro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboVaiTroActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(cboVaiTro, 0, 209, Short.MAX_VALUE)
                    .addComponent(txtMatKhau)
                    .addComponent(txtTenTaiKhoan)
                    .addComponent(txtIDTaiKhoan))
                .addGap(12, 12, 12))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIDTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addGap(18, 24, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addContainerGap(42, Short.MAX_VALUE))
        );

        jLabel5.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel5.setText("Số dư:");

        txtSoDu.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel8.setText("Tên hội viên:");

        txtTenHoiVien.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        txtIDHoiVien.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        jLabel9.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel9.setText("Tạo lúc:");

        txtTaoLuc.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel2.setText("ID hội viên:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtIDHoiVien, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
                    .addComponent(txtTenHoiVien)
                    .addComponent(txtSoDu)
                    .addComponent(txtTaoLuc))
                .addGap(51, 51, 51))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(txtIDHoiVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(txtTenHoiVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(16, 16, 16)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSoDu, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTaoLuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        txtTao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Luu1.png"))); // NOI18N
        txtTao.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtTao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTaoActionPerformed(evt);
            }
        });

        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/sua3.png"))); // NOI18N
        btnSua.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/LamMoi1.png"))); // NOI18N
        btnLamMoi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/xoa4.png"))); // NOI18N
        btnXoa.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTao, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(49, 49, 49)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap(20, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtTao, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnXoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnSua, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout pnlTaiKhoanLayout = new javax.swing.GroupLayout(pnlTaiKhoan);
        pnlTaiKhoan.setLayout(pnlTaiKhoanLayout);
        pnlTaiKhoanLayout.setHorizontalGroup(
            pnlTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTaiKhoanLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlTaiKhoanLayout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTaiKhoanLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(275, 275, 275))
        );
        pnlTaiKhoanLayout.setVerticalGroup(
            pnlTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTaiKhoanLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(pnlTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(36, Short.MAX_VALUE))
        );

        pnlChinh.add(pnlTaiKhoan);
        pnlTaiKhoan.setBounds(120, 20, 930, 410);

        lblBackGround.setFont(new java.awt.Font("Palatino Linotype", 1, 18)); // NOI18N
        lblBackGround.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BGThemHoiVien.jpg"))); // NOI18N
        pnlChinh.add(lblBackGround);
        lblBackGround.setBounds(0, -110, 1350, 780);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChinh, javax.swing.GroupLayout.DEFAULT_SIZE, 1134, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChinh, javax.swing.GroupLayout.DEFAULT_SIZE, 527, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtTenTaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenTaiKhoanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenTaiKhoanActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {                                          
        // TODO add your handling code here:
        txtIDHoiVien.setText(null);
        txtIDTaiKhoan.setText(null);
        txtMatKhau.setText(null);
        txtSoDu.setText(null);
        txtTao.setText(null);
        txtTaoLuc.setText(null);
        txtTenHoiVien.setText(null);
        txtTenTaiKhoan.setText(null);
    }  
    private void cboVaiTroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboVaiTroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cboVaiTroActionPerformed

  private void txtTaoActionPerformed(java.awt.event.ActionEvent evt) {                                       
try {
            insert();
        } catch (Exception e) {
            e.printStackTrace();
        }        
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
 try {
            delete();
        } catch (Exception e) {
            e.printStackTrace();
        }        // TODO add your handling code here:
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
            java.util.logging.Logger.getLogger(ThemTaiKhoanHoiVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ThemTaiKhoanHoiVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ThemTaiKhoanHoiVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ThemTaiKhoanHoiVienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ThemTaiKhoanHoiVienJDialog dialog = null;
                try {
                    dialog = new ThemTaiKhoanHoiVienJDialog(new javax.swing.JFrame(), true,accountId);
                } catch (Exception ex) {
                    Logger.getLogger(ThemTaiKhoanHoiVienJDialog.class.getName()).log(Level.SEVERE, null, ex);
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
    private javax.swing.JLabel lblBackGround;
    private javax.swing.JPanel pnlChinh;
    private javax.swing.JPanel pnlTaiKhoan;
    private javax.swing.JTextField txtIDHoiVien;
    private javax.swing.JTextField txtIDTaiKhoan;
    private javax.swing.JTextField txtMatKhau;
    private javax.swing.JTextField txtSoDu;
    private javax.swing.JButton txtTao;
    private javax.swing.JTextField txtTaoLuc;
    private javax.swing.JTextField txtTenHoiVien;
    private javax.swing.JTextField txtTenTaiKhoan;
    // End of variables declaration//GEN-END:variables

//    public JButton getBtnHuy() {
//        return btnHuy;
//    }
//
//    public void setBtnHuy(JButton btnHuy) {
//        this.btnHuy = btnHuy;
//    }
//
//    public JButton getBtnLamMoi() {
//        return btnLamMoi;
//    }
//
//    public void setBtnLamMoi(JButton btnLamMoi) {
//        this.btnLamMoi = btnLamMoi;
//    }
//
//    public JButton getBtnSua() {
//        return btnSua;
//    }
//
//    public void setBtnSua(JButton btnSua) {
//        this.btnSua = btnSua;
//    }
//
//    public JButton getBtnTao() {
//        return btnTao;
//    }
//
//    public void setBtnTao(JButton btnTao) {
//        this.btnTao = btnTao;
//    }
//
//    public JButton getBtnThem() {
//        return btnThem;
//    }
//
//    public void setBtnThem(JButton btnThem) {
//        this.btnThem = btnThem;
//    }

    public JComboBox<String> getCboVaiTro() {
        return cboVaiTro;
    }

    public void setCboVaiTro(JComboBox<String> cboVaiTro) {
        this.cboVaiTro = cboVaiTro;
    }

    public JTextField getTxtIDTaiKhoan() {
        return txtIDTaiKhoan;
    }

    public void setTxtIDTaiKhoan(JTextField txtIDTaiKhoan) {
        this.txtIDTaiKhoan = txtIDTaiKhoan;
    }

    public JTextField getTxtMatKhau() {
        return txtMatKhau;
    }

    public void setTxtMatKhau(JTextField txtMatKhau) {
        this.txtMatKhau = txtMatKhau;
    }

    public JTextField getTxtSoDu() {
        return txtSoDu;
    }

   

    public JTextField getTxtTenTaiKhoan() {
        return txtTenTaiKhoan;
    }

    public void setTxtTenTaiKhoan(JTextField txtTenTaiKhoan) {
        this.txtTenTaiKhoan = txtTenTaiKhoan;
    }
    
    
    
    
    //  tên hội viên
    
    
   public JTextField getTxtTenHoiVien() {
        return txtTenHoiVien;
    }
   public void setgetTxtTenHoiVien(JTextField txtTenHoiVien) {
        this.txtTenHoiVien = txtTenHoiVien;
    }
    
    
    
    //ID hội viên
    public JTextField getTxtIDHoiVien() {
        return txtIDHoiVien;
    }
    
    public void setTxtIDHoiVien(JTextField txtIDHoiVien) {
        this.txtIDHoiVien = txtIDHoiVien;
    }

    private Account getFormAccount() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    

    
}
