import chess.*;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Scanner;
import static ui.EscapeSequences.*;


public class Main {
    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Please enter the server port number as the first argument");
            return;
        }
        System.out.println(args[0]);
        ServerFacade serverFacade = new ServerFacade(Integer.parseInt(args[0]));
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Chess. Type help to see options.");
        boolean isLive = true;
        boolean isLoggedIn = false;
        String status;
        while (isLive){
            status = getStatus(isLoggedIn);
            System.out.print(status);
            String option = scanner.nextLine();
            if (isLoggedIn){
                if(option.equals("help") || option.equals("Help")){
                    loggedInHelp();
                }
                else if (option.contains("create")){
                    String [] create = option.split(" +");
                    if(create.length == 2 && create[0].equals("create")){
                        try{
                            int gameID = serverFacade.createGame(create[1]);
                            System.out.println("GameID is " + gameID);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                        //call create request
                    }
                    else{
                        System.out.println("invalid format");
                    }
                }
                else if (option.equals("list")){
                    //call list request
                    try{
                        ArrayList<GameData> gameList = (ArrayList<GameData>) serverFacade.listGames();
                        printGames(gameList);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
                else if (option.contains("join")){
                    String [] join = option.split(" +");
                    if ((join.length == 3 || join.length == 2) && join[0].equals("join")){
                        GameData game;
                        //call join request
                        if (join.length == 3 ){
                            join[2] = join[2].toUpperCase(Locale.ROOT);
                            try{
                                game = serverFacade.joinGame(Integer.parseInt(join[1]),join[2]);
                                printBoard(game.game().getBoard());
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        for(String a: join){
                            System.out.println(a);
                        }
                        try{
                            game = serverFacade.joinGame(Integer.parseInt(join[1]),null);
                            printBoard(game.game().getBoard());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                else if (option.contains("observe")){
                    String [] observe = option.split(" +");
                    if (observe.length == 2 && observe[0].equals("observe")){
                        //call join request
                        try{
                            GameData game = serverFacade.joinGame(Integer.parseInt(observe[1]),null);
                            printBoard(game.game().getBoard());
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                }
                else if (option.equals("logout")){
                    //call logout request
                    try{
                        serverFacade.logout();
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                    isLoggedIn = false;
                }
                else{
                    System.out.println("Invalid command");
                }
            }
            else{
                GameLoop results = loggedOutOptions(option,serverFacade);
                isLive = results.isLive();
                isLoggedIn = results.isLoggedIn();
            }
        }

        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        //close scanner
        scanner.close();
    }

    private static String getStatus (boolean isLoggedIn){
        String status;
        if (isLoggedIn){
            status = "[LOGGED_IN] >>> ";
        }
        else{
            status = "[LOGGED_OUT] >>> ";
        }
        return status;
    }

    private static GameLoop loggedOutOptions(String option, ServerFacade serverFacade){
        boolean isLive = true;
        boolean isLoggedIn = false;
        if(option.equals("help") || option.equals("Help")){
            loggedOutHelp();
        }
        else if(option.contains("register")){
            String [] registration = option.split(" +");
            if (registration.length == 4 && registration[0].equals("register")){
                //call register request
                try{
                    serverFacade.registerRequest(registration[1],registration[2],registration[3]);
                    isLoggedIn = true;
                }catch (Exception e){
                        System.out.println(e.getMessage());
                }
            }
            else{
                System.out.println("Invalid format");
            }
        }
        else if(option.contains("login") || option.contains("Login")){
            String [] login = option.split(" +");
            if (login.length == 3 && login[0].equals("login")){
                //call login request
                try{
                    serverFacade.login(login[1], login[2]);
                    isLoggedIn = true;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
            else{
                System.out.println("Invalid login format");
            }
        }
        else if (option.equals("quit") || option.equals("Quit")){
            System.out.println("quit");
            isLive = false;
        }
        else{
            System.out.println("Invalid command");
        }
        return new GameLoop(isLive,isLoggedIn);
    }

    private static void loggedOutHelp(){
        System.out.println("\t register <USERNAME> <PASSWORD> <EMAIL> - used to create an account");
        System.out.println("\t login <USERNAME> <PASSWORD> - used to login");
        System.out.println("\t help - show possible commands");
        System.out.println("\t quit - end program");
    }
    private static void loggedInHelp(){
        System.out.println("\t create <NAME> - create a game");
        System.out.println("\t list - list all games");
        System.out.println("\t join <ID> [WHITE|BLACK|<empty>]- join a game as white black or observe");
        System.out.println("\t observe <ID> - observe a game");
        System.out.println("\t help - show possible commands");
        System.out.println("\t logout - logout");
    }

    private static void printGames(ArrayList<GameData> gameList){
        System.out.println("select code   gameID   game name   White Player  Black Player");
        for(int i = 0; i < gameList.size(); i++){
            GameData game = gameList.get(i);
            System.out.println((i + 1) + " " + game.gameID() + " " + game.gameName() + " " + game.whiteUsername()
                    + " " + game.blackUsername());
        }
    }

    private static void printBoard(ChessBoard board){
        String Header = "  h  g   f  e   d  c   b   a ";
        //print black top
        System.out.println(SET_TEXT_ITALIC + "  " + Header + RESET_TEXT_ITALIC);
        int count = 1;
        for (int row = 8; row > 0; row--){
            System.out.print(count + " ");
            for (int col = 8; col > 0; col--){
                ChessPosition pos = new ChessPosition(row,col);
                ChessPiece piece = board.getPiece(pos);
                //get background color for odd and even
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
                        System.out.print(backColor + WHITE_PAWN);
                    } else{
                        System.out.print(backColor + BLACK_PAWN);
                    }
                }
                else if (piece.getPieceType() == ChessPiece.PieceType.BISHOP){
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        System.out.print(backColor + WHITE_BISHOP);
                    } else{
                        System.out.print(backColor + BLACK_BISHOP);
                    }
                }
                else if (piece.getPieceType() == ChessPiece.PieceType.KNIGHT){
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        System.out.print(backColor + WHITE_KNIGHT);
                    }else{
                        System.out.print(backColor + BLACK_KNIGHT );
                    }
                }
                else if (piece.getPieceType() == ChessPiece.PieceType.ROOK){
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        System.out.print(backColor + WHITE_ROOK);
                    }else{
                        System.out.print(backColor + BLACK_ROOK);
                    }
                }
                else if (piece.getPieceType() == ChessPiece.PieceType.KING){
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        System.out.print(backColor + WHITE_KING);
                    }else{
                        System.out.print(backColor + BLACK_KING);
                    }
                }
                else if (piece.getPieceType() == ChessPiece.PieceType.QUEEN){
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE){
                        System.out.print(backColor + WHITE_QUEEN);
                    }else{
                        System.out.print(backColor + BLACK_QUEEN);
                    }
                }
            }
            System.out.print(SET_BG_COLOR_DARK_GREY + count + "\n");
            count++;
        }
        System.out.println(SET_TEXT_ITALIC + "  " + Header + RESET_TEXT_ITALIC);
    }
}