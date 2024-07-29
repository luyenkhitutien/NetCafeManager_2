/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.server;


import Interface.UpdateListener;
import dao.ProductDAO;
import entity.Product;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import utils.Ximage;

/**
 *
 * @author ASUS
 */
public class QuanLySanPhamJPanel extends javax.swing.JPanel  {
    DefaultTableModel tablemodel = new DefaultTableModel();
    List<Product> list = new ArrayList<>();
    ProductDAO proDao = new ProductDAO();
    int STT=1;
    int current;
    /**
     * Creates new form QuanLySanPhamJPanel
     */
    public QuanLySanPhamJPanel( ) {
        initComponents();
        initTable();
        initPopupMenu();
        addPopupToTable();
        tablemodel = (DefaultTableModel) tblSanPham.getModel();
        loadDataToTable();
        fillToTable();
    }

     void refresh(){
        try{
            this.loadDataToTable();
            this.fillToTable();
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
    public void loadDataToTable(){
        try {
            STT = 1;
            list.clear();
            list = proDao.selectAll();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void fillToTable(){
        try {
            tablemodel.setRowCount(0);
            for(Product p : list){
                Object[] row = new Object[]{
                    STT++,p.getId(),p.getName(),p.getDescription(),p.getPrice()
                };
                tablemodel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    boolean searchId(String text)throws Exception{
        try {
           for(char c : text.toCharArray()){
               if(!Character.isDigit(c)){
                   return false;
               }
           }
        } catch (Exception e) {
            System.out.println("Khong tim thay sp: "+txtTimKiem.getText());
            e.printStackTrace();
        }
        return true;
    }
   void setForm(int i) {

        try {
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            TaoSanPhamJDialog tsp = new TaoSanPhamJDialog(frame, true);
            loadDataToTable();
            Product p = list.get(i);
            tsp.getTxtIDSanPham().setText(String.valueOf(p.getId()));
            tsp.getTxtTenSanPham().setText(p.getName());
            tsp.getTxtGia().setText(String.valueOf(p.getPrice()));
            if (p.getDescription() != null) {
                tsp.getLblPicture().setText(null);
                tsp.getLblPicture().setIcon(Ximage.read(p.getDescription()));
                tsp.getLblPicture().setToolTipText(p.getDescription());
                System.out.println("p setform:" + p);
            } else {
                tsp.getLblPicture().setIcon(null);
                tsp.getLblPicture().setText("Don't have image yet!");
            }
            if(p.getType().trim().equalsIgnoreCase("Đồ Ăn")){
                tsp.getCboType().setSelectedIndex(1);
            }
            else if(p.getType().trim().equalsIgnoreCase("Nước Uống")){
                tsp.getCboType().setSelectedIndex(0);
            }else{
                tsp.getCboType().setSelectedIndex(2);
            }
            tsp.setProductListener(new UpdateListener(){
            @Override
            public void onUpdate(){
                refresh();
            }
        });
            tsp.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
   void search(){
        // TODO add your handling code here:
         Product pro = new Product();
        try {
            if(txtTimKiem.getText().trim().equalsIgnoreCase("")){
                loadDataToTable();
                fillToTable();
            }else{
                if(searchId(txtTimKiem.getText())){
                    STT = 1;
                    list.clear();
                     pro = proDao.selectByID(Integer.parseInt(txtTimKiem.getText()));
                    if(pro != null){
                        list.add(pro);
                        System.out.println("Search for productId: "+pro.getId());
                        fillToTable();
                    }
                }else{
                    STT = 1;
                    list.clear();
                    list = proDao.searchByName(txtTimKiem.getText());
                    fillToTable();
                }
            }
        } catch (Exception e) {
            System.out.println("Khong tim duoc san pham ID: "+pro.getId()+" Ten: "+pro.getName());
        }
   }
 public void openDialog() {
        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        TaoSanPhamJDialog dialog = new TaoSanPhamJDialog(frame, true);
        dialog.setProductListener(new UpdateListener() {
            @Override
            public void onUpdate() {
                refresh();
            }
        });
        dialog.setVisible(true);
    }
    public void initTable(){
        // Tạo một Font mới với kiểu chữ "Times New Roman" và kích thước 24
        Font headerFont = new Font("Times New Roman", Font.BOLD, 20);

        // Lấy tiêu đề của bảng và đặt font mới
        JTableHeader tableHeader = tblSanPham.getTableHeader();
        tableHeader.setFont(headerFont);
        
          // Tạo một DefaultTableCellRenderer để căn giữa tiêu đề
        DefaultTableCellRenderer renderer = (DefaultTableCellRenderer) tableHeader.getDefaultRenderer();
        renderer.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        
        // Tạo một DefaultTableCellRenderer để căn phải cột thứ 5
        DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
        rightRenderer.setHorizontalAlignment(DefaultTableCellRenderer.RIGHT);
        
        tblSanPham.getColumnModel().getColumn(4).setCellRenderer(rightRenderer);

        // Đảm bảo rằng bảng và tiêu đề bảng được cập nhật
        tblSanPham.repaint();
        
    }
    public void Listener(){
        btnTaoSanPham.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnTaoSanPhamActionPerformed(evt);
                loadDataToTable();
                fillToTable();
            }
        });
    }
    private void initPopupMenu() {
        pup = new JPopupMenu();

        JMenuItem menuItem1 = new JMenuItem("Xem chi tiết");
        menuItem1.addActionListener(e -> itXemActionPerformed(e));
        pup.add(menuItem1);

        
    }

    private void addPopupToTable() {
        tblSanPham.addMouseListener(new MouseAdapter() {
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
                int row = tblSanPham.rowAtPoint(e.getPoint());
                int column = tblSanPham.columnAtPoint(e.getPoint());
                if (!tblSanPham.isRowSelected(row)) {
                    tblSanPham.changeSelection(row, column, false, false);
                }
                pup.show(e.getComponent(), e.getX(), e.getY());
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

        pup = new javax.swing.JPopupMenu();
        itXem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnTaoSanPham = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblSanPham = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        txtTimKiem = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();

        itXem.setText("Chi tiết");
        itXem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                itXemMouseClicked(evt);
            }
        });
        itXem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                itXemActionPerformed(evt);
            }
        });
        pup.add(itXem);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel1.setFont(new java.awt.Font("Times New Roman", 1, 24)); // NOI18N
        jLabel1.setText("Quản lý sản phẩm");

        btnTaoSanPham.setFont(new java.awt.Font("Times New Roman", 1, 18)); // NOI18N
        btnTaoSanPham.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ThemSanPham1_1.png"))); // NOI18N
        btnTaoSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTaoSanPhamMouseClicked(evt);
            }
        });
        btnTaoSanPham.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoSanPhamActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnTaoSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 21, Short.MAX_VALUE))
                    .addComponent(btnTaoSanPham, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        tblSanPham.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        tblSanPham.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "STT", "ID sản phẩm", "Tên sản phẩm", "Mô tả", "Giá bán"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, true, true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblSanPham.setRowHeight(32);
        tblSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblSanPhamMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblSanPham);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 2, 18)); // NOI18N
        jLabel2.setText("Danh sách sản phẩm: ");

        txtTimKiem.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/KinhLup.png"))); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 796, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnTaoSanPhamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoSanPhamActionPerformed
        // TODO add your handling code here:
        // Tạo và hiển thị JDialog từ JPanel
        openDialog();
    }//GEN-LAST:event_btnTaoSanPhamActionPerformed

    private void btnTaoSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTaoSanPhamMouseClicked
        // TODO add your handling code here:
       
    }//GEN-LAST:event_btnTaoSanPhamMouseClicked

    private void tblSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblSanPhamMouseClicked
        // TODO add your handling code here:
            
    }//GEN-LAST:event_tblSanPhamMouseClicked

    private void itXemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_itXemMouseClicked
        // TODO add your handling code here:
        
        
    }//GEN-LAST:event_itXemMouseClicked

    private void itXemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_itXemActionPerformed
        // TODO add your handling code here:
       try {
            current = tblSanPham.getSelectedRow();
           System.out.println(current);
            setForm(current);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//GEN-LAST:event_itXemActionPerformed

    private void txtTimKiemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTimKiemKeyPressed
       search();
    }//GEN-LAST:event_txtTimKiemKeyPressed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
            // TODO add your handling code here:
            search();
    }//GEN-LAST:event_jButton1ActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnTaoSanPham;
    private javax.swing.JMenuItem itXem;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu pup;
    private javax.swing.JTable tblSanPham;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
