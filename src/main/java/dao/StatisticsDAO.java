package dao;

import java.sql.CallableStatement;
import java.sql.Connection;

import java.util.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.JdbcHelper;

/**
 *
 * @author doanc
 */
public class StatisticsDAO {

   public List<Object[]> getEmployeeSessionsDetail(Date startAt, Date endAt) {
        List<Object[]> list = new ArrayList<>();
        String sql = "{CALL GetEmployeeSessionDetails(?, ?)}";
        
        try (ResultSet rs = JdbcHelper.executeQuery(sql, startAt, endAt)) {
            while (rs.next()) {
                Object[] row = {
                    rs.getString("ID"),
                    rs.getString("Name"),
                    rs.getString("username"),
                    rs.getTimestamp("startTime"),
                    rs.getTimestamp("endTime"),
                    rs.getDouble("balance")
                };
                list.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return list;
    }
   private List<Object[]>  getListOfArray(String sql,String[] cols,Object... args){
        try {
            List<Object[]> list = new ArrayList<>();
            ResultSet rs = JdbcHelper.executeQuery(sql, args);
            while(rs.next()){
                Object[] vals = new Object[cols.length];
                for(int i = 0;i < cols.length;i++){
                    vals[i] = rs.getObject(cols[i]);
                }
                list.add(vals);
            }
            rs.getStatement().getConnection().close();
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public List<Object[]> getStatisticsInvoice(
            java.util.Date dayBegin,
            java.util.Date dayEnd,
            String computerName,
            String userName,
            Integer idEmployee) {

        List<Object[]> list = new ArrayList<>();
        String sql = "{CALL sp_StatisticsInvoice(?, ?, ?, ?, ?)}";

        try (Connection conn = JdbcHelper.getConnection(); CallableStatement stmt = conn.prepareCall(sql)) {

            if (dayBegin != null) {
                stmt.setDate(1, new java.sql.Date(dayBegin.getTime()));
            } else {
                stmt.setNull(1, java.sql.Types.DATE);
            }

            if (dayEnd != null) {
                stmt.setDate(2, new java.sql.Date(dayEnd.getTime()));
            } else {
                stmt.setNull(2, java.sql.Types.DATE);
            }

            if (computerName != null) {
                stmt.setString(3, computerName);
            } else {
                stmt.setNull(3, java.sql.Types.NVARCHAR);
            }

            if (userName != null) {
                stmt.setString(4, userName);
            } else {
                stmt.setNull(4, java.sql.Types.NVARCHAR);
            }

            if (idEmployee != null) {
                stmt.setInt(5, idEmployee);
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Object[] row = {
                        rs.getInt("IdHD"),
                        rs.getInt("IdEmployee"),
                        rs.getInt("IdMember"),
                        rs.getTimestamp("createdAt"),
                        rs.getString("status"),
                        rs.getDouble("totalAmount")
                    };
                    list.add(row);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
    public List<Object[]> getStatisticsList(){
        String sql = "select e.ID,e.name,a.username,s.startTime,s.endTime,e.balance from employee e \n" +
"				join account a on e.accountID = a.ID \n" +
"				join invoice i on i.employeeID = e.ID \n" +
"				join sessions s on s.invoiceID = i.ID";
          String[] cols = {"ID", "name", "username", "startTime", "endTime", "balance"};
    return getListOfArray(sql, cols);
    }
    public List<Object[]> getStatisticsByIdEmployee(Integer id){
        String sql ="select e.ID,e.name,a.username,s.startTime,s.endTime,e.balance from employee e \n" +
"				join account a on e.accountID = a.ID \n" +
"				join invoice i on i.employeeID = e.ID \n" +
"				join sessions s on s.invoiceID = i.ID where e.ID = ?";
        String [] cols = {"ID","name","username","startTime","endTime","balance"};
        return getListOfArray(sql, cols, id);
    }
//    // Lấy chi tiết phiên làm việc của nhân viên
//    public List<Object[]> getEmployeeSessionsDetail(Date startAt, Date endAt) {
//        String sql = "{CALL GetEmployeeSessionDetails(?, ?)}";
//        String[] cols = {"ID", "name", "username", "startTime", "endTime", "balance"};
//        return getListArray(sql, cols, startAt, endAt);
//    }
}
