package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import service.helper.TableCreation;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;

public class PersistentGameDAO implements GameDAO{

    public PersistentGameDAO(){
        try(Connection conn = DatabaseManager.getConnection()){
            if(!TableCreation.isUserTable(conn,"game")){
                TableCreation.createTable(conn, TableCreation.Tables.game);
                conn.commit();
            }
        }catch (SQLException e) {
            e.printStackTrace();
        } catch (DataAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int createGame(String whiteUsername, String blackUsername, String gameName, ChessGame game) throws DataAccessException {
        Gson gson = new Gson();
        String json = gson.toJson(game);
        int gameID = 0;
        try(Connection conn = DatabaseManager.getConnection()){
            String sql = "INSERT INTO game(whiteUsername, blackUsername, gameName, game) VALUES(?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1,whiteUsername);
            stmt.setString(2,blackUsername);
            stmt.setString(3,gameName);
            stmt.setString(4,json);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next()){
                gameID = rs.getInt(1);
                conn.commit();
            }
            if (gameID == 0){
                throw new DataAccessException("Error creating new game");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error creating game");
        }
        return gameID;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        Gson gson = new Gson();
        GameData game = null;
        try(Connection conn = DatabaseManager.getConnection()){
            String sql = "SELECT * FROM game where gameID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1,gameID);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                game = getServerGame(rs);
            }
            return game;
        }catch(SQLException e){
            throw new DataAccessException("Error getting game");
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> list = new ArrayList<GameData>();
        try(Connection conn = DatabaseManager.getConnection()){
            String sql = "SELECT * FROM game";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                list.add(getServerGame(rs));
            }
            return list;
        }catch(SQLException e){
            throw new DataAccessException("Error getting games");
        }
    }

    private GameData getServerGame(ResultSet rs) throws SQLException {
        Gson gson = new Gson();
        String json = rs.getString("game");
        ChessGame returnedGame = gson.fromJson(json, ChessGame.class);
        GameData game = new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"),
                rs.getString("blackUsername"),rs.getString("gameName"), returnedGame);
        return game;
    }

    @Override
    public void updateGame(int gameID, GameData updatedGame) throws DataAccessException {
        Gson gson = new Gson();
        String json = gson.toJson(updatedGame.game());
        try(Connection conn = DatabaseManager.getConnection()){
            String sql = "UPDATE game SET whiteUsername = ?, blackUsername = ?, game = ? WHERE gameID = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, updatedGame.whiteUsername());
            stmt.setString(2,updatedGame.blackUsername());
            stmt.setString(3,json);
            stmt.setInt(4,gameID);
            stmt.executeUpdate();
            conn.commit();

        } catch (SQLException e) {
            throw new DataAccessException("Error updating game");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try(Connection conn = DatabaseManager.getConnection()){
            String sql = "DELETE FROM game";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.executeUpdate();
            conn.commit();
        }catch (SQLException e){
            throw new DataAccessException("error deleting games");
        }

    }
}
