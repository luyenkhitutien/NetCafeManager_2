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
public class Computer {
    private int id;
    private String name;
    private BigDecimal pricePerHour;
    private String type;
    private String status;

    public Computer() {
    }

    public Computer(int id, String name, BigDecimal pricePerHour, String type, String status) {
        this.id = id;
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.type = type;
        this.status = status;
    }

    public Computer(String name, BigDecimal pricePerHour, String type, String status) {
        this.name = name;
        this.pricePerHour = pricePerHour;
        this.type = type;
        this.status = status;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPricePerHour() {
        return pricePerHour;
    }

    public void setPricePerHour(BigDecimal pricePerHour) {
        this.pricePerHour = pricePerHour;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Computer{" + "id=" + id + ", name=" + name + ", pricePerHour=" + pricePerHour + ", type=" + type + ", status=" + status + '}';
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
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
        final Computer other = (Computer) obj;
        return this.id == other.id;
    }

    
}

