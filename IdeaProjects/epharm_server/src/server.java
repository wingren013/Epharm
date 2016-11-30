/**
 * Created by smifsud on 11/29/16.
 */

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server{

    private myserverstarter message_to;
    private static int port;
    public server (int listen_port, myserverstarter starter)
    {
        message_to = starter;
        port = listen_port;
        port = 8080;
    }

    public static void main(String[] args) throws FileNotFoundException {
        // TODO Auto-generated method stub
        try(ServerSocket s = new ServerSocket(8080)){
            ExecutorService executor = Executors.newCachedThreadPool();

            int i = 1;
            while (true) {
                Socket incoming = s.accept();
                System.out.println("Spawning: " + i);
                executor.execute(new ServeThread(incoming, 8080));
               /* Runnable r = new ServeThread(port);
                Thread t = new Thread(r);
                t.start(); */
                i++;
            }
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }
}