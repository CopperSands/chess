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
    private ChessPiece [][] chessBoard;

    public ChessBoard() {
        //initialize board positions
        chessBoard = new ChessPiece[9][9];
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     * In tests values will start at 1 and end at 8
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        int xCoor = position.getColumn();
        int yCoor = position.getRow();
        if (xCoor <= 8 && yCoor <= 8) {
            chessBoard[yCoor][xCoor] = piece;
        }
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {

        int xCoor = position.getColumn();
        int yCoor = position.getRow();
        ChessPiece piece = chessBoard[xCoor][yCoor];

        //ChessPiece piece = piecesMap.get(position);
        return piece;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     * The tests have an off by one error for the 2d array adjustments were made
     */
    public void resetBoard()
    {
        chessBoard = new ChessPiece[9][9];
        //create white team
        chessBoard[1][1] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        chessBoard[1][2] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        chessBoard[1][3] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        chessBoard[1][4] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN);
        chessBoard[1][5] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING);
        chessBoard[1][6] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        chessBoard[1][7] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        chessBoard[1][8] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        //set up white pawns
        for (int i = 1; i < 9; i ++){
            chessBoard[2][i] = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        }
        //set up black team
        chessBoard[8][1] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        chessBoard[8][2] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        chessBoard[8][3] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        chessBoard[8][4] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN);
        chessBoard[8][5] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING);
        chessBoard[8][6] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        chessBoard[8][7] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        chessBoard[8][8] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        for (int i = 1; i < 9; i++){
            chessBoard[7][i] = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;

        //check each spot of the board
        boolean isEqual = true;
        for (int i = 0; i < 9; i++){
            for (int j = 0; j < 9; j++){

                //if both chessboards have a null piece at the same coordinate they are the same do
                //do nothing. if the current chessboard has a null piece but the other does not they
                //they aren't equal. The last if statement uses the ChessPiece equals and compares
                // values between the two. It will also return false if the other chessboard has a null
                // value.
                if(chessBoard[i][j] == null && that.chessBoard[i][j] == null){
                }
                else if (chessBoard[i][j] == null && that.chessBoard[i][j] != null){
                    isEqual = false;
                }
                else if (!(chessBoard[i][j].equals(that.chessBoard[i][j]))){
                    isEqual = false;
                    break;
                }
            }
        }
        return isEqual;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chessBoard);
    }
}
