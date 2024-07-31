/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import java.awt.AWTException;
import java.awt.Component;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author doanc
 */
public class Xnoti {
    
    private static TrayIcon trayIcon;
    public static void msg(Component parent,String msg,String title){
        JOptionPane.showMessageDialog(parent, msg, title, 1);
    }
    
    public static boolean confirm(Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message,
                "NETCAFE", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_NO_OPTION;
    }
    public static void msd(Component parent,String msd,String title){
        JOptionPane.showMessageDialog(parent, msd, title, 0);
    }
       public static void showTrayMessage(String caption, String text, TrayIcon.MessageType messageType) {
        if (SystemTray.isSupported()) {
            if (trayIcon == null) {
                SystemTray tray = SystemTray.getSystemTray();
                Image image = Toolkit.getDefaultToolkit().createImage("icon.png");
                trayIcon = new TrayIcon(image, "App Notification");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("App Notification");

                try {
                    tray.add(trayIcon);
                } catch (AWTException e) {
                    System.err.println("TrayIcon could not be added.");
                }
            }
            trayIcon.displayMessage(caption, text, messageType);
        } else {
            System.err.println("SystemTray is not supported.");
        }
    }
       public void ThanhBinbucu(){
           System.out.println("Bin bú lồn");
           System.out.println("Test push 1 đoạn code");
       }
}
