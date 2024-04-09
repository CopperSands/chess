import client.ClientOptions;
import client.ClientPrint;
import client.ClientWebSocket;
import client.ServerFacade;
import clientRecords.GameLoop;
import model.GameData;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1){
            System.out.println("Please enter the server port number as the first argument");
            return;
        }
        ServerFacade serverFacade = new ServerFacade(Integer.parseInt(args[0]));
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Chess. Type help to see options.");
        boolean isLive = true;
        boolean isLoggedIn = false;
        ClientWebSocket webSocket = null;
        String status;
        while (isLive){
            status = ClientPrint.getStatus(isLoggedIn);
            System.out.print(status);
            String option = scanner.nextLine();
            if (webSocket != null){
                GameLoop results = ClientOptions.socketOptions(option,webSocket);
                webSocket = results.webSocket();
            }
            else if (isLoggedIn){
              GameLoop results = ClientOptions.loggedInOptions(option,serverFacade,Integer.parseInt(args[0]));
              isLoggedIn = results.isLoggedIn();
              webSocket = results.webSocket();
            } else{
                GameLoop results = ClientOptions.loggedOutOptions(option,serverFacade);
                isLive = results.isLive();
                isLoggedIn = results.isLoggedIn();
            }
        }
        //close scanner
        scanner.close();
    }


}