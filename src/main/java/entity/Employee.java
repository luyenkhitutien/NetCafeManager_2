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
public class Employee {
    private int id;
    private int accountID;
    private String name;
    private BigDecimal salaryPerHour;
    private String address;
    private String phoneNumber;
    private String otherInformation;
    private BigDecimal balance;

    public Employee() {
    }

    public Employee(int id, int accountID, String name, BigDecimal salaryPerHour, String address, String phoneNumber, String otherInformation, BigDecimal balance) {
        this.id = id;
        this.accountID = accountID;
        this.name = name;
        this.salaryPerHour = salaryPerHour;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.otherInformation = otherInformation;
        this.balance = balance;
    }

    public Employee(int accountID, String name, BigDecimal salaryPerHour, String address, String phoneNumber, String otherInformation, BigDecimal balance) {
        this.accountID = accountID;
        this.name = name;
        this.salaryPerHour = salaryPerHour;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.otherInformation = otherInformation;
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

    public BigDecimal getSalaryPerHour() {
        return salaryPerHour;
    }

    public void setSalaryPerHour(BigDecimal salaryPerHour) {
        this.salaryPerHour = salaryPerHour;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getOtherInformation() {
        return otherInformation;
    }

    public void setOtherInformation(String otherInformation) {
        this.otherInformation = otherInformation;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Employee{" + "id=" + id + ", accountID=" + accountID + ", name=" + name + ", salaryPerHour=" + salaryPerHour + ", address=" + address + ", phoneNumber=" + phoneNumber + ", otherInformation=" + otherInformation + ", balance=" + balance + '}';
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 83 * hash + this.id;
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
        final Employee other = (Employee) obj;
        return this.id == other.id;
    }
 
}

