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
import utils.CustomPanel;
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

    public void setEmployeeListener(UpdateListener listener) {
        this.listener = listener;
    }

    private void fillOnUpdate() {
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

    private Account getForm() {

        Account account = new Account();

//        account.setId(Integer.parseInt(txtIDTaiKhoan.getText()));
        account.setUsername(txtTenTaiKhoan.getText());
        account.setPassword(txtMatKhau.getText());
        account.setRole(String.valueOf(cboVaiTro.getSelectedItem()));
        account.setCreatedAt(new Date());

        return account;
    }

    private boolean valiDateForm_getForm() {

        if (txtTenTaiKhoan.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Tên Tài khoản rỗng!", "Thông báo");
            txtTenTaiKhoan.requestFocus();
            return false;
        }
        if (txtMatKhau.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Mật khẩu rỗng!", "Thông báo");
            txtMatKhau.requestFocus();
            return false;
        }
        if (txtTenNhanVien.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Tên nhân viên rỗng!", "Thông báo");
            txtTenNhanVien.requestFocus();
            return false;
        }

        return true;
    }

    private Employee getFormEm() {

        Employee em = new Employee();
//        em.setId(Integer.parseInt(txtIDTaiKhoan.getText()));
        em.setAccountID(Integer.parseInt(txtIDNhanVIen.getText()));
        em.setName(txtTenNhanVien.getText());
        em.setSalaryPerHour(BigDecimal.valueOf(Double.parseDouble(txtLuong.getText())));
        em.setAddress(txtDiaChi.getText());
        em.setPhoneNumber(txtSoDienThoai.getText());
        em.setOtherInformation(txtThongTinKhac.getText());

        return em;
    }

    private Employee getFormEm_Update() {

        Employee em = new Employee();

        em.setAccountID(Integer.parseInt(txtIDTaiKhoan.getText()));
        em.setId(Integer.parseInt(txtIDNhanVIen.getText()));
        em.setName(txtTenNhanVien.getText());
        em.setSalaryPerHour(BigDecimal.valueOf(Double.parseDouble(txtLuong.getText())));
        em.setAddress(txtDiaChi.getText());
        em.setPhoneNumber(txtSoDienThoai.getText());
        em.setOtherInformation(txtThongTinKhac.getText());
        em.setBalance(BigDecimal.valueOf(Double.parseDouble(txtTongLuong.getText())));
        return em;
    }
    
    private boolean valiDateForm_update() {
        if (txtIDTaiKhoan.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: ID Tài khoản rỗng!", "Thông báo");
            return false;
        }
        if (txtIDNhanVIen.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: ID tài khoản nhân viên rỗng!", "Thông báo");
            return false;
        }
        if (txtTenNhanVien.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Tên nhân viên rỗng!", "Thông báo");
            txtTenNhanVien.requestFocus();
            return false;
        }
        if (txtLuong.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Lương/giờ của nhân viên rỗng!", "Thông báo");
            txtLuong.requestFocus();
            return false;
        }
        double luong = Double.parseDouble(txtLuong.getText().trim());
        if (luong < 0) {
            Xnoti.msg(this, "erro: Lương/giờ phải là số dương!", "Thông báo");
            txtLuong.requestFocus();
            return false;
        }
        if (txtDiaChi.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Địa chỉ của nhân viên rỗng!", "Thông báo");
            txtDiaChi.requestFocus();
            return false;
        }
        if (txtSoDienThoai.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Số điện thoại của nhân viên rỗng!", "Thông báo");
            txtSoDienThoai.requestFocus();
            return false;
        }
        String soDienThoai = txtSoDienThoai.getText().trim();
        if (!soDienThoai.matches("\\d{10,11}")) {
        Xnoti.msg(this, "erro: Số điện thoại phải là số và có độ dài từ 10 đến 11 ký tự!", "Thông báo");
        txtSoDienThoai.requestFocus();
        return false;
    }
        

        return true;
    }

    private boolean valiDateForm_insert() {
        
        if (txtTenNhanVien.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Tên nhân viên rỗng!", "Thông báo");
            txtTenNhanVien.requestFocus();
            return false;
        }
        if (txtLuong.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Lương/giờ của nhân viên rỗng!", "Thông báo");
            txtLuong.requestFocus();
            return false;
        }
        double luong = Double.parseDouble(txtLuong.getText().trim());
        if (luong < 0) {
            Xnoti.msg(this, "erro: Lương/giờ phải là số dương!", "Thông báo");
            txtLuong.requestFocus();
            return false;
        }
        if (txtDiaChi.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Địa chỉ của nhân viên rỗng!", "Thông báo");
            txtDiaChi.requestFocus();
            return false;
        }
        if (txtSoDienThoai.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Số điện thoại của nhân viên rỗng!", "Thông báo");
            txtSoDienThoai.requestFocus();
            return false;
        }
        String soDienThoai = txtSoDienThoai.getText().trim();
        if (!soDienThoai.matches("\\d{10,11}")) {
        Xnoti.msg(this, "erro: Số điện thoại phải là số và có độ dài từ 10 đến 11 ký tự!", "Thông báo");
        txtSoDienThoai.requestFocus();
        return false;
        }
        if (txtTenTaiKhoan.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: Tên Tài khoản rỗng!", "Thông báo");
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

    private void setForm(Integer id) throws Exception {
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

    private void insert() {
        if(valiDateForm_insert()){
            try {
            Account account = getForm();
            AccountDAO accDao = new AccountDAO();
            accDao.insert(account);
            txtIDNhanVIen.setText(String.valueOf(account.getId()));
            
                Employee em = getFormEm();
                em.setBalance(BigDecimal.ZERO);
                System.out.println("inst_em: " + em);
                EmployeeDAO emDao = new EmployeeDAO();
                emDao.insert(em);
                Xnoti.msg(this, "Thêm Thành Công", "Thông báo");
                fillOnUpdate();
            

        } catch (Exception e) {
            e.printStackTrace();
            Xnoti.msg(this, "erro: ID tài khoản hoặc tên tài khoản đã tồn tại!", "Thông báo");
        }
        }      
    }

    private void update() {

        if (valiDateForm_update() && valiDateForm_getForm()) {

            try {
                Employee em = getFormEm_Update();
                em.setBalance(BigDecimal.ONE);
                Account acc = getForm();
                accDao.update(acc);
                emDao.update(em);
                Xnoti.msg(this, "Cập Nhật Thành Công", "Thông báo");
                fillOnUpdate();
            } catch (Exception e) {
                Xnoti.msg(this, "erro: ID Tài khoản hoặc nhân viên chưa có!.\n Vui lòng chọn vào thông tin trên bảng để xóa", "Thông báo");
                e.printStackTrace();
            }
        }

    }

    private void delete() {

        if (!txtIDTaiKhoan.getText().trim().isEmpty() && !txtIDNhanVIen.getText().trim().isEmpty()) {

            try {
                Employee em = getFormEm();
                Account acc = getForm();
                int IDnv = Integer.parseInt(txtIDNhanVIen.getText());
                int IDtk = Integer.parseInt(txtIDTaiKhoan.getText());
                emDao.delete(IDnv);
                accDao.delete(IDtk);
                Xnoti.msg(this, "Xóa Thành Công", "Thông báo");
                fillOnUpdate();
            } catch (Exception e) {
                Xnoti.msg(this, "erro: Xóa không thành công!. Bạn cần phải xóa ID Nhân Viên có trong hóa đơn trước!", "Thông báo");
            }
        } else {
            Xnoti.msg(this, "erro: ID Tài khoản hoặc nhân viên chưa có!.\n Vui lòng chọn vào thông tin trên bảng để xóa", "Thông báo");
        }
    }
    
    private void reset() {
        txtDiaChi.setText(null);
        txtIDNhanVIen.setText(null);
        txtIDTaiKhoan.setText(null);
        txtLuong.setText(null);
        txtMatKhau.setText(null);
        txtSoDienThoai.setText(null);
        txtTaoLuc.setText(null);
        txtTenNhanVien.setText(null);
        txtTenTaiKhoan.setText(null);
        txtThongTinKhac.setText(null);
        txtTongLuong.setText(null);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new CustomPanel();
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
        jPanel3 = new CustomPanel();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        txtTaoLuc = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        txtTongLuong = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel1.setLayout(null);

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setLayout(null);

        jLabel2.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel2.setText("ID tài khoản:");
        jPanel2.add(jLabel2);
        jLabel2.setBounds(20, 90, 160, 23);

        jLabel3.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel3.setText("Tên tài khoản:");
        jPanel2.add(jLabel3);
        jLabel3.setBounds(20, 150, 170, 23);

        jLabel4.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel4.setText("Mật khẩu:");
        jPanel2.add(jLabel4);
        jLabel4.setBounds(20, 230, 120, 23);

        jLabel5.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel5.setText("Vai trò:");
        jPanel2.add(jLabel5);
        jLabel5.setBounds(20, 300, 100, 23);

        txtIDTaiKhoan.setEditable(false);
        txtIDTaiKhoan.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtIDTaiKhoan.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel2.add(txtIDTaiKhoan);
        txtIDTaiKhoan.setBounds(200, 80, 250, 40);

        txtTenTaiKhoan.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtTenTaiKhoan.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel2.add(txtTenTaiKhoan);
        txtTenTaiKhoan.setBounds(200, 140, 250, 40);

        txtMatKhau.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtMatKhau.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel2.add(txtMatKhau);
        txtMatKhau.setBounds(200, 220, 250, 40);

        cboVaiTro.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        cboVaiTro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nhân Viên", "Admin" }));
        cboVaiTro.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel2.add(cboVaiTro);
        cboVaiTro.setBounds(200, 290, 250, 40);

        jLabel6.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel6.setText("ID nhân viên:");
        jPanel2.add(jLabel6);
        jLabel6.setBounds(520, 90, 150, 23);

        jLabel7.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel7.setText("Tên nhân viên:");
        jPanel2.add(jLabel7);
        jLabel7.setBounds(520, 150, 170, 23);

        jLabel8.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel8.setText("Lương (vnd/giờ):");
        jPanel2.add(jLabel8);
        jLabel8.setBounds(520, 230, 200, 23);

        jLabel9.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel9.setText("Số điện thoại:");
        jPanel2.add(jLabel9);
        jLabel9.setBounds(520, 370, 170, 23);

        jLabel10.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel10.setText("Địa chỉ:");
        jPanel2.add(jLabel10);
        jLabel10.setBounds(520, 430, 100, 23);

        txtDiaChi.setColumns(20);
        txtDiaChi.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtDiaChi.setRows(5);
        jScrollPane1.setViewportView(txtDiaChi);

        jPanel2.add(jScrollPane1);
        jScrollPane1.setBounds(730, 430, 250, 90);

        jLabel11.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel11.setText("Thông tin khác:");
        jPanel2.add(jLabel11);
        jLabel11.setBounds(20, 440, 180, 23);

        txtThongTinKhac.setColumns(20);
        txtThongTinKhac.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtThongTinKhac.setRows(5);
        jScrollPane2.setViewportView(txtThongTinKhac);

        jPanel2.add(jScrollPane2);
        jScrollPane2.setBounds(200, 430, 250, 90);

        txtIDNhanVIen.setEditable(false);
        txtIDNhanVIen.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtIDNhanVIen.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel2.add(txtIDNhanVIen);
        txtIDNhanVIen.setBounds(730, 80, 250, 40);

        txtTenNhanVien.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtTenNhanVien.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel2.add(txtTenNhanVien);
        txtTenNhanVien.setBounds(730, 140, 250, 40);

        txtLuong.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtLuong.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel2.add(txtLuong);
        txtLuong.setBounds(730, 220, 250, 40);

        txtSoDienThoai.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtSoDienThoai.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel2.add(txtSoDienThoai);
        txtSoDienThoai.setBounds(730, 360, 250, 40);

        btnThem.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        btnThem.setText("Thêm ");
        btnThem.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnThem.setPreferredSize(new java.awt.Dimension(150, 49));
        btnThem.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/update.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.setToolTipText("");
        btnSua.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnSua.setPreferredSize(new java.awt.Dimension(150, 49));
        btnSua.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnLamMoi.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh.png"))); // NOI18N
        btnLamMoi.setText("Làm mới");
        btnLamMoi.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnLamMoi.setPreferredSize(new java.awt.Dimension(150, 49));
        btnLamMoi.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Monospaced", 1, 22)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnXoa.setPreferredSize(new java.awt.Dimension(150, 49));
        btnXoa.setVerticalTextPosition(javax.swing.SwingConstants.TOP);
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
                .addGap(75, 75, 75)
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(83, 83, 83)
                .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 94, Short.MAX_VALUE)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(10, 10, 10))
        );

        jPanel2.add(jPanel3);
        jPanel3.setBounds(0, 540, 1000, 70);

        jLabel13.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel13.setText("Tạo lúc:");
        jPanel2.add(jLabel13);
        jLabel13.setBounds(20, 370, 100, 23);

        txtTaoLuc.setEditable(false);
        txtTaoLuc.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtTaoLuc.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel2.add(txtTaoLuc);
        txtTaoLuc.setBounds(200, 360, 250, 40);

        jLabel12.setFont(new java.awt.Font("Monospaced", 1, 28)); // NOI18N
        jLabel12.setText("TÀI KHOẢN NHÂN VIÊN");
        jPanel2.add(jLabel12);
        jLabel12.setBounds(320, 10, 350, 38);

        jLabel14.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel14.setText("Tổng Lương:");
        jPanel2.add(jLabel14);
        jLabel14.setBounds(520, 290, 160, 30);

        txtTongLuong.setEditable(false);
        txtTongLuong.setFont(new java.awt.Font("Source Code Pro", 0, 18)); // NOI18N
        txtTongLuong.setPreferredSize(new java.awt.Dimension(250, 40));
        jPanel2.add(txtTongLuong);
        txtTongLuong.setBounds(730, 290, 250, 40);

        jPanel1.add(jPanel2);
        jPanel2.setBounds(160, 40, 1000, 610);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/BGThemNV_admin_N.jpg"))); // NOI18N
        jPanel1.add(jLabel1);
        jLabel1.setBounds(0, 0, 1280, 720);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1280, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 720, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        insert();

    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        delete();

    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        reset();

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
                ThemTaiKhoanNhanVienJDialog dialog = null;
                try {
                    dialog = new ThemTaiKhoanNhanVienJDialog(new javax.swing.JFrame(), true, accountId);
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
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboVaiTro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
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
    private javax.swing.JTextField txtTaoLuc;
    private javax.swing.JTextField txtTenNhanVien;
    private javax.swing.JTextField txtTenTaiKhoan;
    private javax.swing.JTextArea txtThongTinKhac;
    private javax.swing.JTextField txtTongLuong;
    // End of variables declaration//GEN-END:variables
}
