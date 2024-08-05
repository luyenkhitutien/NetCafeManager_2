/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.server;

import dao.InvoiceDAO;
import dao.InvoiceDetailDAO;
import dao.ProductDAO;
import dao.SessionDAO;
import entity.Invoice;
import entity.InvoiceDetail;
import entity.Product;
import entity.Session;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import com.mycompany.netcafemanager.MainTest;
import utils.XInitTable;
import utils.Xnoti;

/**
 *
 * @author ASUS
 */
public class ChiTietHoaDonJDialog extends javax.swing.JDialog {

    private final int invoiceID;

    private int current = 0;
    private InvoiceDetail invoiceDetail;
    private Session session;
    private Product product;
    private Invoice invoice;
    private Boolean isConfirm = true;

    InvoiceDetailDAO invoiceDetailDAO = new InvoiceDetailDAO();
    InvoiceDAO invoiceDAO = new InvoiceDAO();
    SessionDAO sessionDAO = new SessionDAO();
    ProductDAO productDAO = new ProductDAO();

    List<InvoiceDetail> listInvoiceDetails = new ArrayList<>();
    List<Session> listSessions = new ArrayList<>();

    DefaultTableModel modelInvoiceDetail = new DefaultTableModel();
    DefaultTableModel modelSession = new DefaultTableModel();

    /**
     * Creates new form HoaDonChiTietJDialog
     */
    public ChiTietHoaDonJDialog(java.awt.Frame parent, boolean modal, int InvoiceID) {

        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        addPopupToTable();
        this.invoiceID = InvoiceID;
        fillToTable();
        autoFinish();

    }

    public void initTable() {
        XInitTable.initTable(tblInvoiceDetali, 5);
        XInitTable.initTable(tblSession, 6);

        int[] widths = {50, 125, 125, 300, 300};
        XInitTable.setColumnWidths(tblSession, widths);
    }

    private void addPopupToTable() {

        tblInvoiceDetali.addMouseListener(new MouseAdapter() {
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
                int row = tblInvoiceDetali.rowAtPoint(e.getPoint());
                int column = tblInvoiceDetali.columnAtPoint(e.getPoint());
                if (!tblInvoiceDetali.isRowSelected(row)) {
                    tblInvoiceDetali.changeSelection(row, column, false, false);
                }
                pup.show(e.getComponent(), e.getX(), e.getY());
            }
        });
    }

    private void loadDataToArray() {
        try {
            listInvoiceDetails = invoiceDetailDAO.selectByInvoiceID(this.invoiceID);
            listSessions = sessionDAO.selectByInvoiceID(this.invoiceID);

            //Continue...
        } catch (Exception ex) {
            Logger.getLogger(ChiTietHoaDonJDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void fillToTable() {

        this.loadDataToArray();
        //InvoiceDetailsTable
        modelInvoiceDetail = (DefaultTableModel) tblInvoiceDetali.getModel();
        modelInvoiceDetail.setRowCount(0);
        int STT_InvoiceDetail = 1;
        for (InvoiceDetail li : listInvoiceDetails) {
            try {
                product = productDAO.selectByID(li.getProductID());
            } catch (Exception ex) {
                Logger.getLogger(ChiTietHoaDonJDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            Object[] row = {
                STT_InvoiceDetail++,
                li.getId(),
                li.getInvoiceID(),
                product.getName(),
                li.getQuantity(),
                li.getPrice(),
                li.isCompleted()
            };
            modelInvoiceDetail.addRow(row);
        }

        //SessionsTable
        modelSession = (DefaultTableModel) tblSession.getModel();
        modelSession.setRowCount(0);
        int STT_Session = 1;
        for (Session s : listSessions) {
            Object[] row = {
                STT_Session++,
                s.getId(),
                s.getInvoiceID(),
                s.getComputerID(),
                s.getStartTime(),
                s.getEndTime(),
                s.getTotalAmount(),};
            modelSession.addRow(row);
        }
    }

    public boolean isFinish() {

        loadDataToArray();
        for (InvoiceDetail detail : listInvoiceDetails) {
            if (detail.isCompleted() == false) {
                return false;
            }
        }
        
        return true;
    }

    private void autoFinish() {
        if (isFinish()) {
            try {
                invoice = invoiceDAO.selectByID(invoiceID);
            } catch (Exception ex) {
                Logger.getLogger(ChiTietHoaDonJDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (invoice.getStatus().equalsIgnoreCase("Hoàn thành")) {
                return;
            }
            session = listSessions.get(0);
            if (session.getEndTime() != null && listInvoiceDetails.isEmpty()) {
                updataStatusInvoice();
                if(isConfirm){
                    Xnoti.msg(this, "Hóa đơn " + invoiceID + " không có ODER. Tự động hoàn thành", "Thông báo");
                }
                
            }
        }
    }

    public void updataStatusInvoice() {
        if (isFinish()) {
            try {
                invoice = invoiceDAO.selectByID(invoiceID);
                if (invoice.getStatus().equalsIgnoreCase("Hoàn thành")) {
                    return;
                }
                Integer memberID = invoice.getMemberID();
                if (memberID != null && memberID != 0) {
                    invoice.setStatus("Hoàn thành");
                    invoiceDAO.update(invoice);
                    MainTest.mainForm.managerInvoice.loadDataToArray();
                    MainTest.mainForm.managerInvoice.fillToTable();
                } else {
                    isConfirm = Xnoti.confirm(this, "Bạn có muốn xác nhận thanh toán cho vãng lai?");
                    if (isConfirm) {
                        invoice.setMemberID(null);
                        invoice.setStatus("Hoàn thành");
                        invoiceDAO.update(invoice);
                        MainTest.mainForm.managerInvoice.loadDataToArray();
                        MainTest.mainForm.managerInvoice.fillToTable();
                    }
                }

            } catch (Exception ex) {
                Logger.getLogger(ChiTietHoaDonJDialog.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void updateStatus() {

        invoiceDetail = new InvoiceDetail();
        current = tblInvoiceDetali.getSelectedRow();
        int invoiceDetailID = Integer.parseInt(tblInvoiceDetali.getValueAt(current, 1).toString());

        try {
            invoiceDetail = invoiceDetailDAO.selectByID(invoiceDetailID);
            invoiceDetail.setCompleted(true);
            invoiceDetailDAO.update(invoiceDetail);
            loadDataToArray();
            fillToTable();
            updataStatusInvoice();

        } catch (Exception ex) {
            Logger.getLogger(ChiTietHoaDonJDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    void printInvoice(){
        StringBuilder sb = new StringBuilder();
        TableModel modelInvoice = tblInvoiceDetali.getModel();
        TableModel modelSessions = tblSession.getModel();

        // Lấy ID hóa đơn từ dòng đầu tiên của bảng hóa đơn chi tiết
        String invoiceId = modelInvoice.getValueAt(0, 2).toString();
        sb.append("==============================================\n");
        sb.append("                  HÓA ĐƠN                     \n");
        sb.append("==============================================\n");
        sb.append(" ID hóa đơn:  ").append(invoiceId).append("\n");
        sb.append("==============================================\n");

        // Thêm tiêu đề phần chi tiết sản phẩm
        sb.append("Chi tiết sản phẩm:\n");
        sb.append(String.format("%-20s%-10s%-15s\n","Tên sản phẩm", "Số lượng", "Thành tiền"));
        // Lấy dữ liệu chi tiết sản phẩm, bỏ qua cột 0, 1 và 6
        for (int row = 0; row < modelInvoice.getRowCount(); row++) {
            sb.append(String.format("%-20s%-10s%-15s\n",
                    modelInvoice.getValueAt(row, 3),
                    modelInvoice.getValueAt(row, 4),
                    modelInvoice.getValueAt(row, 5)));
        }
        sb.append("==============================================\n");

         // Thêm tiêu đề phần phí sử dụng máy
        sb.append("Phí sử dụng máy:\n");
        sb.append("ID Máy tính: ");
        // Lấy dữ liệu phiên, bỏ qua cột 0, 1 và 2
        for (int row = 0; row < modelSessions.getRowCount(); row++) {
            sb.append(modelSessions.getValueAt(row, 3)).append("\nThời gian bắt đầu: ");
            sb.append(modelSessions.getValueAt(row, 4)).append("\nThời gian kết thúc: ");
            sb.append(modelSessions.getValueAt(row, 5)).append("\nTiền giờ chơi: ");
            sb.append(modelSessions.getValueAt(row, 6)).append("\n");
        }
        sb.append("==============================================\n");

        // Sử dụng JFileChooser để chọn nơi lưu trữ file
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Chọn nơi lưu file hóa đơn");
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileToSave+".pdf"))) {
                writer.write(sb.toString());
                JOptionPane.showMessageDialog(this, "In thành công hóa đơn.");
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi in file");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Hủy lưu file.");
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

        pup = new javax.swing.JPopupMenu();
        mnitThayDoiTrangThai = new javax.swing.JMenuItem();
        pnlChinh = new javax.swing.JPanel();
        pnlTitle = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlTableSessions = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSession = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        pnlTable = new javax.swing.JPanel();
        pnlTableHoaDoinChiTiet = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblInvoiceDetali = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        btnPrint = new javax.swing.JButton();

        mnitThayDoiTrangThai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/ThayDoi40x40.png"))); // NOI18N
        mnitThayDoiTrangThai.setText("Thay đổi trạng thái thanh toán");
        mnitThayDoiTrangThai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnitThayDoiTrangThaiActionPerformed(evt);
            }
        });
        pup.add(mnitThayDoiTrangThai);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/HoaDonChiTiet.png"))); // NOI18N

        javax.swing.GroupLayout pnlTitleLayout = new javax.swing.GroupLayout(pnlTitle);
        pnlTitle.setLayout(pnlTitleLayout);
        pnlTitleLayout.setHorizontalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTitleLayout.createSequentialGroup()
                .addGap(389, 389, 389)
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(36, 36, 36))
        );
        pnlTitleLayout.setVerticalGroup(
            pnlTitleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
        );

        tblSession.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N
        tblSession.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "ID Phiên sử dụng", "ID Hóa đơn", "ID Máy tính", "Thời gian bắt đầu", "Thời gian kết thúc", "Tiền giờ chơi"
            }
        ));
        tblSession.setRowHeight(30);
        jScrollPane2.setViewportView(tblSession);

        jLabel3.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jLabel3.setText("PHIÊN SỬ DỤNG:");

        javax.swing.GroupLayout pnlTableSessionsLayout = new javax.swing.GroupLayout(pnlTableSessions);
        pnlTableSessions.setLayout(pnlTableSessionsLayout);
        pnlTableSessionsLayout.setHorizontalGroup(
            pnlTableSessionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 1462, Short.MAX_VALUE)
            .addGroup(pnlTableSessionsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlTableSessionsLayout.setVerticalGroup(
            pnlTableSessionsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTableSessionsLayout.createSequentialGroup()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pnlTableHoaDoinChiTiet.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblInvoiceDetali.setFont(new java.awt.Font("Source Code Pro", 1, 14)); // NOI18N
        tblInvoiceDetali.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "ID", "ID hóa đơn", "Tên sản phẩm", "Số lượng", "Thành tiền", "Trạng thái"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblInvoiceDetali.setRowHeight(40);
        jScrollPane1.setViewportView(tblInvoiceDetali);

        jLabel2.setFont(new java.awt.Font("Times New Roman", 2, 24)); // NOI18N
        jLabel2.setText("ODER:");

        javax.swing.GroupLayout pnlTableHoaDoinChiTietLayout = new javax.swing.GroupLayout(pnlTableHoaDoinChiTiet);
        pnlTableHoaDoinChiTiet.setLayout(pnlTableHoaDoinChiTietLayout);
        pnlTableHoaDoinChiTietLayout.setHorizontalGroup(
            pnlTableHoaDoinChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTableHoaDoinChiTietLayout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        pnlTableHoaDoinChiTietLayout.setVerticalGroup(
            pnlTableHoaDoinChiTietLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTableHoaDoinChiTietLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36))
        );

        javax.swing.GroupLayout pnlTableLayout = new javax.swing.GroupLayout(pnlTable);
        pnlTable.setLayout(pnlTableLayout);
        pnlTableLayout.setHorizontalGroup(
            pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTableHoaDoinChiTiet, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlTableLayout.setVerticalGroup(
            pnlTableLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTableLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlTableHoaDoinChiTiet, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        btnPrint.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnPrint.setText("Print");
        btnPrint.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrintActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlChinhLayout = new javax.swing.GroupLayout(pnlChinh);
        pnlChinh.setLayout(pnlChinhLayout);
        pnlChinhLayout.setHorizontalGroup(
            pnlChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlTableSessions, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlChinhLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52))
        );
        pnlChinhLayout.setVerticalGroup(
            pnlChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChinhLayout.createSequentialGroup()
                .addComponent(pnlTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTable, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlTableSessions, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnPrint, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlChinh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void mnitThayDoiTrangThaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnitThayDoiTrangThaiActionPerformed
        updateStatus();
    }//GEN-LAST:event_mnitThayDoiTrangThaiActionPerformed

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrintActionPerformed
        // TODO add your handling code here:
        printInvoice();
    }//GEN-LAST:event_btnPrintActionPerformed

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
            java.util.logging.Logger.getLogger(ChiTietHoaDonJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ChiTietHoaDonJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ChiTietHoaDonJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ChiTietHoaDonJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ChiTietHoaDonJDialog dialog = new ChiTietHoaDonJDialog(new javax.swing.JFrame(), true, 1);
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
    private javax.swing.JButton btnPrint;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuItem mnitThayDoiTrangThai;
    private javax.swing.JPanel pnlChinh;
    private javax.swing.JPanel pnlTable;
    private javax.swing.JPanel pnlTableHoaDoinChiTiet;
    private javax.swing.JPanel pnlTableSessions;
    private javax.swing.JPanel pnlTitle;
    private javax.swing.JPopupMenu pup;
    private javax.swing.JTable tblInvoiceDetali;
    private javax.swing.JTable tblSession;
    // End of variables declaration//GEN-END:variables
}
