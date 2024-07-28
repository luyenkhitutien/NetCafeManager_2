/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import utils.JdbcHelper;
import entity.Session;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class SessionDAO implements IBaseDAO<Session, Integer> {

    @Override
    public void insert(Session entity) throws Exception {
        String sql = "INSERT INTO sessions (computerID, startTime, endTime, totalAmount, invoiceID) VALUES (?, ?, ?, ?, ?)";
        int generatedID = JdbcHelper.executeInsertAndGetGeneratedKey(sql, entity.getComputerID(), entity.getStartTime(), entity.getEndTime(), entity.getTotalAmount(), entity.getInvoiceID());
        entity.setId(generatedID);
    }

    @Override
    public void update(Session entity) throws Exception {
        String sql = "UPDATE sessions SET computerID=?, startTime=?, endTime=?, totalAmount=?, invoiceID=? WHERE ID=?";
        JdbcHelper.executeUpdate(sql, entity.getComputerID(), entity.getStartTime(), entity.getEndTime(), entity.getTotalAmount(), entity.getInvoiceID(), entity.getId());
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM sessions WHERE ID=?";
        JdbcHelper.executeUpdate(sql, id);
    }

    @Override
    public Session selectByID(Integer id) throws Exception {
        String sql = "SELECT * FROM sessions WHERE ID=?";
        List<Session> list = selectBySQL(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Session> selectAll() throws Exception {
        String sql = "SELECT * FROM sessions";
        return selectBySQL(sql);
    }

    @Override
    public List<Session> selectBySQL(String sql, Object... args) throws Exception {
        List<Session> list = new ArrayList<>();
        try (ResultSet rs = JdbcHelper.executeQuery(sql, args)) {
            while (rs.next()) {
                Session entity = new Session();
                entity.setId(rs.getInt("ID"));
                entity.setComputerID(rs.getInt("computerID"));
                entity.setStartTime(rs.getTimestamp("startTime"));
                entity.setEndTime(rs.getTimestamp("endTime"));
                entity.setTotalAmount(rs.getBigDecimal("totalAmount"));
                entity.setInvoiceID(rs.getInt("invoiceID"));
                list.add(entity);
            }
        }
        return list;
    }
    
    public List<Session> selectByInvoiceID(int invoiceID) throws Exception {
        String sql = "SELECT * FROM sessions WHERE invoiceID = ?";
        return selectBySQL(sql, invoiceID);
    }
}

