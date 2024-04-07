package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor teamTurn;
    private TeamColor resigned;


    public ChessGame() {
        board = new ChessBoard();
        board.resetBoard();
        teamTurn = TeamColor.WHITE;
        resigned = null;
    }

    public void setResigned(TeamColor team){
        if(resigned == null){
            resigned = team;
        }
    }

    public TeamColor getResigned(){
        return resigned;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Collection<ChessMove> moves = board.getPiece(startPosition).pieceMoves(board,startPosition);
        Collection<ChessMove> validMoves = new ArrayList<ChessMove>();
        ChessPiece piece = board.getPiece(startPosition);


        //loop through all possible moves
        for (Iterator<ChessMove> iterator = moves.iterator(); iterator.hasNext();){
            ChessMove nextMove = iterator.next();

            //create a would be table for move testing
            ChessPosition nextSpace = nextMove.getEndPosition();
            ChessPiece pieceAtNext = board.getPiece(nextSpace);
            board.addPiece(nextSpace,piece);
            board.addPiece(startPosition,null);

            //check if team's king is in check
            if (!isInCheck(piece.getTeamColor())){
                validMoves.add(nextMove);
            }


            //cleanup
            board.addPiece(startPosition,piece);
            board.addPiece(nextSpace,pieceAtNext);

        }

        return validMoves;
    }

    /**
     * used to find the position of a team's king
     * @param teamColor
     * @return position of a team's king
     */
    private ChessPosition getKingPos(TeamColor teamColor){
        ChessPosition kingPos = null;
        for (int i = 1; i < 9; i++){
            for (int j = 1; j < 9; j++ ){
                ChessPosition square = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(square);
                if (piece != null){
                    if(piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING){
                        kingPos = square;
                        break;
                    }
                }
            }
        }

        return kingPos;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {

        Collection<ChessMove> validMoves = validMoves(move.getStartPosition());
        //check that move is valid
        if ( validMoves != null){
            if (validMoves.contains(move)){
                ChessPiece piece = board.getPiece(move.getStartPosition());
                if (piece.getTeamColor() == teamTurn) {
                    //check if a pawn needs promotion. the getPromotionPiece will be null if not a pawn from promotion
                    if (move.getPromotionPiece() == null) {
                        board.addPiece(move.getEndPosition(), piece);
                    }
                    else{
                        ChessPiece proPiece = new ChessPiece(teamTurn,move.getPromotionPiece());
                        board.addPiece(move.getEndPosition(),proPiece);
                    }
                    board.addPiece(move.getStartPosition(), null);
                    nextPlayerTurn();
                }
                else {
                    throw new InvalidMoveException("not your turn");
                }
            }
            else {
                throw new InvalidMoveException("move is invalid");
            }
        }
        else {
            throw new InvalidMoveException("move is invalid");
        }
    }

    private void nextPlayerTurn(){
        if (teamTurn == TeamColor.WHITE){
            teamTurn = TeamColor.BLACK;
        }
        else{
            teamTurn = TeamColor.WHITE;
        }
    }

    /**
     * Determines if the given team is in check
     * This is done by looping over the whole board. If a piece on the opposite team
     * can capture the king then true is returned
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor)
    {
        boolean isChecked = false;
        ChessPosition kingPos = getKingPos(teamColor);
        if (kingPos != null) {
            //go over whole chessboard
            for (int i = 1; i < 9; i++) {
                for (int j = 1; j < 9; j++) {
                    ChessPosition position = new ChessPosition(i, j);
                    ChessPiece piece = board.getPiece(position);
                    if (piece != null) {
                        if (piece.getTeamColor() != teamColor) {
                            //check if the king's position is in the other team piece's move set
                            Collection<ChessMove> oMoves = piece.pieceMoves(board, position);
                            for (Iterator<ChessMove> iterator = oMoves.iterator(); iterator.hasNext(); ) {
                                ChessMove oMove = iterator.next();
                                if (kingPos.equals(oMove.getEndPosition())) {
                                    isChecked = true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return isChecked;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean isCheckMate = false;
        ChessPosition kingPos = getKingPos(teamColor);
        Collection<ChessMove> moves = validMoves(kingPos);
        if (moves.size() == 0 && isInCheck(teamColor)){
            isCheckMate = true;
        }
        return isCheckMate;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        boolean isStalemate = true;
        for (int i = 1; i < 9; i++){
            for(int j = 1; j < 9; j++){
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null){
                    if (piece.getTeamColor() == teamColor){
                        Collection<ChessMove> moves = validMoves(position);
                        if (!moves.isEmpty()){
                            isStalemate = false;
                        }
                    }
                }
            }
        }
        return isStalemate;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard()
    {
        return board;
    }
}
