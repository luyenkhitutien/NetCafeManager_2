/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import utils.JdbcHelper;
import entity.InvoiceDetail;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDetailDAO implements IBaseDAO<InvoiceDetail, Integer> {

    @Override
    public void insert(InvoiceDetail entity) throws Exception {
        String sql = "INSERT INTO invoiceDetail (invoiceID, productID, quantity, price, completed) VALUES (?, ?, ?, ?, ?)";
        int generatedID = JdbcHelper.executeInsertAndGetGeneratedKey(sql, entity.getInvoiceID(), entity.getProductID(), entity.getQuantity(), entity.getPrice(), entity.isCompleted());
        entity.setId(generatedID);
    }

    @Override
    public void update(InvoiceDetail entity) throws Exception {
        String sql = "UPDATE invoiceDetail SET invoiceID=?, productID=?, quantity=?, price=?, completed = ? WHERE ID=?";
        JdbcHelper.executeUpdate(sql, entity.getInvoiceID(), entity.getProductID(), entity.getQuantity(), entity.getPrice(), entity.isCompleted(),entity.getId());
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM invoiceDetail WHERE ID=?";
        JdbcHelper.executeUpdate(sql, id);
    }

    @Override
    public InvoiceDetail selectByID(Integer id) throws Exception {
        String sql = "SELECT * FROM invoiceDetail WHERE ID=?";
        List<InvoiceDetail> list = selectBySQL(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<InvoiceDetail> selectAll() throws Exception {
        String sql = "SELECT * FROM invoiceDetail";
        return selectBySQL(sql);
    }

    @Override
    public List<InvoiceDetail> selectBySQL(String sql, Object... args) throws Exception {
        List<InvoiceDetail> list = new ArrayList<>();
        try (ResultSet rs = JdbcHelper.executeQuery(sql, args)) {
            while (rs.next()) {
                InvoiceDetail entity = new InvoiceDetail();
                entity.setId(rs.getInt("ID"));
                entity.setInvoiceID(rs.getInt("invoiceID"));
                entity.setProductID(rs.getInt("productID"));
                entity.setQuantity(rs.getInt("quantity"));
                entity.setPrice(rs.getBigDecimal("price"));
                entity.setCompleted(rs.getBoolean("completed"));
                list.add(entity);
            }
        }
        return list;
    }
    
    public List<InvoiceDetail> selectByInvoiceID(Integer id) throws Exception{
        String sql = "SELECT * FROM invoiceDetail WHERE invoiceID = ?";
        return selectBySQL(sql, id);
    }
}

