/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import utils.JdbcHelper;
import entity.Product;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements IBaseDAO<Product, Integer> {

    @Override
    public void insert(Product entity) throws Exception {
        String sql = "INSERT INTO product (name, price, description, type) VALUES (?, ?, ?, ?)";
        int generatedID = JdbcHelper.executeInsertAndGetGeneratedKey(sql, entity.getName(), entity.getPrice(), entity.getDescription(), entity.getType());
        entity.setId(generatedID);
    }

    @Override
    public void update(Product entity) throws Exception {
        String sql = "UPDATE product SET name=?, price=?, description=?, type=? WHERE ID=?";
        JdbcHelper.executeUpdate(sql, entity.getName(), entity.getPrice(), entity.getDescription(), entity.getType(), entity.getId());
    }

    @Override
    public void delete(Integer id) throws Exception {
        String sql = "DELETE FROM product WHERE ID=?";
        JdbcHelper.executeUpdate(sql, id);
    }

    @Override
    public Product selectByID(Integer id) throws Exception {
        String sql = "SELECT * FROM product WHERE ID=?";
        List<Product> list = selectBySQL(sql, id);
        return list.isEmpty() ? null : list.get(0);
    }

    @Override
    public List<Product> selectAll() throws Exception {
        String sql = "SELECT * FROM product";
        return selectBySQL(sql);
    }

    @Override
    public List<Product> selectBySQL(String sql, Object... args) throws Exception {
        List<Product> list = new ArrayList<>();
        try (ResultSet rs = JdbcHelper.executeQuery(sql, args)) {
            while (rs.next()) {
                Product entity = new Product();
                entity.setId(rs.getInt("ID"));
                entity.setName(rs.getString("name"));
                entity.setPrice(rs.getBigDecimal("price"));
                entity.setDescription(rs.getString("description"));
                entity.setType(rs.getString("type"));
                list.add(entity);
            }
        }
        return list;
    }
    
    public Product selectByName(String name) throws Exception{
        String sql = "SELECT * FROM product WHERE name = ?";
        List<Product> list = selectBySQL(sql, name);
        return list.isEmpty() ? null : list.get(0);
    }
    public List<Product> searchByName(String name) throws Exception{
        String sql = "select * from product where name like ?";
        return selectBySQL(sql, "%"+name+"%");
    }
}

