package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import service.helper.TableCreation;

import java.sql.*;
import java.util.Collection;

public class PersistentGameDAO implements GameDAO{

    private final String createUserTable = "CREATE TABLE IF NOT EXISTS game(" +
            "gameID INT NOT NULL AUTO_INCREMENT," +
            "whiteUsername VARCHAR(100)," +
            "blackUsername VARCHAR(100)," +
            "gameName VARCHAR(150)," +
            "game VARCHAR(1900) NOT NULL," +
            "PRIMARY KEY (gameID)" +
            "Unique )";

    public PersistentGameDAO(){
        try(Connection conn = DatabaseManager.getConnection()){
            if(!isUserTable(conn)){
                TableCreation.createTable(conn,createUserTable);
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
                gameID = rs.getInt("gameID");
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
            ResultSet result = stmt.executeQuery();
            if(result.next()){
                String json = result.getString("game");
                ChessGame returnedGame = gson.fromJson(json, ChessGame.class);
                game = new GameData(result.getInt("gameID"), result.getString("whiteUsername"),
                        result.getString("blackUsername"),result.getString("gameName"), returnedGame);
            }
            return game;
        }catch(SQLException e){
            throw new DataAccessException("Error getting game");
        }
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void updateGame(int gameID, GameData updatedGame) throws DataAccessException {

    }

    @Override
    public void clear() throws DataAccessException {

    }

    private boolean isUserTable(Connection conn) {
        boolean isTable = true;
        try {
            PreparedStatement stmt = conn.prepareStatement("select count(*) from user");
            stmt.execute();
            return isTable;
        } catch (SQLException e) {
            return false;
        }
    }
}
