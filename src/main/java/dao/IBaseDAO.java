package dao;


import java.util.List;
/**
 *
 * @author Admin
 * @param <T>
 * @param <ID>
 */
public interface IBaseDAO<T, ID> {
    void insert(T entity) throws Exception;
    void update(T entity) throws Exception;
    void delete(ID id) throws Exception;
    T selectByID(ID id) throws Exception;
    List<T> selectAll() throws Exception;
    List<T> selectBySQL(String sql, Object... args) throws Exception;
}