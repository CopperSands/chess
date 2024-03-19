package dataAccess;

import model.UserData;
import service.helper.TableCreation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PersistentUserDAO implements UserDAO{

    public PersistentUserDAO(){
        try(Connection conn = DatabaseManager.getConnection()){
            if(!TableCreation.isUserTable(conn,"user")){
                TableCreation.createTable(conn, TableCreation.Tables.user);
                conn.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void createUser(String username, String password, String email) throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            String sql = "INSERT INTO user(username,password,email) VALUES (?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,username);
            stmt.setString(2,password);
            stmt.setString(3,email);
            stmt.executeUpdate();
            conn.commit();
        }catch (SQLException e){
            throw new DataAccessException("Error creating user");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        UserData userData = null;
        try(Connection conn = DatabaseManager.getConnection()){
            String sql = "SELECT * FROM user where username = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1,username);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                userData = new UserData(username,rs.getString("password"),rs.getString("email"));
            }
            return userData;
        }catch (SQLException e){
            throw new DataAccessException("Error getting user");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            String sql = "DELETE FROM user";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            conn.commit();
        }catch(SQLException e){
            throw new DataAccessException("Error clearing users");
        }

    }
}
