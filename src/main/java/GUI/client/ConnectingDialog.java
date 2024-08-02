/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI.client;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
/**
 *
 * @author Admin
 */


public class ConnectingDialog extends JDialog {
    public ConnectingDialog() {
        setTitle("Đang kết nối...");
        setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        setSize(300, 100);
        setLocationRelativeTo(null);
        
        JLabel label = new JLabel("Đang kết nối đến máy chủ. Vui lòng chờ...");
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);

        getContentPane().add(label, "North");
        getContentPane().add(progressBar, "Center");
    }

    public void showDialog() {
        SwingUtilities.invokeLater(() -> setVisible(true));
    }

    public void closeDialog() {
        SwingUtilities.invokeLater(() -> setVisible(false));
    }
}

