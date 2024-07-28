/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author Admin
 */
public class Session {
    private int id;
    private int computerID;
    private Date startTime;
    private Date endTime;
    private BigDecimal totalAmount;
    private Integer invoiceID;

    public Session() {
    }

    public Session(int id, int computerID, Date startTime, Date endTime, BigDecimal totalAmount, Integer invoiceID) {
        this.id = id;
        this.computerID = computerID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalAmount = totalAmount;
        this.invoiceID = invoiceID;
    }

    public Session(int computerID, Date startTime, Date endTime, BigDecimal totalAmount, Integer invoiceID) {
        this.computerID = computerID;
        this.startTime = startTime;
        this.endTime = endTime;
        this.totalAmount = totalAmount;
        this.invoiceID = invoiceID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getComputerID() {
        return computerID;
    }

    public void setComputerID(int computerID) {
        this.computerID = computerID;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getInvoiceID() {
        return invoiceID;
    }

    public void setInvoiceID(Integer invoiceID) {
        this.invoiceID = invoiceID;
    }

    @Override
    public String toString() {
        return "Session{" + "id=" + id + ", computerID=" + computerID + ", startTime=" + startTime + ", endTime=" + endTime + ", totalAmount=" + totalAmount + ", invoiceID=" + invoiceID + '}';
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + this.id;
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
        final Session other = (Session) obj;
        return this.id == other.id;
    }

    
}

