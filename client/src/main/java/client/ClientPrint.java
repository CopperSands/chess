package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;

import java.util.ArrayList;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class ClientPrint {

    public static void socketHelp(){
        System.out.println("\t redraw - redraw the chess board");
        System.out.println("\t move <from> <to> - move a chess piece example: move a2 a4");
        System.out.println("\t legal-moves <position> - highlights legal moves for a piece");
        System.out.println("\t leave - leave the game for now");
        System.out.println("\t resign - resign the game by forfeit");
    }

    public static void loggedOutHelp(){
        System.out.println("\t register <USERNAME> <PASSWORD> <EMAIL> - used to create an account");
        System.out.println("\t login <USERNAME> <PASSWORD> - used to login");
        System.out.println("\t help - show possible commands");
        System.out.println("\t quit - end program");
    }
    public static void loggedInHelp(){
        System.out.println("\t create <NAME> - create a game");
        System.out.println("\t list - list all games");
        System.out.println("\t join <ID> [WHITE|BLACK|<empty>]- join a game as white black or observe");
        System.out.println("\t observe <ID> - observe a game");
        System.out.println("\t help - show possible commands");
        System.out.println("\t logout - logout");
    }

    public static void printGames(ArrayList<GameData> gameList){
        System.out.println("select code   gameID   game name   White Player  Black Player");
        for(int i = 0; i < gameList.size(); i++){
            GameData game = gameList.get(i);
            System.out.println((i + 1) + " " + game.gameID() + " " + game.gameName() + " " + game.whiteUsername()
                    + " " + game.blackUsername());
        }
    }

    public static void printBoard(ChessBoard board){
        String header = "  h  g   f  e   d  c   b   a ";
        //print black top
        System.out.println(SET_TEXT_ITALIC + "  " + header + RESET_TEXT_ITALIC);
        int count = 1;
        for (int row = 8; row > 0; row--){
            System.out.print(count + " ");
            for (int col = 8; col > 0; col--){
                ChessPosition pos = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(pos);
                //get background color for odd and even
                printSquare(row,col,piece);
            }
            System.out.print(SET_BG_COLOR_DARK_GREY + count + "\n");
            count++;
        }
        System.out.println(SET_TEXT_ITALIC + "  " + header + RESET_TEXT_ITALIC);
    }
    public static void printReverseBoard(ChessBoard board){
        String header = "  a  b   c  d   e  f   g   h ";
        System.out.println(SET_TEXT_ITALIC + "  " + header + RESET_TEXT_ITALIC);
        int count = 8;
        for (int row = 1; row < 9; row++){
            System.out.print(count + " ");
            for (int col = 1; col < 9; col++){
                ChessPosition pos = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(pos);
                printSquare(row,col,piece);
            }
            System.out.print(SET_BG_COLOR_DARK_GREY + count + "\n");
            count--;
        }
        System.out.println(SET_TEXT_ITALIC + "  " + header + RESET_TEXT_ITALIC);
    }
    public static void printSquare(int row, int col, ChessPiece piece){
        String backColor;
        if ((row % 2) == 0){
            if ((col % 2) == 0){
                backColor = SET_BG_COLOR_WHITE;
            }
            else{
                backColor = SET_BG_COLOR_BLUE;
            }
        }else{
            if(col % 2 == 0){
                backColor = SET_BG_COLOR_BLUE;
            }
            else{
                backColor = SET_BG_COLOR_WHITE;
            }
        }
        if (piece == null){
            System.out.print(backColor +EMPTY);
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.PAWN){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                System.out.print(backColor + SET_TEXT_COLOR_LIGHT_GREY + WHITE_PAWN + SET_TEXT_COLOR_WHITE);
            } else{
                System.out.print(backColor  + SET_TEXT_COLOR_BLACK + BLACK_PAWN + SET_TEXT_COLOR_WHITE);
            }
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                System.out.print(backColor + SET_TEXT_COLOR_LIGHT_GREY + WHITE_BISHOP + SET_TEXT_COLOR_WHITE);
            } else{
                System.out.print(backColor + SET_TEXT_COLOR_BLACK +BLACK_BISHOP + SET_TEXT_COLOR_WHITE);
            }
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                System.out.print(backColor + SET_TEXT_COLOR_LIGHT_GREY + WHITE_KNIGHT + SET_TEXT_COLOR_WHITE);
            }else{
                System.out.print(backColor + SET_TEXT_COLOR_BLACK + BLACK_KNIGHT + SET_TEXT_COLOR_WHITE);
            }
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.ROOK){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                System.out.print(backColor + SET_TEXT_COLOR_LIGHT_GREY + WHITE_ROOK + SET_TEXT_COLOR_WHITE);
            }else{
                System.out.print(backColor + SET_TEXT_COLOR_BLACK + BLACK_ROOK + SET_TEXT_COLOR_WHITE);
            }
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.KING){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                System.out.print(backColor + SET_TEXT_COLOR_LIGHT_GREY + WHITE_KING + SET_TEXT_COLOR_WHITE);
            }else{
                System.out.print(backColor + SET_TEXT_COLOR_BLACK + BLACK_KING + SET_TEXT_COLOR_WHITE);
            }
        }
        else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN){
            if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                System.out.print(backColor + SET_TEXT_COLOR_LIGHT_GREY + WHITE_QUEEN + SET_TEXT_COLOR_WHITE);
            }else{
                System.out.print(backColor + SET_TEXT_COLOR_BLACK + BLACK_QUEEN + SET_TEXT_COLOR_WHITE);
            }
        }
    }

    public static String getStatus (boolean isLoggedIn){
        String status;
        if (isLoggedIn){
            status = "[LOGGED_IN] >>> ";
        } else{
            status = "[LOGGED_OUT] >>> ";
        }
        return status;
    }
}
