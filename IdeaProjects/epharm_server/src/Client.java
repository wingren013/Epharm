/**
 * Created by smifsud on 11/29/16.
 */
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;


public class Client {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        try(Socket s = new Socket("localhost", 8080)){


            ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
            ObjectInputStream in = new ObjectInputStream(s.getInputStream());
            System.out.println(in.readUTF());
            try(Scanner scanner = new Scanner(System.in)){
                while(true){
                    String instring = in.readUTF();
                    String outstring = scanner.next();

                    if(instring != null){
                        System.out.println(instring);
                    }

                    if(outstring != null){
                        out.writeUTF(outstring);
                    }
                    outstring = null;
                }
            }
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }

}