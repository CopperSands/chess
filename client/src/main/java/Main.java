import chess.*;

import java.util.Locale;
import java.util.Scanner;

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
                        //call create request
                    }
                    else{
                        System.out.println("invalid format");
                    }
                }
                else if (option.equals("list")){
                    //call list request
                }
                else if (option.contains("join")){
                    String [] join = option.split(" +");
                    if ((join.length == 3 || join.length == 2) && join[0].equals("join")){
                        if (join.length == 3 ){
                            join[2] = join[2].toUpperCase(Locale.ROOT);
                        }
                        for(String a: join){
                            System.out.println(a);
                        }
                        //call join request
                    }
                }
                else if (option.contains("observe")){
                    String [] observe = option.split(" +");
                    if (observe.length == 2 && observe[0].equals("observe")){
                        //call join request
                    }
                }
                else if (option.equals("logout")){
                    //call logout request
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
                    if (e.getMessage().equals("Internal Server Error") ||
                            e.getMessage().equals("Error username is taken") ||
                            e.getMessage().equals("Error bad request")){
                        System.out.println(e.getMessage());
                    }
                    else{
                        System.out.println("Application Error");
                    }
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
                isLoggedIn = true;
            }
            else{
                System.out.println("Invalid login format");
            }
            System.out.println("login");
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
        System.out.println("\t join <gameID> [WHITE|BLACK|<empty>]- join a game as white black or observe");
        System.out.println("\t observe <gameID> - observe a game");
        System.out.println("\t help - show possible commands");
        System.out.println("\t logout - logout");
    }



}