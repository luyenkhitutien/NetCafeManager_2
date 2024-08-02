 /*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.server;

import dao.EmployeeDAO;
import dao.StatisticsDAO;
import entity.Employee;
import java.awt.Font;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import utils.Xnoti;

/**
 *
 * @author ASUS
 */
public class ThongKeLuongNhanVienJPanel extends javax.swing.JPanel {
     DefaultTableModel model = new DefaultTableModel();
    DefaultComboBoxModel<String> sortModel;
    StatisticsDAO statisticsDAO = new StatisticsDAO();
    EmployeeDAO emDao = new EmployeeDAO();
    List<Employee> list = new ArrayList<>();
    Employee em = new Employee();    
    /**
     * Creates new form ThongKeLuongNhanVien
     */
    public ThongKeLuongNhanVienJPanel() {
        initComponents();
        initTable();
        model = (DefaultTableModel) tblThongKeLuong.getModel();
        fillIdEmployeeCbo();
        fillToTable();
    }
    void fillIdEmployeeCbo(){
        try {
             DefaultComboBoxModel model = (DefaultComboBoxModel) cboCuaNhanVien.getModel();
        model.removeAllElements();
        list = emDao.selectAll();
        Set<Integer> IdEmployee = new HashSet<>();
        for(Employee p : list){
            IdEmployee.add(p.getId());
        }
        for(Integer IdEmployees : IdEmployee){
            model.addElement(IdEmployees);
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void cboFillToTable(){
            System.out.println("<== Thong Ke NV ===>");
//            Integer IdEm = Integer.parseInt((String) cboCuaNhanVien.getSelectedItem());
//            System.out.println(IdEm);
            try {
            model.setRowCount(0);
//                int idEm = (int) cboCuaNhanVien.getSelectedItem();
            List<Object[]> data = statisticsDAO.getStatisticsByIdEmployee((Integer) cboCuaNhanVien.getSelectedItem());
            for(Object[] row : data){
                model.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    void fillToTable(){
         // Lấy dữ liệu từ DAO
        List<Object[]> data = statisticsDAO.getStatisticsList();
        
        // Xóa tất cả các hàng hiện tại trong bảng
        model.setRowCount(0);
        
        // Thêm dữ liệu vào bảng
        for (Object[] row : data) {
            model.addRow(row);
        }
        tongLuong();
    }
    void fillToTableByDate(Date startAt, Date endAt) {
        // Lấy dữ liệu từ DAO
        List<Object[]> data = statisticsDAO.getEmployeeSessionsDetail(startAt, endAt);
        
        // Xóa tất cả các hàng hiện tại trong bảng
        model.setRowCount(0);
        
        // Thêm dữ liệu vào bảng
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
    void tongLuong(){
        double tong = 0;
        for (int i = 0; i < tblThongKeLuong.getRowCount(); i++) {
            double tl = (double) tblThongKeLuong.getValueAt(i, 5);
            tong += tl;
        }
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formatTong = currencyFormat.format(tong);
        lblTongTienTra.setText(String.valueOf(formatTong));
    }
    ThongKeLuongNhanVienJPanel(ServerMain aThis) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void initTable() {
        // Tạo một Font mới với kiểu chữ "Times New Roman" và kích thước 24
        Font headerFont = new Font("Times New Roman", Font.BOLD, 17);

        // Lấy tiêu đề của bảng và đặt font mới
        JTableHeader tableHeader = tblThongKeLuong.getTableHeader();
        tableHeader.setFont(headerFont);

        // Tạo một DefaultTableCellRenderer để căn giữa tiêu đề
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

        // Đảm bảo rằng bảng và tiêu đề bảng được cập nhật
        tblThongKeLuong.repaint();

    }   
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlChinh = new javax.swing.JPanel();
        pnlTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTuNgay = new javax.swing.JTextField();
        txtDenNgay = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        cboCuaNhanVien = new javax.swing.JComboBox<>();
        btnMoi = new javax.swing.JButton();
        btnTimKiem = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        lblTongTienTra = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblThongKeLuong = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Source Code Pro", 1, 24)); // NOI18N
        jLabel1.setText("Thống kê lương nhân viên");

        javax.swing.GroupLayout pnlTitleLayout = new javax.swing.GroupLayout(pnlTitle);
        pnlTitle.setLayout(pnlTitleLayout);
        pnlTitleLayout.setHorizontalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitleLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTitleLayout.setVerticalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTitleLayout.createSequentialGroup()
                .addContainerGap(12, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel2.setText("Từ ngày:");

        jLabel3.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel3.setText("Đến ngày:");

        txtTuNgay.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        txtTuNgay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTuNgayActionPerformed(evt);
            }
        });
        txtTuNgay.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTuNgayKeyPressed(evt);
            }
        });

        txtDenNgay.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        txtDenNgay.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDenNgayKeyPressed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        jLabel6.setText("Của nhân viên:");

        cboCuaNhanVien.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        cboCuaNhanVien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cboCuaNhanVien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboCuaNhanVienActionPerformed(evt);
            }
        });

        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/moi3.png"))); // NOI18N
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnTimKiem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/KinhLup.png"))); // NOI18N
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(373, 373, 373)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel6)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(txtTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(94, 94, 94)
                        .addComponent(jLabel3)))
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(btnMoi)
                            .addGap(69, 69, 69)
                            .addComponent(btnTimKiem))
                        .addComponent(cboCuaNhanVien, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(txtDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(136, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(txtTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(31, 31, 31)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(cboCuaNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(21, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel7.setText("Tổng tiền trả:");

        lblTongTienTra.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        lblTongTienTra.setText("0.000đ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(1024, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addGap(29, 29, 29)
                .addComponent(lblTongTienTra, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblTongTienTra))
                .addContainerGap())
        );

        tblThongKeLuong.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N
        tblThongKeLuong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "ID nhân viên", "Tên nhân viên", "Tên tài khoản", "Bắt đầu lúc", "Kết thúc lúc", "Tiền lương"
            }
        ));
        tblThongKeLuong.setRowHeight(24);
        jScrollPane1.setViewportView(tblThongKeLuong);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlChinhLayout = new javax.swing.GroupLayout(pnlChinh);
        pnlChinh.setLayout(pnlChinhLayout);
        pnlChinhLayout.setHorizontalGroup(
            pnlChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlChinhLayout.setVerticalGroup(
            pnlChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChinhLayout.createSequentialGroup()
                .addComponent(pnlTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        try {
           if(txtDenNgay.getText().trim().equalsIgnoreCase("") || txtTuNgay.getText().trim().equalsIgnoreCase("")){
//                Xnoti.msg(this, "Vui lòng điền đầy đủ thời gian bắt đầu và thời gian kết thúc!", "Thông báo trống!");
                fillToTable();
            }else if(isValidateSearch()){
                Date startAt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(txtTuNgay.getText()+" 00:00:00.0");
                Date endAt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(txtDenNgay.getText()+" 23:59:59.9");
                fillToTableByDate(startAt, endAt);
            } 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_btnTimKiemActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        // TODO add your handling code here:
        fillToTable();
    }//GEN-LAST:event_btnMoiActionPerformed

    private void txtTuNgayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTuNgayActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_txtTuNgayActionPerformed

    private void txtTuNgayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTuNgayKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()== java.awt.event.KeyEvent.VK_ENTER){
            try {
            // Chuyển đổi dữ liệu từ các trường nhập liệu
            if(txtDenNgay.getText().trim().equalsIgnoreCase("") || txtTuNgay.getText().trim().equalsIgnoreCase("")){
                Xnoti.msg(this, "Vui lòng điền đầy đủ thời gian bắt đầu và thời gian kết thúc!\nNăm-Tháng-Ngày", "Thông báo trống!");
            }else{
                Date startAt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(txtTuNgay.getText()+" 00:00:00.0");
            Date endAt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(txtDenNgay.getText()+" 23:59:59.9");
            fillToTableByDate(startAt, endAt);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        }
    }//GEN-LAST:event_txtTuNgayKeyPressed

    private void txtDenNgayKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDenNgayKeyPressed
        // TODO add your handling code here:
        if(evt.getKeyCode()== java.awt.event.KeyEvent.VK_ENTER){
            try {
            // Chuyển đổi dữ liệu từ các trường nhập liệu
            if(txtDenNgay.getText().trim().equalsIgnoreCase("") || txtTuNgay.getText().trim().equalsIgnoreCase("")){
                Xnoti.msg(this, "Vui lòng điền đầy đủ thời gian bắt đầu và thời gian kết thúc!", "Thông báo trống!");
            }else{
                Date startAt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(txtTuNgay.getText()+" 00:00:00.0");
            Date endAt = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(txtDenNgay.getText()+" 23:59:59.9");
            fillToTableByDate(startAt, endAt);
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        }
    }//GEN-LAST:event_txtDenNgayKeyPressed

    private void cboCuaNhanVienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboCuaNhanVienActionPerformed
        // TODO add your handling code here:
         cboFillToTable();
    }//GEN-LAST:event_cboCuaNhanVienActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JComboBox<String> cboCuaNhanVien;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTongTienTra;
    private javax.swing.JPanel pnlChinh;
    private javax.swing.JPanel pnlTitle;
    private javax.swing.JTable tblThongKeLuong;
    private javax.swing.JTextField txtDenNgay;
    private javax.swing.JTextField txtTuNgay;
    // End of variables declaration//GEN-END:variables
    boolean isValidateSearch(){
        if(txtTuNgay.getText().trim().equals("") || txtDenNgay.getText().trim().equals("")){
            if(!txtTuNgay.getText().matches("^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")) {
            Xnoti.msg(this, "Sai Định Dạng Thời Gian'Bắt Đầu'!\nNăm-Tháng-Ngày", "NETCAFE");
            return false;
        }
         if(!txtDenNgay.getText().matches("^(\\d{4})-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")) {
            Xnoti.msg(this, "Sai Định Dạng Thời Gian'Bắt Đầu'!\nNăm-Tháng-Ngày", "NETCAFE");
            return false;
        }
        }
        
        return true;
    }
}
