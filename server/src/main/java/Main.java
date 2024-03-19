import chess.*;
import server.Server;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Server server = new Server();
        int port = server.run(0);
        System.out.println("Chess server started on port " + port);
        System.out.println("Type \'Stop\' to stop server");
        Scanner scanner = new Scanner(System.in);
        boolean serverOn = true;
        while (serverOn){
            String input = scanner.nextLine();
            System.out.println(input + input.getClass());
            if (input.equals("Stop") || input.equals("stop") ){
                server.stop();
                serverOn = false;
            }
        }
    }
}