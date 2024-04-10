package webSocketMessages.userCommands;

import chess.ChessMove;

import java.util.Objects;

public class MoveCommand extends UserGameCommand{

    private int gameID;
    private ChessMove move;

    public MoveCommand(String authToken) {
        super(authToken);
    }

    public MoveCommand(String authToken,int gameID,ChessMove move){
        super(authToken);
        this.commandType = CommandType.MAKE_MOVE;
        this.gameID = gameID;
        this.move = move;
    }

    public int getGameID() {
        return gameID;
    }

    public ChessMove getMove() {
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MoveCommand that = (MoveCommand) o;
        return gameID == that.gameID && move.equals(that.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameID, move);
    }
}
