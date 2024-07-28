/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.math.BigDecimal;

/**
 *
 * @author Admin
 */
public class Member {
    private int id;
    private int accountID;
    private String name;
    private BigDecimal balance;

    public Member() {
    }

    public Member(int id, int accountID, String name, BigDecimal balance) {
        this.id = id;
        this.accountID = accountID;
        this.name = name;
        this.balance = balance;
    }

    public Member(int accountID, String name, BigDecimal balance) {
        this.accountID = accountID;
        this.name = name;
        this.balance = balance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 89 * hash + this.id;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Member other = (Member) obj;
        return this.id == other.id;
    }

    @Override
    public String toString() {
        return "Member{" + "id=" + id + ", accountID=" + accountID + ", name=" + name + ", balance=" + balance + '}';
    }

    
}

