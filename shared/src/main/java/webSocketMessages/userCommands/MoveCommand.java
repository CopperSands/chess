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

    public String getStartEnd(){
        String message = "";
        if (move != null){
            if (move.getStartPosition() != null && move.getEndPosition() != null){
                int sRow = move.getStartPosition().getRow();
                sRow = 9 - sRow;
                int sCol = move.getStartPosition().getColumn();
                int eRow = move.getEndPosition().getRow();
                eRow = 9 - eRow;
                int eCol = move.getEndPosition().getColumn();

                char sCharRow = (char)(sRow + 48);
                char sCharCol = (char)(sCol + 96);
                char eCharRow = (char)(eRow + 48);
                char eCharCol = (char)(eCol + 96);

                message = "" + sCharCol + sCharRow + " to " + eCharCol + eCharRow;
            }
        }
        return message;
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
