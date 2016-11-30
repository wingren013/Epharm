import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;

/**
 * Created by smifsud on 11/29/16.
 */
public class ServeThread implements Runnable{

    private int port;

    public ServeThread (int portvar){
        this.port = portvar;
    }

    private void s(String s2)
    {
        System.err.println(s2);
    }

    public void run()
    {
        ServerSocket sock = null;
        s("httpserver for epharm");
        try
        {
            s("Trying to bind to localhost on port " + Integer.toString(port));
            s("...");
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
            s("DON'T STOP");
            try {
                s("Don't stop believing");
                Socket connection = sock.accept();
                s("Keep on holding on and dreaming");
                InetAddress client = connection.getInetAddress();
                s("This code will be debugged someday");
                s("\n" + client.getHostName() + " connected to the server\n");
                BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                DataOutputStream output = new DataOutputStream(connection.getOutputStream());

                requesthandler(input, output);
            }
            catch (Exception e)
            {
                s("ERROR " + e.getMessage());
                s("\n" + e.getLocalizedMessage());
            }
            s("smells so good");
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

            if (method == 0)
            {
                try{
                    out.writeBytes(construct_http_header(501, 0));
                    out.close();
                    return ;
                }
                catch (Exception a){
                    s("Welp " + a.getLocalizedMessage());
                }
            }
            if (method == 1)
            {
                //GET
                try {
                    ResultSet info = null;
                    String    ret = null;

                    SQLiteJDBC db = new SQLiteJDBC();
                    String query = makequery(tmp2);
                    info = db.query(query);
                    info.first();
                    ret = info.getString(1);
                    out.writeBytes(ret);
                }
                catch (Exception b) {
                    s("Danger Will Robinson! " + b.getLocalizedMessage());
                }
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

    private String makequery(String request)
    {
        String ret = "";

        for (int i = 0; i < request.length(); i++)
        {
            if (request.charAt(i) == '[') {
                i++;
                while (request.charAt(i) != ']') {
                    ret.concat(String.valueOf(request.charAt(i)));
                }
            }
        }
        return ret;
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
