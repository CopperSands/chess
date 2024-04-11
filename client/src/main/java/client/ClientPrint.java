package client;

import chess.*;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;
import static ui.EscapeSequences.SET_TEXT_COLOR_WHITE;

public class ClientPrint {

    public static void socketHelp(){
        System.out.println("\t redraw - redraw the chess board");
        System.out.println("\t move <from> <to> [pawn Promotion] - move a chess piece example: move a2 a4." +
                "\n\t\t The forth field is optional and only used upgrading pawns");
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

    public static void printBoard(ChessBoard board, Collection<ChessPosition> pieceMoves){
        String header = "  h  g   f  e   d  c   b   a ";
        //print black top
        System.out.println(SET_TEXT_ITALIC + "  " + header + RESET_TEXT_ITALIC);
        // for (int row = 8; row > 0; row--){
        for (int row = 8; row > 0; row--){
            System.out.print(row + " ");
            // for (int col = 8; col > 0; col--)
            for (int col = 8; col > 0; col--){
                createSquare(row,col,board,pieceMoves);
            }
            System.out.print(SET_BG_COLOR_DARK_GREY + row + "\n");
        }
        System.out.println(SET_TEXT_ITALIC + "  " + header + RESET_TEXT_ITALIC);
    }

    public static void printReverseBoard(ChessBoard board, Collection<ChessPosition> pieceMoves){
        String header = "  a  b   c  d   e  f   g   h ";
        System.out.println(SET_TEXT_ITALIC + "  " + header + RESET_TEXT_ITALIC);
        //for (int row = 1; row < 9; row++)
        for (int row = 1; row < 9; row++){
            System.out.print(row + " ");
            //for (int col = 1; col < 9; col++)
            for (int col = 1; col < 9; col++){
                createSquare(row,col,board,pieceMoves);
            }
            System.out.print(SET_BG_COLOR_DARK_GREY + row + "\n");
        }
        System.out.println(SET_TEXT_ITALIC + "  " + header + RESET_TEXT_ITALIC);
    }

    private static void createSquare(int row,int col, ChessBoard board,Collection<ChessPosition> pieceMoves){
        ChessPosition pos = new ChessPosition(row,col);
        ChessPiece piece = board.getPiece(pos);
        if (pieceMoves == null){
            //get background color for odd and even
            printSquare(row,col,piece,false);
        }
        else{
            if (pieceMoves.contains(pos)){
                printSquare(row,col,piece,true);
            }
            else{
                printSquare(row,col,piece,false);
            }
        }
    }
    public static void printSquare(int row, int col, ChessPiece piece,boolean highLight){
        String backColor;
        if ((row % 2) == 0){
            if ((col % 2) == 0){
                if (!highLight){
                    backColor = SET_BG_COLOR_WHITE;
                }
                else{
                    backColor = SET_BG_COLOR_GREEN;
                }
            }
            else{
                if (!highLight){
                    backColor = SET_BG_COLOR_BLUE;
                }
                else{
                    backColor = SET_BG_COLOR_DARK_GREEN;
                }
            }
        }else{
            if(col % 2 == 0){
                if (!highLight){
                    backColor = SET_BG_COLOR_BLUE;
                }
                else{
                    backColor = SET_BG_COLOR_DARK_GREEN;
                }
            }
            else{
                if (!highLight){
                    backColor = SET_BG_COLOR_WHITE;
                }
                else{
                    backColor = SET_BG_COLOR_GREEN;
                }
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
