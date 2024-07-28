/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import utils.JdbcHelper;
import entity.Invoice;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO implements IBaseDAO<Invoice, Integer> {

    @Override
    public void insert(Invoice entity) throws Exception {
        String sql = "INSERT INTO invoice (memberID, employeeID, totalAmount, createdAt, status) VALUES (?, ?, ?, ?, ?)";
        int generatedID = JdbcHelper.executeInsertAndGetGeneratedKey(sql, entity.getMemberID(), entity.getEmployeeID(), entity.getTotalAmount(), entity.getCreatedAt(), entity.getStatus());
        entity.setId(generatedID);
    }

    @Override
    public void update(Invoice entity) throws Exception {
        String sql = "UPDATE invoice SET memberID=?, employeeID=?, totalAmount=?, createdAt=?, status =? WHERE ID=?";
        JdbcHelper.executeUpdate(sql, entity.getMemberID(), entity.getEmployeeID(), entity.getTotalAmount(), entity.getCreatedAt(), entity.getStatus(), entity.getId());
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM invoice WHERE ID=?";
        JdbcHelper.executeUpdate(sql, id);
    }

    @Override
    public Invoice selectByID(Integer id) throws Exception {
        String sql = "SELECT * FROM invoice WHERE ID=?";
        List<Invoice> list = selectBySQL(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Invoice> selectAll() throws Exception {
        String sql = "SELECT * FROM invoice";
        return selectBySQL(sql);
    }

    @Override
    public List<Invoice> selectBySQL(String sql, Object... args) throws Exception {
        List<Invoice> list = new ArrayList<>();
        try (ResultSet rs = JdbcHelper.executeQuery(sql, args)) {
            while (rs.next()) {
                Invoice entity = new Invoice();
                entity.setId(rs.getInt("ID"));
                entity.setMemberID(rs.getInt("memberID"));
                entity.setEmployeeID(rs.getInt("employeeID"));
                entity.setTotalAmount(rs.getBigDecimal("totalAmount"));
                entity.setCreatedAt(rs.getTimestamp("createdAt"));
                entity.setStatus(rs.getString("status"));
                list.add(entity);
            }
        }
        return list;
    }
}
