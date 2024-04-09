package webSocketMessages.userCommands;

import chess.ChessGame;

import java.util.Objects;

public class JoinCommand extends UserGameCommand{

    private int gameID;
    private ChessGame.TeamColor playerColor;


    public JoinCommand(String authToken) {
        super(authToken);
    }

    public JoinCommand(String authToken, int gameID, String playerColor){
        super(authToken);
        this.gameID = gameID;
        commandType = CommandType.JOIN_PLAYER;
        if (playerColor == null){
            this.playerColor = null;
            commandType = CommandType.JOIN_OBSERVER;
        }
        else if (playerColor.equals("WHITE")){
            this.playerColor = ChessGame.TeamColor.WHITE;

        }
        else {
            this.playerColor = ChessGame.TeamColor.BLACK;
        }
    }

    public int getGameID() {
        return gameID;
    }

    public String getPlayerColor() {
        if (playerColor == null){
            return null;
        }
        else{
            return playerColor.toString();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        JoinCommand that = (JoinCommand) o;
        return gameID == that.gameID && playerColor == that.playerColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameID, playerColor);
    }
}
