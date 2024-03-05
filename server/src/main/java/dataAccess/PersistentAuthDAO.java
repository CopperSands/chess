package dataAccess;

import model.AuthData;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersistentAuthDAO implements AuthDAO{

    private final String createTableSql = "CREATE TABLE IF NOT EXISTS auth(" +
            "authToken CHAR(60) NOT NULL, " +
            "username VARCHAR NOT NULL," +
            "PRIMARY KEY (authToken)" +
            ")";
    //check connection to database and creates new database if needed
    public PersistentAuthDAO() throws DataAccessException{
        try(Connection conn = DatabaseManager.getConnection()){
            if(!isAuthTable(conn)){
                createTable(conn);
            }

        }catch(DataAccessException e){
            try{
                //if the database doesn't exit
                DatabaseManager.createDatabase();
                try(Connection conn = DatabaseManager.getConnection()){
                    createTable(conn);
                } catch (SQLException ex) {
                    throw new DataAccessException("Error internal server error");
                }
            } catch(DataAccessException ex){
                throw ex;
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error internal server error");
        }
    }
    @Override
    public void createAuth(String authToken, String username) throws DataAccessException {
        //check if token exits
        AuthData found = getAuth(authToken);
        if (found != null){
            throw new DataAccessException("Error failed to create authToken");
        }
        try(Connection conn = DatabaseManager.getConnection()){
            String sql = "INSERT INTO auth (authToken, username) VALUES (?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,authToken);
            stmt.setString(2,username);
            stmt.executeUpdate();
        }catch (SQLException e) {
            throw new DataAccessException(e.getMessage());
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        AuthData token = null;
        try(Connection conn = DatabaseManager.getConnection()){
            String sql = "SELECT * FROM auth WHERE authToken = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, authToken);
            ResultSet result = stmt.executeQuery();
            if (result.next()){
                token = new AuthData(result.getString("authToken"), result.getString("username"));
            }
            return token;
        } catch (SQLException e) {
            throw new DataAccessException("Error internal server error");
        }
    }

    @Override
    public void deleteAuth(AuthData authToken) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }

    private boolean isAuthTable(Connection conn) {
        boolean isTable = true;
        try {
            PreparedStatement stmt = conn.prepareStatement("select count(*) from auth");
            stmt.execute();
            return isTable;
        } catch (SQLException e) {
            return false;
        }
    };


    private void createTable(Connection conn) throws SQLException {
        try{
            PreparedStatement stmt = conn.prepareStatement(createTableSql);
            stmt.executeUpdate();
        }catch (SQLException e) {
            throw e;
        }
    }
}
