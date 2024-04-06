package webSocketMessages.serverMessages;

public class NoticeMessage extends ServerMessage{

    private String message;

    public NoticeMessage(ServerMessageType type, String message) {
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
