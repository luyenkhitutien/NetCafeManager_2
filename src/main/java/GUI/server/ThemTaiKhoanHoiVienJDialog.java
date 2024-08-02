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
import utils.CustomPanel;
import utils.Xnoti;

public class ThemTaiKhoanHoiVienJDialog extends javax.swing.JDialog {

    public static Integer accountId;
    List<Member> listMember = new ArrayList<>();
    MemberDAO memberDAO = new MemberDAO();
    Member mem = new Member();
    List<Account> listAccount = new ArrayList<>();
    Account account = new Account();
    AccountDAO accountDAO = new AccountDAO();

    private UpdateListener listener;

    public void setMemberListener(UpdateListener listener) {
        this.listener = listener;
    }

    public void fillOnUpdate() {
        if (listener != null) {
            listener.onUpdate();
        }
    }

    public ThemTaiKhoanHoiVienJDialog(java.awt.Frame parent, boolean modal, Integer accountID) throws Exception {
        super(parent, modal);
        initComponents();
        this.setLocationRelativeTo(null);
        init();
        this.accountId = accountID;
        checkNull(accountID);
    }

    public void init() {
        // Đặt kích thước cho JDialog
        setSize(new Dimension(1280, 720));

        // Đặt kích thước ưu tiên cho JPanel pnlChinh
        pnlChinh.setPreferredSize(new Dimension(1280, 720));
    }

    private boolean valiDateGetForm() {

        if (txtTenTaiKhoan.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Tên tài khoản rỗng!", "Thông báo");
            txtTenTaiKhoan.requestFocus();
            return false;
        }
        if (txtMatKhau.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Mật khẩu rỗng!", "Thông báo");
            txtMatKhau.requestFocus();
            return false;
        }

        return true;
    }

    private Account getForm() {
        Account account = new Account();
        account.setUsername(txtTenTaiKhoan.getText());
        account.setPassword(txtMatKhau.getText());
        account.setRole(String.valueOf(cboVaiTro.getSelectedItem()));
        account.setCreatedAt(new Date());

        return account;
    }

    private boolean valiDateGetFormMem_Insert() {

        if (txtTenHoiVien.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Tên hội viên rỗng!", "Thông báo");
            txtTenHoiVien.requestFocus();
            return false;
        }
        if (txtSoDu.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Số dư rỗng!", "Thông báo");
            txtSoDu.requestFocus();
            return false;
        }
        try {
            double soDu = Double.parseDouble(txtSoDu.getText().trim());
            if (soDu <= 0) {
                Xnoti.msg(this, "erro: Số dư phải là số dương!", "Thông báo");
                txtSoDu.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            Xnoti.msg(this, "erro: Số dư phải là số!", "Thông báo");
            txtSoDu.requestFocus();
            return false;
        }

        return true;
    }

    private Member getFormMem_Insert() {

        Member mem = new Member();
        mem.setAccountID(Integer.parseInt(txtIDHoiVien.getText()));
        mem.setName(txtTenHoiVien.getText());
        mem.setBalance(BigDecimal.valueOf(Double.parseDouble(txtSoDu.getText())));

        return mem;
    }

    private boolean valiDateGetFormMem_Update() {

        if (txtIDTaiKhoan.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: ID tài khoản rỗng!", "Thông báo");
            txtIDTaiKhoan.requestFocus();
            return false;
        }
        if (txtIDHoiVien.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: ID hội viên rỗng!", "Thông báo");
            txtIDHoiVien.requestFocus();
            return false;
        }
        if (txtTenHoiVien.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Tên hội viên rỗng!", "Thông báo");
            txtTenHoiVien.requestFocus();
            return false;
        }
        if (txtSoDu.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Số dư rỗng!", "Thông báo");
            txtSoDu.requestFocus();
            return false;
        }
        try {
            double soDu = Double.parseDouble(txtSoDu.getText().trim());
            if (soDu <= 0) {
                Xnoti.msg(this, "erro: Số dư phải là số dương!", "Thông báo");
                txtSoDu.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            Xnoti.msg(this, "erro: Số dư phải là số!", "Thông báo");
            txtSoDu.requestFocus();
            return false;
        }

        return true;
    }

    private Member getFormMem_Update() {

        Member mem = new Member();

        mem.setAccountID(Integer.parseInt(txtIDTaiKhoan.getText()));
        mem.setId(Integer.parseInt(txtIDHoiVien.getText()));
        mem.setName(txtTenHoiVien.getText());
        mem.setBalance(BigDecimal.valueOf(Double.parseDouble(txtSoDu.getText())));

        return mem;
    }

    private void setForm(Integer id) throws Exception {
        System.out.println("THEMTK:" + id);
        account = accountDAO.selectByID(id);
        System.out.println(account);
        txtIDTaiKhoan.setText(String.valueOf(id));
        txtTenTaiKhoan.setText(account.getUsername());
        txtMatKhau.setText(account.getPassword());
        txtTaoLuc.setText(String.valueOf(account.getCreatedAt()));

        Member mem = new Member();
        mem = memberDAO.selectByAccountID(account.getId());
        System.out.println(mem);
        txtIDHoiVien.setText(String.valueOf(mem.getId()));
        txtTenHoiVien.setText(mem.getName());
        txtSoDu.setText(String.valueOf(mem.getBalance()));
    }

    private void checkNull(Integer accountId) throws Exception {
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
            Xnoti.msg(this, "Thêm hội viên thất bại. ID hoặc tên tài khoản đã tồn tại! ", "Thông báo");
        }
    }

    private void update() {
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
            Xnoti.msg(this, "Cập nhập không thành công!. Không tồn tại ID tài khoản hoặc ID hội viên", "Thông báo");
        }
    }

    private boolean validate_delete() {
        if (txtIDTaiKhoan.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: ID tài khoản rỗng!", "Thông báo");
            txtIDTaiKhoan.requestFocus();
            return false;
        }
        if (txtIDHoiVien.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: ID hội viên rỗng!", "Thông báo");
            txtIDHoiVien.requestFocus();
            return false;
        }

        return true;
    }

    private void delete() {
        try {

            int IDhv = Integer.parseInt(txtIDHoiVien.getText());
            int IDtk = Integer.parseInt(txtIDTaiKhoan.getText());
            memberDAO.delete(IDhv);
            accountDAO.delete(IDtk);
            Xnoti.msg(this, "Xóa Thành Công", "NetCaFe");
            fillOnUpdate();
        } catch (Exception e) {
            Xnoti.msg(this, "Xóa không thành công, bạn cần phải xóa Id hội viên có trong hóa đơn trước.", "NetCaFe");
            e.printStackTrace();
        }

    }

    private void reset() {
        txtIDHoiVien.setText(null);
        txtIDTaiKhoan.setText(null);
        txtMatKhau.setText(null);
        txtSoDu.setText(null);
        txtTaoLuc.setText(null);
        txtTenHoiVien.setText(null);
        txtTenTaiKhoan.setText(null);
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
        pnlTaiKhoan = new CustomPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new CustomPanel();
        jLabel3 = new javax.swing.JLabel();
        txtIDTaiKhoan = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtTenTaiKhoan = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMatKhau = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cboVaiTro = new javax.swing.JComboBox<>();
        jPanel1 = new CustomPanel();
        jLabel5 = new javax.swing.JLabel();
        txtSoDu = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        txtTenHoiVien = new javax.swing.JTextField();
        txtIDHoiVien = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtTaoLuc = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jPanel3 = new CustomPanel();
        txtTao = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        lblBackGround = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        pnlChinh.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Monospaced", 1, 28)); // NOI18N
        jLabel1.setText("TÀI KHOẢN HỘI VIÊN");

        jPanel2.setPreferredSize(new java.awt.Dimension(465, 211));

        jLabel3.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        jLabel3.setText("ID tài khoản:");

        txtIDTaiKhoan.setEditable(false);
        txtIDTaiKhoan.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtIDTaiKhoan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel7.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        jLabel7.setText("Tên tài khoản:");

        txtTenTaiKhoan.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtTenTaiKhoan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTenTaiKhoanActionPerformed(evt);
            }
        });

        jLabel4.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        jLabel4.setText("Mật khẩu:");

        txtMatKhau.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N

        jLabel6.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        jLabel6.setText("Vai trò:");

        cboVaiTro.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        cboVaiTro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Hội viên" }));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(txtIDTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addGap(0, 21, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(txtTenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(cboVaiTro, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIDTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 42, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(txtTenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 31, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtMatKhau, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboVaiTro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6))
                .addGap(26, 26, 26))
        );

        jPanel1.setPreferredSize(new java.awt.Dimension(465, 255));

        jLabel5.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        jLabel5.setText("Số dư:");

        txtSoDu.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N

        jLabel8.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        jLabel8.setText("Tên hội viên:");

        txtTenHoiVien.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N

        txtIDHoiVien.setEditable(false);
        txtIDHoiVien.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtIDHoiVien.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel9.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        jLabel9.setText("Tạo lúc:");

        txtTaoLuc.setEditable(false);
        txtTaoLuc.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        jLabel2.setText("ID hội viên:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel8)
                    .addComponent(jLabel5)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 46, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtTenHoiVien, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSoDu, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtIDHoiVien, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTaoLuc, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIDHoiVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTenHoiVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtSoDu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(txtTaoLuc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26))
        );

        jPanel3.setPreferredSize(new java.awt.Dimension(930, 77));

        txtTao.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        txtTao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        txtTao.setText("Thêm ");
        txtTao.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        txtTao.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        txtTao.setPreferredSize(new java.awt.Dimension(93, 49));
        txtTao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTaoActionPerformed(evt);
            }
        });

        btnSua.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/update.png"))); // NOI18N
        btnSua.setText("Sửa ");
        btnSua.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnSua.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnLamMoi.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh.png"))); // NOI18N
        btnLamMoi.setText("Làm mới ");
        btnLamMoi.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnLamMoi.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnLamMoi.setPreferredSize(new java.awt.Dimension(145, 49));
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete.png"))); // NOI18N
        btnXoa.setText("Xóa ");
        btnXoa.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        btnXoa.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(txtTao, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlTaiKhoanLayout = new javax.swing.GroupLayout(pnlTaiKhoan);
        pnlTaiKhoan.setLayout(pnlTaiKhoanLayout);
        pnlTaiKhoanLayout.setHorizontalGroup(
            pnlTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlTaiKhoanLayout.createSequentialGroup()
                .addGroup(pnlTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTaiKhoanLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlTaiKhoanLayout.createSequentialGroup()
                        .addGroup(pnlTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlTaiKhoanLayout.createSequentialGroup()
                                .addGap(293, 293, 293)
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlTaiKhoanLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 459, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlTaiKhoanLayout.setVerticalGroup(
            pnlTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTaiKhoanLayout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addGroup(pnlTaiKhoanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))
                .addGap(2, 2, 2)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlChinh.add(pnlTaiKhoan);
        pnlTaiKhoan.setBounds(160, 170, 930, 410);

        lblBackGround.setFont(new java.awt.Font("Palatino Linotype", 1, 18)); // NOI18N
        lblBackGround.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BgThemHV.gif"))); // NOI18N
        pnlChinh.add(lblBackGround);
        lblBackGround.setBounds(0, 0, 1280, 720);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChinh, javax.swing.GroupLayout.DEFAULT_SIZE, 1280, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChinh, javax.swing.GroupLayout.DEFAULT_SIZE, 720, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        if (valiDateGetFormMem_Update()) {
            update();
        }

    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        if (validate_delete())
            delete();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        reset();

    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void txtTaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTaoActionPerformed
        // TODO add your handling code here:
        if (valiDateGetForm() && valiDateGetFormMem_Insert()) {
            insert();
        }
    }//GEN-LAST:event_txtTaoActionPerformed

    private void txtTenTaiKhoanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTenTaiKhoanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTenTaiKhoanActionPerformed

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
                    dialog = new ThemTaiKhoanHoiVienJDialog(new javax.swing.JFrame(), true, accountId);
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
