package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage{

    private String errorMessage;

    public ErrorMessage(ServerMessageType type, String message) {
        super(type);
        this.errorMessage = message;
    }

    public String getMessage() {
        return errorMessage;
    }
}
