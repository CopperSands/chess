package webSocketMessages.serverMessages;

public class LoadMessage extends ServerMessage {

    private String message;
    public LoadMessage(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
