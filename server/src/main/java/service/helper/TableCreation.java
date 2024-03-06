package service.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableCreation {
    public static void createTable(Connection conn, String sql) throws SQLException {
        try{
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
        }catch (SQLException e) {
            throw e;
        }
    }
}
