/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.server;

import dao.ComputerDAO;
import entity.Computer;
import java.awt.Font;
import java.math.BigDecimal;
import java.util.List;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import utils.Xnoti;

/**
 *
 * @author ASUS
 */
public class QuanLyMayJPanel extends javax.swing.JPanel {
    
    ComputerDAO comDao = new ComputerDAO();
    int row = -1;
    
    public QuanLyMayJPanel() {
        initComponents();
        initTable();
        filltotable();
        
    }
    
    public void initTable() {
        // Tạo một Font mới với kiểu chữ "Times New Roman" và kích thước 24
        Font headerFont = new Font("Times New Roman", Font.BOLD, 18);

        // Lấy tiêu đề của bảng và đặt font mới
        JTableHeader tableHeader = tblQuanLyMay.getTableHeader();
        tableHeader.setFont(headerFont);

        // Tạo một DefaultTableCellRenderer để căn giữa tiêu đề
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);

        // Đảm bảo rằng bảng và tiêu đề bảng được cập nhật
        tblQuanLyMay.repaint();
        
    }
    
    public void filltotable() {
        DefaultTableModel model = (DefaultTableModel) tblQuanLyMay.getModel();
        model.setRowCount(0);
        int STT = 0;
        try {
            List<Computer> list = comDao.selectAll();
            for (Computer comp : list) {
                Object[] row = {
                    STT++,
                    comp.getId(),
                    comp.getName(),
                    comp.getPricePerHour(),
                    comp.getType(),
                    comp.getStatus()
                };
                model.addRow(row);
            }
        } catch (Exception e) {
            Xnoti.msg(this, "Lỗi truy vấn dữ liệu", "Thông báo lỗi");
        }
    }
    
    public Computer getForm() {
        Computer computer = new Computer();
        //getId này chỉ cần cho Upadte       
        if (!txtIDmay.getText().trim().isEmpty()) {
            //Cần có điều kiện để insert không bị lỗi
            computer.setId(Integer.parseInt(txtIDmay.getText().trim()));
        }
        computer.setName(txtTenMay.getText());
        computer.setPricePerHour(new BigDecimal(txtGia.getText()));
        computer.setType((String) cboLoaiMay.getSelectedItem());
        return computer;
    }
    
    public void setForm(Computer computer) {
        txtIDmay.setText(String.valueOf(computer.getId()));
        txtTenMay.setText(computer.getName());
        txtGia.setText(computer.getPricePerHour().toString());
        if (computer.getType().equals("VIP") || computer.getType().equals("Thường")) {
            cboLoaiMay.setSelectedItem(computer.getType());
        }
    }
    
    public void insert() {
        
        if (!txtTenMay.getText().trim().isEmpty()) {
            Computer computer = getForm();
            try {
                comDao.insert(computer);
                this.filltotable();
                this.clearForm();
                Xnoti.msg(this, "Thêm máy thành công", "Thông báo");
            } catch (Exception e) {
                Xnoti.msg(this, "erro: Tên của máy đã tồn tại!", "Thông báo");
                System.out.println(e);
            }
        }else{
            Xnoti.msg(this, "erro: Tên máy rỗng ", "Thông báo");
            txtTenMay.requestFocus();
        }
        
    }

    public void update() {
        if (validateForm()) {

            if (Xnoti.confirm(this, "Bạn muốn cập nhật chiếc máy này")) {
                try {
                    Computer computer = getForm();
                    comDao.update(computer);
                    this.filltotable();
                    Xnoti.msg(this, "Cập nhật thành công" + computer.getName(), "Thông bao cập nhật");
                } catch (Exception e) {
                    Xnoti.msg(this, "Cập nhật thất bại", "Thông báo cập nhật");
                    System.out.println(e);
                    e.printStackTrace();
                }
            }
        }

    }

    public void delete() {
        if (!txtIDmay.getText().trim().isEmpty()) {
            Integer id = Integer.parseInt(txtIDmay.getText());
            try {
                comDao.delete(id);
                this.filltotable();
                this.clearForm();
                Xnoti.msg(this, "Xóa Thành Công", "Thông báo");
            } catch (Exception e) {
                Xnoti.msg(this, "Xóa thất bại", "Thông báo");
                System.out.println(e);
                e.printStackTrace();
            }
        } else {
            Xnoti.msg(this, "ID Máy rỗng", "Thông báo");
        }

    }

    public void clearForm() {
        txtIDmay.setText("");
        txtTenMay.setText("");
    }

    public void ChoseComputer() {

        // khi chọn máy VIP->10000,Thường->5000
        Computer computer = new Computer();
        //lấy giá trị trong cboLoaiMay gán cho type. 
        String type = (String) cboLoaiMay.getSelectedItem();
        if (type != null) {
            if (type.equals("VIP")) {
                computer.setPricePerHour(new BigDecimal("10000"));
            } else if (type.equals("Thường")) {
                computer.setPricePerHour(new BigDecimal("5000"));
            }
        }
        //chuyển đổi giá đối tượng computer thành chuỗi và cập nhật vào txtGia
        txtGia.setText(computer.getPricePerHour().toString());
    }

    private boolean validateForm() {
        if (txtIDmay.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: ID Máy rỗng! Bạn phải chọn máy ở trên bảng trước", "Thông báo");
            return false;
        }
        if (txtTenMay.getText().trim().isEmpty()) {
            Xnoti.msg(this, "erro: tên Máy rỗng! Bạn phải chọn máy ở trên bảng trướ", "Thông báo");
            txtTenMay.requestFocus();
            return false;
        }
        return true;

    }

    private void TableCliked() {
        try {
            this.row = tblQuanLyMay.getSelectedRow();
            tblQuanLyMay.setRowSelectionInterval(row, row);
            // Lấy giá trị từ bảng và chuyển đổi nó thành chuỗi
            String idStr = tblQuanLyMay.getValueAt(this.row, 1).toString();
            Integer id = Integer.parseInt(idStr);
            Computer computer = comDao.selectByID(id);
            this.setForm(computer);
        } catch (Exception ex) {
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
        pnlTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        pnlControl = new javax.swing.JPanel();
        btnSua = new javax.swing.JButton();
        btnThem = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        txtIDmay = new javax.swing.JTextField();
        txtTenMay = new javax.swing.JTextField();
        txtGia = new javax.swing.JTextField();
        cboLoaiMay = new javax.swing.JComboBox<>();
        pnlTable = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblQuanLyMay = new javax.swing.JTable();

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Quản lý máy");

        javax.swing.GroupLayout pnlTitleLayout = new javax.swing.GroupLayout(pnlTitle);
        pnlTitle.setLayout(pnlTitleLayout);
        pnlTitleLayout.setHorizontalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitleLayout.createSequentialGroup()
                .addGap(36, 36, 36)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTitleLayout.setVerticalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTitleLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel2.setText("ID máy:");

        jLabel3.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel3.setText("Tên máy");

        jLabel4.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel4.setText("Giá:");

        jLabel5.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel5.setText("Loại máy");

        btnSua.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/update.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnThem.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/save.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnXoa.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnMoi.setFont(new java.awt.Font("Monospaced", 1, 14)); // NOI18N
        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/refresh.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlControlLayout = new javax.swing.GroupLayout(pnlControl);
        pnlControl.setLayout(pnlControlLayout);
        pnlControlLayout.setHorizontalGroup(
            pnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlControlLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(btnXoa, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        pnlControlLayout.setVerticalGroup(
            pnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlControlLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnXoa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(pnlControlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 40, Short.MAX_VALUE)
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(btnMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        txtIDmay.setEditable(false);
        txtIDmay.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        txtTenMay.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        txtGia.setEditable(false);
        txtGia.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txtGia.setText("5000");
        txtGia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGiaActionPerformed(evt);
            }
        });

        cboLoaiMay.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cboLoaiMay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Thường", "VIP" }));
        cboLoaiMay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cboLoaiMayActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtIDmay)
                            .addComponent(txtTenMay)
                            .addComponent(txtGia)
                            .addComponent(cboLoaiMay, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(pnlControl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtIDmay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(txtTenMay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(42, 42, 42)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtGia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(cboLoaiMay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(36, 36, 36)
                .addComponent(pnlControl, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlTable.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblQuanLyMay.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "STT", "ID Máy", "Tên máy", "Giá", "Loại", "Trạng thái"
            }
        ));
        tblQuanLyMay.setRowHeight(24);
        tblQuanLyMay.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblQuanLyMayMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblQuanLyMay);

        javax.swing.GroupLayout pnlTableLayout = new javax.swing.GroupLayout(pnlTable);
        pnlTable.setLayout(pnlTableLayout);
        pnlTableLayout.setHorizontalGroup(
            pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 592, Short.MAX_VALUE)
        );
        pnlTableLayout.setVerticalGroup(
            pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout pnlChinhLayout = new javax.swing.GroupLayout(pnlChinh);
        pnlChinh.setLayout(pnlChinhLayout);
        pnlChinhLayout.setHorizontalGroup(
            pnlChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlChinhLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlChinhLayout.setVerticalGroup(
            pnlChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChinhLayout.createSequentialGroup()
                .addComponent(pnlTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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

    private void txtGiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGiaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGiaActionPerformed

    private void cboLoaiMayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cboLoaiMayActionPerformed
        // TODO add your handling code here:
        ChoseComputer();
    }//GEN-LAST:event_cboLoaiMayActionPerformed

    private void tblQuanLyMayMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQuanLyMayMouseClicked
        // TODO add your handling code here: 
       TableCliked();
    }//GEN-LAST:event_tblQuanLyMayMouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        insert();
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        update();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_btnMoiActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        delete();
    }//GEN-LAST:event_btnXoaActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cboLoaiMay;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel pnlChinh;
    private javax.swing.JPanel pnlControl;
    private javax.swing.JPanel pnlTable;
    private javax.swing.JPanel pnlTitle;
    private javax.swing.JTable tblQuanLyMay;
    private javax.swing.JTextField txtGia;
    private javax.swing.JTextField txtIDmay;
    private javax.swing.JTextField txtTenMay;
    // End of variables declaration//GEN-END:variables
}
