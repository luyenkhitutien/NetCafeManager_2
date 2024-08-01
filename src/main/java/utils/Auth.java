/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import dao.AccountDAO;
import entity.Account;

/**
 
 * @author doanc
 */
public class Auth {
    public static AccountDAO accDao = new AccountDAO();
    public static boolean isAdminLogin = false;
    public static boolean isAdmin (Account account)throws Exception{
        return !isEmployee(account) && !isMembers(account);
    }
    public static boolean isEmployee(Account account) throws Exception{
        if(account == null){
            return false;
        }
        return account.getRole().equalsIgnoreCase("Nhân viên");
    }
    public static boolean isMembers(Account account) throws Exception{
        if(account == null){
            return false;
        }
        return account.getRole().equalsIgnoreCase("Hội viên");
    }
    
}
