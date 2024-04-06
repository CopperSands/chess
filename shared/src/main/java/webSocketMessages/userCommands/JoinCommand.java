package webSocketMessages.userCommands;

import chess.ChessGame;

import java.util.Objects;

public class JoinCommand extends UserGameCommand{

    private int gameID;
    private ChessGame.TeamColor playerColor;


    public JoinCommand(String authToken) {
        super(authToken);
    }

    public int getGameID() {
        return gameID;
    }

    public String getPlayerColor() {
        return playerColor.toString();
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
