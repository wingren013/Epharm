/**
 * Created by smifsud on 11/29/16.
 */
import javax.management.Query;
import java.sql.*;
import java.lang.String;

public class SQLiteJDBC
{
    private static Connection connection = null;

    public static void main( String args[] )
    {
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:test.db");
        } catch ( Exception e ) {
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
            System.exit(0);
        }
        System.out.println("Opened database successfully");
    }

    public static ResultSet preparedquery(String query)
    {
        ResultSet ret = null;
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            ret = statement.executeQuery();
        }catch (Exception e){
        System.err.println("Error " + e.getMessage());
    }
        return ret;
    }
    public static ResultSet query(String query)
    {
        ResultSet ret = null;

        try {
            Statement statement = connection.createStatement();
            ret = statement.executeQuery(query);
        }catch (Exception e){
            System.err.println("Error " + e.getMessage());
        }
        return ret;
    }
    public static void insert(String insert)
    {
        try{
            Statement statement = connection.createStatement();
            statement.execute(insert);
            System.out.println(insert + " successfully executed");
        }catch (Exception e){
            System.err.println("Error " + e.getMessage());
        }
    }
}
