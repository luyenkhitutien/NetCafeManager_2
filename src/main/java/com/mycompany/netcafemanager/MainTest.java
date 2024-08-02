/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.netcafemanager;


import GUI.server.DangNhapJDialog;
import GUI.server.ServerMain;
import io.IOServer;
import java.io.IOException;
import test_server_client_GUI.ServerChatGUI;

/**
 *
 * @author Admin
 */
public class MainTest{
    public static int EMPLOYEE_ID;
    public static IOServer server;
    public static ServerChatGUI chatGUI;
    public static ServerMain mainForm;
    
        public static void main(String[] args) throws IOException, Exception {

        com.formdev.flatlaf.intellijthemes.FlatSolarizedLightIJTheme.setup();

        //GUI hiện lên
        mainForm = new ServerMain();
        DangNhapJDialog dangNhapForm = new DangNhapJDialog(mainForm, false);
        dangNhapForm.setVisible(true);
        
        //lấy employeeID cho server
        EMPLOYEE_ID = dangNhapForm.getEmployeeID();
        System.out.println("ID Nhan Vien: "+ EMPLOYEE_ID);
        server = new IOServer(EMPLOYEE_ID);
        
        MainTest.chatGUI = new ServerChatGUI(server);
        MainTest.server.setChatGUI(chatGUI);
        chatGUI.setVisible(false);
        
        //Bắt đầu khởi động server
        server.start();
    }
}
