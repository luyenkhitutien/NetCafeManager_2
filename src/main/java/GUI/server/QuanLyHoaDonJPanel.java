/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.server;

import dao.AccountDAO;
import dao.ComputerDAO;
import dao.EmployeeDAO;
import dao.InvoiceDAO;
import dao.InvoiceDetailDAO;
import dao.SessionDAO;
import dao.StatisticsDAO;
import entity.Account;
import entity.Computer;
import entity.Employee;
import entity.Invoice;
import entity.InvoiceDetail;
import entity.Session;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import utils.Auth;
import utils.XInitTable;

/**
 *
 * @author ASUS
 */
public class QuanLyHoaDonJPanel extends javax.swing.JPanel {

    StatisticsDAO Statistics = new StatisticsDAO();
    ChiTietHoaDonJDialog hoaDonChiTietJDialog;
    private int current = -1;
    private Invoice invoice;
    ComputerDAO comDao = new ComputerDAO();
    EmployeeDAO emDao = new EmployeeDAO();
    AccountDAO accDao = new AccountDAO();
    InvoiceDAO invoiceDAO = new InvoiceDAO();
    List<Invoice> list = new ArrayList<>();
    DefaultTableModel model = new DefaultTableModel();
    int index;

    /**
     * Creates new form QuanLyHoaDonJPanel
     */
    public QuanLyHoaDonJPanel() {

        initComponents();
        initTable();
        fillToTableNotFinish();
        addPopupToTable();
    }

    private void initTable() {
        XInitTable.initTable(tblQuanLyHoaDon, 6);

        int[] widths = {50, 80, 80, 80, 210, 140};
        XInitTable.setColumnWidths(tblQuanLyHoaDon, widths);
    }

    private void addPopupToTable() {
        tblQuanLyHoaDon.addMouseListener(new MouseAdapter() {
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
                int row = tblQuanLyHoaDon.rowAtPoint(e.getPoint());
                int column = tblQuanLyHoaDon.columnAtPoint(e.getPoint());
                if (!tblQuanLyHoaDon.isRowSelected(row)) {
                    tblQuanLyHoaDon.changeSelection(row, column, false, false);
                }
                if (Auth.isAdminLogin == false) {
                    mnitXoa.setVisible(false);
                    pup.show(e.getComponent(), e.getX(), e.getY());
                } else {
                    mnitXoa.setVisible(true);
                    pup.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
    }

    public void loadDataToArray() {

        try {
            list = invoiceDAO.selectAll();
            sortInvoicesByStatus();
        } catch (Exception ex) {
            Logger.getLogger(QuanLyHoaDonJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void sortInvoicesByStatus() {
        Collections.sort(list, new Comparator<Invoice>() {
            @Override
            public int compare(Invoice o1, Invoice o2) {
                // Hóa đơn chưa hoàn thành lên trên
                if (o1.getStatus().equalsIgnoreCase("Chưa hoàn thành") && o2.getStatus().equalsIgnoreCase("Hoàn thành")) {
                    return -1;
                }
                // Hóa đơn hoàn thành xuống dưới
                if (o1.getStatus().equalsIgnoreCase("Hoàn thành") && o2.getStatus().equalsIgnoreCase("Chưa hoàn thành")) {
                    return 1;
                }
                // Giữ nguyên thứ tự nếu cả hai cùng trạng thái
                return 0;
            }
        });
    }

    public void fillToTable() {
        model = (DefaultTableModel) tblQuanLyHoaDon.getModel();
        model.setRowCount(0);
        int STT = 1;
        for (Invoice i : list) {
            Object[] row = {
                STT++,
                i.getId(),
                i.getEmployeeID(),
                i.getMemberID(),
                i.getCreatedAt(),
                i.getStatus(),
                i.getTotalAmount()
            };
            model.addRow(row);
        }
        totalAmount();

    }

    //Hóa đơn Chưa hoàn thành
    private void fillToTableNotFinish() {

        loadDataToArray();
        cboChonMay.removeAllItems();
        cboNhanVien.removeAllItems();
        cboTenTaiKhoan.removeAllItems();
        List<Invoice> tempList = new ArrayList<>();
        for (Invoice i : list) {
            if (!i.getStatus().equalsIgnoreCase("Hoàn thành")) {
                tempList.add(i);
            }
        }

        list = tempList;
        fillToTable();

    }

    private void updateStatus() {

    }

    private void selectedInvoiceDetailsByRow() {

        current = tblQuanLyHoaDon.getSelectedRow();
        invoice = list.get(current);

        JFrame jFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        hoaDonChiTietJDialog = new ChiTietHoaDonJDialog(jFrame, true, invoice.getId());
        hoaDonChiTietJDialog.setVisible(true);
    }

    private void totalAmount() {

        BigDecimal price = BigDecimal.ZERO;

        for (Invoice i : list) {
            if (i.getStatus().equalsIgnoreCase("Hoàn thành")) {
                price = price.add(i.getTotalAmount());
            }
        }

        NumberFormat numberFormat = NumberFormat.getInstance(new Locale("vi", "VN"));
        String format = numberFormat.format(price.doubleValue());

        String totalAmouts = (format + " VND");
        lblTongTien.setText(String.valueOf(totalAmouts));

    }

    void fillCboComputer() {
        try {
            DefaultComboBoxModel cboComputer = (DefaultComboBoxModel) cboChonMay.getModel();
            cboComputer.removeAllElements();
            Set<String> computerName = new HashSet<>();
            List<Computer> listCom = comDao.selectAll();
            for (Computer p : listCom) {
                computerName.add(p.getName());
            }
            for (String computerNames : computerName) {
                cboComputer.addElement(computerNames);
            }
            System.out.println("Fill thành công combox Computer");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void fillCboUserName() {
        try {
            DefaultComboBoxModel cboUserName = (DefaultComboBoxModel) cboTenTaiKhoan.getModel();
            cboUserName.removeAllElements();
            Set<String> userName = new HashSet<>();
            List<Account> listAcc = accDao.selectAll();
            for (Account a : listAcc) {
                userName.add(a.getUsername());
            }
            for (String userNames : userName) {
                cboUserName.addElement(userNames);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fillCboIdEmployee() {
        try {

            DefaultComboBoxModel cboIdEmployee = (DefaultComboBoxModel) cboNhanVien.getModel();
            cboIdEmployee.removeAllElements();

            Set<Integer> idEmployee = new HashSet<>();

            List<Employee> listEm = emDao.selectAll();

            for (Employee e : listEm) {
                idEmployee.add(e.getId());
            }
            // fillToCbo

            for (Integer idEmployees : idEmployee) {
                cboIdEmployee.addElement(idEmployees);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void openInvoiceDetails() {
        int idHoaDon = (int) tblQuanLyHoaDon.getValueAt(index, 1);
        System.out.println("Lay thanh cong idHd: " + idHoaDon);
        index = tblQuanLyHoaDon.getSelectedRow();
        try {
            loadDataToArray();
            invoice = invoiceDAO.selectByID(idHoaDon);
            System.out.println("QLHD =>" + invoice);
            try {
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
                ChiTietHoaDonJDialog hdct = new ChiTietHoaDonJDialog(frame, true, idHoaDon);
                hdct.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("QLHD => loi open hdct");
        }
    }

    void getStatisticsSearchInvoice(Date dayBegin, Date dayEnd, String computerName, String userName, Integer idEmployee) {
        List<Object[]> data = Statistics.getStatisticsInvoice(dayBegin, dayEnd, computerName, userName, idEmployee);
        model.setRowCount(0);
        int STT = 1;
        for (Object[] row : data) {
            Object[] newRow = new Object[row.length + 1];
            newRow[0] = STT++;
            System.arraycopy(row, 0, newRow, 1, row.length); // Correct usage of arraycopy
            model.addRow(newRow);
        }
    }

    void search() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startAt = new Date(sdf.parse(txtTuNgay.getText()).getTime());
            System.out.println("dateBegin => :" + startAt);
            Date endAt = new Date(sdf.parse(txtDenNgay.getText()).getTime() + 24 * 60 * 60 * 1000 - 1); // set end of the day
            System.out.println("dateEnd => :" + endAt);
            String computerName = (String) cboChonMay.getSelectedItem();
            System.out.println("Lay duoc computer name => :" + computerName);
            String userName = (String) cboTenTaiKhoan.getSelectedItem();
            System.out.println("Lay duoc userName => :" + userName);
            Integer idEmployee = null;
            if (cboNhanVien.getSelectedItem() != null) {
                idEmployee = Integer.parseInt(cboNhanVien.getSelectedItem().toString());
                System.out.println("Lay duoc idEmployee => :" + idEmployee);
            }

            getStatisticsSearchInvoice(startAt, endAt, computerName, userName, idEmployee);
        } catch (ParseException e) {
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

        pup = new javax.swing.JPopupMenu();
        mnitHoaDonChiTiet = new javax.swing.JMenuItem();
        mnitXoa = new javax.swing.JMenuItem();
        pnlChinh = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlTimKiem = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtTuNgay = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtDenNgay = new javax.swing.JTextField();
        btnLamMoi = new javax.swing.JButton();
        btnTimKiem = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        cboChonMay = new javax.swing.JComboBox<>();
        cboTenTaiKhoan = new javax.swing.JComboBox<>();
        cboNhanVien = new javax.swing.JComboBox<>();
        chkComputer = new javax.swing.JCheckBox();
        chkUsername = new javax.swing.JCheckBox();
        chkIdEmployee = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblQuanLyHoaDon = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        lblTongTien = new javax.swing.JLabel();
        btnHoaDonChuaXuli = new javax.swing.JButton();
        btnHienTatCa = new javax.swing.JButton();

        mnitHoaDonChiTiet.setText("Hóa đơn chi tiết");
        mnitHoaDonChiTiet.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnitHoaDonChiTietMouseClicked(evt);
            }
        });
        mnitHoaDonChiTiet.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnitHoaDonChiTietActionPerformed(evt);
            }
        });
        pup.add(mnitHoaDonChiTiet);

        mnitXoa.setText("Xóa");
        mnitXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnitXoaActionPerformed(evt);
            }
        });
        pup.add(mnitXoa);

        jLabel1.setFont(new java.awt.Font("Source Code Pro", 1, 24)); // NOI18N
        jLabel1.setText("Quản lý hóa đơn");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addContainerGap())
        );

        pnlTimKiem.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jLabel2.setFont(new java.awt.Font("Source Code Pro", 1, 20)); // NOI18N
        jLabel2.setText("Tìm kiếm");

        jLabel3.setFont(new java.awt.Font("Source Code Pro", 2, 18)); // NOI18N
        jLabel3.setText("Từ ngày:");

        txtTuNgay.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        jLabel4.setFont(new java.awt.Font("Source Code Pro", 2, 18)); // NOI18N
        jLabel4.setText("Đến ngày:");

        txtDenNgay.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        txtDenNgay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDenNgayActionPerformed(evt);
            }
        });

        btnLamMoi.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        btnLamMoi.setText("Làm mới FORM");

        btnTimKiem.setFont(new java.awt.Font("Source Code Pro", 1, 18)); // NOI18N
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Source Code Pro", 2, 18)); // NOI18N
        jLabel5.setText("Chọn máy:");

        jLabel6.setFont(new java.awt.Font("Source Code Pro", 2, 18)); // NOI18N
        jLabel6.setText("Tên tài khoản:");

        jLabel7.setFont(new java.awt.Font("Source Code Pro", 2, 18)); // NOI18N
        jLabel7.setText("Nhân viên:");

        cboChonMay.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cboChonMay.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboTenTaiKhoan.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cboTenTaiKhoan.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cboNhanVien.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N
        cboNhanVien.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        chkComputer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkComputerActionPerformed(evt);
            }
        });

        chkUsername.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkUsernameActionPerformed(evt);
            }
        });

        chkIdEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkIdEmployeeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlTimKiemLayout = new javax.swing.GroupLayout(pnlTimKiem);
        pnlTimKiem.setLayout(pnlTimKiemLayout);
        pnlTimKiemLayout.setHorizontalGroup(
            pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimKiemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTimKiemLayout.createSequentialGroup()
                        .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnLamMoi)
                            .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(txtTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel3)))
                        .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlTimKiemLayout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel4)
                                    .addComponent(txtDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 181, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTimKiemLayout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(28, 28, 28))))
                    .addGroup(pnlTimKiemLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(43, 43, 43)
                        .addComponent(cboChonMay, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chkComputer))))
            .addGroup(pnlTimKiemLayout.createSequentialGroup()
                .addGap(173, 173, 173)
                .addComponent(jLabel2))
            .addGroup(pnlTimKiemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(18, 18, 18)
                .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlTimKiemLayout.createSequentialGroup()
                        .addComponent(cboNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chkIdEmployee))
                    .addGroup(pnlTimKiemLayout.createSequentialGroup()
                        .addComponent(cboTenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chkUsername))))
        );
        pnlTimKiemLayout.setVerticalGroup(
            pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTimKiemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(38, 38, 38)
                .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlTimKiemLayout.createSequentialGroup()
                        .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlTimKiemLayout.createSequentialGroup()
                                .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtTuNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtDenNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(23, 23, 23)
                                .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkComputer, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel5)
                                        .addComponent(cboChonMay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(20, 20, 20)
                                .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(cboTenTaiKhoan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(chkUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(26, 26, 26)
                        .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(cboNhanVien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(chkIdEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(48, 48, 48)
                .addGroup(pnlTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(627, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        tblQuanLyHoaDon.setFont(new java.awt.Font("Source Code Pro", 0, 16)); // NOI18N
        tblQuanLyHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "STT", "ID Hóa đơn", "ID Nhân viên", "ID Hội viên", "Ngày tạo", "Trạng thái", "Tổng tiền"
            }
        ));
        tblQuanLyHoaDon.setRowHeight(24);
        tblQuanLyHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblQuanLyHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblQuanLyHoaDon);

        jLabel8.setFont(new java.awt.Font("Source Code Pro", 2, 20)); // NOI18N
        jLabel8.setText("Danh sách hóa đơn:");

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel9.setText("Tổng tiền:");

        lblTongTien.setFont(new java.awt.Font("Dialog", 2, 18)); // NOI18N
        lblTongTien.setText("0.00đ");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel9)
                .addGap(38, 38, 38)
                .addComponent(lblTongTien, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(lblTongTien)))
        );

        btnHoaDonChuaXuli.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        btnHoaDonChuaXuli.setText("Hiện chưa xử lý");
        btnHoaDonChuaXuli.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.green, java.awt.Color.green, java.awt.Color.green, java.awt.Color.green));
        btnHoaDonChuaXuli.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHoaDonChuaXuliActionPerformed(evt);
            }
        });

        btnHienTatCa.setFont(new java.awt.Font("Source Code Pro", 1, 16)); // NOI18N
        btnHienTatCa.setText("Hiện tất cả");
        btnHienTatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHienTatCaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnHienTatCa)
                .addGap(28, 28, 28)
                .addComponent(btnHoaDonChuaXuli, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 778, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel8)
                    .addComponent(btnHienTatCa, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(btnHoaDonChuaXuli, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 915, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout pnlChinhLayout = new javax.swing.GroupLayout(pnlChinh);
        pnlChinh.setLayout(pnlChinhLayout);
        pnlChinhLayout.setHorizontalGroup(
            pnlChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(pnlChinhLayout.createSequentialGroup()
                .addComponent(pnlTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlChinhLayout.setVerticalGroup(
            pnlChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChinhLayout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlChinhLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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

    private void txtDenNgayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDenNgayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDenNgayActionPerformed

    private void mnitHoaDonChiTietActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnitHoaDonChiTietActionPerformed
        openInvoiceDetails();
    }//GEN-LAST:event_mnitHoaDonChiTietActionPerformed

    private void tblQuanLyHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQuanLyHoaDonMouseClicked
        // TODO add your handling code here:
        if (SwingUtilities.isLeftMouseButton(evt)) {
            index = tblQuanLyHoaDon.getSelectedRow();
            int idHd = (int) tblQuanLyHoaDon.getValueAt(index, 1);
            System.out.println("Row: " + index + " IdHd: " + idHd);
        } else {
            index = tblQuanLyHoaDon.getSelectedRow();
            int idHd = (int) tblQuanLyHoaDon.getValueAt(index, 1);
            System.out.println(index + " IdHd: " + idHd);
        }
    }//GEN-LAST:event_tblQuanLyHoaDonMouseClicked

    private void mnitXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnitXoaActionPerformed
        // TODO add your handling code here:
        index = tblQuanLyHoaDon.getSelectedRow();
        int idHd = (int) tblQuanLyHoaDon.getValueAt(index, 1);
        int comfirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xóa?", "Thay đổi dữ liệu", JOptionPane.YES_NO_OPTION);
        try {
            InvoiceDetailDAO iDetailDao = new InvoiceDetailDAO();
            InvoiceDetail iDetail = new InvoiceDetail();
            SessionDAO sesDao = new SessionDAO();
            Session sess = new Session();
            if (comfirm == JOptionPane.YES_OPTION) {
                if (sess == null && iDetail == null) {
                    invoiceDAO.delete(idHd);
                    System.out.println("Xóa hóa đơn có id: " + idHd);
                } else if (sess == null && iDetail != null) {
                    iDetailDao.delete(iDetail.getId());
                    invoiceDAO.delete(idHd);
                    System.out.println("Xoa thanh cong HDCT => Id: " + iDetail.getId());
                } else if (sess != null && iDetail == null) {
                    sesDao.delete(sess.getId());
                    invoiceDAO.delete(idHd);
                    System.out.println("Xoa thanh cong Session => Id: " + sess.getId());
                } else {
                    iDetailDao.delete(iDetail.getId());
                    sesDao.delete(sess.getId());
                    invoiceDAO.delete(idHd);
                }
                loadDataToArray();
                fillToTable();
            }
        } catch (Exception e) {
            System.out.println("xoa khong thanhg cong");
            e.printStackTrace();
        }
    }//GEN-LAST:event_mnitXoaActionPerformed

    private void mnitHoaDonChiTietMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnitHoaDonChiTietMouseClicked
        // TODO add your handling code here:

    }//GEN-LAST:event_mnitHoaDonChiTietMouseClicked

    private void btnHienTatCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHienTatCaActionPerformed
        // TODO add your handling code here:
        loadDataToArray();
        fillToTable();
    }//GEN-LAST:event_btnHienTatCaActionPerformed

    private void btnHoaDonChuaXuliActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHoaDonChuaXuliActionPerformed
        // TODO add your handling code here:
        fillToTableNotFinish();
    }//GEN-LAST:event_btnHoaDonChuaXuliActionPerformed

    private void chkComputerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkComputerActionPerformed
        // TODO add your handling code here:
        if (chkComputer.isSelected()) {
            // Nếu checkbox được chọn, thực hiện việc điền dữ liệu vào ComboBox
            fillCboComputer();
        } else {
            // Nếu checkbox không được chọn, xoá tất cả các mục trong ComboBox
            cboChonMay.removeAllItems();
        }
    }//GEN-LAST:event_chkComputerActionPerformed

    private void chkUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkUsernameActionPerformed
        // TODO add your handling code here:
        if (chkUsername.isSelected()) {
            fillCboUserName();
        } else {
            cboTenTaiKhoan.removeAllItems();
        }
    }//GEN-LAST:event_chkUsernameActionPerformed

    private void chkIdEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkIdEmployeeActionPerformed
        // TODO add your handling code here:
        if (chkIdEmployee.isSelected()) {
            fillCboIdEmployee();
        } else {
            cboNhanVien.removeAllItems();
        }
    }//GEN-LAST:event_chkIdEmployeeActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        try {
            search();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }//GEN-LAST:event_btnTimKiemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnHienTatCa;
    private javax.swing.JButton btnHoaDonChuaXuli;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JComboBox<String> cboChonMay;
    private javax.swing.JComboBox<String> cboNhanVien;
    private javax.swing.JComboBox<String> cboTenTaiKhoan;
    private javax.swing.JCheckBox chkComputer;
    private javax.swing.JCheckBox chkIdEmployee;
    private javax.swing.JCheckBox chkUsername;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTongTien;
    private javax.swing.JMenuItem mnitHoaDonChiTiet;
    private javax.swing.JMenuItem mnitXoa;
    private javax.swing.JPanel pnlChinh;
    private javax.swing.JPanel pnlTimKiem;
    private javax.swing.JPopupMenu pup;
    private javax.swing.JTable tblQuanLyHoaDon;
    private javax.swing.JTextField txtDenNgay;
    private javax.swing.JTextField txtTuNgay;
    // End of variables declaration//GEN-END:variables
}
