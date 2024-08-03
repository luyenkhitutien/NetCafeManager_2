/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.server;

import Interface.UpdateListener;
import dao.AccountDAO;
import dao.EmployeeDAO;
import dao.MemberDAO;
import entity.Account;
import entity.Employee;
import entity.Member;
import javax.swing.SwingUtilities;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.table.DefaultTableModel;
import utils.XInitTable;
import utils.Xnoti;

/**
 *
 * @author ASUS
 */
public class QuanLyTaiKhoanJPanel extends javax.swing.JPanel {

    DefaultTableModel tablemodel = new DefaultTableModel();
    List<Account> listAcount = new ArrayList<>();
    List<Member> listMember = new ArrayList<>();
    Account account = new Account();
    AccountDAO accountDAO = new AccountDAO();
    Member member = new Member();
    MemberDAO memberDAO = new MemberDAO();
    EmployeeDAO emDao = new EmployeeDAO();
    int STT = 1;
    int current = 0;

    private ServerMain serverMain;

    public QuanLyTaiKhoanJPanel() {

        initComponents();
        initTable();

        addPopupToTable();
        tablemodel = (DefaultTableModel) tblQuanLyTaiKhoan.getModel();
        loadDataToArray();
        fillToTable();
    }

    void refresh() {
        try {
            this.loadDataToArray();
            this.fillToTable();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public QuanLyTaiKhoanJPanel(ServerMain serverMain) {
        this.serverMain = serverMain;
    }

    public void initTable() {
        XInitTable.initTable(tblQuanLyTaiKhoan);

        int[] widths = {50, 100, 250, 250, 175, 175};
        XInitTable.setColumnWidths(tblQuanLyTaiKhoan, widths);
    }

    public void loadDataToArray() {
        STT = 1;
        listAcount.clear();
        try {
            listAcount = accountDAO.selectAll();
        } catch (Exception ex) {
            Logger.getLogger(QuanLyTaiKhoanJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean searchId(String text) throws Exception {
        try {
            for (char c : text.toCharArray()) {
                // nếu là chữ-> false
                // số -> true
                if (!Character.isDigit(c)) {
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Không tìm thấy Account");
            e.printStackTrace();
        }
        return true;
    }

    void fillToTable() {
        tablemodel.setRowCount(0);
        try {
            Collections.sort(listAcount, new Comparator<Account>(){
                @Override
                public int compare(Account acc1, Account acc2) {
                    //admin luôn luôn đứng trước
                    if (acc1.getRole().equalsIgnoreCase("admin")) {
                        return -1;
                    }
                    if(acc2.getRole().equalsIgnoreCase("admin")){
                        return 1;
                    }
                    if (acc1.getRole().equalsIgnoreCase("Nhân Viên") && acc2.getRole().equalsIgnoreCase("Hội Viên")) {
                        return -1; // acc1 nên xuất hiện trước acc2
                    }else if(acc1.getRole().equalsIgnoreCase("Hội Viên") && acc2.getRole().equalsIgnoreCase("Nhân Viên")){
                        return 1;  // acc1 nên xuất hiện sau acc2
                    }else{
                        return acc1.getRole().compareTo(acc2.getRole());
                    }
                }
            });
            for (Account account : listAcount) {
                Object[] rows = new Object[]{
                    STT++,
                    account.getId(),
                    account.getUsername(),
                    account.getPassword(),
                    account.getRole(),
                    account.getCreatedAt()
                };
                tablemodel.addRow(rows);
            }           
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void getRowDelete(int index) {
        
        try {
            
            int rowId = (int) tblQuanLyTaiKhoan.getValueAt(index, 1);
            MemberDAO memDao = new MemberDAO();
            EmployeeDAO emDao = new EmployeeDAO();
            Account acc = listAcount.get(index);
            if (acc.getRole().trim().equalsIgnoreCase("Hội viên")) {
                Member mem = memDao.selectByAccountID(rowId);
                System.out.println(mem);
                memDao.delete(mem.getId());
                System.out.println("Xoa thanh cong hv: " + rowId);
                accountDAO.delete(rowId);
            } else if (acc.getRole().trim().equalsIgnoreCase("Nhân viên")) {
                Employee em = emDao.selectByAccountID(rowId);
                System.out.println(em);
                emDao.delete(em.getId());
                System.out.println("Xoa thanh cong nv: " + rowId);
                accountDAO.delete(rowId);
            }
            System.out.println("QLTK => Fill table");
            loadDataToArray();
            fillToTable();
            Xnoti.msg(this, "Xóa thành công", "Thông báo");
        } catch (Exception e) {
            Xnoti.msg(this, "erro: Xóa bại. Bạn cần phải xóa ID hội viên có trong hóa đơn!", "Thông báo");
            e.printStackTrace();
            
        }
    }

    private void addPopupToTable() {

        tblQuanLyTaiKhoan.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    showPopup(e);
                }
            }

            private void showPopup(MouseEvent e) {
                int row = tblQuanLyTaiKhoan.rowAtPoint(e.getPoint());
                int column = tblQuanLyTaiKhoan.columnAtPoint(e.getPoint());
                if (!tblQuanLyTaiKhoan.isRowSelected(row)) {
                    tblQuanLyTaiKhoan.changeSelection(row, column, false, false);
                }
                int index = tblQuanLyTaiKhoan.getSelectedRow();
                String role = (String) tblQuanLyTaiKhoan.getValueAt(index, 4);
                if (role.trim().equalsIgnoreCase("Hội viên")) {
                    mnitNapTien.setVisible(true);
                    mnitChiTiet.setVisible(true);
                    mnitXoa.setVisible(true);
                    pup.show(e.getComponent(), e.getX(), e.getY());
                } else if (role.trim().equalsIgnoreCase("admin")) {
                    mnitChiTiet.setVisible(false);
                    mnitNapTien.setVisible(false);
                    mnitXoa.setVisible(false);
                } else {
                    mnitNapTien.setVisible(false);
                    mnitChiTiet.setVisible(true);
                    mnitXoa.setVisible(true);
                    pup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jTextField1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        pup = new javax.swing.JPopupMenu();
        mnitChiTiet = new javax.swing.JMenuItem();
        mnitXoa = new javax.swing.JMenuItem();
        mnitNapTien = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblQuanLyTaiKhoan = new javax.swing.JTable();
        btnThemHoiVien = new javax.swing.JButton();
        btnThemNhanVien = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();

        jLabel2.setText("Danh sách tài khoản:");

        jToggleButton1.setText("jToggleButton1");

        jTextField1.setText("jTextField1");

        jLabel3.setText("jLabel3");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jToggleButton1)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 173, Short.MAX_VALUE)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jToggleButton1)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addGap(0, 453, Short.MAX_VALUE))
        );

        jLabel1.setText("Quản lý tài khoản");

        jScrollPane2.setViewportView(jTextPane1);

        mnitChiTiet.setText("Chi tiết");
        mnitChiTiet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnitChiTietMouseClicked(evt);
            }
        });
        mnitChiTiet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnitChiTietActionPerformed(evt);
            }
        });
        pup.add(mnitChiTiet);

        mnitXoa.setText("Xóa");
        mnitXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnitXoaActionPerformed(evt);
            }
        });
        pup.add(mnitXoa);

        mnitNapTien.setText("Nạp tiền");
        mnitNapTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnitNapTienActionPerformed(evt);
            }
        });
        pup.add(mnitNapTien);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel4.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel4.setText("Quản lý tài khoản");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel4)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel5.setFont(new java.awt.Font("Source Code Pro", 3, 18)); // NOI18N
        jLabel5.setText("Danh sách tài khoản:");

        tblQuanLyTaiKhoan.setFont(new java.awt.Font("Source Code Pro", 0, 16)); // NOI18N
        tblQuanLyTaiKhoan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "ID", "Tên tài khoản", "Mật khẩu", "Vai trò", "Ngày tạo"
            }
        ));
        tblQuanLyTaiKhoan.setRowHeight(23);
        tblQuanLyTaiKhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblQuanLyTaiKhoanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblQuanLyTaiKhoan);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addComponent(jLabel5)
                .addContainerGap(752, Short.MAX_VALUE))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 407, Short.MAX_VALUE))
        );

        btnThemHoiVien.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        btnThemHoiVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ThemHoiVien200x40.png"))); // NOI18N
        btnThemHoiVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemHoiVienActionPerformed(evt);
            }
        });

        btnThemNhanVien.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        btnThemNhanVien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ThemNhanVien200x40.png"))); // NOI18N
        btnThemNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemNhanVienActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Source Code Pro", 3, 16)); // NOI18N
        jLabel6.setText("Tìm theo tên hoặc ID:");

        txtTimKiem.setFont(new java.awt.Font("Dialog", 0, 24)); // NOI18N
        txtTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTimKiemActionPerformed(evt);
            }
        });
        txtTimKiem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTimKiemKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(btnThemHoiVien)
                .addGap(33, 33, 33)
                .addComponent(btnThemNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnThemNhanVien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnThemHoiVien, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel6)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void tblQuanLyTaiKhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQuanLyTaiKhoanMouseClicked
        if (SwingUtilities.isLeftMouseButton(evt)) {
            current = tblQuanLyTaiKhoan.getSelectedRow();
            System.out.println(current);
        } else {
            current = tblQuanLyTaiKhoan.getSelectedRow();
            System.out.println(current);
        }
//        current = tblQuanLyTaiKhoan.getSelectedRow();
//        System.out.println(current);

    }//GEN-LAST:event_tblQuanLyTaiKhoanMouseClicked

    private void mnitChiTietActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnitChiTietActionPerformed
        // TODO add your handling code here:
        String rl = (String) tblQuanLyTaiKhoan.getValueAt(current, 4);
        System.out.println(rl); // => sys role
        current = tblQuanLyTaiKhoan.getSelectedRow();
        try {
            loadDataToArray();
            Account acc = listAcount.get(current); // sys row clicked
            try {
                System.out.println(acc.getId()); // sys id
                if (rl.trim().equalsIgnoreCase("hội viên")) {
                    try {
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                        ThemTaiKhoanHoiVienJDialog taoHoiVien = new ThemTaiKhoanHoiVienJDialog(frame, true, acc.getId());

                        //taoHoiVien = new ThemTaiKhoanHoiVienJDialog(frame, true);
                        System.out.println(acc.getId());
                        taoHoiVien.setMemberListener(new UpdateListener() {
                            @Override
                            public void onUpdate() {
                                refresh();
                            }
                        });
                        taoHoiVien.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                } else if (rl.trim().equalsIgnoreCase("Nhân viên")) {
                    try {
                        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                        ThemTaiKhoanNhanVienJDialog taoNhanVien = new ThemTaiKhoanNhanVienJDialog(frame, true, acc.getId());
                        System.out.println(acc.getId());
                        taoNhanVien.setEmployeeListener(() -> refresh());
                        taoNhanVien.setVisible(true);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_mnitChiTietActionPerformed

    private void mnitChiTietMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnitChiTietMouseClicked

    }//GEN-LAST:event_mnitChiTietMouseClicked

    private void btnThemNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemNhanVienActionPerformed
        // TODO add your handling code here:
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        ThemTaiKhoanNhanVienJDialog taoNhanVien = null;

        try {
            taoNhanVien = new ThemTaiKhoanNhanVienJDialog(frame, true, null);

        } catch (Exception ex) {
            Logger.getLogger(QuanLyTaiKhoanJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        taoNhanVien.setEmployeeListener(() -> refresh());
        taoNhanVien.setVisible(true);
    }//GEN-LAST:event_btnThemNhanVienActionPerformed

    private void btnThemHoiVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemHoiVienActionPerformed
        // TODO add your handling code here:
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        ThemTaiKhoanHoiVienJDialog taoHoiVien = null;

        try {
            taoHoiVien = new ThemTaiKhoanHoiVienJDialog(frame, true, null);

        } catch (Exception ex) {
            Logger.getLogger(QuanLyTaiKhoanJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
        taoHoiVien.setMemberListener(() -> refresh());
        taoHoiVien.setVisible(true);
    }//GEN-LAST:event_btnThemHoiVienActionPerformed

    private void mnitNapTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnitNapTienActionPerformed
        // TODO add your handling code here:
        try {
            int index = tblQuanLyTaiKhoan.getSelectedRow();
            int rowId = (int) tblQuanLyTaiKhoan.getValueAt(index, 1);
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            NapTienJDialog napTien = new NapTienJDialog(frame, true, rowId);
            napTien.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }//GEN-LAST:event_mnitNapTienActionPerformed

    private void mnitXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnitXoaActionPerformed
        // TODO add your handling code here:
        try {
            current = tblQuanLyTaiKhoan.getSelectedRow();
            System.out.println(current);
            getRowDelete(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_mnitXoaActionPerformed

    private void txtTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTimKiemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTimKiemActionPerformed

    private void txtTimKiemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyPressed
        // TODO add your handling code here:
        try {
            if (txtTimKiem.getText().trim().equalsIgnoreCase("")) {
                loadDataToArray();
                fillToTable();
            } else {
                if (searchId(txtTimKiem.getText())) {
                    STT = 1;
                    listAcount.clear();
                    account = accountDAO.selectByID(Integer.parseInt(txtTimKiem.getText()));
                    if (account != null) {
                        listAcount.add(account);
                        System.out.println("Search for accountID : " + account.getId());
                        fillToTable();
                    }
                } else {
                    STT = 1;
                    listAcount.clear();
                    listAcount = accountDAO.selectByName(txtTimKiem.getText());
//                    listAcount.add(account);
                    fillToTable();
                }
            }
        } catch (Exception ex) {
            System.out.println("Khong tim duoc account: " + account.getId() + "Name: " + account.getUsername());
        }
    }//GEN-LAST:event_txtTimKiemKeyPressed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThemHoiVien;
    private javax.swing.JButton btnThemNhanVien;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JMenuItem mnitChiTiet;
    private javax.swing.JMenuItem mnitNapTien;
    private javax.swing.JMenuItem mnitXoa;
    private javax.swing.JPopupMenu pup;
    private javax.swing.JTable tblQuanLyTaiKhoan;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
