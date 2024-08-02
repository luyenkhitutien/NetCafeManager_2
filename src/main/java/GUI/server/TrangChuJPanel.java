/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.server;

import entity.Account;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.JLabel;
import main_server.MainTest;
import static main_server.MainTest.chatGUI;
import static main_server.MainTest.server;
import test_server_client_GUI.ServerChatGUI;
import utils.Xnoti;

/**
 *
 * @author ASUS
 */
public class TrangChuJPanel extends javax.swing.JPanel {

    private HashMap<Integer, JLabel> labelsMay;
    public int selectedComputerId = 0;

    /**
     * Creates new form TrangChuJPanel
     */
    public TrangChuJPanel() {
        labelsMay = new HashMap<>();
        initComponents();
        addLabel();
        addMouseListeners();
    }

    public void addLabel() {

        try {
            for (int i = 1; i <= 20; i++) {
                java.lang.reflect.Field field = getClass().getDeclaredField("lblmay" + i); // Lấy field của JLabel
                JLabel lblMay = (JLabel) field.get(this); // Lấy giá trị của JLabel từ field
                lblMay.setOpaque(true);
                lblMay.setBackground(this.getBackground()); // Đặt màu nền
                labelsMay.put(i, lblMay); // Đưa JLabel vào Map labelsMay
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
        }
    }

    public void updateLabelColor(int id, Color color) {
        JLabel lblMay = labelsMay.get(id);
        if (lblMay != null) {
            lblMay.setBackground(color);
        }

    }

    private void updatePopupMenuItems(int id, Color color) {
        if (color == Color.GREEN) {
            mnitMoMay.setVisible(false);
            mnitTatMay.setVisible(true);
            mnitTinNhan.setVisible(true);
            mnitNapTien.setVisible(true);
        } else if (color == Color.YELLOW) {
            mnitMoMay.setVisible(true);
            mnitTatMay.setVisible(true);
            mnitTinNhan.setVisible(false);
            mnitNapTien.setVisible(false);
        } else{
            mnitMoMay.setVisible(false);
            mnitTatMay.setVisible(false);
            mnitTinNhan.setVisible(false);
            mnitNapTien.setVisible(false);
            Xnoti.msg(this, "Máy "+ id +" chưa được khởi động!" , "Thông báo");
        }
    }

    private void OpenForGust() {
        try {

            if (selectedComputerId != 0) {
                MainTest.server.openForGuest(selectedComputerId);
            }else {
                Xnoti.msg(this, "Máy đang tắt", "Thông báo");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(TrangChuJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void ShutDownComputerID() {
        try {

            if (selectedComputerId != 0) {
                MainTest.server.closeClientByComputerID(selectedComputerId);
            } else {
                Xnoti.msg(this, "Máy đang tắt", "Thông báo");
            }

        } catch (IOException ex) {
            Logger.getLogger(TrangChuJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void message() {
        // Tạo form chat
        MainTest.chatGUI = new ServerChatGUI(server);
        MainTest.server.setChatGUI(chatGUI);
        MainTest.chatGUI.clientIdField.setText(String.valueOf(selectedComputerId));
    }

    private void recharge() {
        // Chưa có
    }

    private void addMouseListeners() {
        for (int i = 1; i <= 20; i++) {
            labelsMay.get(i).addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    labelMouseClicked(evt);
                }
            });
        }
    }

    private void labelMouseClicked(MouseEvent evt) {
        JLabel clickedLabel = (JLabel) evt.getSource();
        int id = -1;
        for (int i = 1; i <= 20; i++) {
            if (labelsMay.get(i) == clickedLabel) {
                id = i;
                break;
            }
        }
        if (id != -1) {
            Color color = clickedLabel.getBackground();
            updatePopupMenuItems(id, color);
            selectedComputerId = id;
            pupTrangChu.show(clickedLabel, evt.getX(), evt.getY());
        }
    }
    
    private void napTienFormTrue(){
        
        int con_clientID = MainTest.mainForm.home.selectedComputerId;
        Account acc = MainTest.server.getAccountByClientID(con_clientID);
        
        if(acc != null){
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            NapTienJDialog napTienJDialog = new NapTienJDialog(frame, true, acc.getId());
            napTienJDialog.setVisible(true);
        } else{
            Xnoti.msg(this, "Không thực hiện nạp tiền cho khách vãng lai", "Cảnh báo!!");
            return;
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

        pupTrangChu = new javax.swing.JPopupMenu();
        mnitMoMay = new javax.swing.JMenuItem();
        mnitTatMay = new javax.swing.JMenuItem();
        mnitNapTien = new javax.swing.JMenuItem();
        mnitTinNhan = new javax.swing.JMenuItem();
        HomePanel = new javax.swing.JPanel();
        lblmay1 = new javax.swing.JLabel();
        lblmay2 = new javax.swing.JLabel();
        lblmay3 = new javax.swing.JLabel();
        lblmay4 = new javax.swing.JLabel();
        lblmay5 = new javax.swing.JLabel();
        lblmay6 = new javax.swing.JLabel();
        lblmay8 = new javax.swing.JLabel();
        lblmay7 = new javax.swing.JLabel();
        lblmay9 = new javax.swing.JLabel();
        lblmay10 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        lblmay11 = new javax.swing.JLabel();
        lblmay12 = new javax.swing.JLabel();
        lblmay13 = new javax.swing.JLabel();
        lblmay14 = new javax.swing.JLabel();
        lblmay15 = new javax.swing.JLabel();
        lblmay16 = new javax.swing.JLabel();
        lblmay17 = new javax.swing.JLabel();
        lblmay18 = new javax.swing.JLabel();
        lblmay19 = new javax.swing.JLabel();
        lblmay20 = new javax.swing.JLabel();

        mnitMoMay.setText("Mở máy");
        mnitMoMay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnitMoMayActionPerformed(evt);
            }
        });
        pupTrangChu.add(mnitMoMay);

        mnitTatMay.setText("Tắt máy");
        mnitTatMay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnitTatMayActionPerformed(evt);
            }
        });
        pupTrangChu.add(mnitTatMay);

        mnitNapTien.setText("Nạp Tiền");
        mnitNapTien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnitNapTienActionPerformed(evt);
            }
        });
        pupTrangChu.add(mnitNapTien);

        mnitTinNhan.setText("Nhắn tin");
        mnitTinNhan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnitTinNhanActionPerformed(evt);
            }
        });
        pupTrangChu.add(mnitTinNhan);

        lblmay1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/1.png"))); // NOI18N
        HomePanel.add(lblmay1);

        lblmay2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/2.png"))); // NOI18N
        lblmay2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay2MouseClicked(evt);
            }
        });
        HomePanel.add(lblmay2);

        lblmay3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/3.png"))); // NOI18N
        lblmay3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay3MouseClicked(evt);
            }
        });
        HomePanel.add(lblmay3);

        lblmay4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/may4.png"))); // NOI18N
        lblmay4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay4MouseClicked(evt);
            }
        });
        HomePanel.add(lblmay4);

        lblmay5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/5.png"))); // NOI18N
        lblmay5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay5MouseClicked(evt);
            }
        });
        HomePanel.add(lblmay5);

        lblmay6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/6.png"))); // NOI18N
        lblmay6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay6MouseClicked(evt);
            }
        });
        HomePanel.add(lblmay6);

        lblmay8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/8.png"))); // NOI18N
        lblmay8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay8MouseClicked(evt);
            }
        });
        HomePanel.add(lblmay8);

        lblmay7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/7.png"))); // NOI18N
        lblmay7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay7MouseClicked(evt);
            }
        });
        HomePanel.add(lblmay7);

        lblmay9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/9.png"))); // NOI18N
        lblmay9.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay9MouseClicked(evt);
            }
        });
        HomePanel.add(lblmay9);

        lblmay10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/10.png"))); // NOI18N
        lblmay10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay10MouseClicked(evt);
            }
        });
        HomePanel.add(lblmay10);

        lblmay11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/11.png"))); // NOI18N
        lblmay11.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay11MouseClicked(evt);
            }
        });
        jPanel2.add(lblmay11);

        lblmay12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/12.png"))); // NOI18N
        lblmay12.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay12MouseClicked(evt);
            }
        });
        jPanel2.add(lblmay12);

        lblmay13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/13.png"))); // NOI18N
        lblmay13.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay13MouseClicked(evt);
            }
        });
        jPanel2.add(lblmay13);

        lblmay14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/14.png"))); // NOI18N
        lblmay14.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay14MouseClicked(evt);
            }
        });
        jPanel2.add(lblmay14);

        lblmay15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/15.png"))); // NOI18N
        lblmay15.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay15MouseClicked(evt);
            }
        });
        jPanel2.add(lblmay15);

        lblmay16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/16.png"))); // NOI18N
        lblmay16.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay16MouseClicked(evt);
            }
        });
        jPanel2.add(lblmay16);

        lblmay17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/17.png"))); // NOI18N
        lblmay17.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay17MouseClicked(evt);
            }
        });
        jPanel2.add(lblmay17);

        lblmay18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/18.png"))); // NOI18N
        lblmay18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay18MouseClicked(evt);
            }
        });
        jPanel2.add(lblmay18);

        lblmay19.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/19.png"))); // NOI18N
        lblmay19.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay19MouseClicked(evt);
            }
        });
        jPanel2.add(lblmay19);

        lblmay20.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/20.png"))); // NOI18N
        lblmay20.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblmay20MouseClicked(evt);
            }
        });
        jPanel2.add(lblmay20);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(HomePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(HomePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 202, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void lblmay2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay2MouseClicked

    }//GEN-LAST:event_lblmay2MouseClicked

    private void lblmay3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay3MouseClicked
        
    }//GEN-LAST:event_lblmay3MouseClicked

    private void lblmay4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay4MouseClicked

    }//GEN-LAST:event_lblmay4MouseClicked

    private void lblmay5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay5MouseClicked

    }//GEN-LAST:event_lblmay5MouseClicked

    private void lblmay6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay6MouseClicked
        
    }//GEN-LAST:event_lblmay6MouseClicked

    private void lblmay7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay7MouseClicked
        
    }//GEN-LAST:event_lblmay7MouseClicked

    private void lblmay8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay8MouseClicked
        
    }//GEN-LAST:event_lblmay8MouseClicked

    private void lblmay9MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay9MouseClicked
        
    }//GEN-LAST:event_lblmay9MouseClicked

    private void lblmay10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay10MouseClicked
        
    }//GEN-LAST:event_lblmay10MouseClicked

    private void lblmay11MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay11MouseClicked
        
    }//GEN-LAST:event_lblmay11MouseClicked

    private void lblmay12MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay12MouseClicked
        
    }//GEN-LAST:event_lblmay12MouseClicked

    private void lblmay13MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay13MouseClicked
       
    }//GEN-LAST:event_lblmay13MouseClicked

    private void lblmay14MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay14MouseClicked
       
    }//GEN-LAST:event_lblmay14MouseClicked

    private void lblmay15MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay15MouseClicked
        
    }//GEN-LAST:event_lblmay15MouseClicked

    private void lblmay16MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay16MouseClicked
        
    }//GEN-LAST:event_lblmay16MouseClicked

    private void lblmay17MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay17MouseClicked
        
    }//GEN-LAST:event_lblmay17MouseClicked

    private void lblmay18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay18MouseClicked
        
    }//GEN-LAST:event_lblmay18MouseClicked

    private void lblmay19MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay19MouseClicked
        
    }//GEN-LAST:event_lblmay19MouseClicked

    private void lblmay20MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblmay20MouseClicked
        
    }//GEN-LAST:event_lblmay20MouseClicked

    private void mnitMoMayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnitMoMayActionPerformed
        OpenForGust();

    }//GEN-LAST:event_mnitMoMayActionPerformed

    private void mnitTatMayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnitTatMayActionPerformed
        ShutDownComputerID();

    }//GEN-LAST:event_mnitTatMayActionPerformed

    private void mnitTinNhanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnitTinNhanActionPerformed

        message();
    }//GEN-LAST:event_mnitTinNhanActionPerformed

    private void mnitNapTienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnitNapTienActionPerformed
        // TODO add your handling code here:
        napTienFormTrue();
    }//GEN-LAST:event_mnitNapTienActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel HomePanel;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblmay1;
    private javax.swing.JLabel lblmay10;
    private javax.swing.JLabel lblmay11;
    private javax.swing.JLabel lblmay12;
    private javax.swing.JLabel lblmay13;
    private javax.swing.JLabel lblmay14;
    private javax.swing.JLabel lblmay15;
    private javax.swing.JLabel lblmay16;
    private javax.swing.JLabel lblmay17;
    private javax.swing.JLabel lblmay18;
    private javax.swing.JLabel lblmay19;
    private javax.swing.JLabel lblmay2;
    private javax.swing.JLabel lblmay20;
    private javax.swing.JLabel lblmay3;
    private javax.swing.JLabel lblmay4;
    private javax.swing.JLabel lblmay5;
    private javax.swing.JLabel lblmay6;
    private javax.swing.JLabel lblmay7;
    private javax.swing.JLabel lblmay8;
    private javax.swing.JLabel lblmay9;
    private javax.swing.JMenuItem mnitMoMay;
    private javax.swing.JMenuItem mnitNapTien;
    private javax.swing.JMenuItem mnitTatMay;
    private javax.swing.JMenuItem mnitTinNhan;
    private javax.swing.JPopupMenu pupTrangChu;
    // End of variables declaration//GEN-END:variables

}
