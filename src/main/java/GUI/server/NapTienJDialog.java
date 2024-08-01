/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JDialog.java to edit this template
 */
package GUI.server;

import dao.MemberDAO;
import entity.Member;
import java.awt.TrayIcon;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.logging.Level;
import java.util.logging.Logger;
import main_server.MainTest;
import utils.Xnoti;

/**
 *
 * @author ASUS
 */
public class NapTienJDialog extends javax.swing.JDialog {
    
    public static Integer id;
    private MemberDAO memDao;
    private Member member;

    public NapTienJDialog(java.awt.Frame parent, boolean modal, Integer idMember) {
        super(parent, modal);
        initComponents();
        setLocationRelativeTo(null);
        NapTienJDialog.id = idMember;
    }

    private void napTien(Integer idMember) {
        try {
            memDao = new MemberDAO();
            member = memDao.selectByAccountID(idMember);

            BigDecimal soDuConLai = member.getBalance();
            BigDecimal soTienNap = new BigDecimal(txtNapTien.getText());

            soDuConLai = soDuConLai.add(soTienNap);
            member.setBalance(soDuConLai);
            memDao.update(member);

            // Hoàn tất nạp tiền
            Xnoti.msg(this, "Nạp tiền thành công", "Thông báo");
        } catch (NumberFormatException e) {
            Xnoti.msg(this, "Giá trị nhập vào không hợp lệ!", "Thông báo");
        } catch (Exception e) {
            Xnoti.msg(this, "Error: Nạp tiền không thành công!", "Thông báo");
        }
    }
    
    public void rechargeForClient() {
        //Gửi lệnh xử lý xuống cho client
        int con_clientID = MainTest.mainForm.home.selectedComputerId;
        if (con_clientID == 0) {
            Xnoti.showTrayMessage("Thông Báo!", "Client is offline", TrayIcon.MessageType.INFO);
            return;
        }
        try {
            MainTest.server.sendMessageToClient(con_clientID, "Recharges money for the client");
        } catch (IOException ex) {
            Logger.getLogger(NapTienJDialog.class.getName()).log(Level.SEVERE, null, ex);
        }
        Xnoti.showTrayMessage("Thông Báo!", "Successfully displays the real-time balance.", TrayIcon.MessageType.INFO);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtNapTien = new javax.swing.JTextField();
        txtOK = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setFont(new java.awt.Font("Source Code Pro", 1, 20)); // NOI18N
        jLabel1.setText("Nhập số tiền:");

        txtNapTien.setFont(new java.awt.Font("Dialog", 0, 18)); // NOI18N

        txtOK.setFont(new java.awt.Font("Dubai", 1, 11)); // NOI18N
        txtOK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/OK.png"))); // NOI18N
        txtOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtOKActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jLabel1)
                .addGap(36, 36, 36)
                .addComponent(txtNapTien, javax.swing.GroupLayout.PREFERRED_SIZE, 337, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                .addComponent(txtOK, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtOK, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtNapTien, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtOKActionPerformed
        // TODO add your handling code here:
        napTien(NapTienJDialog.id);
        rechargeForClient();
        this.setVisible(false);
    }//GEN-LAST:event_txtOKActionPerformed

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
            java.util.logging.Logger.getLogger(NapTienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NapTienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NapTienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NapTienJDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                NapTienJDialog dialog = new NapTienJDialog(new javax.swing.JFrame(), true,id);
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
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField txtNapTien;
    private javax.swing.JButton txtOK;
    // End of variables declaration//GEN-END:variables
}
