package dataAccess;

import model.AuthData;
import service.helper.TableCreation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersistentAuthDAO implements AuthDAO{

    //check connection to database and creates new database if needed
    public PersistentAuthDAO(){
        try(Connection conn = DatabaseManager.getConnection()){
            if(!TableCreation.isUserTable(conn,"auth")){
                TableCreation.createTable(conn, TableCreation.Tables.auth);
            }

        }catch(DataAccessException e){
            try{
                //if the database doesn't exit
                DatabaseManager.createDatabase();
                try(Connection conn = DatabaseManager.getConnection()){
                    TableCreation.createTable(conn, TableCreation.Tables.auth);
                } catch (SQLException ex) {
                    throw new DataAccessException("Error internal server error");
                }
            } catch(DataAccessException ex){
                ex.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
        try(Connection conn = DatabaseManager.getConnection()){
            String sql = "DELETE FROM auth where authToken = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,authToken.authToken());
            stmt.executeUpdate();
        }catch(SQLException e){
            throw new DataAccessException("Error deleting authToken");
        }

    }

    @Override
    public void clear() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            String sql = "DELETE FROM auth";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();

        }catch (SQLException e){
            throw new DataAccessException("Error clearing database");
        }

    }
}
