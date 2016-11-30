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
                SQLiteJDBC db = new SQLiteJDBC();
                db.insert("BEGIN TRANSACTION;\n" +
                        "CREATE TABLE doctor(ID INTEGER PRIMARY KEY, FIRSTNAME TEXT NOT NULL, LASTNAME TEXT NOT NULL, HOSPITAL TEXT);\n" +
                        "INSERT INTO \"doctor\" VALUES(1,'Doctor','Doctor','Generic Hospital of Her Genirc Hospitale');\n" +
                        "INSERT INTO \"doctor\" VALUES(2,'Good','Person','Not a Hospital');\n" +
                        "INSERT INTO \"doctor\" VALUES(3,'Al','Norm','Not a Hospital');\n" +
                        "INSERT INTO \"doctor\" VALUES(4,'Foo','Bar','Not a Hospital');\n" +
                        "CREATE TABLE patient(ID INTEGER PRIMARY KEY, DOCTORID INTEGER REFERENCES doctor(ID), FIRSTNAME TEXT NOT NULL, LASTNAME TEXT NOT NULL);\n" +
                        "CREATE TABLE drug(ID INTEGER PRIMARY KEY, NAME TEXT NOT NULL);\n" +
                        "INSERT INTO \"drug\" VALUES(1,'Iron Supplement');\n" +
                        "INSERT INTO \"drug\" VALUES(2,'Strength Potion');\n" +
                        "CREATE TABLE pharmacy(ID INTEGER PRIMARY KEY, NAME TEXT NOT NULL);\n" +
                        "INSERT INTO \"pharmacy\" VALUES(1,'Ye Olde Mines');\n" +
                        "INSERT INTO \"pharmacy\" VALUES(2,'Panoramix');\n" +
                        "CREATE TABLE inventory(ID INTEGER PRIMARY KEY, PHARMACYID REFERENCES pharmacy(ID), DRUG REFERENCES drug(NAME), STOCK INTEGER DEFAULT 0, UNITPRICE REAL);\n" +
                        "INSERT INTO \"inventory\" VALUES(1,1,'Iron Supplement',1000,0.08);\n" +
                        "INSERT INTO \"inventory\" VALUES(2,1,'Strength Potion',0,0.0);\n" +
                        "INSERT INTO \"inventory\" VALUES(3,2,'Iron Supplement',10,0.1);\n" +
                        "INSERT INTO \"inventory\" VALUES(4,2,'Strength Potion',42,10.0);\n" +
                        "CREATE TABLE perscription(ID INTEGER PRIMARY KEY, PATIENTID REFERENCES patient(ID), DRUG REFERENCES drug(NAME), QUANTITY INTEGER NOT NULL, REASON TEXT NOT NULL);\n" +
                        "CREATE TABLE order_instance(ID INTEGER PRIMARY KEY , PERSCRIPTIONID REFERENCES perscription(ID), PHARMACYID REFERENCES pharmacy(ID), TOTALPRICE REAL);");
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
