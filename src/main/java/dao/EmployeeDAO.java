/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import utils.JdbcHelper;
import entity.Employee;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDAO implements IBaseDAO<Employee, Integer> {

    @Override
    public void insert(Employee entity) throws Exception {
        String sql = "INSERT INTO employee (accountID, name, salaryPerHour, address, phoneNumber, otherInformation, balance) VALUES (?, ?, ?, ?, ?, ?, ?)";
        int generatedID = JdbcHelper.executeInsertAndGetGeneratedKey(sql, entity.getAccountID(), entity.getName(), entity.getSalaryPerHour(), entity.getAddress(), entity.getPhoneNumber(), entity.getOtherInformation(), entity.getBalance());
        entity.setId(generatedID);
    }

    @Override
    public void update(Employee entity) throws Exception {
        String sql = "UPDATE employee SET accountID=?, name=?, salaryPerHour=?, address=?, phoneNumber=?, otherInformation=?, balance=? WHERE ID=?";
        JdbcHelper.executeUpdate(sql, entity.getAccountID(), entity.getName(), entity.getSalaryPerHour(), entity.getAddress(), entity.getPhoneNumber(), entity.getOtherInformation(), entity.getBalance(), entity.getId());
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM employee WHERE ID=?";
        JdbcHelper.executeUpdate(sql, id);
    }

    @Override
    public Employee selectByID(Integer id) throws Exception {
        String sql = "SELECT * FROM employee WHERE ID=?";
        List<Employee> list = selectBySQL(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Employee> selectAll() throws Exception {
        String sql = "SELECT * FROM employee";
        return selectBySQL(sql);
    }

    @Override
    public List<Employee> selectBySQL(String sql, Object... args) throws Exception {
        List<Employee> list = new ArrayList<>();
        try (ResultSet rs = JdbcHelper.executeQuery(sql, args)) {
            while (rs.next()) {
                Employee entity = new Employee();
                entity.setId(rs.getInt("ID"));
                entity.setAccountID(rs.getInt("accountID"));
                entity.setName(rs.getString("name"));
                entity.setSalaryPerHour(rs.getBigDecimal("salaryPerHour"));
                entity.setAddress(rs.getString("address"));
                entity.setPhoneNumber(rs.getString("phoneNumber"));
                entity.setOtherInformation(rs.getString("otherInformation"));
                entity.setBalance(rs.getBigDecimal("balance"));
                list.add(entity);
            }
        }
        return list;
    }

    public Employee selectByAccountID(int accountID) throws Exception {
        String sql = "SELECT * FROM employee WHERE accountID=?";
        List<Employee> list = selectBySQL(sql, accountID);
        return list.isEmpty() ? null : list.get(0);
    }
}

