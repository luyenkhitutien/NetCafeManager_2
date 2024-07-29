/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import utils.JdbcHelper;
import entity.Member;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO implements IBaseDAO<Member, Integer> {

    @Override
    public void insert(Member entity) throws Exception {
        String sql = "INSERT INTO members (accountID, name, balance) VALUES (?, ?, ?)";
        int generatedID = JdbcHelper.executeInsertAndGetGeneratedKey(sql, entity.getAccountID(), entity.getName(), entity.getBalance());
        entity.setId(generatedID);
    }

    @Override
    public void update(Member entity) throws Exception {
        String sql = "UPDATE members SET accountID=?, name=?, balance=? WHERE ID=?";
        JdbcHelper.executeUpdate(sql, entity.getAccountID(), entity.getName(), entity.getBalance(), entity.getId());
    }
    public void NapTien(Integer id,Double money)throws Exception{
        String sql = "Update members set balance = ? where accountID =?";
        JdbcHelper.executeUpdate(sql, money ,id);
    }
    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM members WHERE ID=?";
        JdbcHelper.executeUpdate(sql, id);
    }

    @Override
    public Member selectByID(Integer id) throws Exception {
        String sql = "SELECT * FROM members WHERE ID=?";
        List<Member> list = selectBySQL(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Member> selectAll() throws Exception {
        String sql = "SELECT * FROM members";
        return selectBySQL(sql);
    }

    @Override
    public List<Member> selectBySQL(String sql, Object... args) throws Exception {
        List<Member> list = new ArrayList<>();
        try (ResultSet rs = JdbcHelper.executeQuery(sql, args)) {
            while (rs.next()) {
                Member entity = new Member();
                entity.setId(rs.getInt("ID"));
                entity.setAccountID(rs.getInt("accountID"));
                entity.setName(rs.getString("name"));
                entity.setBalance(rs.getBigDecimal("balance"));
                list.add(entity);
            }
        }
        return list;
    }
    
    public Member selectByAccountID(Integer accountId) throws Exception{
        String sql = "SELECT * FROM members WHERE accountID = ?";
        List<Member> list = selectBySQL(sql, accountId);
        return list.isEmpty() ? null : list.get(0);
    }
}
