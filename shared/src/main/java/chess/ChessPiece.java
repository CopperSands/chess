package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor teamColor;
    private PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.teamColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor()
    {
        return teamColor;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType()
    {
        return type;
        //throw new RuntimeException("Not implemented");
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        if(type == PieceType.KING ){

        }
        else if (type == PieceType.QUEEN){

        }
        else if (type == PieceType.BISHOP){
        moves = bishopMoves(board,myPosition);

        }
        else if (type == PieceType.KNIGHT){

        }
        else if (type == PieceType.ROOK){

        }
        else if (type == PieceType.PAWN){

        }
        return moves;
        //throw new RuntimeException("Not implemented");
    }

    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition startPosition){
        Collection<ChessMove> moves = new ArrayList<ChessMove>();
        int startRow = startPosition.getRow();
        int startCol = startPosition.getColumn();

        //check the up right diagonal for moves
        // first if statement adds a move if the next space is empty
        // the second if adds a move if the space is occupied by an opposing team piece then exits the loop
        // third if breaks the loop if a team piece is encountered.
        int i = 1;
        while (((startRow + i) < 9) && ((startCol + i) < 9)){
            ChessPosition nextSpace = new ChessPosition(startRow + i, startCol + i);
            if (board.getPiece(nextSpace) == null){
                ChessMove newMove = new ChessMove(startPosition,nextSpace,null);
                moves.add(newMove);
            }
            else if (board.getPiece(nextSpace).getTeamColor() != this.teamColor){
                ChessMove newMove = new ChessMove(startPosition,nextSpace,null);
                moves.add(newMove);
                break;
            }
            else if (board.getPiece(nextSpace).getTeamColor() == this.teamColor){
                break;
            }
            i++;
        }

        //checks for down left diagonal
        i = -1;
        while (((startRow + i) > 0) && ((startCol + i) > 0)){
            ChessPosition nextSpace = new ChessPosition(startRow + i, startCol + i);
            if (board.getPiece(nextSpace) == null){
                ChessMove newMove = new ChessMove(startPosition,nextSpace,null);
                moves.add(newMove);
            }
            else if (board.getPiece(nextSpace).getTeamColor() != this.teamColor){
                ChessMove newMove = new ChessMove(startPosition,nextSpace,null);
                moves.add(newMove);
                break;
            }
            else if (board.getPiece(nextSpace).getTeamColor() == this.teamColor){
                break;
            }
            i--;
        }

        //check for top left
        i = 1;
        int j = -1;
        while (((startRow + i) < 9) && ((startCol + j) > 0)){
            ChessPosition nextSpace = new ChessPosition(startRow + i, startCol + j);
            if (board.getPiece(nextSpace) == null){
                ChessMove newMove = new ChessMove(startPosition,nextSpace,null);
                moves.add(newMove);
            }
            else if (board.getPiece(nextSpace).getTeamColor() != this.teamColor){
                ChessMove newMove = new ChessMove(startPosition,nextSpace,null);
                moves.add(newMove);
                break;
            }
            else if (board.getPiece(nextSpace).getTeamColor() == this.teamColor){
                break;
            }
            i++;
            j--;
        }

        //check bottom right
        i = -1;
        j = 1;
        while (((startRow + i) > 0) && ((startCol + j) < 9)){
            ChessPosition nextSpace = new ChessPosition(startRow + i, startCol + j);
            if (board.getPiece(nextSpace) == null){
                ChessMove newMove = new ChessMove(startPosition,nextSpace,null);
                moves.add(newMove);
            }
            else if (board.getPiece(nextSpace).getTeamColor() != this.teamColor){
                ChessMove newMove = new ChessMove(startPosition,nextSpace,null);
                moves.add(newMove);
                break;
            }
            else if (board.getPiece(nextSpace).getTeamColor() == this.teamColor){
                break;
            }
            i--;
            j++;
        }


        return moves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return teamColor == that.teamColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(teamColor, type);
    }
}
