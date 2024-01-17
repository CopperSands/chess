package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    /****
     * boardPiece {
     *     Position
     *     Piece
     * }
     *
     */
    //HashMap can be used to look up chess pieces based on position
    private ArrayList<ChessPosition> board;
    private Map<ChessPosition,ChessPiece> piecesMap;


    public ChessBoard() {
        //initialize board positions
        board = new ArrayList<ChessPosition>();
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                ChessPosition position = new ChessPosition(i,j);
                board.add(position);
            }
        }
        //initialize Map
        piecesMap = new HashMap<ChessPosition,ChessPiece>();
        //resetBoard();
        //

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        //check that the ChessPosition is valid
        boolean isValPosition = false;
        for( int i = 0; i < board.size(); i++){
            if (position == board.get(i)){
                isValPosition = true;
                break;
            }
        }
        // create chessPiece
        piecesMap.put(position,piece);
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        ChessPiece piece = piecesMap.get(position);
        return piece;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard()
    {
        piecesMap = new HashMap<ChessPosition,ChessPiece>();
        //white Rook
        ChessPosition position = new ChessPosition(0,0);
        ChessPiece piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(position,piece);

        position = new ChessPosition(0,1);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(position, piece);

        position = new ChessPosition(0,2);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(position,piece);

        position = new ChessPosition(0,3);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        addPiece(position,piece);

        position = new ChessPosition(0,4);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        addPiece(position,piece);

        position = new ChessPosition(0,5);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        addPiece(position,piece);

        position = new ChessPosition(0,6);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        addPiece(position, piece);

        position = new ChessPosition(0,7);
        piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        addPiece(position,piece);

        //add white pawns
        for (int i = 0; i < 8; i++){
            position = new ChessPosition(1,i);
            piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            addPiece(position,piece);
        }

        //black pieces
        position = new ChessPosition(7,0);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(position,piece);

        position = new ChessPosition(7,1);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(position, piece);

        position = new ChessPosition(7,2);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(position,piece);

        position = new ChessPosition(7,3);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        addPiece(position,piece);

        position = new ChessPosition(7,4);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        addPiece(position,piece);

        position = new ChessPosition(7,5);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        addPiece(position,piece);

        position = new ChessPosition(7,6);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        addPiece(position, piece);

        position = new ChessPosition(7,7);
        piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        addPiece(position,piece);

        for (int i = 0; i < 8; i++){
            position = new ChessPosition(6,i);
            piece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            addPiece(position,piece);
        }
        //throw new RuntimeException("Not implemented");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;

        //check board
        boolean isEqual = true;
        if (board.size() == that.board.size()){
            for (int i = 0; i < board.size(); i++){
                ChessPosition thisBoard = board.get(i);
                ChessPosition thatBoard = that.board.get(i);
                //only overrided the equals function in ChessPosition
                if (!thisBoard.equals(thatBoard)){
                    isEqual = false;
                }
            }
        }
        else{
            isEqual = false;
        }

        //compare pieceMaps
        if(piecesMap.size() == that.piecesMap.size()){
            for(int i = 0; i < board.size(); i++){
                if(piecesMap.get(i) != that.piecesMap.get(i)){
                    isEqual = false;
                }
            }
        }
        else {
            isEqual = false;
        }

        return isEqual;
        //return Objects.equals(board, that.board) && Objects.equals(piecesMap, that.piecesMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, piecesMap);
    }
}
