package clientRecords;

import client.ClientWebSocket;

public record GameLoop(boolean isLive, boolean isLoggedIn, ClientWebSocket webSocket) {
}
