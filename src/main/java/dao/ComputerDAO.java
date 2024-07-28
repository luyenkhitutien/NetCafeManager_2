/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import utils.JdbcHelper;
import entity.Computer;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ComputerDAO implements IBaseDAO<Computer, Integer> {

    @Override
    public void insert(Computer entity) throws Exception {
        String sql = "INSERT INTO computer (name, pricePerHour, type, status) VALUES (?, ?, ?, ?)";
        int generatedID = JdbcHelper.executeInsertAndGetGeneratedKey(sql, entity.getName(), entity.getPricePerHour(), entity.getType(), entity.getStatus());
        entity.setId(generatedID);
    }

    @Override
    public void update(Computer entity) throws Exception {
        String sql = "UPDATE computer SET name=?, pricePerHour=?, type=?, status=? WHERE ID=?";
        JdbcHelper.executeUpdate(sql, entity.getName(), entity.getPricePerHour(), entity.getType(), entity.getStatus(), entity.getId());
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM computer WHERE ID=?";
        JdbcHelper.executeUpdate(sql, id);
    }

    @Override
    public Computer selectByID(Integer id) throws Exception {
        String sql = "SELECT * FROM computer WHERE ID=?";
        List<Computer> list = selectBySQL(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Computer> selectAll() throws Exception {
        String sql = "SELECT * FROM computer";
        return selectBySQL(sql);
    }

    @Override
    public List<Computer> selectBySQL(String sql, Object... args) throws Exception {
        List<Computer> list = new ArrayList<>();
        try (ResultSet rs = JdbcHelper.executeQuery(sql, args)) {
            while (rs.next()) {
                Computer entity = new Computer();
                entity.setId(rs.getInt("ID"));
                entity.setName(rs.getString("name"));
                entity.setPricePerHour(rs.getBigDecimal("pricePerHour"));
                entity.setType(rs.getString("type"));
                entity.setStatus(rs.getString("status"));
                list.add(entity);
            }
        }
        return list;
    }
}

