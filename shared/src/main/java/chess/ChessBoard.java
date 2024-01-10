package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
            for(int j = 0; i < 8; i++){
                ChessPosition position = new ChessPosition(i,j);
                board.add(position);
            }
        }
        //initialize Map
        piecesMap = new HashMap<ChessPosition,ChessPiece>();

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
    public void resetBoard() {
        throw new RuntimeException("Not implemented");
    }
}
