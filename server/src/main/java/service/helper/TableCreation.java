package service.helper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableCreation {

    public enum Tables{
        game,
        user,
        auth
    }
    private static final String createGameTable = "CREATE TABLE IF NOT EXISTS game(" +
            "gameID INT NOT NULL AUTO_INCREMENT," +
            "whiteUsername VARCHAR(100)," +
            "blackUsername VARCHAR(100)," +
            "gameName VARCHAR(150)," +
            "game VARCHAR(1900) NOT NULL," +
            "PRIMARY KEY (gameID)" +
            ")";

    private static final String createAuthTable = "CREATE TABLE IF NOT EXISTS auth(" +
            "authToken CHAR(60) NOT NULL, " +
            "username VARCHAR(100) NOT NULL," +
            "PRIMARY KEY (authToken)" +
            ")";

    private static final String createUserTable = "CREATE TABLE IF NOT EXISTS user(" +
            "username VARCHAR(150) NOT NULL," +
            "password CHAR(60) NOT NULL," +
            "email VARCHAR(100) NOT NULL, " +
            "Primary Key (username)," +
            "Unique (email)" +
            ")";
    public static void createTable(Connection conn, Tables table) throws SQLException {
        String sql = null;
        if (table == Tables.game){
            sql = createGameTable;
        }
        else if(table ==Tables.user){
            sql = createUserTable;
        }
        else if (table ==Tables.auth){
            sql = createAuthTable;
        }
        if (sql != null){
            try{
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.executeUpdate();
            }catch (SQLException e) {
                throw e;
            }
        }
    }
    public static boolean isUserTable(Connection conn,String table) {
        boolean isTable = true;
        try {
            PreparedStatement stmt = conn.prepareStatement("select count(*) from ?");
            stmt.setString(1,table);
            stmt.execute();
            return isTable;
        } catch (SQLException e) {
            return false;
        }
    }
}
