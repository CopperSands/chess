package webSocketMessages.userCommands;

import java.util.Objects;

public class GenCommand extends UserGameCommand{

    private int gameID;

    public GenCommand(String authToken, int gameID,CommandType type){
        super(authToken);
        this.commandType = type;
        this.gameID = gameID;
    }

    public int getGameID() {
        return gameID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        GenCommand that = (GenCommand) o;
        return gameID == that.gameID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), gameID);
    }
}
