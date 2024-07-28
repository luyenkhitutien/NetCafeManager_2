/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import utils.JdbcHelper;
import entity.Account;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO implements IBaseDAO<Account, Integer> {

    @Override
    public void insert(Account entity) throws Exception {
        String sql = "INSERT INTO account (username, password, role, createdAt) VALUES (?, ?, ?, ?)";
        int generatedID = JdbcHelper.executeInsertAndGetGeneratedKey(sql, entity.getUsername(), entity.getPassword(), entity.getRole(), entity.getCreatedAt());
        entity.setId(generatedID);
    }

    @Override
    public void update(Account entity) throws Exception {
        String sql = "UPDATE account SET username=?, password=?, role=?, createdAt=? WHERE ID=?";
        JdbcHelper.executeUpdate(sql, entity.getUsername(), entity.getPassword(), entity.getRole(), entity.getCreatedAt(), entity.getId());
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM account WHERE ID=?";
        JdbcHelper.executeUpdate(sql, id);
    }

    @Override
    public Account selectByID(Integer id) throws Exception {
        String sql = "SELECT * FROM account WHERE ID=?";
        List<Account> list = selectBySQL(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Account> selectAll() throws Exception {
        String sql = "SELECT * FROM account";
        return selectBySQL(sql);
    }

    @Override
    public List<Account> selectBySQL(String sql, Object... args) throws Exception {
        List<Account> list = new ArrayList<>();
        try (ResultSet rs = JdbcHelper.executeQuery(sql, args)) {
            while (rs.next()) {
                Account entity = new Account();
                entity.setId(rs.getInt("ID"));
                entity.setUsername(rs.getString("username"));
                entity.setPassword(rs.getString("password"));
                entity.setRole(rs.getString("role"));
                entity.setCreatedAt(rs.getTimestamp("createdAt"));
                list.add(entity);
            }
        }
        return list;
    }
    
    public Account selectByUsernameAndPassword(String username, String password) throws Exception {
        String sql = "SELECT * FROM account WHERE username=? AND password=?";
        List<Account> list = selectBySQL(sql, username, password);
        return list.isEmpty() ? null : list.get(0);
    }
}

