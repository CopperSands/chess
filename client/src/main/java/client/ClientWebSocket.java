package client;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import clientRecords.ClientChessPos;
import com.google.gson.Gson;
import webSocketMessages.serverMessages.ErrorMessage;
import webSocketMessages.serverMessages.LoadMessage;
import webSocketMessages.serverMessages.NoticeMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.GenCommand;
import webSocketMessages.userCommands.JoinCommand;
import webSocketMessages.userCommands.MoveCommand;
import webSocketMessages.userCommands.UserGameCommand;

import javax.websocket.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClientWebSocket extends Endpoint {

    public Session session;
    private ChessGame game;
    private int gameID;
    private ChessGame.TeamColor teamColor;
    private String authToken;
    public ClientWebSocket(int port, String authToken) throws Exception {
        game = null;
        teamColor = null;
        this.gameID = 0;
        this.authToken = authToken;
        String urlBase = "ws://localhost:" + port;
            URI uri = new URI(urlBase + "/connect");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this,uri);


            //listens for messages from server
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Gson gson = new Gson();
                    ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
                    switch (serverMessage.getServerMessageType()) {
                        case LOAD_GAME -> loadGame(message);
                        case NOTIFICATION -> getNotification(message);
                        case ERROR -> getError(message);
                    }
                }
            });


    }

    public void send(String message) throws Exception {
        if(this.session.isOpen()){
            this.session.getBasicRemote().sendText(message);
        }
        else{
            throw new Exception("session closed");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    protected void getError(String message){
        Gson gson = new Gson();
        ErrorMessage errorMessage = gson.fromJson(message,ErrorMessage.class);
        System.out.println(errorMessage.getMessage());
        System.out.print(ClientPrint.getStatus(true));
    }

    protected void getNotification(String message){
        Gson gson = new Gson();
        NoticeMessage notice = gson.fromJson(message, NoticeMessage.class);
        System.out.println(notice.getMessage());
        System.out.print(ClientPrint.getStatus(true));
    }

    protected void loadGame(String message){
        Gson gson = new Gson();
        LoadMessage loadMessage = gson.fromJson(message,LoadMessage.class);
        System.out.println(loadMessage.getMessage());
        game = loadMessage.getGame();
        redrawBoard();
        System.out.print(ClientPrint.getStatus(true));

    }

    protected void redrawBoard(){
        if (game != null){
            if (teamColor == null || teamColor == ChessGame.TeamColor.WHITE){
                ClientPrint.printBoard(game.getBoard(),null);
            }
            else{
                ClientPrint.printReverseBoard(game.getBoard(),null);
            }
        }
    }

    protected void joinGame(int gameID,String playerColor) throws Exception {
        JoinCommand joinCommand = new JoinCommand(authToken,gameID,playerColor);
        Gson gson = new Gson();
        String req = gson.toJson(joinCommand);
        if (playerColor != null && playerColor.equals("WHITE")){
            teamColor = ChessGame.TeamColor.WHITE;
        }
        else if (playerColor != null && playerColor.equals("BLACK")){
            teamColor = ChessGame.TeamColor.BLACK;
        }
        send(req);
        this.gameID = gameID;

    }

    protected void getValidMoves(String pieceAt) throws Exception{
        ClientChessPos pos = getChessSquare(pieceAt);
        ChessPosition position = new ChessPosition(pos.row(), pos.col());
        Collection<ChessMove> validMoves = null;
        ArrayList<ChessPosition> validPositions = null;
        //check if piece exists
        if (game.getBoard().getPiece(position) != null){
            System.out.println(game.getBoard().getPiece(position).getPieceType() + " " + game.getBoard().getPiece(position).getTeamColor());
            validMoves = game.validMoves(position);
            validPositions = new ArrayList<>();
        }
        if(validMoves != null){
            for(Iterator it = validMoves.iterator(); it.hasNext();){
                validPositions.add( ((ChessMove)it.next()).getEndPosition());
            }
        }
        if (game != null){
            if (teamColor == null || teamColor == ChessGame.TeamColor.WHITE){
                ClientPrint.printBoard(game.getBoard(),validPositions);
            }
            else{
                ClientPrint.printReverseBoard(game.getBoard(),validPositions);
            }
        }

    }

    private ClientChessPos getChessSquare(String pieceAt) throws Exception{
        //check that the chess square is within the valid range
        Pattern p = Pattern.compile("[a-h][1-8]");
        Matcher m = p.matcher(pieceAt);
        if (!m.matches() || pieceAt.length() != 2){
            throw new Exception("Invalid position");
        }
        int col = pieceAt.charAt(0) - 96;
        int revRow = pieceAt.charAt(1) - 48 ;
        //display row numbers are the inverse of the actual board row in storage
        int row = 9 - revRow;
        return new ClientChessPos(row,col);
    }

    protected void leaveGame() throws Exception {
        sendGenCommand(UserGameCommand.CommandType.LEAVE);
    }

    public void resignGame() throws Exception {
        sendGenCommand(UserGameCommand.CommandType.RESIGN);
    }
    private void sendGenCommand(UserGameCommand.CommandType type) throws Exception{
        GenCommand command = new GenCommand(authToken,gameID, type);
        Gson gson = new Gson();
        String req = gson.toJson(command);
        send(req);
    }

    protected void makeMove(String startAt, String endAt, String promotionType) throws Exception{
        System.out.println("the turn gose to" + game.getTeamTurn());
        ClientChessPos startPos = getChessSquare(startAt);
        ClientChessPos endPos = getChessSquare(endAt);
        ChessPosition start = new ChessPosition(startPos.row(),startPos.col());
        ChessPosition end = new ChessPosition(endPos.row(), endPos.col());
        ChessPiece.PieceType pieceType = getPieceType(promotionType);
        ChessMove move = new ChessMove(start,end,pieceType);
        MoveCommand moveCommand = new MoveCommand(authToken,gameID,move);
        Gson gson = new Gson();
        String req = gson.toJson(moveCommand);
        send(req);
    }

    private ChessPiece.PieceType getPieceType(String type){
        if (type == null){
            return null;
        }
        type = type.toUpperCase(Locale.ROOT);
        ChessPiece.PieceType pieceType = null;
        if (type.equals(ChessPiece.PieceType.PAWN.toString())){
            pieceType = ChessPiece.PieceType.PAWN;
        }
        else if (type.equals(ChessPiece.PieceType.BISHOP.toString())){
            pieceType = ChessPiece.PieceType.BISHOP;
        }
        else if (type.equals(ChessPiece.PieceType.ROOK.toString())){
            pieceType = ChessPiece.PieceType.ROOK;
        }
        else if (type.equals(ChessPiece.PieceType.KNIGHT.toString())){
            pieceType = ChessPiece.PieceType.KNIGHT;
        }
        else if (type.equals(ChessPiece.PieceType.QUEEN.toString())){
            pieceType = ChessPiece.PieceType.QUEEN;
        }
        else if (type.equals(ChessPiece.PieceType.KING.toString())){
            pieceType = ChessPiece.PieceType.KING;
        }
        return pieceType;
    }
}
