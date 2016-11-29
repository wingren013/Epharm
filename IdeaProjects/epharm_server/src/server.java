/**
 * Created by smifsud on 11/29/16.
 */

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class server extends Thread{

    public server (int listen_port, myserverstarter starter)
    {
        message_to = starter;
        port = listen_port;
        this.start();
    }

    private void s(String s2)
    {
        System.err.println(s2);
    }

    private myserverstarter message_to;
    private int port;

    public void run()
    {
        ServerSocket sock = null;
        s("httpserver for epharm");
        try
        {
            s("Tring to bind to localhost on port " + Integer.toString(port) + "...");
            sock = new ServerSocket(port);
        }
        catch (Exception e)
        {
            s("Fatal Error " + e.getMessage());
            return ;
        }
        s("OK");
        while (true)
        {
            s("\n----Waiting for requests----\n");
            try {
                Socket connection = sock.accept();
                InetAddress client = connection.getInetAddress();
                s("\n" + client.getHostName() + " connected to the server\n");

                BufferedReader   input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                DataOutputStream output = new DataOutputStream(connection.getOutputStream());

                requesthandler(input, output);
            }
            catch (Exception e)
            {
                s("ERROR " + e.getMessage());
                s("\n" + e.getLocalizedMessage());
            }
        }
    }

    private void requesthandler(BufferedReader in, DataOutputStream out)
    {
        int method = 0;
        String http = new String();
        String path = new String();
        String file = new String();
        String user = new String();
        try {
            String tmp = in.readLine();
            String tmp2 = new String(tmp);
            tmp.toUpperCase();
            if (tmp.startsWith("GET")) {
                method = 1;
            }
            if (tmp.startsWith("SET"))
            {
                method = 2;
            }
        }
        catch (Exception e)
        {
            s("Error " + e.getMessage());
        }
    }

    private void send_message_to(String message)
    {
        System.out.println(message);
    }

    public static void main(String[] args) throws FileNotFoundException {
        try ServerSocket s = new ServerSocket(8000){

            //get db


        }
    }

    private String construct_http_header(int return_code, int file_type) {
        String s = "HTTP/1.0 ";
        //you probably have seen these if you have been surfing the web a while
        switch (return_code) {
            case 200:
                s = s + "200 OK";
                break;
            case 400:
                s = s + "400 Bad Request";
                break;
            case 403:
                s = s + "403 Forbidden";
                break;
            case 404:
                s = s + "404 Not Found";
                break;
            case 500:
                s = s + "500 Internal Server Error";
                break;
            case 501:
                s = s + "501 Not Implemented";
                break;
        }

        s = s + "\r\n"; //other header fields,
        s = s + "Connection: close\r\n"; //we can't handle persistent connections
        s = s + "Server: SimpleHTTPtutorial v0\r\n"; //server name

        //Construct the right Content-Type for the header.
        //This is so the browser knows what to do with the
        //file, you may know the browser dosen't look on the file
        //extension, it is the servers job to let the browser know
        //what kind of file is being transmitted. You may have experienced
        //if the server is miss configured it may result in
        //pictures displayed as text!
        switch (file_type) {
            //plenty of types for you to fill in
            case 0:
                break;
            case 1:
                s = s + "Content-Type: image/jpeg\r\n";
                break;
            case 2:
                s = s + "Content-Type: image/gif\r\n";
            case 3:
                s = s + "Content-Type: application/x-zip-compressed\r\n";
            default:
                s = s + "Content-Type: text/html\r\n";
                break;
        }

        ////so on and so on......
        s = s + "\r\n"; //this marks the end of the httpheader
        //and the start of the body
        //ok return our newly created header!
        return s;
    }
}