package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadMessage extends ServerMessage {

    private String message;

    private ChessGame game;

    public LoadMessage(ServerMessageType type, String message, ChessGame game) {
        super(type);
        this.message = message;
        this.game = game;
    }

    public String getMessage() {
        return message;
    }

    public ChessGame getGame(){
        return game;
    }
}
