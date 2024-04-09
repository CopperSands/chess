package client;

import clientRecords.GameLoop;
import model.GameData;
import webSocketMessages.userCommands.JoinCommand;

import java.util.ArrayList;
import java.util.Locale;

public class ClientOptions {

    public static GameLoop loggedOutOptions(String option, ServerFacade serverFacade){
        boolean isLive = true;
        boolean isLoggedIn = false;
        if(option.equals("help") || option.equals("Help")){
            ClientPrint.loggedOutHelp();
        } else if(option.contains("register")){
            String [] registration = option.split(" +");
            if (registration.length == 4 && registration[0].equals("register")){
                //call register request
                try{
                    serverFacade.registerRequest(registration[1],registration[2],registration[3]);
                    isLoggedIn = true;
                }catch (Exception e){System.out.println(e.getMessage());}
            } else{
                System.out.println("Invalid format");
            }
        } else if(option.contains("login") || option.contains("Login")){
            String [] login = option.split(" +");
            if (login.length == 3 && login[0].equals("login")){
                //call login request
                try{
                    serverFacade.login(login[1], login[2]);
                    isLoggedIn = true;
                } catch (Exception e) {System.out.println(e.getMessage());}
            } else{
                System.out.println("Invalid login format");
            }
        } else if (option.equals("quit") || option.equals("Quit")){
            System.out.println("quit");
            isLive = false;
        } else{
            System.out.println("Invalid command");
        }
        return new GameLoop(isLive,isLoggedIn, null);
    }

    public static GameLoop loggedInOptions(String option, ServerFacade serverFacade, int port){
        boolean isLive = true;
        boolean isLoggedIn = true;
        ClientWebSocket webSocket = null;
        if(option.equals("help") || option.equals("Help")){
            ClientPrint.loggedInHelp();
        } else if (option.contains("create")){
            String [] create = option.split(" +");
            if(create.length == 2 && create[0].equals("create")){
                try{
                    //call create request
                    int gameID = serverFacade.createGame(create[1]);
                    System.out.println("GameID is " + gameID);
                } catch (Exception e) {System.out.println(e.getMessage());}
            } else{
                System.out.println("invalid format");
            }
        } else if (option.equals("list")){
            //call list request
            try{
                ArrayList<GameData> gameList = (ArrayList<GameData>) serverFacade.listGames();
                ClientPrint.printGames(gameList);
            } catch (Exception e) {System.out.println(e.getMessage());}
        } else if (option.contains("join")){
            String [] join = option.split(" +");
            if ((join.length == 3 || join.length == 2) && join[0].equals("join")){
                GameData game;
                //call join request
                if (join.length == 3 ){
                    join[2] = join[2].toUpperCase(Locale.ROOT);
                    try{
                        game = serverFacade.joinGame(Integer.parseInt(join[1]),join[2]);
                        ClientPrint.printBoard(game.game().getBoard());
                        ClientPrint.printReverseBoard(game.game().getBoard());
                        webSocket = new ClientWebSocket(port,serverFacade.getAuthToken());
                        webSocket.joinGame(game.gameID(),join[2]);
                    } catch (Exception e) {System.out.println(e.getMessage());}
                }else{
                    try{
                        game = serverFacade.joinGame(Integer.parseInt(join[1]),null);
                        ClientPrint.printBoard(game.game().getBoard());
                        ClientPrint.printReverseBoard(game.game().getBoard());
                        webSocket = new ClientWebSocket(port, serverFacade.getAuthToken());
                        webSocket.joinGame(game.gameID(), null);
                    } catch (Exception e) {System.out.println(e.getMessage());}
                }
            }
        } else if (option.contains("observe")){
            String [] observe = option.split(" +");
            if (observe.length == 2 && observe[0].equals("observe")){
                //call join request
                try{
                    GameData game = serverFacade.joinGame(Integer.parseInt(observe[1]),null);
                    ClientPrint.printBoard(game.game().getBoard());
                    ClientPrint.printReverseBoard(game.game().getBoard());
                    webSocket = new ClientWebSocket(port,serverFacade.getAuthToken());
                    webSocket.joinGame(game.gameID(), null);
                } catch (Exception e) {System.out.println(e.getMessage());}
            }
        } else if (option.equals("logout")){
            //call logout request
            try{
                serverFacade.logout();
            } catch (Exception e) {System.out.println(e.getMessage());}
            isLoggedIn = false;
        } else{
            System.out.println("Invalid command");
        }
        //add ClientWebSocket
        return new GameLoop(isLive,isLoggedIn,webSocket);
    }

    public static GameLoop socketOptions(String option, ClientWebSocket webSocket){
        boolean isLive = true;
        boolean isLoggedIn = true;
        if (option.equals("help") || option.equals("Help")){
            ClientPrint.socketHelp();
        }

        return new GameLoop(isLive,isLoggedIn,webSocket);
    }
}
